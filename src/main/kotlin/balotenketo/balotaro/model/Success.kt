package balotenketo.balotaro.model

open class Success(val success: Boolean = true)

open class Failure(val message: String) : Success(false)