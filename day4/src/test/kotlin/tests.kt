package advent.day4

import kotlin.test.*
import org.junit.Test

val real1 = parse("aaaa-bbb-z-y-x-123[abxyz]")
val real2 = parse("a-b-c-d-e-f-g-h-987[abcde]")
val real3 = parse("not-a-real-room-404[oarel]")
val fake1 = parse("totally-real-room-200[decoy]")

class ParseTest {
    @Test
    fun real() {
        real1!!.apply {
            assertEquals(123, sector)
            assertEquals("abxyz", checksum)
            assertEquals("aaaa-bbb-z-y-x", name)
        }
        real2!!.apply {
            assertEquals(987, sector)
            assertEquals("abcde", checksum)
            assertEquals("a-b-c-d-e-f-g-h", name)
        }
        real3!!.apply {
            assertEquals(404, sector)
            assertEquals("oarel", checksum)
            assertEquals("not-a-real-room", name)
        }
    }

    @Test
    fun fake() {
        fake1!!.apply {
            assertEquals(200, sector)
            assertEquals("decoy", checksum)
            assertEquals("totally-real-room", name)
        }
    }
}

class ChecksumTest {
    @Test
    fun real() {
        assertTrue(check(real1!!))
        assertTrue(check(real2!!))
        assertTrue(check(real3!!))
    }

    @Test
    fun fake() {
        assertFalse(check(fake1!!))
    }
}

class DecryptTest {
    @Test
    fun sample() {
        assertEquals("very encrypted name", decrypt(parse("qzmt-zixmtkozy-ivhz-343[asdf]")!!))
    }
}

class RegexTest {
    @Test
    fun whats_going_on() {
        assertTrue(Regex("foo") in "foo bar baz")
    }
}
