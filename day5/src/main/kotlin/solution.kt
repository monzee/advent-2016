package advent.day5

import java.security.MessageDigest
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ArrayBlockingQueue

fun main(vararg args: String) {
    readLine()?.let {
        val simpleScheme = ArrayBlockingQueue<Char>(8)
        solve(it, simpleScheme)
        println(simpleScheme.joinToString(""))
    }
}

fun solve(
        prefix: String,
        aside: BlockingQueue<Char>? = null
): String = generateSequence(0, Int::inc)
        .map { "$prefix$it" }
        .map(::md5)
        .filter { it.startsWith("00000") }
        .onEach {
            aside?.apply { offer(it[5]) }
        }
        .filter { it[5] in '0'..'7' }
        .distinctBy { it[5] }
        .take(8)
        .fold(StringBuilder("_".repeat(8))) { password, hash ->
            password[hash[5] - '0'] = hash[6]
            password.apply(::println)
        }
        .toString()

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
