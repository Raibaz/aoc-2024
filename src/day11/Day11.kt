package day11

import println
import readInput

fun List<Long>.blink(): List<Long> {
    return this.fold(mutableListOf()) { acc, value ->
        if(value == 0L) {
            acc.add(1)
        } else if (value.toString().length % 2 == 0) {
           val str = value.toString()
            acc.add(str.take(str.length / 2).toLong())
            acc.add(str.drop(str.length / 2).toLong())
        } else {
            acc.add(value * 2024)
        }
        acc
    }
}

fun Map<Long, Long>.blink(): Map<Long, Long> {
    val ret = mutableMapOf<Long, Long>()
    this.keys.forEach { key ->
        val count = this[key]!!.toLong()
        if(key == 0L) {
            ret[1] = ret.getOrDefault(1, 0L) + count
        } else if (key.toString().length % 2 == 0) {
            val str = key.toString()
            ret[str.take(str.length / 2).toLong()] = ret.getOrDefault(str.take(str.length / 2).toLong(), 0L) + count
            ret[str.drop(str.length / 2).toLong()] = ret.getOrDefault(str.drop(str.length / 2).toLong(), 0L) + count
        } else {
            ret[key * 2024] = ret.getOrDefault(key * 2024, 0L) + count
        }
    }
    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        var start = input.first().split(" ").map { it.toLong() }
        (1..25).forEach { _ ->
            start = start.blink()
        }

        return start.size
    }

    fun part2(input: List<String>): Long {
        var start = input.first().split(" ").associate { it.toLong() to 1L }
        (1..75).forEach { _ ->
            start = start.blink()
        }

        return start.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11/Day11_test")
    check(part1(testInput) == 55312)

    val input = readInput("Day11/Day11")
    part1(input).println()

    part2(input).println()
}
