package advent.day6

import kotlin.comparisons.compareBy

import org.junit.Test
import org.junit.Assert.*

fun main(vararg args: String) {
    generateSequence(::readLine)
            .toList()
            .let { decode(it, { it.first() }, { it.last() }) }
            .forEachIndexed { i, it -> it.echo("part ${i + 1}:") }
}

fun List<String>.rotate(): List<String> =
        first().indices.map { col -> map { it[col] }.joinToString("") }

fun decode(
        lines: List<String>,
        vararg selectors: (List<Char>) -> Char
): List<String> = lines
        .rotate()
        .map {
            it.groupBy { it }
                .map { it.value.size to it.key }
                .sortedWith(compareBy({ -it.first }, { it.second }))
                .map { it.second }
        }
        .let { xs -> selectors.map { xs.map(it).joinToString("") } }

val SAMPLE = """
        eedadn
        drvtee
        eandsr
        raavrd
        atevrs
        tsrnev
        sdttsa
        rasrtv
        nssdts
        ntnada
        svetve
        tesnvt
        vntsnd
        vrdear
        dvrsen
        enarar
        """.trimIndent()

class tests {
    @Test
    fun scratch() {
        SAMPLE.echo
        val xs = SAMPLE.lines().rotate()
        xs.forEach {
            it.echo(":")
            it.groupBy { it }
            .map { it.value.size to it.key }
            .sortedWith(compareBy({ it.first }, { it.second }))
            .first()
            .echo
        }
    }

    @Test
    fun sample() {
        assertEquals("easter", decode(SAMPLE.lines(), { it.first() }).first())
        assertEquals("advent", decode(SAMPLE.lines(), { it.last() }).first())
    }
}

fun <T> T?.echo(
        prefix: String? = null,
        color: Int = 6,
        intense: Boolean = true
): T? = this.apply {
    if (prefix == null) {
        println(this)
    } else {
        val ESC = "\u001b"
        val COLOR = "${if (intense) 9 else 3}${color.coerceIn(0, 7)}m"
        println("$ESC[$COLOR$prefix$ESC[0m $this")
    }
}

val <T> T?.echo: T? get() = echo()
