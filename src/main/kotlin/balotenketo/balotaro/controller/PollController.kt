package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import balotenketo.balotaro.model.*
import com.google.common.base.Preconditions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kondorcet.SimplePoll
import kondorcet.result
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@Api("Polls", description = "Polls management")
@RestController
@Suppress("unused")
class PollController {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @ApiOperation("Create a new poll")
    @RequestMapping("/poll/create",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(@RequestBody argument: PollCreationArgument, request: HttpServletRequest): PollCreationResult {

        Preconditions.checkArgument(argument.choices.isNotEmpty(), "You have to specify choices")
        Preconditions.checkArgument(argument.choices.size <= Configuration.maxCandidatesByPoll, "The number of candidates cannot exceed ${Configuration.maxCandidatesByPoll}")

        val ip = request.remoteAddr
        val todayStart = DateTime.now().withMillisOfDay(0)
        val todayEnd = todayStart.plusDays(1).minusMillis(1)
        val pollCount = pollRepository.countByCreatorIPAndCreationDateBetween(ip, todayStart.toDate(), todayEnd.toDate())

        Preconditions.checkArgument(pollCount < Configuration.maxPollCountByIP,
                "You cannot create more poll from this IP address today. You have to close you previous polls first")

        val poll = Poll(ip, choices = argument.choices.toSet()).apply { pollRepository.save(this) }
        val tokens = (1..Configuration.tokensToCreate(0, argument.tokenCount)).map {
            VoteToken(poll).apply { tokenRepository.save(this) }
        }

        return PollCreationResult(
                poll = encode(poll.id, poll.secret),
                tokens = tokens.map { encode(it.id, it.secret) }
        )
    }

    @ApiOperation("Create new tokens for a poll")
    @RequestMapping("/poll/createTokens",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun createTokens(@RequestBody argument: TokenCreationArgument): List<String> {

        val (pollID, secret) = argument.poll?.decode() ?: throw IllegalArgumentException("No poll specified")
        val poll = pollRepository.findOne(pollID)
        Preconditions.checkArgument(poll?.secret == secret, "Invalid poll")

        return (1..Configuration.tokensToCreate(tokenRepository.countByPoll(poll), argument.tokenCount)).map {
            VoteToken(poll).apply { tokenRepository.save(this) }.let { encode(it.id, it.secret) }
        }
    }

    @ApiOperation("Close a poll and return the result")
    @RequestMapping("/poll/close",
            method = arrayOf(RequestMethod.DELETE),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun close(@RequestBody pollToken: String): List<Set<String>> {
        val (id, secret) = pollToken.decode()

        val poll = pollRepository.findOne(id) ?: throw IllegalArgumentException("Invalid poll token")
        Preconditions.checkArgument(poll.secret == secret, "Invalid poll token")

        val result = ballotRepository.findByPoll(poll).fold(SimplePoll<String>()) { p, b -> p.vote(b); p }.result().orderedCandidates

        tokenRepository.deleteByPoll(poll)
        ballotRepository.deleteByPoll(poll)
        pollRepository.delete(poll)

        return result
    }
}
