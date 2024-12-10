package day09

import println
import readInput

fun Map<Int, Int>.print() {
    val max = this.keys.max()
    (0..max).forEach {
        print(this[it].toString() + "|")
    }
    print("\n")
}

fun String.convertToRepresentation() : Map<Int, Int> {
    var currentId = 0
    val ret = mutableMapOf<Int, Int>()

    var currentIndex = 0
    this.forEachIndexed { index, c ->
        val charCount = c.digitToInt()
        if ((index % 2) == 0) {
            (currentIndex..<(currentIndex + charCount)).forEach {
                ret[it] = currentId
            }
            currentId++
        } else {
            (currentIndex..<(currentIndex + charCount)).forEach {
                ret[it] = -1
            }
        }
        currentIndex += charCount
    }
    return ret
}

fun MutableMap<Int, Int>.compact(): Map<Int, Int> {
    val max = this.keys.max()
    var start = (0..max).first { this[it] == -1 }
    var end = (0..max).reversed().first { this[it] != -1 }

    while(start <= end) {
        val tmp = this[start]!!
        this[start] = this[end]!!
        this[end] = tmp

        start = ((start+1)..max).first { this[it] == -1 }
        end = (0..<end).reversed().first { this[it] != -1 }
    }

    return this
}

fun Map<Int, Int>.computeChecksum(): Long {
    return this.keys.filter { this[it] != -1 }.sumOf { it * this[it]!!.toLong() }
}


fun main() {
    fun part1(input: List<String>): Long {
        val representation = input.first().convertToRepresentation()
        representation.print()
        val compact = representation.toMutableMap().compact()
        compact.print()
        return compact.computeChecksum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09/Day09_test")
    check(part1(testInput) == 1928L)

    val input = readInput("Day09/Day09")
    part1(input).println()

    /*check(part2(testInput) == 11387L)
    part2(input).println()*/
}
