package com.project.gamersgeek.utils.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RequestWrapper constructor(private val request: Request,
                                 private val pagingRequestHelper: PagingRequestHelper,
                                 private val requestType: RequestType): Runnable, CoroutineScope {
    private val job = Job()
    override fun run() {
       this.request.run(Request.Callback(this, this.pagingRequestHelper))
    }
    fun retry() {
        launch {
            pagingRequestHelper.runIfNotRunning(requestType, request)
        }
    }
    fun getRequestType(): RequestType {
        return this.requestType
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}