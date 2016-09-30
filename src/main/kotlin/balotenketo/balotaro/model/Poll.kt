package balotenketo.balotaro.model

import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import java.util.*


class Poll(
        val creatorIP: String,
        val creationDate: Date = DateTime.now().toDate(),
        val expirationDate: Date = DateTime(creationDate).plusMonths(1).toDate(),
        val choices: Set<String> = emptySet()
) {

    @Id
    @ApiModelProperty("Id of the poll")
    lateinit var id: String
        private set

    val secrect = SecretGenerator.generate()
}
