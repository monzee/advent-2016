package advent.day5

import java.security.MessageDigest
import java.util.BitSet

fun main(vararg args: String) {
    readLine()?.let {
        when {
            "--hard-mode" in args -> solve(it)
            else -> simple(it)
        }
    }
}

fun solve(prefix: String): String = generateSequence(0) { it + 1 }
        .map { "$prefix$it" }
        .map(::hash)
        .mapNotNull(::nextCharInPosition)
        .fold(Password()) { password, found ->
            val (i, char) = found
            password[i] = char
            password.apply {
                if (complete) {
                    return chars.toString()
                }
            }
        }
        .let { "unreachable!" }

fun simple(prefix: String): String = generateSequence(0) { it + 1 }
        .map { "$prefix$it" }
        .map(::hash)
        .mapNotNull(::nextChar)
        .take(8)
        .map(::print)
        .joinToString("")

private val MD5 = MessageDigest.getInstance("MD5")
private val BYTE_STR = (-128..127).map { "%02x".format(it.toByte()) }

fun hash(text: String): CharSequence = StringBuilder().apply {
    text.toByteArray()
            .let { MD5.digest(it) }
            .forEach { append(BYTE_STR[it + 128]) }
}

fun nextChar(hash: CharSequence): Char? = when {
    hash.startsWith("00000") -> hash[5]
    else -> null
}

data class Password(val size: Int = 8) {
    val chars = StringBuilder("_".repeat(size))
    val complete: Boolean get() = flags.cardinality() == size
    private val flags = BitSet(size)

    operator fun set(i: Int, char: Char) {
        require(i in 0 until size) { "out of bounds" }
        if (!flags.get(i)) {
            flags.set(i)
            chars[i] = char
            println(chars)
        }
    }
}

fun nextCharInPosition(hash: CharSequence): Pair<Int, Char>? = when {
    !hash.startsWith("00000") || hash[5] !in '0'..'7' -> null
    else -> (hash[5] - '0') to hash[6]
}
