package day06

import println
import readInput

data class Cell(val row: Int, val column: Int, val isObstacle: Boolean = false)

enum class Direction() { NORTH, SOUTH, EAST, WEST }

data class Guard(val position: Cell, val facingDirection: Direction)

typealias Grid = Map<Int, Map<Int, Cell>>

fun parseInput(input: List<String>): Grid {
    return input.mapIndexed { rowIndex, row ->
        rowIndex to row.mapIndexed { colIndex, char ->
            colIndex to Cell(rowIndex, colIndex, char == '#')
        }.toMap()
    }.toMap()
}

fun findGuardStart(input: List<String>): Guard {
    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, char ->
            when(char) {
                '^' -> return Guard(Cell(rowIndex, colIndex, false), Direction.NORTH)
                'v' -> return Guard(Cell(rowIndex, colIndex, false), Direction.SOUTH)
                '>' -> return Guard(Cell(rowIndex, colIndex, false), Direction.EAST)
                '<' -> return Guard(Cell(rowIndex, colIndex, false), Direction.WEST)
            }
        }
    }
    throw Exception("Guard not found!")
}

fun Grid.get(x: Int, y: Int): Cell? = getOrDefault(x, mapOf())[y]

fun Grid.print(guard: Guard, addedObstacle: Cell?) {
    keys.forEach { rowKey ->
        getOrDefault(rowKey, mapOf()).values.forEach { cell ->
            if (addedObstacle != null && addedObstacle.row == cell.row && addedObstacle.column == cell.column) {
                print('O')
            } else if(guard.position.row == cell.row && guard.position.column == cell.column) {
                print('G')
            } else {
                print(if (cell.isObstacle) '#' else '.')
            }
        }
        print('\n')
    }
}

fun Grid.findNextPosition(guard: Guard, addedObstacle: Cell?): Guard? {
    val candidate = when(guard.facingDirection) {
        Direction.NORTH -> get(guard.position.row - 1, guard.position.column) ?: return null
        Direction.EAST -> get(guard.position.row, guard.position.column + 1) ?: return null
        Direction.WEST -> get(guard.position.row, guard.position.column - 1) ?: return null
        Direction.SOUTH -> get(guard.position.row + 1, guard.position.column) ?: return null
    }

    if (candidate.isObstacle ||
        (addedObstacle != null && candidate.row == addedObstacle.row && candidate.column == addedObstacle.column)) {
        return when (guard.facingDirection) {
            Direction.NORTH -> findNextPosition(Guard(guard.position, Direction.EAST), addedObstacle)
            Direction.EAST -> findNextPosition(Guard(guard.position, Direction.SOUTH), addedObstacle)
            Direction.WEST -> findNextPosition(Guard(guard.position, Direction.NORTH), addedObstacle)
            Direction.SOUTH -> findNextPosition(Guard(guard.position, Direction.WEST), addedObstacle)
        }
    }

    return Guard(candidate, guard.facingDirection)
}

fun Grid.getPathPart1(guardStart: Guard?): Set<Cell> {
    var currentGuard = guardStart
    val visited = hashSetOf<Cell>()
    while(currentGuard != null) {
        visited.add(currentGuard.position)
        currentGuard = findNextPosition(currentGuard, null)
    }
    return visited
}

fun Grid.getPathPart2(guardStart: Guard?, addedObstacle: Cell?): Set<Guard> {
    var currentGuard = guardStart
    val visited = hashSetOf<Guard>()
    while(currentGuard != null) {
        if (visited.contains(currentGuard)) {
            throw Exception("Loop!")
        }
        visited.add(currentGuard)
        currentGuard = findNextPosition(currentGuard, addedObstacle)
    }
    return visited
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val guard: Guard = findGuardStart(input)
        val visited = grid.getPathPart1(guard)
        return visited.size
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        val guard: Guard = findGuardStart(input)
        val basePath = grid.getPathPart2(guard, null)
        val loops = hashSetOf<Cell>()
        basePath.forEach { step ->
            if (loops.contains(step.position)) {
                return@forEach
            }
            try {
                grid.getPathPart2(guard, step.position)
            } catch (e: Exception) {
                loops.add(step.position)
            }
        }
        return loops.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06/Day06_test")
    check(part1(testInput) == 41)

    val input = readInput("Day06/Day06")
    part1(input).println()

    check(part2(testInput) == 6)
    part2(input).println()
}
