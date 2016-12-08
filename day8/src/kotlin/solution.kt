package advent.day8

import ph.codeia.klee.*

import org.junit.Test
import org.junit.Assume.*

fun main(vararg args: String) {
    val screen = Display()
    generateSequence(::readLine)
            .forEach { screen.exec(it) }
            .let { screen.echo.count.echo("part 1") }
}

const val SCREEN_W = 50
const val SCREEN_H = 6
val SPEC = Regex("""
        rect \s+ (\d+) \s* x \s* (\d+) |
        rotate \s+ (row \s+ y|column \s+ x) \s* = \s* (\d{1,2}) \s+ by \s+ (\d+)
        """.trimIndent(), RegexOption.COMMENTS)

class Display(val width: Int = SCREEN_W, val height: Int = SCREEN_H) {
    val pixels = Array<BooleanArray>(height) { BooleanArray(width) }
    val count: Int get() = pixels.map { it.count { it } }.sum()

    fun rect(w: Int, h: Int) {
        (0 until h).forEach { row ->
            (0 until w).forEach { col ->
                pixels[row][col] = true
            }
        }
    }

    fun rotateRow(at: Int, by: Int) {
        pixels[at].let {
            val n = by % it.size
            (it.takeLast(n) + it.dropLast(n)).forEachIndexed { i, b ->
                it[i] = b
            }
        }
    }

    fun rotateCol(at: Int, by: Int) {
        val col = BooleanArray(pixels.size) { pixels[it][at] }
        val n = by % pixels.size
        (col.takeLast(n) + col.dropLast(n)).forEachIndexed { i, b ->
            pixels[i][at] = b
        }
    }

    fun exec(program: String) {
        SPEC.findAll(program).forEach {
            val inst = it.groupValues.first()
            val params = it.groupValues.drop(1)
            when {
                inst.startsWith("rect") -> rect(params[0].toInt(), params[1].toInt())
                inst.startsWith("rotate") -> {
                    val axis = params[2]
                    val (at, by) = params.takeLast(2).map(String::toInt)
                    when {
                        axis.startsWith("row") -> rotateRow(at, by)
                        axis.startsWith("column") -> rotateCol(at, by)
                    }
                }
            }
        }
    }

    override fun toString() = pixels.map {
        it.map { if (it) '*' else ' ' }.joinToString("")
    }.joinToString("\n")
}





class tests {
    val sample = """
            rect 3x2
            rotate column x=1 by 1
            rotate row y=0 by 4
            rotate column x=1 by 1
            """.trimIndent()

    @Test
    fun scratch_pad() {
        SPEC.findAll(sample).forEach {
            val inst = it.groupValues.first()
            val params = it.groupValues.drop(1)
            when {
                inst.startsWith("rect") -> "(${params[0]}, ${params[1]})".echo("rect")
                inst.startsWith("rotate") -> "${params[3]} by ${params[4]}".echo("rotate ${params[2]}")
            }
        }
        Display(10, 4).apply {
            rect(3, 2)
            rotateRow(0, 1)
            rotateRow(1, 102)
            echo
            rotateCol(2, 3)
            rect(2, 4)
            echo
            count.echo("count")
        }
        Display().apply {
            exec(sample)
            echo.count.echo("count")
        }
        assumeTrue(false)
    }
}
