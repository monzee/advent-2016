fun main(vararg args: String) {
    readLine()?.let { println(solve(it)) }
}

fun solve(map: String): Int = map.split(", ").fold(Turtle()) { curr, step ->
    val dir = step.first()
    val disp = step.drop(1).toInt()
    when (dir.toUpperCase()) {
        'L' -> curr.left(disp)
        'R' -> curr.right(disp)
        else -> throw IllegalArgumentException("direction must be L or R")
    }
}.distance

enum class Dir { UP, DOWN, LEFT, RIGHT }

class Turtle(val x: Int, val y: Int, val dir: Dir) {
    constructor(): this(0, 0, Dir.UP)

    val distance: Int by lazy { Math.abs(x) + Math.abs(y) }

    fun left(disp: Int): Turtle = when (dir) {
        Dir.UP -> Turtle(x - disp, y, Dir.LEFT)
        Dir.DOWN -> Turtle(x + disp, y, Dir.RIGHT)
        Dir.LEFT -> Turtle(x,  y - disp, Dir.DOWN)
        Dir.RIGHT -> Turtle(x, y + disp, Dir.UP)
    }

    fun right(disp: Int): Turtle = when (dir) {
        Dir.UP -> Turtle(x + disp, y, Dir.RIGHT)
        Dir.DOWN -> Turtle(x - disp, y, Dir.LEFT)
        Dir.LEFT -> Turtle(x,  y + disp, Dir.UP)
        Dir.RIGHT -> Turtle(x, y - disp, Dir.DOWN)
    }
}
