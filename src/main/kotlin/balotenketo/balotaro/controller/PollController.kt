@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import balotenketo.balotaro.model.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import kondorcet.SimplePoll
import kondorcet.result
import kondorcet.with
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

class PollQuotaExceededException : Exception()
class TokenQuotaExceededException : Exception()
class InvalidNumberOfCandidatesException : Exception()

@Api("Polls", description = "Polls management")
@RestController
class PollController {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @ResponseStatus(HttpStatus.FORBIDDEN, reason = "You cannot create more poll today. Close open polls or wait until tomorrow.")
    @ExceptionHandler(PollQuotaExceededException::class)
    fun handlePollQuotaExceeded() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST, reason = "Invalid number of candidates. You have to specify from 2 to 100 candidates.")
    @ExceptionHandler(InvalidNumberOfCandidatesException::class)
    fun handleInvalidNumberOfCandidates() {
    }

    @ApiOperation("Create a new poll")
    @ApiResponses(*arrayOf(
            ApiResponse(code = 201, message = "Poll created"),
            ApiResponse(code = 400, message = "Invalid number of candidates"),
            ApiResponse(code = 403, message = "Maximum number of poll creation by day exceeded")))
    @RequestMapping("/poll/create",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody argument: PollCreationArgument, request: HttpServletRequest): PollCreationResult {

        if (argument.candidates.size < 2 || argument.candidates.size > Configuration.maxCandidatesByPoll)
            throw InvalidNumberOfCandidatesException()

        val ip = request.remoteAddr
        val todayStart = DateTime.now().withMillisOfDay(0)
        val todayEnd = todayStart.plusDays(1).minusMillis(1)
        val pollCount = pollRepository.countByCreatorIPAndCreationDateBetween(ip, todayStart.toDate(), todayEnd.toDate())

        if (pollCount >= Configuration.maxPollCountByIP)
            throw PollQuotaExceededException()

        val poll = Poll(ip, argument.secure, candidates = argument.candidates.toSet()).apply { pollRepository.save(this) }
        val nbToken = if (argument.secure) Configuration.tokensToCreate(0, argument.tokenCount) else 1
        val tokens = (1..nbToken).map {
            VoteToken(poll).apply { tokenRepository.save(this) }
        }

        return PollCreationResult(
                poll = encode(poll.id, poll.secret),
                tokens = tokens.map { encode(it.id, it.secret) }
        )
    }

    @ApiOperation("Create new tokens for a poll")
    @ApiResponses(*arrayOf(
            ApiResponse(code = 201, message = "Tokens created"),
            ApiResponse(code = 400, message = "Poll unspecified"),
            ApiResponse(code = 404, message = "Unknown poll")))
    @RequestMapping("/poll/createTokens",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    fun createTokens(@RequestBody argument: TokenCreationArgument): List<String> {

        val poll = pollRepository[argument.poll]

        val nbTokens = if (poll.isSecure) Configuration.tokensToCreate(tokenRepository.countByPoll(poll), argument.tokenCount) else 0

        return (1..nbTokens).map {
            VoteToken(poll).apply { tokenRepository.save(this) }.let { encode(it.id, it.secret) }
        }
    }

    @ApiOperation("Close a poll and return the result")
    @ApiResponses(*arrayOf(
            ApiResponse(code = 200, message = "Poll closed an result returned"),
            ApiResponse(code = 400, message = "Poll unspecified"),
            ApiResponse(code = 404, message = "Unknown poll")))
    @RequestMapping("/poll/close",
            method = arrayOf(RequestMethod.DELETE),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    @ResponseStatus(HttpStatus.OK)
    fun close(@RequestBody argument: PollClosingArgument): List<Set<String>> {
        val poll = pollRepository[argument.poll]

        val result = ballotRepository.findByPoll(poll)
                .fold(SimplePoll<String>()) { p, b -> p.vote(b); p }
                .result()
                .with(poll.candidates)
                .orderedCandidates

        tokenRepository.deleteByPoll(poll)
        ballotRepository.deleteByPoll(poll)
        pollRepository.delete(poll)

        return result
    }
}
