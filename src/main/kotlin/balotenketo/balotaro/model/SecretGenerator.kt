package balotenketo.balotaro.model

import org.springframework.util.Base64Utils
import java.math.BigInteger
import java.security.SecureRandom

object SecretGenerator {

    val defaultEntropy = 128

    val rng by lazy { SecureRandom() }

    fun generate(entropy: Int = defaultEntropy): String =
            BigInteger(entropy, rng).toByteArray().let(Base64Utils::encodeToUrlSafeString)
}