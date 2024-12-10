package day10

import println
import readInput

typealias Grid = Map<Int, Map<Int, Cell>>
data class Cell(val row: Int, val col: Int, val value: Int)
typealias Path = List<Cell>

fun List<String>.toGrid(): Grid {
    return this.foldIndexed(mutableMapOf()) { rowIndex, acc, row ->
        acc[rowIndex] = row.foldIndexed(mutableMapOf()) { colIndex, colAcc, c ->
            colAcc[colIndex] = Cell(rowIndex, colIndex, c.digitToInt())
            colAcc
        }
        acc
    }
}

fun Grid.findZeroes(): List<Cell> = this.values.flatMap { row -> row.values.filter { it.value == 0 } }

fun Cell.getNextSteps(grid: Grid): List<Cell> {
    val ret = mutableListOf<Cell>()
    val top = grid.getOrDefault(this.row - 1, mapOf())[this.col]
    if (top != null && top.value == this.value + 1) {
        ret.add(top)
    }

    val bottom = grid.getOrDefault(this.row + 1, mapOf())[this.col]
    if (bottom != null && bottom.value == this.value + 1) {
        ret.add(bottom)
    }

    val left = grid.getOrDefault(this.row, mapOf())[this.col - 1]
    if (left != null && left.value == this.value + 1) {
        ret.add(left)
    }

    val right = grid.getOrDefault(this.row, mapOf())[this.col + 1]
    if (right != null && right.value == this.value + 1) {
        ret.add(right)
    }

    return ret
}

fun Cell.computeScore(grid: Grid): Int {
    val reachableNines = hashSetOf<Cell>()

    val cells = mutableListOf(this)
    while(cells.isNotEmpty()) {
        val currentCell = cells.removeFirst()
        cells.addAll(currentCell.getNextSteps(grid))
        if (currentCell.value == 9) {
            reachableNines.add(currentCell)
        }
    }


    return reachableNines.size
}

fun Cell.computeRating(grid: Grid): Int {
    val pathsByLastCell = mutableMapOf<Cell, MutableSet<Path>>()

    pathsByLastCell[this] = hashSetOf(listOf(this))

    val queue = mutableListOf(this)
    while(queue.isNotEmpty()) {
        val currentCell = queue.removeFirst()
        val nextSteps = currentCell.getNextSteps(grid)
        val currentPaths = pathsByLastCell.getOrDefault(currentCell, listOf())
        nextSteps.forEach { cell ->
            val p = pathsByLastCell.getOrDefault(cell, mutableSetOf())
            p.addAll(currentPaths.map { it + cell })
            pathsByLastCell[cell] = p
        }
        queue.addAll(nextSteps)
    }

    val ret = pathsByLastCell.keys.filter { it.value == 9 }.sumOf { pathsByLastCell[it]!!.size }
    println("Cell $this has rating $ret")
    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        val zeroes = grid.findZeroes()
        return zeroes.sumOf { it.computeScore(grid) }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        val zeroes = grid.findZeroes()
        return zeroes.sumOf { it.computeRating(grid) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10/Day10_test")
    check(part1(testInput) == 36)

    val input = readInput("Day10/Day10")
    part1(input).println()

    val input3 = readInput("Day10/Day10_test3")
    check(part2(input3) == 3)

    val input2 = readInput("Day10/Day10_test2")
    check(part2(input2) == 227)

    check(part2(testInput) == 81)
    part2(input).println()
}
