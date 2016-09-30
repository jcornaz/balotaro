package balotenketo.balotaro

import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.VoteToken
import org.springframework.data.mongodb.repository.MongoRepository

interface PollRepository : MongoRepository<Poll, String> {
    fun countByCreatorIP(creatorIP: String): Int
    fun findOneByCreatorIPOrderByExpirationDateDesc(creatorIP: String): Poll
}

interface VoteTokenRepository : MongoRepository<VoteToken, String> {
    fun findByPoll(poll: Poll): Collection<VoteToken>
    fun countByPoll(poll: Poll): Int
}

interface BallotRepository : MongoRepository<Ballot, String>