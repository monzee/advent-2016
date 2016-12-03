fun main(vararg args: String) {
    when {
        "--vertical" in args -> generateSequence {
            generateSequence(::readLine)
                    .map(::parse)
                    .take(3)
                    .toList()
                    .let(isSufficient(3))
                    ?.let(rotate())
                    ?.asSequence()
        }.flatMap { it }
        else -> generateSequence(::readLine).map(::parse)
    }.count(::isTriangle).let(::println)
}

fun parse(spec: String): List<Int> = spec
        .trim()
        .split(Regex("\\s+"), limit = 3)
        .map { it.toInt() }

fun isTriangle(sides: List<Int>): Boolean = sides
        .max()
        ?.let { it < sides.sum() - it }
        ?: false

fun <T> isSufficient(count: Int): (List<T>) -> List<T>? = { xs ->
    when (xs.size) {
        count -> xs
        else -> null
    }
}

fun <T> rotate(): (List<List<T>>) -> List<List<T>> = { xss ->
    xss.first().indices.map { col -> xss.map { it[col] } }
}
