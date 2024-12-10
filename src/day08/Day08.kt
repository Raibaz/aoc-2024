package day08

import println
import readInput

data class Cell(val row: Int, val col: Int)

fun elementPairs(arr: List<Cell>): Sequence<Pair<Cell, Cell>> = sequence {
    for(i in 0 until arr.size-1)
        for(j in i+1 until arr.size)
            yield(arr[i] to arr[j])
}

fun generateAntinodes(cells: Pair<Cell, Cell>): List<Cell> {
    val deltaRow = cells.first.row - cells.second.row
    val deltaCol = cells.first.col - cells.second.col

    val ret = listOf(
        Cell(cells.first.row  + deltaRow, cells.first.col + deltaCol),
        Cell(cells.second.row - deltaRow, cells.second.col - deltaCol)
    )
    return ret
}

fun generateAntinodesPart2(cells: Pair<Cell, Cell>, maxRow: Int, maxCol: Int): List<Cell> {
    val deltaRow = cells.first.row - cells.second.row
    val deltaCol = cells.first.col - cells.second.col

    var currentDeltaRow = deltaRow
    var currentDeltaCol = deltaCol

    val ret = mutableListOf<Cell>()
    (0..60).forEach { _ ->
        val newCell1 = Cell(cells.first.row  + currentDeltaRow, cells.first.col + currentDeltaCol)
        if (newCell1.row in 0..maxRow && newCell1.col in 0 .. maxCol) {
            ret.add(newCell1)
        }

        val newCell2 = Cell(cells.second.row - currentDeltaRow, cells.second.col - currentDeltaCol)
        if (newCell2.row in 0..maxRow && newCell2.col in 0 .. maxCol) {
            ret.add(newCell2)
        }
        currentDeltaRow += deltaRow
        currentDeltaCol += deltaCol
    }


    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val antennas = mutableMapOf<Char, MutableList<Cell>>()
        val allAntennas = mutableListOf<Cell>()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                val value = antennas.getOrDefault(char, mutableListOf())
                value.add(Cell(rowIndex, colIndex))
                antennas[char] = value
                allAntennas.add(Cell(rowIndex, colIndex))
            }
        }

        val antinodes = antennas.keys.filter { it != '.' }.map { char ->
            elementPairs(antennas.getOrDefault(char, listOf()))
                .flatMap { generateAntinodes(it) }
                .toList()
        }.flatten()

        return antinodes.intersect(allAntennas.toSet()).size
    }

    fun part2(input: List<String>): Int {
        val antennas = mutableMapOf<Char, MutableList<Cell>>()
        val allAntennas = mutableListOf<Cell>()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                val value = antennas.getOrDefault(char, mutableListOf())
                value.add(Cell(rowIndex, colIndex))
                antennas[char] = value
                allAntennas.add(Cell(rowIndex, colIndex))
            }
        }

        val antinodes = antennas.keys.filter { it != '.' }.map { char ->
            elementPairs(antennas.getOrDefault(char, listOf()))
                .flatMap { generateAntinodesPart2(it, input.size, input.first().length) }
                .toSet()
        }.flatten().toSet()

        return (antinodes + allAntennas.toSet()).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08/Day08_test")
    check(part1(testInput) == 14)

    val input = readInput("Day08/Day08")
    part1(input).println()

    check(part2(testInput) == 39)
    //part2(input).println()
}
