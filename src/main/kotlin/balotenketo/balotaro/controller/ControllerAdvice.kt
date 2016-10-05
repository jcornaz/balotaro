@file:Suppress("unused")

package balotenketo.balotaro.controller

import balotenketo.balotaro.Configuration
import balotenketo.balotaro.model.Poll
import balotenketo.balotaro.model.PollRepository
import balotenketo.balotaro.model.VoteToken
import balotenketo.balotaro.model.VoteTokenRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*
import javax.servlet.ServletRequest

class PollNotFound : Exception()
class TokenNotFound : Exception()
class QuotaExceededException : Exception()
class InvalidNumberOfCandidatesException : Exception()

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

    @ResponseStatus(HttpStatus.FORBIDDEN, reason = "Your entity creation quota have been exceeded. You should close some poll.")
    @ExceptionHandler(QuotaExceededException::class)
    fun handleQuotaExceeded() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST, reason = "Invalid number of candidates")
    @ExceptionHandler(InvalidNumberOfCandidatesException::class)
    fun handleInvalidNumberOfCandidates() {
    }
}

interface IPOwnedEntityRepository {
    fun countByCreatorIP(creatorIP: String): Int
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

    if (voteToken.secret != secret)
        throw TokenNotFound()

    return voteToken
}

fun ServletRequest.assertCreatedQuota(countToAdd: Int = 1, vararg repositories: IPOwnedEntityRepository) {
    repositories.fold(countToAdd) { count, repo ->

        if (count > Configuration.maxDocumentCountByIP)
            throw QuotaExceededException()

        count + repo.countByCreatorIP(remoteAddr)
    }
}