@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.PollRepository
import balotenketo.balotaro.model.VoteToken
import balotenketo.balotaro.model.VoteTokenRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

class PollNotFound : Exception()
class TokenNotFound : Exception()

@ControllerAdvice
class ControllerAdvice() {

    @ResponseStatus(HttpStatus.BAD_REQUEST, reason = "Invalid arguments")
    @ExceptionHandler(IllegalArgumentException::class, NoSuchElementException::class)
    fun handleIllegalArguments() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND, reason = "Invalid poll")
    @ExceptionHandler(PollNotFound::class)
    fun handlePollNotFound() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND, reason = "Invalid token")
    @ExceptionHandler(TokenNotFound::class)
    fun handleTokenNotFound() {
    }
}

operator fun PollRepository.get(token: String): Poll {
    val (id, secret) = token.decode()
    val poll = findOne(id) ?: throw PollNotFound()

    if (poll.secret != secret)
        throw PollNotFound()

    return poll
}

operator fun VoteTokenRepository.get(token: String): VoteToken {
    val (id, secret) = token.decode()
    val voteToken = findOne(id) ?: throw TokenNotFound()

    if (voteToken.secret != secret || voteToken.used)
        throw TokenNotFound()

    return voteToken
}