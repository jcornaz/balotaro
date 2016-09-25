package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

data class Poll(

    @JsonIgnore
    val admin: String,

    val createdDate: Date = DateTime.now().toDate(),
    val timeToLive: Long = 60 * 24 * 30, // In minutes

    val choices: List<String>,
    var tokens: Set<String>
) {

    @Id
    lateinit var id: String
}

interface PollRepository : MongoRepository<Poll, String>