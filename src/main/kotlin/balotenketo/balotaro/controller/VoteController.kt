package balotenketo.balotaro.controller

import balotenketo.balotaro.model.Ballot
import balotenketo.balotaro.model.BallotRepository
import balotenketo.balotaro.model.VoteTokenRepository
import com.google.common.base.Preconditions
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Api("Vote", description = "Voting endpoint")
@RestController
@Suppress("unused")
class VoteController {

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @Autowired
    lateinit var ballotRepository: BallotRepository

    @ApiOperation("Submit a argument",
            notes = "This method consume the vote token which will be no longer valid.\n" +
                    "\nThe character '|' can be used in the choices to specify status quo.\n" +
                    "Exemple : a,b|c,d.\n" +
                    "\nCandidates can be omitted.")
    @RequestMapping("/vote/",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun vote(@RequestBody argument: BallotArgument): Success {

        val (tokenID, secret) = argument.token?.decode() ?: throw IllegalArgumentException("No token specified")
        val token = tokenRepository.findOne(tokenID)
        Preconditions.checkArgument(!token.used && token?.secret == secret, "Invalid token")

        val ballot = Ballot(token.poll, argument.candidates)
        Preconditions.checkArgument(!ballot.hasDuplicates(), "This ballot contains duplicates")
        Preconditions.checkArgument(ballot.candidates().all { it in token.poll.choices }, "This ballot contains unknown candidates")

        tokenRepository.save(token.apply { used = true })
        ballotRepository.save(ballot)

        return Success()
    }
}