package day25

import println
import readInput

enum class Type { LOCK, KEY }

data class Item(val type: Type, val pins: List<Int>)

infix fun Item.matches(other: Item): Boolean = this.pins.zip(other.pins).all { (k, l) -> k + l <= 5 }

fun List<String>.parse(): List<Item> {
    return this.windowed(8, 8).map { window ->
        val type = if (window.first().any { it == '#' }) Type.LOCK else Type.KEY
        val pins = (0..4).map { index ->
            window.dropLast(1).map { it[index] }.count { it == '#' } - 1
        }
        Item(type, pins)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val items = input.parse()
        val (locks, keys) = items.partition { it.type == Type.LOCK }
        return locks.sumOf { lock ->
            keys.count { it matches lock }
        }
    }

    fun part2(input: List<String>): String {
        return ""
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25/Day25_test")
    //check(part1(testInput) == 3)

    val input = readInput("Day25/Day25")
    part1(input).println()

    //check(part2(testInput) == "co,de,ka,ta")
    //part2(input).println()
}
