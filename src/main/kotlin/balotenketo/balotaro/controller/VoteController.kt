@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.BallotRepository
import balotenketo.balotaro.model.PollRepository
import balotenketo.balotaro.model.VoteTokenRepository
import com.google.common.base.Preconditions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Api("Vote", description = "Voting endpoint")
@RestController
class VoteController {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @ApiOperation("Submit a argument", notes = "This method consume the vote token which will be no longer valid.")
    @ApiResponses(*arrayOf(
            ApiResponse(code = 201, message = "Ballot successfully submitted"),
            ApiResponse(code = 400, message = "Token unspecified or invalid list of candidates"),
            ApiResponse(code = 404, message = "Unknown token")))
    @RequestMapping("/vote",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.CREATED)
    fun vote(@RequestBody argument: BallotArgument, request: HttpServletRequest): Success {

        val token = tokenRepository[argument.token]

        if (token.poll.isSecure)
            tokenRepository.delete(token)
        else
            request.assertCreatedQuota(1, pollRepository, tokenRepository, ballotRepository)

        val ballot = Ballot(request.remoteAddr, token.poll, argument.candidates)
        Preconditions.checkArgument(!ballot.hasDuplicates(), "This ballot contains duplicates")
        Preconditions.checkArgument(ballot.candidates().all { it in token.poll.candidates }, "This ballot contains unknown candidates")

        ballotRepository.save(ballot)

        return Success()
    }
}