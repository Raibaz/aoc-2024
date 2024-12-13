package day12

import println
import readInput

data class Cell(val row: Int, val col: Int, val char: Char)

typealias Grid = Map<Int, Map<Int, Cell>>

fun List<String>.parse(): Pair<Grid, Map<Char, List<Cell>>> {
    val grid = mutableMapOf<Int, MutableMap<Int, Cell>>()
    val areas = mutableMapOf<Char, MutableList<Cell>>()
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, c ->
            val cell = Cell(rowIndex, colIndex, c)
            val currentRow = grid.getOrDefault(rowIndex, mutableMapOf())
            currentRow[colIndex] = cell
            grid[rowIndex] = currentRow
            val currentAreas = areas.getOrDefault(c, mutableListOf())
            currentAreas.add(cell)
            areas[c] = currentAreas
        }
    }

    return grid to areas
}

fun Cell.findNeighbors(grid: Grid): List<Cell> {
    val ret = mutableListOf<Cell>()
    // Top
    grid.getOrDefault(row - 1, mapOf())[col]?.let { ret.add(it) }

    // Bottom
    grid.getOrDefault(row + 1, mapOf())[col]?.let { ret.add(it) }

    // Left
    grid.getOrDefault(row, mapOf())[col - 1]?.let { ret.add(it) }

    // Right
    grid.getOrDefault(row, mapOf())[col + 1]?.let { ret.add(it) }

    return ret
}

fun Cell.getPerimeter(grid: Grid): Int {
    val ret = findNeighbors(grid).filter { it.char != char }.size

    val isTop = if(row == 0) 1 else 0
    val isBottom = if(row == grid.size - 1) 1 else 0
    val isLeft = if(col == 0) 1 else 0
    val isRight = if(col == grid.values.first().size - 1) 1 else 0

    return ret + isTop + isBottom + isLeft + isRight
}

fun Cell.isNeighbor(other: Cell): Boolean {
    return other.row == row - 1 && other.col == col || // Top
        other.row == row + 1 && other.col == col || // Bottom
        other.row == row && other.col == col - 1 || // Left
        other.row == row && other.col == col + 1 // Right
}

fun List<Cell>.computeRegions(): Set<Set<Cell>> {
    val ret = hashSetOf<HashSet<Cell>>()
    forEach { cell ->
        val queue = mutableListOf(cell)
        val region = hashSetOf(cell)
        val visited = hashSetOf(cell)
        while(queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val neighbors = this.filter { it != current && it.isNeighbor(current) && !visited.contains(it) }
            queue.addAll(neighbors)
            region.addAll(neighbors)
            visited.addAll(neighbors)
        }
        ret.add(region)
    }

    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val (grid, areas) = input.parse()
        return areas.entries.sumOf { entry ->
            val regions = entry.value.computeRegions()
            regions.sumOf {
                val area = it.size
                val perimeter = it.sumOf { cell -> cell.getPerimeter(grid) }
                //println("Region with cell ${it.first()} contributes area $area and perimeter $perimeter")
                area * perimeter
            }
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12/Day12_test1")
    check(part1(testInput) == 140)
    val testInput2 = readInput("Day12/Day12_test2")
    check(part1(testInput2) == 772)
    val testInput3 = readInput("Day12/Day12_test3")
    check(part1(testInput3) == 1930)

    val input = readInput("Day12/Day12")
    //part1(input).println()

    check(part2(testInput) == 80)
    check(part2(testInput2) == 436)
    check(part2(testInput3) == 1206)
    /*part2(input).println()*/
}
