package balotenketo.balotaro.controller

import balotenketo.balotaro.PollRepository
import balotenketo.balotaro.VoteTokenRepository
import balotenketo.balotaro.auth.StormPath
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
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(
            @ApiParam("Choices possible for the poll", required = true)
            @RequestParam
            choices: Array<String>,

            @ApiParam("Number of token to generate (10 by default)", required = false)
            @RequestParam(required = false)
            tokenCount: Int? = null,

            request: HttpServletRequest
    ) = let {
        Preconditions.checkArgument(choices.isNotEmpty())
        Preconditions.checkArgument(choices.none { "|" in it })

        @ApiModel("Result of a poll creation")
        object {

            @ApiModelProperty("The created poll")
            val poll = Poll(
                    admin = StormPath.getRequiredAccount(request).href,
                    choices = choices.toSet()
            ).apply { pollRepository.save(this) }

            @ApiModelProperty("Generated tokens")
            val tokens = (1..(tokenCount ?: 10)).map {
                VoteToken(poll).apply { tokenRepository.save(this) }
            }
        }
    }

    @ApiOperation("Create new tokens for a poll",
            notes = "This method need the authentication of the admin of the poll",
            authorizations = arrayOf(Authorization("OAuth2")))
    @RequestMapping("/poll/createTokens/{pollID}",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun createTokens(
            @ApiParam("Poll ID for which create tokens", required = true)
            @PathVariable
            pollID: String,

            @ApiParam("Number of token to generate (10 by default)", required = false)
            @RequestParam(required = false)
            count: Int? = null,

            request: HttpServletRequest
    ): List<VoteToken> {
        val poll = pollRepository.findOne(pollID)
        Preconditions.checkArgument(poll?.admin == StormPath.getRequiredAccount(request).href, "Invalid poll")

        return (1..(count ?: 10)).map { VoteToken(poll).apply { tokenRepository.save(this) } }
    }

}