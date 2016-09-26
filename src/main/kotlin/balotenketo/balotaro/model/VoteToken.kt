package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

class VoteToken(

        @DBRef
        @JsonIgnore
        val poll: Poll
) {

    @Id
    lateinit var id: String

    val secret = TokenGenerator.generate()
}