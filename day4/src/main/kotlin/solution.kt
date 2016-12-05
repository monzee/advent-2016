package advent.day4

import kotlin.comparisons.compareBy

fun main(vararg args: String) {
    generateSequence(::readLine)
            .let(solve(args.firstOrNull() ?: "north"))
            .let { println("real sector sum: $it") }
}

fun solve(search: String): (Sequence<String>) -> Int = {
    it.mapNotNull(::parse)
        .onEach(grepDecrypt(search) { println("$it - $sector") })
        .filter(::check)
        .map(Room::sector)
        .sum()
}

data class Room(val name: String, val sector: Int, val checksum: String)

private const val CHECKSUM_LENGTH = 5
private const val SPEC =
        """
        ([a-z]+(?:-[a-z]+)*)              # dashed name
        -(\d+)                            # sector
        \[ ([a-z]{1,$CHECKSUM_LENGTH}) \] # checksum
        """

fun parse(raw: String): Room? = Regex(SPEC, RegexOption.COMMENTS)
        .matchEntire(raw)
        ?.groupValues
        ?.let {
            val (_ignored, name, sector, checksum) = it
            Room(name, sector.toInt(), checksum)
        }

fun check(room: Room): Boolean = room.checksum == room.name
        .filter { it != '-' }
        .groupBy { it }
        .let { it.toSortedMap(compareBy({ k -> -it[k]!!.size }, { it })) }
        .keys
        .take(CHECKSUM_LENGTH)
        .joinToString("")

fun decrypt(room: Room): String = room.name
        .map {
            when (it) {
                '-' -> ' '
                else -> rotate(it, room.sector)
            }
        }
        .joinToString("")

private const val FIRST = 'a'
private const val LENGTH = 'z' - FIRST + 1

private fun rotate(char: Char, by: Int): Char =
        FIRST + (char - FIRST + by) % LENGTH

private inline fun grepDecrypt(
        pattern: String,
        crossinline action: Room.(String) -> Unit
): (Room) -> Unit = {
    val secret = decrypt(it)
    if (pattern in secret) {
        action(it, secret)
    }
}

private inline fun <T> Sequence<T>.onEach(
        crossinline action: (T) -> Unit
): Sequence<T> = map {
    action(it)
    it
}
