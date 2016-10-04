@file:Suppress("unused")

package balotenketo.balotaro.model

import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

class Poll(
        val creatorIP: String = "",
        val isSecure: Boolean = true,
        val creationDate: Date = DateTime.now().toDate(),
        val expirationDate: Date = DateTime(creationDate).plusMonths(1).toDate(),
        val candidates: Set<String> = emptySet()
) {

    @Id
    lateinit var id: String
        private set

    val secret = SecretGenerator.generate()
}

interface PollRepository : MongoRepository<Poll, String> {
    fun countByCreatorIP(creatorIP: String): Int
    fun findByExpirationDateLessThan(expirationDate: Date): Collection<Poll>
}