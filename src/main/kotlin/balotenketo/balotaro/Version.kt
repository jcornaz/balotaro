package balotenketo.balotaro

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Version get the web service")
object Version {

    @ApiModelProperty("Major version")
    val major = 1

    @ApiModelProperty("Minor version")
    val minor = 0

    @ApiModelProperty("Patch version (null if it is a pre-release)")
    val patch: Int? = null

    @ApiModelProperty("Pre-release label (null if it is not a pre-release)")
    val label: String? = "SNAPSHOT"

    @ApiModelProperty("True if it is a pre-release and false otherwise. (Pre-releases shouldn't be used in production)")
    val isPreRelease = label != null

    override fun toString() = "$major.$minor${patch ?: "-$label"}"
}