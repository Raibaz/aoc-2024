package day18

import println
import readInput

data class Cell(val row: Int, val col: Int, val char: Char)
data class Step(val cell: Cell, val distance: Int)

typealias Grid = List<List<Cell>>

fun Grid.get(row: Int, col: Int): Cell {
    return this[row][col]
}

fun Grid.corrupt(row: Int, col: Int): Grid {
    val ret = toMutableList()
    val column = ret[row].toMutableList()
    column[col] = Cell(row, col, '#')
    ret[row] = column
    return ret
}

fun buildGrid(size: Int): Grid {
    return buildList {
        (0..size).forEach { rowIndex ->
            val col = buildList {
                (0..size).forEach { colIndex ->
                    add(Cell(rowIndex, colIndex, '.'))
                }
            }
            add(col)
        }
    }
}

fun List<String>.populateGrid(grid: Grid): Grid {
    var currentGrid = grid
    this.take(1024).forEach { inputRow ->
        val coords = inputRow.split(",").map { it.toInt() }
        currentGrid = currentGrid.corrupt(coords[0], coords[1])
    }
    return currentGrid
}

fun Grid.print() {
    forEach { row ->
        row.forEach { cell -> print(cell.char) }
        print("\n")
    }
}

fun Grid.getNeighbors(cell: Cell): List<Cell> {
    val ret = mutableListOf<Cell>()
    // Top
    if (cell.row > 0) {
        ret.add(get(cell.row - 1, cell.col))
    }

    // Bottom
    if (cell.row < size - 1) {
        ret.add(get(cell.row + 1, cell.col))
    }

    // Left
    if (cell.col > 0) {
        ret.add(get(cell.row, cell.col - 1))
    }

    // Right
    if (cell.col < size - 1) {
        ret.add(get(cell.row, cell.col + 1))
    }

    return ret
}

fun Grid.bfs(): List<Step> {
    val queue = mutableListOf(Step(Cell(0, 0, '.'), 0))
    val visited = hashSetOf<Cell>()
    val ends = mutableListOf<Step>()

    while(queue.isNotEmpty()) {
        val currentStep = queue.removeFirst()
        val neighbors = getNeighbors(currentStep.cell).filter { it.char != '#' }.filter { !visited.contains(it) }
        val nextSteps = neighbors.map { Step(it, currentStep.distance + 1) }
        ends.addAll(nextSteps.filter { it.cell.row == size - 1 && it.cell.col == size - 1 })
        queue.addAll(nextSteps)
        visited.addAll(neighbors)
    }

    return ends
}

fun main() {
    fun part1(input: List<String>, gridSize: Int): Int {
        val grid = buildGrid(gridSize)
        val populated = input.populateGrid(grid)
        populated.print()

        return populated.bfs().minOf { it.distance }
    }

    fun part2(input: List<String>, gridSize: Int): String {
        var grid = buildGrid(gridSize)
        return input.first { line ->
            val coords = line.split(",").map { it.toInt() }
            grid = grid.corrupt(coords[0], coords[1])
            grid.bfs().isEmpty()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18/Day18_test")
    //check(part1(testInput, 6) == 22)

    val input = readInput("Day18/Day18")
    //part1(input, 70).println()

    //check(part2(testInput, 6) == "6,1")
    part2(input, 70).println()
}
