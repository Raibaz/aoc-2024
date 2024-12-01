import kotlin.math.abs

fun main() {

    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> = input
        .map { it.split("   ") }
        .map { it[0].toInt() to it[1].toInt() }
        .unzip()

    fun part1(input: List<String>): Int {
        val (firstList, secondList) = parseInput(input)

        val sortedFirst = firstList.sorted()
        val sortedSecond = secondList.sorted()

        return sortedFirst
            .mapIndexed { index, value -> abs(value - sortedSecond[index]) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val (firstList, secondList) = parseInput(input)

        val counts = secondList.groupingBy { it }.eachCount()

        return firstList.sumOf { it * (counts[it] ?: 0) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/Day01_test")
    check(part1(testInput) == 11)

    val input = readInput("Day01/Day01")
    part1(input).println()
    val testInput2 = readInput("Day01/Day01_test")
    check(part2(testInput2) == 31)
    part2(input).println()
}
