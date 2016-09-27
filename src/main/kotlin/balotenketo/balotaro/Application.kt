package balotenketo.balotaro

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Balotaro

fun main(args: Array<String>) {
    SpringApplication.run(Balotaro::class.java, *args)
}