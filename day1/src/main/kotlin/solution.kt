fun main(vararg args: String) {
    readLine()?.let { println(solve(it)) }
}

fun solve(moves: String): Int {
    return moves.split(", ").fold(Cursor.Up(0, 0) as Cursor) { current, move ->
        val dir = move.first()
        val disp = move.drop(1).toInt()
        when (dir.toUpperCase()) {
            'L' -> current.left(disp)
            'R' -> current.right(disp)
            else -> throw IllegalArgumentException("direction must be L or R")
        }
    }.distance
}

sealed class Cursor(val x: Int, val y: Int) {
    val distance: Int by lazy { Math.abs(x) + Math.abs(y) }

    abstract fun left(disp: Int): Cursor
    abstract fun right(disp: Int): Cursor

    class Up(x: Int, y: Int) : Cursor(x, y) {
        override fun left(disp: Int) = Left(x - disp, y)
        override fun right(disp: Int) = Right(x + disp, y)
    }

    class Down(x: Int, y: Int) : Cursor(x, y) {
        override fun left(disp: Int) = Right(x + disp, y)
        override fun right(disp: Int) = Left(x - disp, y)
    }

    class Left(x: Int, y: Int) : Cursor(x, y) {
        override fun left(disp: Int) = Down(x, y - disp)
        override fun right(disp: Int) = Up(x, y + disp)
    }

    class Right(x: Int, y: Int) : Cursor(x, y) {
        override fun left(disp: Int) = Up(x, y + disp)
        override fun right(disp: Int) = Down(x, y - disp)
    }
}
