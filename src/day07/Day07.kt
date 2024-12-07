package day07

import println
import readInput

fun List<Long>.isValid(expectedResult: Long): Boolean {
    if (this.isEmpty()) return false

    if (this.size == 1) return this.first() == expectedResult

    return (listOf(this[0] + this[1]) + this.drop(2).toList()).isValid(expectedResult) ||
            (listOf(this[0] * this[1]) + this.drop(2).toList()).isValid(expectedResult)
}

fun List<Long>.isValidPart2(expectedResult: Long): Boolean {
    if (this.isEmpty()) return false

    if (this.size == 1) return this.first() == expectedResult

    return (listOf(this[0] + this[1]) + this.drop(2).toList()).isValidPart2(expectedResult) ||
            (listOf(this[0] * this[1]) + this.drop(2).toList()).isValidPart2(expectedResult) ||
            (listOf((this[0].toString() + this[1].toString()).toLong()) + this.drop(2).toList()).isValidPart2(expectedResult)
}

fun main() {
    fun part1(input: List<String>): Long {
        return input
            .map { it.split((": ")) }
            .map { it[0].toLong() to it[1].split(" ").map { num -> num.toLong() } }
            .filter { it.second.isValid(it.first) }
            .sumOf { it.first }
    }

    fun part2(input: List<String>): Long {
        return input
            .map { it.split((": ")) }
            .map { it[0].toLong() to it[1].split(" ").map { num -> num.toLong() } }
            .filter {
                val valid = it.second.isValidPart2(it.first)
                println("${it.first} -> $valid")
                valid
            }
            .sumOf { it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07/Day07_test")
    check(part1(testInput) == 3749L)

    val input = readInput("Day07/Day07")
    part1(input).println()

    check(part2(testInput) == 11387L)
    part2(input).println()
}
