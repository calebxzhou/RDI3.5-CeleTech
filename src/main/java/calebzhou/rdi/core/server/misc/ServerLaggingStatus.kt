package calebzhou.rdi.core.server.misc

//服务器的流畅程度
object ServerLaggingStatus {
    //当前tick时间 与下次应该tick的时间 相距多长（流畅的时候是-50）
    var msBehind: Long = 0
        private set

    //落后这些ms代表卡顿
    private const val behindThreshold: Long = 100

    //更新落后的tick时间
	@JvmStatic
	fun updateMilliSecondsBehind(msBehind: Long) {
        ServerLaggingStatus.msBehind = msBehind
    }
    @JvmStatic
    val isServerVeryLagging: Boolean
        get() = msBehind > 200
    @JvmStatic
    val isServerFrozen: Boolean
        get() = msBehind > 10000
    val isServerLagging: Boolean
        get() = msBehind > behindThreshold
}
