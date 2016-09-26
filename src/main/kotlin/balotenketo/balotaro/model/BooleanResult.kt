package balotenketo.balotaro.model

data class BooleanResult(val success: Boolean = true, val message: String? = null) {
    companion object {
        val TRUE by lazy { BooleanResult(true) }
        val FALSE by lazy { BooleanResult(false) }
    }
}