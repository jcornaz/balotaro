package balotenketo.balotaro.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.repository.MongoRepository

class VoteToken(
        val creatorIP: String = "",
        poll: Poll? = null
) {

    @Id
    lateinit var id: String
        private set

    @DBRef
    lateinit var poll: Poll
        private set

    val secret = SecretGenerator.generate()

    @Version
    val version: Long = 0

    init {
        if (poll != null)
            this.poll = poll
    }
}

interface VoteTokenRepository : MongoRepository<VoteToken, String> {
    fun countByCreatorIP(creatorIP: String): Int
    fun countByPoll(poll: Poll): Int
    fun deleteByPoll(poll: Poll)
}