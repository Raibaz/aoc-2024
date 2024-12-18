package day14

import println
import readInput

data class Robot(val startX: Int, val startY: Int, val speedX: Int, val speedY: Int)

fun String.toRobot(): Robot {
    val regex = Regex("p=([0-9]+?),([0-9]+?) v=([\\-0-9]+?),([\\-0-9]+?)")
    val matches = regex.matchEntire(this)!!.groupValues
    return Robot(matches[1].toInt(), matches[2].toInt(), matches[3].toInt(), matches[4].toInt())
}

fun Robot.computePosition(seconds: Int, gridWidth: Int, gridHeight: Int): Pair<Int, Int> {

    var x = ((speedX * seconds) + startX) % gridWidth
    if (x < 0) {
        x += gridWidth
    }

    var y = ((speedY * seconds) + startY) % gridHeight
    if (y < 0) {
        y += gridHeight
    }

    //println("Robot $this ends in ($x, $y)")
    return x to y
}

fun Pair<Int, Int>.computeQuadrant(gridWidth: Int, gridHeight: Int): Int {
    val midX = gridWidth / 2
    val midY = gridHeight / 2

    val ret = if (this.first == midX || this.second == midY) {
        -1
    } else if(this.first < midX && this.second < midY) {
        1
    } else if(this.first > midX && this.second < midY) {
        2
    } else if(this.first < midX && this.second > midY) {
        3
    } else  4

    println("Point $this is in quadrant $ret")
    return ret
}

fun main() {
    fun part1(input: List<String>, gridWidth: Int, gridHeight: Int): Int {
        val robots = input.map { it.toRobot() }
        return robots
            .map { it.computePosition(100, gridWidth, gridHeight) }
            .groupBy { it.computeQuadrant(gridWidth, gridHeight) }
            .filter { it.key != -1 }
            .values.fold(1) { acc, value -> acc * value.size}
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14/Day14_test")
    //check(part1(testInput, 11, 7) == 12)

    val input = readInput("Day14/Day14")
    part1(input, 101, 103).println()

    //check(part2(testInput) == 0)
    //part2(testInput).println()
}
