package com.project.gamersgeek.utils.paging

import androidx.annotation.AnyThread
import androidx.annotation.GuardedBy
import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext

class PagingRequestHelper: CoroutineScope {
    private val mLock = Any()
    private val job = Job()
    @GuardedBy("mLock")
    private val mRequestQueue = arrayOf(
        RequestQueue(RequestType.INITIAL),
        RequestQueue(RequestType.BEFORE),
        RequestQueue(RequestType.AFTER)
    )
    private val mListeners: CopyOnWriteArrayList<Listener> = CopyOnWriteArrayList()

    fun recordResult(requestWrapper: RequestWrapper, throwable: Throwable?) = runBlocking {
        delay(5000)
        lockedRecordResult(requestWrapper, throwable)
    }
    fun runIfNotRunning(type: RequestType, request:() -> Request): Boolean {
        var isRunningOrNot: Boolean?= null
        runBlocking {
            val jobRunOrNotRunning: Deferred<Boolean> = async { isRunningOrNot(type, request)}
            isRunningOrNot = jobRunOrNotRunning.await()
        }
        return isRunningOrNot!!
    }

    fun isAllRetryFailed(): Boolean {
        var isAllRetryFailed: Boolean?= null
        runBlocking {
            val jobIsAllRetryFailed: Deferred<Boolean> = async { checkIfAllRetryFailed() }
            isAllRetryFailed = jobIsAllRetryFailed.await()
        }
        return isAllRetryFailed!!
    }
    @AnyThread
    fun addListener(listener: () -> Listener): Boolean {
        return this.mListeners.add(listener.invoke())
    }
    private fun checkIfAllRetryFailed(): Boolean {
        val toBeRetried: Array<RequestWrapper?> = arrayOfNulls(RequestType.values().size)
        var retried = false
        synchronized(this.mLock) {
            for (typeIndices in RequestType.values().indices) {
                toBeRetried[typeIndices] = this.mRequestQueue[typeIndices].getFailed()
                this.mRequestQueue[typeIndices].setFailed(null)
            }
        }
        for (failed: RequestWrapper? in toBeRetried) {
            failed?.let {
                failed.retry()
                retried = true
            }
        }
        return retried
    }
    @AnyThread
    private fun isRunningOrNot(type: RequestType, request: () -> Request): Boolean {
        val hasListeners: Boolean = this@PagingRequestHelper.mListeners.isNotEmpty()
        var report: StatusReport?= null
        synchronized(this.mLock) {
            val requestQueue: RequestQueue = this.mRequestQueue[type.ordinal]
            if (requestQueue.getRunning() != null) {
                return false
            }
            val invokeRequest: Request = request.invoke()
            requestQueue.setRunning(invokeRequest)
            requestQueue.mStatus = Status.RUNNING
            requestQueue.setFailed(null)
            requestQueue.mLastError = null
            if (hasListeners) {
                report = prepareStatusReportLocked()
            }
        }
        report?.let {
            dispatchReport(it)
        }
        val wrapper = RequestWrapper(request.invoke(), this, type)
        wrapper.run()
        return true
    }
    @AnyThread
    private fun lockedRecordResult(requestWrapper: RequestWrapper, throwable: Throwable?) {
        var statusReport: StatusReport?= null
        val success: Boolean = throwable == null
        val hasListener: Boolean = this@PagingRequestHelper.mListeners.isNotEmpty()
        launch(Dispatchers.IO) {
            synchronized(mLock) {
                val requestQueue: RequestQueue = mRequestQueue[requestWrapper.getRequestType().ordinal]
                requestQueue.setRunning(null)
                requestQueue.mLastError = throwable
                if (success) {
                    requestQueue.setFailed(null)
                    requestQueue.mStatus = Status.SUCCESS
                } else {
                    requestQueue.setFailed(requestWrapper)
                    requestQueue.mStatus = Status.FAILED
                }
                if (hasListener) {
                    statusReport = prepareStatusReportLocked()
                }
            }
            statusReport?.let {
                dispatchReport(statusReport)
            }
        }

    }

    private fun dispatchReport(statusReport: StatusReport?){
        for (listener: Listener in this.mListeners) {
            statusReport?.let {
                listener.onStatusChange { it }
            }
        }
    }

    @GuardedBy("mLock")
    private fun prepareStatusReportLocked(): StatusReport? {
        val errors: Array<Throwable?> = arrayOf(
            this.mRequestQueue[0].mLastError,
            this.mRequestQueue[1].mLastError,
            this.mRequestQueue[2].mLastError
        )
        return StatusReport(
            getStatusReportLocked(RequestType.INITIAL),
            getStatusReportLocked(RequestType.BEFORE),
            getStatusReportLocked(RequestType.AFTER),
            errors
        )
    }
    @GuardedBy("mLock")
    private fun getStatusReportLocked(type: RequestType): Status {
        return this.mRequestQueue[type.ordinal].mStatus!!
    }


    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    internal inner class RequestQueue constructor(private val requestType: RequestType) {
        private var mFailed: RequestWrapper? = null
        private var mRunning: Request?= null
        var mLastError: Throwable?= null
        var mStatus: Status?= Status.SUCCESS

        fun setRunning(request: Request?) {
            this.mRunning = request
        }
        fun getRunning(): Request? {
            return this.mRunning
        }
        fun setFailed(wrapper: RequestWrapper?) {
            this.mFailed = wrapper
        }
        fun getFailed(): RequestWrapper? {
            return this.mFailed
        }
    }
    interface Listener {
        fun onStatusChange(report: () -> StatusReport)
    }
    companion object {
        @JvmStatic private val TAG: String = PagingRequestHelper::class.java.canonicalName!!
    }
}
