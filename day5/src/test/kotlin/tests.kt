package advent.day5

import kotlin.test.*
import org.junit.Test
import java.util.concurrent.ArrayBlockingQueue

class Md5Test {
    @Test
    fun starts_with_five_zeroes() {
        assertEquals("00000", md5("abc3231929").take(5))
        assertEquals("00000", md5("abc5017308").take(5))
    }

    @Test
    fun next_char() {
        assertEquals('1', md5("abc3231929").let(::nextChar))
        assertEquals('8', md5("abc5017308").let(::nextChar))
        assertNull(md5("whatever").let(::nextChar))
    }

    @Test
    fun positioned_char() {
        assertEquals(1 to '5', md5("abc3231929").let(::nextCharInPosition))
        assertNull(md5("abc5017308").let(::nextCharInPosition))
        assertEquals(4 to 'e', md5("abc5357525").let(::nextCharInPosition))
    }
}

class Solution {
    @Test
    fun example() {
        val simple = ArrayBlockingQueue<Char>(8)
        assertEquals("05ace8e3", solve("abc", simple))
        assertEquals("18f47a30", simple.joinToString(""))
    }
}

fun nextChar(hash: CharSequence): Char? = when {
    hash.startsWith("00000") -> hash[5]
    else -> null
}

fun nextCharInPosition(hash: CharSequence): Pair<Int, Char>? = when {
    hash[5] in '0'..'7' -> (hash[5] - '0') to hash[6]
    else -> null
}
