package cz.ttcup.util.service

inline fun <T> List<T>.indexOfNth(n: Int = 0, predicate: (T) -> Boolean): Int {
    if (n < 0) return -1
    var m = 0;
    for ((index, item) in this.withIndex()) {
        if (predicate(item)) {
            if (m++ == n) return index
        }
    }
    return -1
}