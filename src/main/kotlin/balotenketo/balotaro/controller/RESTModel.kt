package balotenketo.balotaro.controller

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.util.Base64Utils
import java.math.BigInteger

fun String.decode(): Pair<String, BigInteger> =
        Base64Utils.decodeFromUrlSafeString(this).let {
            BigInteger(it.copyOfRange(0, 12)).toString(16) to BigInteger(it.copyOfRange(12, it.size))
        }

fun Pair<String, BigInteger>.encode(): String =
        (BigInteger(first, 16).toByteArray() + second.toByteArray()).let {
            Base64Utils.encodeToUrlSafeString(it)
        }

fun encode(id: String, secret: BigInteger): String = (id to secret).encode()

open class Success(val success: Boolean = true)
open class Failure(val message: String) : Success(false)

@ApiModel("Poll creation argument")
data class PollCreationArgument(

        @ApiModelProperty("Choices possible for the poll", required = true)
        val choices: Set<String> = emptySet(),

        @ApiModelProperty("Number of token to generate (10 by default)", required = false)
        val tokenCount: Int = 10
)

@ApiModel("Poll creation result")
data class PollCreationResult(

        @ApiModelProperty("Created poll")
        val poll: String? = null,

        @ApiModelProperty("Created tokens")
        val tokens: Collection<String> = emptyList()
)

@ApiModel("Token creation argument")
data class TokenCreationArgument(
        @ApiModelProperty("Poll credentials", required = true)
        val poll: String? = null,

        @ApiModelProperty("Number of token to generate (10 by default)", required = false)
        val tokenCount: Int? = null
)

@ApiModel("Ballot submission")
data class BallotArgument(
        @ApiModelProperty("Vote token", required = true)
        val token: String? = null,

        @ApiModelProperty("Ordered candidates", required = false)
        val candidates: List<Set<String>> = emptyList()
)