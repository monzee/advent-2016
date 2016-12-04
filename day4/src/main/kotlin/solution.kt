package advent.day4

import kotlin.comparisons.compareBy

fun main(vararg args: String) {
    generateSequence(::readLine)
            .let(::solve)
            .apply { print("sector sum: ") }
            .let(::println)
}

fun solve(problem: Sequence<String>): Int = problem
        .mapNotNull(::parse)
        .filter(::check)
        .map(::printStorageRooms)
        .map { it.sector }
        .sum()

data class Room(val name: String, val sector: Int, val checksum: String)

fun parse(raw: String): Room? =
        Regex("^([a-z\\-]+)(?:-(\\d+))\\[([a-z]{1,5})\\]")
                .matchEntire(raw)
                ?.groupValues
                ?.drop(1)
                ?.let {
                    val (name, sector, checksum) = it
                    Room(name, sector.toInt(), checksum)
                }

fun check(room: Room): Boolean = room.checksum == room.name
        .filter { it != '-' }
        .let {
            val counts = mutableMapOf<Char, Int>()
            it.forEach {
                counts += it to ((counts[it] ?: 0) + 1)
            }
            counts.toSortedMap(compareBy({ -counts[it]!! }, { it }))
        }
        .keys
        .take(5)
        .joinToString("")

fun decrypt(room: Room): String = room.name
        .map {
            when (it) {
                '-' -> ' '
                else -> rotate(it, room.sector)
            }
        }
        .joinToString("")

internal const val FIRST = 'a'
internal const val RANGE = 'z' - FIRST + 1

internal fun rotate(char: Char, by: Int): Char =
        FIRST + ((char - FIRST + by) % RANGE)

internal fun printStorageRooms(room: Room): Room = room.apply {
    val decrypted = decrypt(this)
    if (decrypted.endsWith("storage")) {
        println("$decrypted - $sector")
    }
}
