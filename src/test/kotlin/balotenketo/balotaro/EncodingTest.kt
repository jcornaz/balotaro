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
    fun testEncodeDecode() {
        val rng = Random()

        for (i in (1..5000)) {

            val id = BigInteger(12 * 8, rng).toString(16)
            val secret = SecretGenerator.generate()

            val (decodedID, decodedSecret) = (id to secret).encode().decode()

            assertEquals(id, decodedID)
            assertEquals(secret, decodedSecret)
        }
    }

    @Test
    fun testEncodeDecodeZero() {
        val id = "0"
        val secret = BigInteger("0")

        val (decodedID, decodedSecret) = (id to secret).encode().decode()

        assertEquals(id, decodedID)
        assertEquals(secret, decodedSecret)
    }

    @Test
    fun testEncodeDecodeMinByteValue() {
        val id = ByteArray(12) { Byte.MIN_VALUE }.let(::BigInteger).toString(16)
        val secret = ByteArray(128 / 8) { Byte.MIN_VALUE }.let(::BigInteger)

        val (decodedID, decodedSecret) = (id to secret).encode().decode()

        assertEquals(id, decodedID)
        assertEquals(secret, decodedSecret)
    }

    @Test
    fun testEncodeDecodeMaxByteValue() {
        val id = ByteArray(12) { Byte.MAX_VALUE }.let(::BigInteger).toString(16)
        val secret = ByteArray(128 / 8) { Byte.MAX_VALUE }.let(::BigInteger)

        val (decodedID, decodedSecret) = (id to secret).encode().decode()

        assertEquals(id, decodedID)
        assertEquals(secret, decodedSecret)
    }
}