package advent.day5

import java.security.MessageDigest

fun main(vararg args: String) {
    readLine()?.let {
        val password = CharArray(8)
        print("\u001b[1Gcracking... ________")
        solve(it, simpleScheme(password))
        println()
        println(password.joinToString(""))
    }
}

inline fun solve(
        prefix: String,
        crossinline aside: (CharSequence) -> Unit
): String = generateSequence(0, Int::inc)
        .map { "$prefix$it" }
        .map(::md5)
        .filter { it.startsWith("00000") }
        .onEach(aside)
        .filter { it[5] in '0'..'7' }
        .distinctBy { it[5] }
        .take(8)
        .fold(StringBuilder("_".repeat(8))) { password, hash ->
            password[hash[5] - '0'] = hash[6]
            password.apply { print("\u001b[13G$this") }
        }
        .toString()

fun simpleScheme(password: CharArray): (CharSequence) -> Unit {
    var i = 0
    return {
        if (i < password.size) {
            password[i++] = it[5]
        }
    }
}

private val MD5 = MessageDigest.getInstance("MD5")
private val BYTE_STR = (-128..127).map { "%02x".format(it.toByte()) }

fun md5(text: String): CharSequence = StringBuilder().apply {
    text.toByteArray()
            .let { MD5.digest(it) }
            .forEach { append(BYTE_STR[it + 128]) }
}

inline fun <T> Sequence<T>.onEach(
        crossinline action: (T) -> Unit
): Sequence<T> = map {
    action(it)
    it
}
