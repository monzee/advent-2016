import kotlin.comparisons.compareBy

fun main(vararg args: String) {
    readLine()?.let {
        Solve().follow(it.split(", ")).apply {
            println(blockDistance)
            println(realLocation?.distanceFromOrigin)
        }
    }
}

class BadDirection : IllegalArgumentException("must start with L or R")

class DidNotMove : IllegalStateException("move distance should be non-zero")

class DiagonalMove : IllegalStateException("can only move in a straight line")

class Solve(val pos: Vector, val path: List<Straight>) {
    constructor() : this(ORIGIN, mutableListOf())

    val blockDistance: Int get() = pos.at.distanceFromOrigin

    val realLocation: Point? by lazy {
        val byX = sortedSetOf<Straight>(compareBy { it.fromLeft })
        val byY = sortedSetOf<Straight>(compareBy { it.fromBottom })
        val seq = path.asSequence()
        seq.drop(1).zip(seq).flatMap {
            val (curr, prev) = it
            byX += prev
            byY += prev
            when (curr) {
                is Straight.Horiz -> when {
                    curr.start <= curr.end -> byX.tailSet(curr)
                    else -> byX.headSet(curr).reversed()
                }
                is Straight.Vert -> when {
                    curr.start <= curr.end -> byY.tailSet(curr)
                    else -> byY.headSet(curr).reversed()
                }
            }
            .asSequence()
            .filter { it != prev }
            .mapNotNull { curr.intersect(it) }
        }.firstOrNull()
    }

    fun follow(cmds: List<String>): Solve = cmds.fold(this) { move, cmd ->
        move.to(cmd.first(), cmd.drop(1).toInt())
    }

    fun to(dir: Char, distance: Int): Solve = when (dir.toUpperCase()) {
        'L' -> pos.left(distance)
        'R' -> pos.right(distance)
        else -> throw BadDirection()
    }.let { Solve(it, path + pos.at.lineTo(it.at)) }
}

val ORIGIN: Vector = Vector.Up(0, 0)

data class Point(val x: Int, val y: Int) {
    val distanceFromOrigin: Int get() = Math.abs(x) + Math.abs(y)

    fun lineTo(end: Point): Straight = when {
        this == end -> throw DidNotMove()
        x == end.x -> Straight.Vert(this, end)
        y == end.y -> Straight.Horiz(this, end)
        else -> throw DiagonalMove()
    }
}

data class Order(val a: Int, val b: Int, val c: Int) : Comparable<Order> {
    override fun compareTo(other: Order) = when {
        a == other.a && b == other.b -> c - other.c
        a == other.a -> b - other.b
        else -> a - other.a
    }
}

sealed class Straight(val axis: Int, val start: Int, val end: Int) {
    abstract fun sameOrientation(that: Straight): Boolean
    abstract fun pointAt(middle: Int): Point
    abstract val fromLeft: Order
    abstract val fromBottom: Order

    val bounds: Pair<Int, Int> = when {
        start <= end -> start to end
        else -> end to start
    }

    val range: IntRange by lazy {
        val (a, b) = bounds
        a..b
    }

    fun intersect(that: Straight): Point? = when {
        sameOrientation(that) -> when {
            axis == that.axis && that.end in range -> pointAt(when {
                start <= end -> that.bounds.first
                else -> that.bounds.second
            })
            else -> null
        }
        else -> when {
            that.axis in range && axis in that.range -> pointAt(that.axis)
            else -> null
        }
    }

    class Horiz(start: Point, end: Point) : Straight(start.y, start.x, end.x) {
        override val fromLeft = Order(bounds.first, bounds.second, axis)
        override val fromBottom = Order(axis, bounds.first, bounds.second)
        override fun sameOrientation(that: Straight) = that is Horiz
        override fun pointAt(middle: Int) = Point(middle, axis)
        override fun toString() = "H:$axis[$start,$end]"
    }

    class Vert(start: Point, end: Point) : Straight(start.x, start.y, end.y) {
        override val fromLeft = Order(axis, bounds.first, bounds.second)
        override val fromBottom = Order(bounds.first, bounds.second, axis)
        override fun sameOrientation(that: Straight) = that is Vert
        override fun pointAt(middle: Int) = Point(axis, middle)
        override fun toString() = "V:$axis[$start,$end]"
    }
}

sealed class Vector(val at: Point) {
    abstract fun left(distance: Int): Vector
    abstract fun right(distance: Int): Vector

    class Up(x: Int, y: Int) : Vector(Point(x, y)) {
        override fun left(distance: Int) = Left(at.x - distance, at.y)
        override fun right(distance: Int) = Right(at.x + distance, at.y)
    }

    class Down(x: Int, y: Int) : Vector(Point(x, y)) {
        override fun left(distance: Int) = Right(at.x + distance, at.y)
        override fun right(distance: Int) = Left(at.x - distance, at.y)
    }

    class Left(x: Int, y: Int) : Vector(Point(x, y)) {
        override fun left(distance: Int) = Down(at.x, at.y - distance)
        override fun right(distance: Int) = Up(at.x, at.y + distance)
    }

    class Right(x: Int, y: Int) : Vector(Point(x, y)) {
        override fun left(distance: Int) = Up(at.x, at.y + distance)
        override fun right(distance: Int) = Down(at.x, at.y - distance)
    }
}
