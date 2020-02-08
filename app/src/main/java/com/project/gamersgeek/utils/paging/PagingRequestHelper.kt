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
        launch {
            lockedRecordResult(requestWrapper, throwable)
        }.join()
    }
    fun runIfNotRunning(type: RequestType, request: Request): Boolean {
        var isRunningOrNot: Boolean?= null
        runBlocking {
            val jobRunOrNotRunning: Deferred<Boolean> = async { isRunningOrNot(type, request)}
            isRunningOrNot = jobRunOrNotRunning.await()
        }
        return isRunningOrNot!!
    }
    @AnyThread
    fun addListener(listener: Listener): Boolean {
        return this.mListeners.add(listener)
    }
    @AnyThread
    private fun isRunningOrNot(type: RequestType, request: Request): Boolean {
        val hasListeners: Boolean = this@PagingRequestHelper.mListeners.isNotEmpty()
        var report: StatusReport?= null
        synchronized(this.mLock) {
            val requestQueue: RequestQueue = this.mRequestQueue[type.ordinal]
            if (requestQueue.mRunning != null) {
                return false
            }
            requestQueue.mRunning = request
            requestQueue.mStatus = Status.RUNNING
            requestQueue.mFailed = null
            requestQueue.mLastError = null
            if (hasListeners) {
                report = prepareStatusReportLocked()
            }
        }
        report?.let {
            dispatchReport(it)
        }
        val wrapper: RequestWrapper = RequestWrapper(request, this, type)
        wrapper.run()
        return true
    }
    @AnyThread
    private fun lockedRecordResult(requestWrapper: RequestWrapper, throwable: Throwable?) {
        var statusReport: StatusReport?= null
        val success: Boolean = throwable == null
        val hasListener: Boolean = this@PagingRequestHelper.mListeners.isNotEmpty()
        synchronized(this.mLock) {
            val requestQueue: RequestQueue = this.mRequestQueue[requestWrapper.getRequestType().ordinal]
            requestQueue.mRunning = null
            requestQueue.mLastError = throwable
            if (success) {
                requestQueue.mFailed = null
                requestQueue.mStatus = Status.SUCCESS
            } else {
                requestQueue.mFailed = requestWrapper
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

    private fun dispatchReport(statusReport: StatusReport?){
        for (listener: Listener in this.mListeners) {
            statusReport?.let {
                listener.onStatusChange(it)
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
        var mFailed: RequestWrapper? = null
        var mRunning: Request?= null
        var mLastError: Throwable?= null
        var mStatus: Status?= null
    }
    interface Listener {
        fun onStatusChange(report: StatusReport)
    }
    companion object {
        @JvmStatic private val TAG: String = PagingRequestHelper::class.java.canonicalName!!
    }
}