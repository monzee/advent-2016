package advent.day9

import ph.codeia.klee.*

import org.junit.Test
import org.junit.Assert.*
import org.junit.Assume.*

fun main(vararg args: String) {
    readLine()?.let {
        decompress(it).length.echo("part 1")
        measure(it).echo("part 2")
    }
}

val MARKER = Regex("""(?x) \( \s* (\d+) \s* x \s* (\d+) \s* \)""")

fun decompress(
        s: CharSequence,
        recurse: Boolean = false
): CharSequence = StringBuilder().apply {
    var i = 0
    while (i < s.length) {
        i = MARKER.find(s, i)?.let { match ->
            append(s.slice(i until match.range.start))
            val (length, count) = match.groupValues.drop(1).map(String::toInt)
            val last = (match.range.last + length).coerceIn(s.indices)
            val segment = s.slice(match.range.last + 1..last)
            repeat(count) {
                append(if (recurse) decompress(segment, true) else segment)
            }
            last + 1
        } ?: run {
            append(s.slice(i..s.lastIndex))
            s.length
        }
    }
}

fun measure(s: CharSequence): Long {
    var c = 0L
    var i = 0
    while (i < s.length) {
        i = MARKER.find(s, i)?.let { match ->
            c += match.range.start - i
            val (length, count) = match.groupValues.drop(1).map(String::toInt)
            val last = match.range.last + length
            val segment = s.slice(match.range.last + 1..last)
            c += measure(segment) * count
            last + 1
        } ?: run {
            c += s.length - i
            s.length
        }
    }
    return c
}




class tests {
    @Test
    fun scratch() {
        assumeTrue(false)
    }

    @Test
    fun samples() {
        mapOf(
                "ADVENT" to 6,
                "A(1x5)BC" to 7,
                "(3x3)XYZ" to 9,
                "A(2x2)BCD(2x2)EFG" to 11,
                "(6x1)(1x3)A" to 6,
                "X(8x2)(3x3)ABCY" to 18
        ).forEach {
            val (s, expected) = it
            assertEquals(expected, decompress(s).length)
        }
    }

    @Test
    fun recursive_decompress() {
        mapOf(
                "(3x3)XYZ" to 9L,
                "X(8x2)(3x3)ABCY" to 20L,
                "(27x12)(20x12)(13x14)(7x10)(1x12)A" to 241920L,
                "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN" to 445L
        ).forEach {
            val (s, expected) = it
            assertEquals(expected, measure(s))
        }
    }
}
