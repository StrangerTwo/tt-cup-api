package cz.ttcup.util

fun String.jdfNormalize(): String {
    return this
        .replace(".", " ")
        .replace(",", " ")
        .lowercase()
        .let {
            var result = it
            while (this.contains("  "))
                result = result.replace("  ", " ")
            result
        }
}