package balotenko

import balotenketo.balotaro.Balotaro
import balotenketo.balotaro.Version
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.test.context.junit4.*


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Balotaro::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BalotaroApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun exampleTest() {
        val version = this.restTemplate.getForObject("/version", Version::class.java)
        assertEquals(Version.major, version.major)
        assertEquals(Version.minor, version.minor)
        assertEquals(Version.patch, version.patch)
        assertEquals(Version.label, version.label)
    }
}
