package balotenketo.balotaro

import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.VoteToken
import org.springframework.data.mongodb.repository.MongoRepository

interface PollRepository : MongoRepository<Poll, String>

interface VoteTokenRepository : MongoRepository<VoteToken, String> {
    fun findByPoll(poll: Poll): Collection<VoteToken>
}

interface BallotRepository : MongoRepository<Ballot, String>