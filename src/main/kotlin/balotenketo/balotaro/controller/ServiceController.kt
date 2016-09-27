package balotenketo.balotaro.controller

import balotenketo.balotaro.Balotaro
import balotenketo.balotaro.Version
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class ServiceController {

    @RequestMapping("/")
    fun getService() = object {
        val service = Balotaro.title
        val description = Balotaro.description
        val version = Version.toString()
    }

    @RequestMapping("/version")
    fun getVersion() = Version
}