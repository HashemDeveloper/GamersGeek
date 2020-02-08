package com.project.gamersgeek.utils

import androidx.annotation.AnyThread
import androidx.annotation.GuardedBy
import androidx.annotation.VisibleForTesting
import androidx.paging.DataSource
import com.project.gamersgeek.utils.PagingRequestHelperCopy.Request.Callback
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

class PagingRequestHelperCopy
/**
 * Creates a new PagingRequestHelper with the given [Executor] which is used to run
 * retry actions.
 *
 * @param retryService The [Executor] that can run the retry actions.
 */(private val mRetryService: Executor) {
    private val mLock = Any()
    @GuardedBy("mLock")
    private val mRequestQueues = arrayOf(
        RequestQueue(RequestType.INITIAL),
        RequestQueue(RequestType.BEFORE),
        RequestQueue(RequestType.AFTER)
    )
    val mListeners =
        CopyOnWriteArrayList<Listener>()

    /**
     * Adds a new listener that will be notified when any request changes [state][Status].
     *
     * @param listener The listener that will be notified each time a request's status changes.
     * @return True if it is added, false otherwise (e.g. it already exists in the list).
     */
    @AnyThread
    fun addListener(listener: Listener): Boolean {
        return mListeners.add(listener)
    }

    /**
     * Removes the given listener from the listeners list.
     *
     * @param listener The listener that will be removed.
     * @return True if the listener is removed, false otherwise (e.g. it never existed)
     */
    fun removeListener(listener: Listener): Boolean {
        return mListeners.remove(listener)
    }

    /**
     * Runs the given [Request] if no other requests in the given request type is already
     * running.
     *
     *
     * If run, the request will be run in the current thread.
     *
     * @param type    The type of the request.
     * @param request The request to run.
     * @return True if the request is run, false otherwise.
     */
    @AnyThread
    fun runIfNotRunning(
        type: RequestType,
        request: Request
    ): Boolean {
        val hasListeners = !mListeners.isEmpty()
        var report: StatusReport? = null
        synchronized(mLock) {
            val queue = mRequestQueues[type.ordinal]
            if (queue.mRunning != null) {
                return false
            }
            queue.mRunning = request
            queue.mStatus = Status.RUNNING
            queue.mFailed = null
            queue.mLastError = null
            if (hasListeners) {
                report = prepareStatusReportLocked()
            }
        }
        if (report != null) {
            dispatchReport(report!!)
        }
        val wrapper =
            RequestWrapper(request, this, type)
        wrapper.run()
        return true
    }

    @GuardedBy("mLock")
    private fun prepareStatusReportLocked(): StatusReport {
        val errors = arrayOf(
            mRequestQueues[0].mLastError,
            mRequestQueues[1].mLastError,
            mRequestQueues[2].mLastError
        )
        return StatusReport(
            getStatusForLocked(RequestType.INITIAL),
            getStatusForLocked(RequestType.BEFORE),
            getStatusForLocked(RequestType.AFTER),
            errors
        )
    }

    @GuardedBy("mLock")
    private fun getStatusForLocked(type: RequestType): Status {
        return mRequestQueues[type.ordinal].mStatus
    }

    @AnyThread
    @VisibleForTesting
    fun recordResult(
        wrapper: RequestWrapper,
        throwable: Throwable?
    ) {
        var report: StatusReport? = null
        val success = throwable == null
        val hasListeners = !mListeners.isEmpty()
        synchronized(mLock) {
            val queue = mRequestQueues[wrapper.mType.ordinal]
            queue.mRunning = null
            queue.mLastError = throwable
            if (success) {
                queue.mFailed = null
                queue.mStatus = Status.SUCCESS
            } else {
                queue.mFailed = wrapper
                queue.mStatus = Status.FAILED
            }
            if (hasListeners) {
                report = prepareStatusReportLocked()
            }
        }
        if (report != null) {
            dispatchReport(report!!)
        }
    }

    private fun dispatchReport(report: StatusReport) {
        for (listener in mListeners) {
            listener.onStatusChange(report)
        }
    }

    /**
     * Retries all failed requests.
     *
     * @return True if any request is retried, false otherwise.
     */
    fun retryAllFailed(): Boolean {
        val toBeRetried =
            arrayOfNulls<RequestWrapper>(RequestType.values().size)
        var retried = false
        synchronized(mLock) {
            for (i in RequestType.values().indices) {
                toBeRetried[i] = mRequestQueues[i].mFailed
                mRequestQueues[i].mFailed = null
            }
        }
        for (failed in toBeRetried) {
            if (failed != null) {
                failed.retry(mRetryService)
                retried = true
            }
        }
        return retried
    }

    class RequestWrapper(
        val mRequest: Request, val mHelperCopy: PagingRequestHelperCopy,
        val mType: RequestType
    ) : Runnable {
        override fun run() {
            mRequest.run(Callback(this, mHelperCopy))
        }

        fun retry(service: Executor) {
            service.execute { mHelperCopy.runIfNotRunning(mType, mRequest) }
        }

    }

    /**
     * Runner class that runs a request tracked by the [PagingRequestHelperCopy].
     *
     *
     * When a request is invoked, it must call one of [Callback.recordFailure]
     * or [Callback.recordSuccess] once and only once. This call
     * can be made any time. Until that method call is made, [PagingRequestHelperCopy] will
     * consider the request is running.
     */
    @FunctionalInterface
    interface Request {
        /**
         * Should run the request and call the given [Callback] with the result of the
         * request.
         *
         * @param callback The callback that should be invoked with the result.
         */
        fun run(callback: Callback?)

        /**
         * Callback class provided to the [.run] method to report the result.
         */
        class Callback internal constructor(
            private val mWrapper: RequestWrapper,
            private val mHelperCopy: PagingRequestHelperCopy
        ) {
            private val mCalled =
                AtomicBoolean()

            /**
             * Call this method when the request succeeds and new data is fetched.
             */
            fun recordSuccess() {
                if (mCalled.compareAndSet(false, true)) {
                    mHelperCopy.recordResult(mWrapper, null)
                } else {
                    throw IllegalStateException(
                        "already called recordSuccess or recordFailure"
                    )
                }
            }

            /**
             * Call this method with the failure message and the request can be retried via
             * [.retryAllFailed].
             *
             * @param throwable The error that occured while carrying out the request.
             */
            fun recordFailure(throwable: Throwable) {
                requireNotNull(throwable) {
                    ("You must provide a throwable describing"
                            + " the error to record the failure")
                }
                if (mCalled.compareAndSet(false, true)) {
                    mHelperCopy.recordResult(mWrapper, throwable)
                } else {
                    throw IllegalStateException(
                        "already called recordSuccess or recordFailure"
                    )
                }
            }

        }
    }

    /**
     * Data class that holds the information about the current status of the ongoing requests
     * using this helper.
     */
    class StatusReport internal constructor(
        /**
         * Status of the latest request that were submitted with [RequestType.INITIAL].
         */
        val initial: Status,
        /**
         * Status of the latest request that were submitted with [RequestType.BEFORE].
         */
        val before: Status,
        /**
         * Status of the latest request that were submitted with [RequestType.AFTER].
         */
        val after: Status,
        private val mErrors: Array<Throwable?>
    ) {
        /**
         * Convenience method to check if there are any running requests.
         *
         * @return True if there are any running requests, false otherwise.
         */
        fun hasRunning(): Boolean {
            return initial == Status.RUNNING || before == Status.RUNNING || after == Status.RUNNING
        }

        /**
         * Convenience method to check if there are any requests that resulted in an error.
         *
         * @return True if there are any requests that finished with error, false otherwise.
         */
        fun hasError(): Boolean {
            return initial == Status.FAILED || before == Status.FAILED || after == Status.FAILED
        }

        /**
         * Returns the error for the given request type.
         *
         * @param type The request type for which the error should be returned.
         * @return The [Throwable] returned by the failing request with the given type or
         * `null` if the request for the given type did not fail.
         */
        fun getErrorFor(type: RequestType): Throwable? {
            return mErrors[type.ordinal]
        }

        override fun toString(): String {
            return ("StatusReport{"
                    + "initial=" + initial
                    + ", before=" + before
                    + ", after=" + after
                    + ", mErrors=" + Arrays.toString(mErrors)
                    + '}')
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as StatusReport
            if (initial != that.initial) return false
            if (before != that.before) return false
            return if (after != that.after) false else Arrays.equals(mErrors, that.mErrors)
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
        }

        override fun hashCode(): Int {
            var result = initial.hashCode()
            result = 31 * result + before.hashCode()
            result = 31 * result + after.hashCode()
            result = 31 * result + Arrays.hashCode(mErrors)
            return result
        }

    }

    /**
     * Listener interface to get notified by request status changes.
     */
    interface Listener {
        /**
         * Called when the status for any of the requests has changed.
         *
         * @param report The current status report that has all the information about the requests.
         */
        fun onStatusChange(report: StatusReport)
    }

    /**
     * Represents the status of a Request for each [RequestType].
     */
    enum class Status {
        /**
         * There is current a running request.
         */
        RUNNING,
        /**
         * The last request has succeeded or no such requests have ever been run.
         */
        SUCCESS,
        /**
         * The last request has failed.
         */
        FAILED
    }

    /**
     * Available request types.
     */
    enum class RequestType {
        /**
         * Corresponds to an initial request made to a [DataSource] or the empty state for
         * a [BoundaryCallback][androidx.paging.PagedList.BoundaryCallback].
         */
        INITIAL,
        /**
         * Corresponds to the `loadBefore` calls in [DataSource] or
         * `onItemAtFrontLoaded` in
         * [BoundaryCallback][androidx.paging.PagedList.BoundaryCallback].
         */
        BEFORE,
        /**
         * Corresponds to the `loadAfter` calls in [DataSource] or
         * `onItemAtEndLoaded` in
         * [BoundaryCallback][androidx.paging.PagedList.BoundaryCallback].
         */
        AFTER
    }

    internal inner class RequestQueue(val mRequestType: RequestType) {
        var mFailed: RequestWrapper? = null
        var mRunning: Request? = null
        var mLastError: Throwable? = null
        var mStatus = Status.SUCCESS

    }

}