package balotenketo.balotaro.controller

import balotenketo.balotaro.Balotaro
import balotenketo.balotaro.Version
import balotenketo.balotaro.auth.StormPath
import balotenketo.balotaro.model.Success
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

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

    @ApiOperation("Register an account", notes = "Only valid accounts can create polls")
    @RequestMapping("/register",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun register(
            @RequestParam email: String,
            @RequestParam password: String,
            request: HttpServletRequest
    ): Success {
        StormPath.createAccount(request, email, password)
        return Success()
    }
}