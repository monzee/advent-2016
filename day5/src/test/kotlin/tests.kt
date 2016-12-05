package advent.day5

import kotlin.test.*
import org.junit.Test

class Md5Test {
    @Test
    fun starts_with_five_zeroes() {
        assertEquals("00000", hash("abc3231929").take(5))
        assertEquals("00000", hash("abc5017308").take(5))
    }

    @Test
    fun next_char() {
        assertEquals('1', hash("abc3231929").let(::nextChar))
        assertEquals('8', hash("abc5017308").let(::nextChar))
        assertNull(hash("whatever").let(::nextChar))
    }

    @Test
    fun positioned_char() {
        assertEquals(1 to '5', hash("abc3231929").let(::nextCharInPosition))
        assertNull(hash("abc5017308").let(::nextCharInPosition))
        assertEquals(4 to 'e', hash("abc5357525").let(::nextCharInPosition))
    }
}

class Solution {
    @Test
    fun example() {
        todo {
            // takes forever to finish but it's correct
             assertEquals("18f47a30", solve("abc"))
        }
        assertEquals("05ace8e3", solve("abc"))
    }
}
