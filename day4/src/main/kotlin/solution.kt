package advent.day4

import kotlin.comparisons.compareBy

fun main(vararg args: String) {
    generateSequence(::readLine)
            .let(::solve)
            .let { println("sector sum: $it") }
}

fun solve(problem: Sequence<String>): Int = problem
        .mapNotNull(::parse)
        .filter(::check)
        .map { it.apply(grep("north")).sector }
        .sum()

data class Room(val name: String, val sector: Int, val checksum: String)

private const val CHECKSUM_LENGTH = 5
private const val SPEC =
        """
        ([a-z-]+)                         # dashed name
        (?: -(\d+) )                      # sector
        \[ ([a-z]{1,$CHECKSUM_LENGTH}) \] # checksum
        """

fun parse(raw: String): Room? = Regex(SPEC, RegexOption.COMMENTS)
        .matchEntire(raw)
        ?.groupValues
        ?.drop(1)
        ?.let {
            val (name, sector, checksum) = it
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

private fun grep(pattern: String): Room.() -> Unit = {
    val secret = decrypt(this)
    if (pattern in secret) {
        println("$secret - $sector")
    }
}
