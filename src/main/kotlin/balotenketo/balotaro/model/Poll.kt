package balotenketo.balotaro.model

import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*


class Poll(
        creatorIP: String? = null,
        val creationDate: Date = DateTime.now().toDate(),
        val expirationDate: Date = DateTime(creationDate).plusMonths(1).toDate(),
        val choices: Set<String> = emptySet()
) {

    @Id
    @ApiModelProperty("Id of the poll")
    lateinit var id: String
        private set

    lateinit var creatorIP: String

    val secret = SecretGenerator.generate()

    init {
        if (creatorIP != null)
            this.creatorIP = creatorIP
    }
}

interface PollRepository : MongoRepository<Poll, String> {
    fun countByCreatorIPAndCreationDateBetween(creatorIP: String, creationDateStart: Date, creationEnd: Date): Int
}