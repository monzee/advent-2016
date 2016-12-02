fun main(vararg args: String) {
    readLine()?.let {
        Brute().follow(it.split(", ")).apply {
            println(blockDistance)
            println(firstIntersection?.distanceFromOrigin)
        }
    }
}

class BadDirection : IllegalArgumentException("must start with L or R")

class DidNotMove : IllegalStateException("move distance should be non-zero")

class DiagonalMove : IllegalStateException("can only move in a straight line")

class Brute(private val cur: Cursor, private val path: List<Straight>) {
    constructor() : this(ORIGIN, mutableListOf())

    val blockDistance: Int get() = cur.at.distanceFromOrigin

    val firstIntersection: Point? by lazy {
        path.asSequence().drop(1).withIndex().flatMap {
            val (i, target) = it
            path.asSequence().take(i).mapNotNull { target.intersect(it) }
        }.firstOrNull()
    }

    fun follow(moves: List<String>): Brute = moves.fold(this) { turn, move ->
        turn.to(move.first(), move.drop(1).toInt())
    }

    fun to(dir: Char, distance: Int): Brute = when (dir.toUpperCase()) {
        'L' -> cur.left(distance)
        'R' -> cur.right(distance)
        else -> throw BadDirection()
    }.let { Brute(it, path + cur.at.lineTo(it.at)) }
}

val ORIGIN: Cursor = Cursor.Up(0, 0)

data class Point(val x: Int, val y: Int) {
    val distanceFromOrigin: Int get() = Math.abs(x) + Math.abs(y)

    fun lineTo(end: Point): Straight = when {
        this == end -> throw DidNotMove()
        x == end.x -> Straight.Vert(this, end)
        y == end.y -> Straight.Horiz(this, end)
        else -> throw DiagonalMove()
    }
}

sealed class Straight(val axis: Int, val start: Int, val end: Int) {
    abstract fun sameOrientation(that: Straight): Boolean
    abstract fun pointAt(middle: Int): Point

    val range: IntRange by lazy {
        when {
            start < end -> start..end
            else -> end..start
        }
    }

    fun intersect(that: Straight): Point? = when {
        sameOrientation(that) -> when {
            axis == that.axis && that.end in range -> pointAt(end)
            else -> null
        }
        else -> when {
            that.axis in range && axis in that.range -> pointAt(that.axis)
            else -> null
        }
    }

    class Horiz(start: Point, end: Point) : Straight(start.y, start.x, end.x) {
        override fun sameOrientation(that: Straight) = that is Horiz
        override fun pointAt(middle: Int) = Point(middle, axis)
        override fun toString() = "H:$axis[$start,$end]"
    }

    class Vert(start: Point, end: Point) : Straight(start.x, start.y, end.y) {
        override fun sameOrientation(that: Straight) = that is Vert
        override fun pointAt(middle: Int) = Point(axis, middle)
        override fun toString() = "V:$axis[$start,$end]"
    }
}

sealed class Cursor(val at: Point) {
    abstract fun left(distance: Int): Cursor
    abstract fun right(distance: Int): Cursor

    class Up(x: Int, y: Int) : Cursor(Point(x, y)) {
        override fun left(distance: Int) = Left(at.x - distance, at.y)
        override fun right(distance: Int) = Right(at.x + distance, at.y)
    }

    class Down(x: Int, y: Int) : Cursor(Point(x, y)) {
        override fun left(distance: Int) = Right(at.x + distance, at.y)
        override fun right(distance: Int) = Left(at.x - distance, at.y)
    }

    class Left(x: Int, y: Int) : Cursor(Point(x, y)) {
        override fun left(distance: Int) = Down(at.x, at.y - distance)
        override fun right(distance: Int) = Up(at.x, at.y + distance)
    }

    class Right(x: Int, y: Int) : Cursor(Point(x, y)) {
        override fun left(distance: Int) = Up(at.x, at.y + distance)
        override fun right(distance: Int) = Down(at.x, at.y - distance)
    }
}
