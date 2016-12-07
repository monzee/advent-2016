package advent.day7

import ph.codeia.klee.*

import org.junit.Test
import org.junit.Assert.*
import org.junit.Assume.*
import org.hamcrest.CoreMatchers.*

fun main(vararg args: String) {
    generateSequence(::readLine).toList().let {
        it.count(::supportsTls).echo("part 1")
        it.count(::supportsSsl).echo("part 2")
    }
}

fun solve(vararg parts: () -> Any): List<Any> = parts.map { it() }

fun spec(minLength: Int): Regex = Regex("""
        [^\[\]]{$minLength,}  \]|  # enclosed segment
        [^\[\]]{$minLength,}  \[?  # outer
        """.trimIndent(), RegexOption.COMMENTS)

fun split(
        address: String,
        minLength: Int = 4
): Pair<List<String>, List<String>> = spec(minLength)
        .findAll(address)
        .map { it.groupValues.first() }
        .partition { it.endsWith(']') }

fun supportsTls(entry: String): Boolean = split(entry).let {
    val (subnets, supernets) = it
    subnets.all { !hasAbba(it) } && supernets.any(::hasAbba)
}

fun hasAbba(segment: String): Boolean = (0..segment.lastIndex - 3)
        .map { segment.substring(it, it + 4) }
        .any { it[0] != it[1] && it[1] == it[2] && it[0] == it[3] }

fun supportsSsl(entry: String): Boolean = split(entry, 3).let {
    val (subnets, supernets) = it
    supernets.asSequence()
            .flatMap {
                (0..it.lastIndex - 2).asSequence()
                        .map { i -> it.substring(i, i + 3) }
                        .filter { it[0] != it[1] && it[0] == it[2] }
            }
            .map(::babOf)
            .any { bab -> subnets.any { bab in it } }
}

fun babOf(aba: String): String = charArrayOf(aba[1], aba[0], aba[1]).joinToString("")

// === test area === //

class scratch {
    @Test
    fun this_is_better_than_a_repl_tbh_fam() {
        (0..5 step 4).map { it..it + 4 }.forEach { it.echo }
        //"abba[mnop]qrst"
        //"ioxxoj[asdfgh]zxcvbn"
        "nefefqadkmytguyp[ucqagcoyxinbrvbw]neksoxgtnnfojobtx[bxhdwvwfhybtbzkijj]poayieifsaocrboesfe[tnggfefcucifowqp]olmjwaqlaiwkkbtruw"
        .let { Regex("""([^\[\]]{4,})\]|([^\[\]]{4,})\[?""").findAll(it) }
        .map { it.groups.first()!!.value }
        .partition { it.endsWith(']') }
        .echo
    }

    @Test
    fun part1_examples() {
        assertTrue(supportsTls("abba[mnop]qrst"))
        assertFalse(supportsTls("abcd[bddb]xyyx"))
        assertFalse(supportsTls("aaaa[qwer]tyui"))
        assertTrue(supportsTls("ioxxoj[asdfgh]zxcvbn"))
    }

    @Test
    fun part2_examples() {
        assertTrue(supportsSsl("aba[bab]xyz"))
        assertFalse(supportsSsl("xyx[xyx]xyx"))
        assertTrue(supportsSsl("aaa[kek]eke"))
        assertTrue(supportsSsl("zazbz[bzb]cdb"))
    }
}
