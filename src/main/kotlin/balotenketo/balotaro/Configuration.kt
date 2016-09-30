package balotenketo.balotaro

object Configuration {
    val maxPollCountByIP = 10

    val defaultTokenCount = 10
    val maxTokenCount = 1000

    fun tokenCount(count: Int? = null) = Math.max(maxTokenCount, count ?: defaultTokenCount)
}