@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.Balotaro
import balotenketo.balotaro.Version
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api("Service", description = "Service information")
@RestController
class ServiceController {

    @ApiOperation("Get service information", notes = "Return name, description and version get the service")
    @ApiResponses(ApiResponse(code = 200, message = "Web service information returned"))
    @RequestMapping("/", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    fun getService() = object {
        val service = Balotaro.title
        val description = Balotaro.description
        val version = Version.toString()
    }

    @ApiOperation("Get service version", notes = "Return detailed version get the service")
    @ApiResponses(ApiResponse(code = 200, message = "Web service version returned"))
    @RequestMapping("/version", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    fun getVersion() = Version
}