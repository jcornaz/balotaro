package balotenketo.balotaro.controller

import balotenketo.balotaro.Version
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServiceController {

    @RequestMapping("/")
    fun getService() = object {
        val service = "Balotaro"
        val description = "Vote web service using the condorcet method"
        val version = Version.toString()
    }

    @RequestMapping("/version")
    fun getVersion() = Version
}