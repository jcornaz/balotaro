package balotenko

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BalotaroApplication

fun main(args: Array<String>) {
    SpringApplication.run(BalotaroApplication::class.java, *args)
}
