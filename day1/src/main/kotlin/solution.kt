fun main(vararg args: String) {
    readLine()?.let { println(solve(it)) }
}

fun solve(moves: String): Int = moves.split(", ").fold(ORIGIN) { turn, move ->
    val dir = move.first()
    val dist = move.drop(1).toInt()
    when (dir.toUpperCase()) {
        'L' -> turn.left(dist)
        'R' -> turn.right(dist)
        else -> throw IllegalArgumentException("must start with L or R")
    }
}.distanceFromOrigin

val ORIGIN: Cursor = Cursor.Up(0, 0)

sealed class Cursor(val x: Int, val y: Int) {
    val distanceFromOrigin: Int by lazy { Math.abs(x) + Math.abs(y) }

    abstract fun left(distance: Int): Cursor
    abstract fun right(distance: Int): Cursor

    class Up(x: Int, y: Int) : Cursor(x, y) {
        override fun left(distance: Int) = Left(x - distance, y)
        override fun right(distance: Int) = Right(x + distance, y)
    }

    class Down(x: Int, y: Int) : Cursor(x, y) {
        override fun left(distance: Int) = Right(x + distance, y)
        override fun right(distance: Int) = Left(x - distance, y)
    }

    class Left(x: Int, y: Int) : Cursor(x, y) {
        override fun left(distance: Int) = Down(x, y - distance)
        override fun right(distance: Int) = Up(x, y + distance)
    }

    class Right(x: Int, y: Int) : Cursor(x, y) {
        override fun left(distance: Int) = Up(x, y + distance)
        override fun right(distance: Int) = Down(x, y - distance)
    }
}
