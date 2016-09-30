package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

data class VoteToken(

        @DBRef
        @JsonIgnore
        var poll: Poll
) {


    @Id
    lateinit var id: String
        private set

    val secret = SecretGenerator.generate()
}