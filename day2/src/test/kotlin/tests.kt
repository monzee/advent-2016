import kotlin.test.*
import org.junit.Test

class DecodeTest {
    @Test
    fun square_sample() {
        assertEquals(1, Square().decode("ULL").at)
        assertEquals(9, Square(1).decode("RRDDD").at)
        assertEquals(8, Square(9).decode("LURDL").at)
        assertEquals(5, Square(8).decode("UUUUD").at)
    }

    @Test
    fun diamond_sample() {
        assertEquals(5, Diamond().decode("ULL").at)
        assertEquals(13, Diamond(5).decode("RRDDD").at)
        assertEquals(11, Diamond(13).decode("LURDL").at)
        assertEquals(3, Diamond(11).decode("UUUUD").at)
    }
}
