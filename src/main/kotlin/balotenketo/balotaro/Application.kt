package balotenketo.balotaro

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
open class Balotaro {

    companion object {
        val title = "Balotaro"
        val description = "Vote web service using the condorcet method"
        val authors = listOf("Jonathan Cornaz")
        val license = "GNU General Public License Version 3"
        val licenseUrl = "https://raw.githubusercontent.com/slimaku/balotaro/master/LICENSE"
        val version = Version
    }

    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apply {
            groupName(groupName)

            apiInfo(ApiInfoBuilder().apply {
                title(title)
                description(description)
                contact(authors.joinToString(", "))
                license(license)
                licenseUrl(licenseUrl)
                version(version.toString())
            }.build())

            select().apply {
                apis(RequestHandlerSelectors.basePackage("balotenketo.balotaro"))
                paths(PathSelectors.any())
            }.build()
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Balotaro::class.java, *args)
}