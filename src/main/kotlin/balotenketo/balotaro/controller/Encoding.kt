package balotenketo.balotaro.controller

import org.springframework.util.Base64Utils
import java.math.BigInteger


fun String.decode(): Pair<String, String> =
        Base64Utils.decodeFromUrlSafeString(this).let {
            val idSize = it.first().toInt()
            val id = BigInteger(it.copyOfRange(1, idSize + 1)).toString(16)
            val secret = BigInteger(it.copyOfRange(idSize + 1, it.size)).toByteArray().let(Base64Utils::encodeToUrlSafeString)

            id to secret
        }

fun Pair<String, String>.encode(): String =
        BigInteger(first, 16).toByteArray().let { array ->
            ByteArray(1) { array.size.toByte() } + array + Base64Utils.decodeFromUrlSafeString(second)
        }.let(Base64Utils::encodeToUrlSafeString)

fun encode(id: String, secret: String): String = (id to secret).encode()