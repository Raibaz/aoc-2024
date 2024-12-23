package day19

import println
import readInput

data class Combination(val string: String, val suffixes: Map<String, Combination>)

val knownUnmatched = hashSetOf<String>()

fun String.isValid(patterns: List<String>): Boolean {

    //println("Processing $this...")

    if (this.isEmpty()) {
        return true
    }

    if (knownUnmatched.contains(this)) {
        return false
    }

    val matches = patterns.filter { this.startsWith(it) }
    if (matches.isEmpty()) {
        knownUnmatched.add(this)
        return false
    }

    val ret = matches.any { this.substring(it.length).isValid(patterns) }
    if (!ret) {
        knownUnmatched.add(this)
    }
    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val patterns = input.first().split(",").map { it.trim() }

        knownUnmatched.clear()
        return input.drop(2).filter {
            val valid = it.isValid(patterns)
            println("$it is valid? $valid")
            valid
        }.size
    }

    fun part2(input: List<String>): Int {

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19/Day19_test")
    //check(part1(testInput) == 6)

    val input = readInput("Day19/Day19")
    //part1(input).println()

    check(part2(testInput) == 16)
    //part2(input).println()
}
