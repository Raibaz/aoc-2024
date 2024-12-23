package day20

import println
import readInput

data class Cell(val row: Int, val col: Int, val char: Char)
data class Step(val cell: Cell, val distance: Int)

typealias Grid = Map<Int, Map<Int, Cell>>

fun Grid.get(row: Int, col: Int): Cell? {
    return this.getOrDefault(row, mapOf())[col]
}

fun List<String>.populateGrid(): Grid {
    return this.foldIndexed(mutableMapOf()) { rowIndex, acc, row ->
        acc[rowIndex] = row.foldIndexed(mutableMapOf()) { colIndex, colAcc, c ->
            colAcc[colIndex] = Cell(rowIndex, colIndex, c)
            colAcc
        }
        acc
    }
}

fun Grid.getNeighbors(cell: Cell): List<Cell> {
    val ret = mutableListOf<Cell>()
    // Top
    val top = get(cell.row - 1, cell.col)
    if (top != null) {
        ret.add(top)
    }

    // Bottom
    val bottom = get(cell.row + 1, cell.col)
    if (bottom != null) {
        ret.add(bottom)
    }

    // Left
    val left = get(cell.row, cell.col - 1)
    if (left != null) {
        ret.add(left)
    }

    // Right
    val right = get(cell.row, cell.col + 1)
    if (right != null) {
        ret.add(right)
    }

    return ret.filter { it.char != '#' }
}

fun Grid.findStart(): Cell {
    return this.keys.firstNotNullOf { row ->
        getOrDefault(row, mapOf()).values.firstOrNull { it.char == 'S' }
    }

}

fun Grid.bfs(): List<Step> {
    val start = findStart()
    val queue = mutableListOf(Step(findStart(), 0))
    val visited = hashSetOf(start)
    val ends = mutableListOf<Step>()

    while(queue.isNotEmpty()) {
        val currentStep = queue.removeFirst()
        val neighbors = getNeighbors(currentStep.cell).filter { !visited.contains(it) }
        val nextSteps = neighbors.map { Step(it, currentStep.distance + 1) }
        ends.addAll(nextSteps.filter { it.cell.char == 'E' })
        queue.addAll(nextSteps)
        visited.addAll(neighbors)
    }

    return ends
}

fun Grid.cheat(rowIndex: Int, columnIndex: Int): Grid {
    val row = getOrDefault(rowIndex, mapOf())
    val newRow = row.toMutableMap()
    newRow[columnIndex] = Cell(rowIndex, columnIndex, '.')
    val ret = this.toMutableMap()
    ret[rowIndex] = newRow
    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.populateGrid()

        val defaultDistance = grid.bfs().first().distance

        println("Default distance = $defaultDistance")

        val walls = grid.values.flatMap { row -> row.values.filter { it.char == '#' } }
        val shorterPaths = walls.mapNotNull { wall ->
            val newGrid = grid.cheat(wall.row, wall.col)
            val newDistance = newGrid.bfs().minOf { it.distance }
            println("Cheating cell (${wall.row}, ${wall.col}) gives a distance of $newDistance")
            if ((defaultDistance - newDistance) >= 100) {
                wall to newDistance
            } else {
                null
            }
        }.toMap()

        return shorterPaths.count()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20/Day20_test")
    //check(part1(testInput) == 44)

    val input = readInput("Day20/Day20")
    part1(input).println()

    //check(part2(testInput, 6) == "6,1")
    //part2(input, 70).println()
}
