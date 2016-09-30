package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import balotenketo.balotaro.PollRepository
import balotenketo.balotaro.VoteTokenRepository
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.VoteToken
import com.google.common.base.Preconditions
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Api("Polls", description = "Polls management")
@RestController
@Suppress("unused")
class PollController {

    @Autowired
    lateinit var pollRepository: PollRepository

    @Autowired
    lateinit var tokenRepository: VoteTokenRepository

    @ApiOperation("Create a new poll",
            notes = "This method need an authenticated user",
            authorizations = arrayOf(Authorization("OAuth2")))
    @RequestMapping("/poll/create",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(
            @ApiParam("Choices possible for the poll", required = true)
            @RequestParam
            choices: Array<String>,

            @ApiParam("Number of token to generate", required = false)
            @RequestParam(required = false)
            tokenCount: Int? = null,

            request: HttpServletRequest
    ) = let {
        Preconditions.checkArgument(choices.isNotEmpty(), "You have to specify choices")

        val ip = request.remoteAddr
        val pollCount = pollRepository.countByCreatorIP(ip)

        Preconditions.checkArgument(pollCount < Configuration.maxPollCountByIP,
                "You cannot create more poll from this IP address. You have to close you previous polls first")

        object {
            val poll = Poll(ip, choices = choices.toSet()).apply { pollRepository.save(this) }

            @ApiModelProperty("Generated tokens")
            val tokens = (1..Configuration.tokenCount(tokenCount)).map {
                VoteToken(poll).apply { tokenRepository.save(this) }
            }
        }
    }

    @ApiOperation("Create new tokens for a poll",
            notes = "This method need the authentication of the admin of the poll",
            authorizations = arrayOf(Authorization("OAuth2")))
    @RequestMapping("/poll/createTokens/{pollID}",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun createTokens(
            @ApiParam("Poll ID for which create tokens", required = true)
            @PathVariable
            pollID: String,

            @ApiParam("Poll secret")
            @RequestParam
            pollSecret: String,

            @ApiParam("Number of token to generate (10 by default)", required = false)
            @RequestParam(required = false)
            count: Int? = null,

            request: HttpServletRequest
    ): List<VoteToken> {
        val poll = pollRepository.findOne(pollID)
        Preconditions.checkArgument(poll?.secrect == pollSecret, "Invalid pollID or secret")

        val currentCount = tokenRepository.countByPoll(poll)
        val toCreate = Math.max(0, Math.min(Configuration.maxTokenCount, currentCount + Configuration.tokenCount(count)) - currentCount)

        return (1..toCreate).map { VoteToken(poll).apply { tokenRepository.save(this) } }
    }

}