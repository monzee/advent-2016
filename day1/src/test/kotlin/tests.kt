import kotlin.test.*
import org.junit.Test

class DistanceTest {
    @Test fun sample_1() {
        assertEquals(5, ORIGIN.right(2).left(3).at.distanceFromOrigin)
    }

    @Test fun sample_2() {
        assertEquals(2, ORIGIN.right(2).right(2).right(2).at.distanceFromOrigin)
    }

    @Test fun sample_3() {
        assertEquals(12, ORIGIN.right(5).left(5).right(5).right(3).at.distanceFromOrigin)
    }
}

class StraightTest {
    @Test fun horizontal() {
        assertTrue(Point(0, 0).lineTo(Point(10, 0)) is Straight.Horiz)
    }

    @Test fun vertical() {
        assertTrue(Point(5, -5).lineTo(Point(5, 5)) is Straight.Vert)
    }
}

class IntersectionTest {
    val horiz = ORIGIN.at.lineTo(Point(8, 0))
    val vert = Point(4, -4).lineTo(Point(4, 4))

    @Test fun perpendicular() {
        assertEquals(Point(4, 0), horiz.intersect(vert)!!)
        assertEquals(Point(4, 0), vert.intersect(horiz)!!)
    }

    @Test fun parallel() {
        assertNull(horiz.intersect(Point(0, 1).lineTo(Point(10, 1))))
        assertNull(vert.intersect(Point(6, -5).lineTo(Point(6, 5))))
    }

    @Test fun disjoint() {
        assertNull(horiz.intersect(Point(11, -5).lineTo(Point(11, 5)).apply {
            assertTrue(this is Straight.Vert)
        }))
    }
}

class TargetTest {
    @Test fun sample() {
        val path = Brute().to('R', 8).to('R', 4).to('R', 4).to('R', 8)
        assertEquals(4, path.firstIntersection?.distanceFromOrigin)
    }
}
