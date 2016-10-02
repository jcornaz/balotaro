package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.util.Base64Utils
import java.math.BigInteger

fun String.decode(): Pair<String, BigInteger> =
        Base64Utils.decodeFromUrlSafeString(this).let {
            val idSize = it.first().toInt()
            BigInteger(it.copyOfRange(1, idSize + 1)).toString(16) to BigInteger(it.copyOfRange(idSize + 1, it.size))
        }

fun Pair<String, BigInteger>.encode(): String =
        BigInteger(first, 16).toByteArray().let { array ->
            ByteArray(1) { array.size.toByte() } + array + second.toByteArray()
        }.let(Base64Utils::encodeToUrlSafeString)

fun encode(id: String, secret: BigInteger): String = (id to secret).encode()

@ApiModel("Success of the operation")
open class Success(
        @ApiModelProperty("True if the operation asked was a success. False otherwise")
        val success: Boolean = true
)

@ApiModel("Failure of the operation")
open class Failure(
        @ApiModelProperty("Why the operation didn't succeed")
        val message: String
) : Success(false)

@ApiModel("Poll creation argument")
data class PollCreationArgument(

        @ApiModelProperty("Choices possible for the poll", required = true)
        val choices: Set<String> = emptySet(),

        @ApiModelProperty("Number of token to generate (10 by default)", required = false)
        val tokenCount: Int = Configuration.defaultTokenCount
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
        val tokenCount: Int = Configuration.defaultTokenCount
)

@ApiModel("Ballot submission")
data class BallotArgument(
        @ApiModelProperty("Vote token", required = true)
        val token: String? = null,

        @ApiModelProperty("Ordered candidates", required = false)
        val candidates: List<Set<String>> = emptyList()
)