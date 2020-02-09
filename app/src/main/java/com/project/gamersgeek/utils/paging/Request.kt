package com.project.gamersgeek.utils.paging

import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

@FunctionalInterface
interface Request {
    fun run(requestCallback: Callback)

    class Callback internal constructor(private val requestWrapper: RequestWrapper,
                                        private val pagingRequestHelper: PagingRequestHelper) {
        private val mCalled = AtomicBoolean()

        fun recordSuccess() {
            if (this.mCalled.compareAndSet(false, true)) {
                this.pagingRequestHelper.recordResult(this.requestWrapper, null)
            } else {
                throwException()
            }
        }
        fun recordFailure(it: String) {
            if (this.mCalled.compareAndSet(false, true)) {
                if (it.isNotEmpty()) {
                    this.pagingRequestHelper.recordResult(this.requestWrapper, Throwable(it))
                }
            } else {
                throwException()
            }
        }
        private fun throwException() {
            throw IllegalStateException(displayThrowableMessage())
        }
        private fun displayThrowableMessage(): String {
            return "recordSuccess/recordFailure had already been called once."
        }

        companion object {
            @JvmStatic private val TAG: String = Request::class.java.canonicalName!!
        }
    }
}