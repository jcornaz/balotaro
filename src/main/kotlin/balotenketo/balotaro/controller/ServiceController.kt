package balotenketo.balotaro.controller

import balotenketo.balotaro.Balotaro
import balotenketo.balotaro.Version
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Api("Service", description = "Service informations")
@RestController
@Suppress("unused")
class ServiceController {

    @ApiOperation("Get service information", notes = "Return name, description and version of the service")
    @RequestMapping("/", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getService() = object {
        val service = Balotaro.title
        val description = Balotaro.description
        val version = Version.toString()
    }

    @ApiOperation("Get service version", notes = "Return detailed version of the service")
    @RequestMapping("/version", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getVersion() = Version
}