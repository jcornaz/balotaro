package balotenketo.balotaro.model

import java.math.BigInteger
import java.security.SecureRandom

object SecretGenerator {

    val defaultEntropy = 128

    val rng by lazy { SecureRandom() }

    fun generate(entropy: Int = defaultEntropy): BigInteger = BigInteger(entropy, rng)
}