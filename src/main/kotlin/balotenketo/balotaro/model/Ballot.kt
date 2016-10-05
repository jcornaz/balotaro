@file:Suppress("unused")

package balotenketo.balotaro.model

import balotenketo.balotaro.controller.IPOwnedEntityRepository
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository

class Ballot(
        val creatorIP: String = "",
        poll: Poll? = null,
        override val orderedCandidates: List<Set<String>> = emptyList()
) : kondorcet.Ballot<String> {

    @Id
    lateinit var id: String
        private set

    lateinit var poll: Poll
        private set

    init {
        if (poll != null)
            this.poll = poll
    }
}

interface BallotRepository : MongoRepository<Ballot, String>, IPOwnedEntityRepository {
    override fun countByCreatorIP(creatorIP: String): Int
    fun findByPoll(poll: Poll): Collection<Ballot>
    fun deleteByPoll(poll: Poll)
}