package balotenketo.balotaro.model

import kondorcet.method.CondorcetMethod
import kondorcet.method.RelativeMajorityMethod
import kondorcet.method.SchulzeMethod

enum class VoteMethod(val implementation: kondorcet.VoteMethod) {
    RELATIVE_MAJORITY(RelativeMajorityMethod),
    CONDORCET(CondorcetMethod),
    SCHULZE(SchulzeMethod);

    companion object {
        fun of(value: String) = when (value.toLowerCase()) {
            "relative_majority" -> RELATIVE_MAJORITY
            "condorcet" -> CONDORCET
            "", "schulze" -> SCHULZE
            else -> throw IllegalArgumentException("Unknown method : $value")
        }
    }
}