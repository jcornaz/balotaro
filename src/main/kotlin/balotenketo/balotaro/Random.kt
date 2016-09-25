package balotenketo.balotaro

import java.util.*

fun Random.nextString(chars: List<Char>, length: Int): String {
    val n = chars.size
    return (1..length).fold("") { str, i -> str + chars[nextInt(n)] }
}

fun Random.nextString(chars: List<Char>, length: Int, exclude: Set<String>): String {
    var result = nextString(chars, length)

    while (result in exclude)
        result = nextString(chars, length)

    return result
}