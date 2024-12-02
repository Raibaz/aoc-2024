import kotlin.math.abs

fun main() {

    fun List<String>.parse(): List<List<Int>> =
        map { line -> line.split(" ").map { it.toInt() } }

    fun List<Int>.isSafe(): Boolean {
        var decreasing : Boolean? = null
        return this.windowed(2).all {
            val first = it[0]
            val second = it[1]
            val thisDecreasing = first > second
            val delta = abs(first - second)
            if (decreasing == null) {
                decreasing = thisDecreasing
            }

            decreasing == thisDecreasing && delta <= 3 && first != second
        }
    }

    fun List<Int>.dampen(): List<List<Int>> = this.map { this.minus(it) }

    fun part1(input: List<String>): Int {
        return input.parse().count { it.isSafe() }
    }

    fun part2(input: List<String>): Int {
        return input.parse().count { row -> row.dampen().any { it.isSafe() }}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02/Day02_test")
    check(part1(testInput) == 2)

    val input = readInput("Day02/Day02")
    part1(input).println()

    check(part2(testInput) == 4)
    part2(input).println()
}
