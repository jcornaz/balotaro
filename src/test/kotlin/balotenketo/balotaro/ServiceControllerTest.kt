package balotenketo.balotaro

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceControllerTest {

    @Autowired
    lateinit var template: TestRestTemplate

    @Test
    fun testVersion() {
        val version = template.getForObject("/version", Version.javaClass)
        assertTrue(version is Version)
    }
}