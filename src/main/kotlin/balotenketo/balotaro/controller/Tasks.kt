@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.model.BallotRepository
import balotenketo.balotaro.model.PollRepository
import balotenketo.balotaro.model.VoteTokenRepository
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Tasks {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    val logger: Logger by lazy { LoggerFactory.getLogger(Tasks::class.java) }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    fun deleteExpiredPolls() {

        val list = pollRepository.findByExpirationDateLessThan(DateTime.now().toDate())

        for (poll in list) {
            tokenRepository.deleteByPoll(poll)
            ballotRepository.deleteByPoll(poll)
            pollRepository.delete(poll)
        }

        logger.info("${list.size} expired poll(s) deleted")
    }
}