package balotenketo.balotaro.model

import kondorcet.Ballot
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository

class Ballot(
        poll: Poll? = null,
        override val orderedCandidates: List<Set<String>> = emptyList()
) : Ballot<String> {

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

interface BallotRepository : MongoRepository<balotenketo.balotaro.model.Ballot, String>