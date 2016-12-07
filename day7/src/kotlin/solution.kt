package advent.day7

import ph.codeia.klee.*
import ph.codeia.klee.style.Rendition

import org.junit.Test
import org.junit.Assert.*
import org.junit.Assume.*
import org.hamcrest.CoreMatchers.*

fun main(vararg args: String) {
    generateSequence(::readLine)
            .count(::hasPalindrome)
            .echo("part 1")
}

fun solve(vararg parts: () -> Any): List<Any> = parts.map { it() }

val SPEC = Regex("""
        [^\[\]]{4,}  \]|  # enclosed segment
        [^\[\]]{4,}  \[?  # outer
        """.trimIndent(), RegexOption.COMMENTS)

fun hasPalindrome(entry: String): Boolean = SPEC
        .findAll(entry)
        .map { it.groups.first()!!.value }
        .partition { it.endsWith(']') }
        .let {
            val (inner, outer) = it
            inner.all { !hasAbba(it) }.apply{if(!this) echo(entry.error)} && outer.any(::hasAbba)
        }
        .apply{if(this) echo(entry.debug)}

fun hasAbba(segment: String): Boolean = (0..segment.lastIndex - 3)
        .map { segment.substring(it, it + 4) }
        .any(::isAbba)

fun isAbba(it: CharSequence): Boolean =
        it[0] != it[1] && it[1] == it[2] && it[0] == it[3]

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
    fun has_external_palindrome() {
        assertTrue(hasPalindrome("abba[mnop]qrst"))
        assertFalse(hasPalindrome("abcd[bddb]xyyx"))
        assertFalse(hasPalindrome("aaaa[qwer]tyui"))
        assertTrue(hasPalindrome("ioxxoj[asdfgh]zxcvbn"))
    }
}
