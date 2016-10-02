package balotenketo.balotaro

import balotenketo.balotaro.controller.decode
import balotenketo.balotaro.controller.encode
import balotenketo.balotaro.model.SecretGenerator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger
import java.util.*

class EncodingTest {

    @Test
    fun encodeDecode() {
        val id: String = BigInteger(12 * 8, Random()).toString(16)
        val secret: BigInteger = SecretGenerator.generate()

        val encoded = (id to secret).encode()
        val (decodedID, decodedSecret) = encoded.decode()

        assertEquals(id, decodedID)
        assertEquals(secret, decodedSecret)
    }

    @Test
    fun encodeDecodeZeros() {
        val id = "0"
        val secret = BigInteger("0", 2)

        val encoded = encode(id, secret)
        println(encoded)

        val (decodedID, decodedSecret) = encoded.decode()

        assertEquals(id, decodedID)
        assertEquals(secret, decodedSecret)
    }
}