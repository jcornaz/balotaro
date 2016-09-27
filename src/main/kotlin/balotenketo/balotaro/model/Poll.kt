package balotenketo.balotaro.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import java.util.*

@ApiModel
data class Poll(

        @JsonIgnore
        val admin: String = "",

        @ApiModelProperty
        val createdDate: Date = DateTime.now().toDate(),

        val timeToLive: Long = 60 * 24 * 30, // In minutes

        @ApiModelProperty("Possible choices")
        val choices: Set<String> = emptySet()
) {

    @Id
    @ApiModelProperty("Id of the poll")
    lateinit var id: String
}
