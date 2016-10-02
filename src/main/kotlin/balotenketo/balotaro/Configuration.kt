package balotenketo.balotaro

object Configuration {
    val maxPollCountByIP = 10

    val defaultTokenCount = 10
    val maxTokenCountByPoll = 1000

    fun tokensToCreate(currentCount: Int = 0, requestCount: Int = defaultTokenCount) =
            Math.max(0, Math.min(maxTokenCountByPoll, currentCount + requestCount) - currentCount)
}