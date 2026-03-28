package cz.ttcup.util

interface ShortNamedEnum {
    val shortName: String
}

inline fun <reified T> getByShortName(shortName: String): T
        where T : Enum<T>, T : ShortNamedEnum {
    return enumValues<T>().firstOrNull { it.shortName == shortName }
        ?: throw IllegalArgumentException("No enum constant with shortName=$shortName for ${T::class}")
}
