package day22

import println
import readInput
import kotlin.math.roundToLong

fun Long.generateNext(): Long {
    var cur = this
    var tmp = cur * 64
    cur = tmp.xor(cur)
    cur %= 16777216
    tmp = (cur / 32)
    cur = tmp.xor(cur)
    cur %= 16777216
    tmp = cur * 2048
    cur = tmp.xor(cur)
    cur %= 16777216

    return cur
}

fun main() {
    fun part1(input: List<String>): Long {
        val inputs = input.map { it.toLong() }
        return inputs.sumOf {
            var cur = it
            (1..2000).forEach {
                cur = cur.generateNext()
            }
            println("$it -> $cur")
            cur
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22/Day22_test")
    //part1(listOf("123"))
    check(part1(testInput) == 37327623L)

    val input = readInput("Day22/Day22")
    part1(input).println()

    //check(part2(testInput, 6) == "6,1")
    //part2(input, 70).println()
}
