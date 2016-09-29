package balotenketo.balotaro.controller

import balotenketo.balotaro.BallotRepository
import balotenketo.balotaro.VoteTokenRepository
import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.Success
import com.google.common.base.Preconditions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import kondorcet.DefaultBallot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@Api("Vote", description = "Voting endpoint")
@RestController
@Suppress("unused")
class VoteController {

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @ApiOperation("Submit a ballot",
            notes = "This method consume the vote token which will be no longer valid.\n" +
                    "\nThe character '|' can be used in the choices to specify status quo.\n" +
                    "Exemple : a,b|c,d.\n" +
                    "\nCandidates can be omitted.")
    @RequestMapping("/vote/{tokenID}/{secret}",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun vote(
            @ApiParam("Token ID", required = true)
            @PathVariable
            tokenID: String,

            @ApiParam("Token secret", required = true)
            @PathVariable
            secret: String,

            @ApiParam("Candidates ordered by preference", required = false)
            @RequestParam(required = false)
            ballot: Array<String>
    ): Success {
        val token = tokenRepository.findOne(tokenID)
        Preconditions.checkArgument(token?.secret == secret, "Invalid token")

        val candidates = DefaultBallot(ballot.map { it.split("|").toSet() }).let {
            Preconditions.checkArgument(!it.hasDuplicates(), "This ballot contains duplicates")
            Preconditions.checkArgument(
                    it.candidates().all { it in token.poll.choices },
                    "This ballot contains unknown candidates"
            )

            it.orderedCandidates
        }

        tokenRepository.delete(token)
        ballotRepository.save(Ballot(token.poll.id, candidates))

        return Success()
    }
}