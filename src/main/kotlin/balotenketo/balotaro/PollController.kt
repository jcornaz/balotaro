package balotenketo.balotaro

import balotenketo.balotaro.auth.StormPath
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.PollRepository
import com.google.common.base.Preconditions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.SecureRandom
import javax.servlet.http.HttpServletRequest

@RestController
class PollController {


    @Autowired
    lateinit var repository: PollRepository

    @RequestMapping("/poll/create")
    fun create(
            @RequestParam choices: Array<String>,
            @RequestParam(required = false) tokenCount: Int? = null,
            request: HttpServletRequest
    ): Poll {
        Preconditions.checkArgument(choices.isNotEmpty())

        val poll = Poll(
                admin = StormPath.getRequiredAccount(request).href,
                choices = choices.toList(),
                tokens = SecureRandom().let { rng ->
                    val chars = ('A'..'Z') + ('0'..'9')
                    (1..(tokenCount ?: 10)).fold(emptySet<String>()) { tokens, i ->
                        tokens + rng.nextString(chars, 32, tokens)
                    }
                }
        )

        repository.save(poll)

        return poll
    }
}