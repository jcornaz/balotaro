package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

class VoteToken(poll: Poll?) {

    @DBRef
    @JsonIgnore
    lateinit var poll: Poll

    @Id
    lateinit var id: String

    val secret = TokenGenerator.generate()

    init {
        if (poll != null)
            this.poll = poll
    }
}