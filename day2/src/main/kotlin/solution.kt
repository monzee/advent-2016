fun main(vararg args: String) = generateSequence { readLine() }
        .fold(listOf(listOf(Square()), listOf(Diamond()))) { keypads, line ->
            keypads.map { it + it.last().decode(line) }
        }
        .map { it.drop(1) }
        .forEach { println(it) }

class BadDirection(c: Char) : IllegalArgumentException(
        "must be one of #{U D L R}, got $c")

class BadKey(n: Int, min: Int, max: Int) : IndexOutOfBoundsException(
        "must be in range [$min, $max], got $n")

interface Keypad {
    val at: Int
    fun decode(line: CharSequence): Keypad
}

class Square(override val at: Int) : Keypad {
    constructor() : this(5)

    override fun decode(seq: CharSequence): Keypad = seq.fold(this) { code, dir ->
        when (dir) {
            'U' -> Square(code.at.up)
            'D' -> Square(code.at.down)
            'L' -> Square(code.at.left)
            'R' -> Square(code.at.right)
            else -> throw BadDirection(dir)
        }
    }

    override fun toString() = at.toString()

    val Int.up: Int get() = when (this) {
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> 1
        5 -> 2
        6 -> 3
        7 -> 4
        8 -> 5
        9 -> 6
        else -> throw BadKey(this, 1, 9)
    }

    val Int.down: Int get() = when (this) {
        1 -> 4
        2 -> 5
        3 -> 6
        4 -> 7
        5 -> 8
        6 -> 9
        7 -> 7
        8 -> 8
        9 -> 9
        else -> throw BadKey(this, 1, 9)
    }

    val Int.left: Int get() = when (this) {
        1 -> 1
        2 -> 1
        3 -> 2
        4 -> 4
        5 -> 4
        6 -> 5
        7 -> 7
        8 -> 7
        9 -> 8
        else -> throw BadKey(this, 1, 9)
    }

    val Int.right: Int get() = when (this) {
        1 -> 2
        2 -> 3
        3 -> 3
        4 -> 5
        5 -> 6
        6 -> 6
        7 -> 8
        8 -> 9
        9 -> 9
        else -> throw BadKey(this, 1, 9)
    }
}

class Diamond(override val at: Int) : Keypad {
    constructor() : this(5)

    override fun decode(seq: CharSequence): Keypad = seq.fold(this) { code, dir ->
        when (dir) {
            'U' -> Diamond(code.at.up)
            'D' -> Diamond(code.at.down)
            'L' -> Diamond(code.at.left)
            'R' -> Diamond(code.at.right)
            else -> throw BadDirection(dir)
        }
    }

    override fun toString() = Integer.toHexString(at)

    val Int.up: Int get() = when (this) {
        1 -> 1
        2 -> 2
        3 -> 1
        4 -> 4
        5 -> 5
        6 -> 2
        7 -> 3
        8 -> 4
        9 -> 9
        10 -> 6
        11 -> 7
        12 -> 8
        13 -> 11
        else -> throw BadKey(this, 1, 13)
    }

    val Int.down: Int get() = when (this) {
        1 -> 3
        2 -> 6
        3 -> 7
        4 -> 8
        5 -> 5
        6 -> 10
        7 -> 11
        8 -> 12
        9 -> 9
        10 -> 10
        11 -> 13
        12 -> 12
        13 -> 13
        else -> throw BadKey(this, 1, 13)
    }

    val Int.left: Int get() = when (this) {
        1 -> 1
        2 -> 2
        3 -> 2
        4 -> 3
        5 -> 5
        6 -> 5
        7 -> 6
        8 -> 7
        9 -> 8
        10 -> 10
        11 -> 10
        12 -> 11
        13 -> 13
        else -> throw BadKey(this, 1, 13)
    }

    val Int.right: Int get() = when (this) {
        1 -> 1
        2 -> 3
        3 -> 4
        4 -> 4
        5 -> 6
        6 -> 7
        7 -> 8
        8 -> 9
        9 -> 9
        10 -> 11
        11 -> 12
        12 -> 12
        13 -> 13
        else -> throw BadKey(this, 1, 13)
    }
}
