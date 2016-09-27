package balotenketo.balotaro.controller

import balotenketo.balotaro.BallotRepository
import balotenketo.balotaro.PollRepository
import balotenketo.balotaro.VoteTokenRepository
import balotenketo.balotaro.auth.StormPath
import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.Success
import balotenketo.balotaro.model.VoteToken
import com.google.common.base.Preconditions
import kondorcet.DefaultBallot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class PollController {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @RequestMapping("/poll/create")
    fun create(
            @RequestParam choices: Array<String>,
            @RequestParam(required = false) tokenCount: Int? = null,
            request: HttpServletRequest
    ) = let {
        Preconditions.checkArgument(choices.isNotEmpty())

        object {
            val poll = Poll(
                    admin = StormPath.getRequiredAccount(request).href,
                    choices = choices.map { it.toLowerCase() to it }.toMap()
            ).apply { pollRepository.save(this) }

            val tokens = (1..(tokenCount ?: 10)).map {
                VoteToken(poll).apply { tokenRepository.save(this) }
            }
        }
    }

    @RequestMapping("/poll/createTokens/{pollID}/{secret}")
    fun createTokens(pollID: String, secret: String, count: Int? = null): List<VoteToken> {
        val poll = pollRepository.findOne(pollID)
        Preconditions.checkArgument(poll?.secret == secret, "Invalid poll")

        return (1..(count ?: 10)).map { VoteToken(poll).apply { tokenRepository.save(this) } }
    }

    @RequestMapping("/poll/vote/{tokenID}/{secret}")
    fun vote(tokenID: String, secret: String, ballots: Array<String>): Success {
        val token = tokenRepository.findOne(tokenID)
        Preconditions.checkArgument(token?.secret == secret, "Invalid token")

        val ballot = DefaultBallot(ballots.map { it.split("|").toSet() })
        Preconditions.checkArgument(!ballot.hasDuplicates(), "This ballot contains duplicates")
        Preconditions.checkArgument(ballot.candidates().all { it in token.poll.choices }, "This ballot contains unknown candidates")

        ballotRepository.save(Ballot(token.poll.id, ballot.orderedCandidates))
        return Success()
    }
}