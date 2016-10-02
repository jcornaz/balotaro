package balotenketo.balotaro.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.repository.MongoRepository

class VoteToken(poll: Poll? = null) {

    @Id
    lateinit var id: String
        private set

    @DBRef
    lateinit var poll: Poll
        private set

    lateinit var creatorIP: String
        private set

    val secret = SecretGenerator.generate()

    var used = false

    @Version
    val version: Long = 0

    init {
        if (poll != null) {
            this.poll = poll
            creatorIP = poll.creatorIP
        }
    }
}

interface VoteTokenRepository : MongoRepository<VoteToken, String> {
    fun countByPoll(poll: Poll): Int
}