@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel("Success get the operation")
open class Success(
        @ApiModelProperty("True if the operation asked was a success. False otherwise")
        val success: Boolean = true
)

@ApiModel("Failure get the operation")
open class Failure(

        @ApiModelProperty("Why the operation didn't succeed")
        val message: String = ""
) : Success(false)

@ApiModel("Poll creation argument")
data class PollCreationArgument(

        @ApiModelProperty("Candidates for the poll", required = true)
        val candidates: Set<String> = emptySet(),

        @ApiModelProperty("Number get token to generate", required = false)
        val tokenCount: Int = Configuration.defaultTokenCount,

        @ApiModelProperty("If false, only one token will be generated and used to vote", required = false)
        val secure: Boolean = true,

        @ApiModelProperty("Voting method to use. Can be \"schulze\" (default) or \"condorcet\" or \"relative_majority\"")
        val method: String = "schulze"
) {

    fun assertValid() {
        if (candidates.size < 2 || candidates.size > Configuration.maxCandidatesByPoll)
            throw InvalidNumberOfCandidatesException()
    }
}

@ApiModel("Poll creation result")
data class PollCreationResult(

        @ApiModelProperty("Created poll")
        val poll: String = "",

        @ApiModelProperty("Created tokens")
        val tokens: Collection<String> = emptyList()
)

@ApiModel("Poll closing argument")
data class PollClosingArgument(

        @ApiModelProperty("Poll to close")
        val poll: String = ""
)

@ApiModel("Token creation argument")
data class TokenCreationArgument(
        @ApiModelProperty("Poll credentials", required = true)
        val poll: String = "",

        @ApiModelProperty("Number get token to generate", required = false)
        val tokenCount: Int = Configuration.defaultTokenCount
)

@ApiModel("Ballot submission")
data class BallotArgument(
        @ApiModelProperty("Vote token", required = true)
        val token: String = "",

        @ApiModelProperty("Ordered chosen candidates", required = false)
        val candidates: List<Set<String>> = emptyList()
)