package balotenketo.balotaro.model

import org.springframework.data.annotation.Id

class Ballot(
        val pollID: String = "",
        val orderedCandidates: List<Set<String>> = emptyList()
) {

    @Id
    lateinit var id: String
}