package day13

import println
import readInput

fun computeResult(input: List<String>, delta: Long = 0): Long {
    return input.windowed(3, 4).map() { problem ->
        val a = problem[0]
        val aMatches = Regex("Button A: X\\+([0-9]+?), Y\\+([0-9]+?)").matchEntire(a)!!.groupValues
        val (ax, ay) = aMatches[1].toDouble() to aMatches[2].toDouble()
        val b = problem[1]
        val bMatches = Regex("Button B: X\\+([0-9]+?), Y\\+([0-9]+?)").matchEntire(b)!!.groupValues
        val (bx, by) = bMatches[1].toDouble() to bMatches[2].toDouble()
        val prize = problem[2]
        val prizeMatches = Regex("Prize: X=([0-9]+?), Y=([0-9]+?)").matchEntire(prize)!!.groupValues
        val (prizeX, prizeY) = prizeMatches[1].toDouble() + delta to prizeMatches[2].toDouble() + delta

        val x = (prizeX * by - prizeY * bx) / (ax * by - bx * ay)
        val y = (prizeX - ax * x) / bx
        val result = if (y % 1 != 0.0 || x % 1 != 0.0) {
            -1
        }
        else {
            x.toLong() * 3 + y.toLong()
        }

        result

    }
        .filter { it > 0 }
        .sum()
}


fun main() {
    fun part1(input: List<String>) = computeResult(input).toInt()

    fun part2(input: List<String>): Long = computeResult(input, 10000000000000)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13/Day13_test")
    check(part1(testInput) == 480)

    val input = readInput("Day13/Day13")
    part1(input).println()

    check(part2(testInput) == 875318608908)
    part2(testInput).println()
}
