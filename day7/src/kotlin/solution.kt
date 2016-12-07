package advent.day7

import ph.codeia.klee.*
import ph.codeia.klee.style.Rendition

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

val SPEC = Regex("""
        [^\[\]]{3,}  \]|  # enclosed segment
        [^\[\]]{3,}  \[?  # outer
        """.trimIndent(), RegexOption.COMMENTS)

fun split(address: String): Pair<List<String>, List<String>> = SPEC
        .findAll(address)
        .map { it.groups.first()!!.value }
        .partition { it.endsWith(']') }

fun supportsTls(entry: String): Boolean = split(entry).let {
    val (subnets, supernets) = it
    subnets.all { !hasAbba(it) } && supernets.any(::hasAbba)
}

fun hasAbba(segment: String): Boolean = (0..segment.lastIndex - 3)
        .map { segment.substring(it, it + 4) }
        .any(::isAbba)

fun isAbba(it: String): Boolean =
        it[0] != it[1] && it[1] == it[2] && it[0] == it[3]

fun supportsSsl(entry: String): Boolean = split(entry).let {
    val (subnets, supernets) = it.echo
    supernets.flatMap(::abasOf)
            .any { 
                babOf(it).let { bab -> subnets.any { bab in it.echo(bab)!! } }
            }
}

fun abasOf(segment: String): List<String> = (0..segment.lastIndex - 2)
        .map { segment.substring(it, it + 3) }
        .filter(::isAba)

fun isAba(it: String): Boolean = it[0] != it[1] && it[0] == it[2]

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
        .let {
            "???".style(Rendition.BLUE.intense)
        }
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
