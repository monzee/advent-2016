fun main(vararg args: String) {
    when {
        "--vertical" in args -> generateSequence {
            generateSequence(::readLine)
                    .map(::parse)
                    .take(3)
                    .toList()
                    .let { sufficient(it) }
                    ?.let { rotate(it) }
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
        ?.let { longest -> longest < sides.sum() - longest }
        ?: false

fun <T> sufficient(xss: List<T>): List<T>? = when (xss.size) {
    3 -> xss
    else -> null
}

fun <T> rotate(xss: List<List<T>>): List<List<T>> = xss
        .first()
        .indices.map { col -> xss.map { it[col] } }
