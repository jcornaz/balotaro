package balotenketo.balotaro

object Version {

    val major = 1
    val minor = 0

    val patch: Int? = null
    val label: String? = "SNAPSHOT"

    override fun toString() = "$major.$minor${patch ?: "-$label"}"
}