package balotenketo.balotaro.model

import java.math.BigInteger
import java.security.SecureRandom

object SecretGenerator {

    val rng by lazy { SecureRandom() }

    fun generate(): String = BigInteger(130, rng).toString(32)
}