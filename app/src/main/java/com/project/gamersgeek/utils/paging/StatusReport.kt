package com.project.gamersgeek.utils.paging

class StatusReport constructor(private val initial: Status,
                               private val before: Status,
                               private val after: Status,
                               private val mErrors: Array<Throwable?>) {

    fun hasRunning(): Boolean {
        return this.initial == Status.RUNNING || this.before == Status.RUNNING || this.after == Status.RUNNING
    }
    fun hasError(): Boolean {
        return this.initial == Status.FAILED || this.before == Status.FAILED || this.after == Status.FAILED
    }
    fun getErrorFor(requestType: RequestType): Throwable? {
        return this.mErrors[requestType.ordinal]
    }

    override fun toString(): String {
        return "StatusReport(initial=$initial, before=$before, after=$after, mErrors=${mErrors.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatusReport) return false

        if (initial != other.initial) return false
        if (before != other.before) return false
        if (after != other.after) return false
        if (!mErrors.contentEquals(other.mErrors)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initial.hashCode()
        result = 31 * result + before.hashCode()
        result = 31 * result + after.hashCode()
        result = 31 * result + mErrors.contentHashCode()
        return result
    }

}