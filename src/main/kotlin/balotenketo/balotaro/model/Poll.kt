package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import java.util.*

data class Poll(

        @JsonIgnore
        val admin: String,

        val secret: String = TokenGenerator.generate(),

        val createdDate: Date = DateTime.now().toDate(),
        val timeToLive: Long = 60 * 24 * 30, // In minutes

        val choices: Map<String, String>
) {

    @Id
    lateinit var id: String
}
