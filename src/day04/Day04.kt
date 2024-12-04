typealias Grid = MutableMap<Int, MutableMap<Int, Char>>

fun newGrid() = mutableMapOf<Int, MutableMap<Int, Char>>()

data class Cell(val x: Int, val y: Int, val value: Char)

fun Grid.get(row: Int, col: Int) = this.getOrDefault(row, mutableMapOf()).getOrDefault(col, '0')

fun Grid.fromInput(input: List<String>) {
    input.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { colIndex, c ->
            val row = getOrDefault(rowIndex, mutableMapOf())
            row[colIndex] = c
            this[rowIndex] = row
        }
    }
}

fun Grid.findAll(char: Char): List<Cell> {
    return (0..<keys.size).fold(mutableListOf()) { acc, row ->
        (0..<this[row]!!.size).filter { col -> this[row]!![col] == char }
            .fold(acc) { colAcc, col ->
                colAcc.add(Cell(row, col, this[row]!![col]!!))
                colAcc
        }
    }
}

fun Grid.findAllDiagonallyAdjacent(cell: Cell, char: Char): List<Cell> {
    val ret = mutableListOf<Cell>()

    // Top left
    if (get(cell.x - 1, cell.y - 1) == char) {
        ret.add(Cell(cell.x - 1, cell.y - 1, char))
    }

    // Top right
    if (get(cell.x - 1, cell.y + 1) == char) {
        ret.add(Cell(cell.x - 1, cell.y + 1, char))
    }

    // Bottom left
    if (get(cell.x + 1, cell.y - 1) == char) {
        ret.add(Cell(cell.x + 1, cell.y - 1, char))
    }

    // Bottom right
    if (get(cell.x + 1, cell.y + 1) == char) {
        ret.add(Cell(cell.x + 1, cell.y + 1, char))
    }

    // println("Cell $cell has adjacents $ret")

    return ret
}

fun Cell.countXmas(grid: Grid): Int {
    var ret = 0
    // NW
    if (grid.get(x - 1, y - 1) == 'M' && grid.get(x - 2, y - 2) == 'A' && grid.get(x - 3, y - 3) == 'S') {
        ret += 1
    }

    if (grid.get(x + 1, y - 1) == 'M' && grid.get(x + 2, y - 2) == 'A' && grid.get(x + 3, y - 3) == 'S') {
        ret += 1
    }

    if (grid.get(x, y - 1) == 'M' && grid.get(x, y - 2) == 'A' && grid.get(x, y - 3) == 'S') {
        ret += 1
    }

    // North
    if (grid.get(x - 1, y) == 'M' && grid.get(x - 2, y) == 'A' && grid.get(x - 3, y) == 'S') {
        ret += 1
    }

    if (grid.get(x + 1, y) == 'M' && grid.get(x + 2, y) == 'A' && grid.get(x + 3, y) == 'S') {
        ret += 1
    }

    if (grid.get(x - 1, y + 1) == 'M' && grid.get(x - 2, y + 2) == 'A' && grid.get(x - 3, y + 3) == 'S') {
        ret += 1
    }

    if (grid.get(x, y + 1) == 'M' && grid.get(x, y + 2) == 'A' && grid.get(x, y + 3) == 'S') {
        ret += 1
    }

    if (grid.get(x + 1, y + 1) == 'M' && grid.get(x + 2, y + 2) == 'A' && grid.get(x + 3, y + 3) == 'S') {
        ret += 1
    }
    return ret
}

fun Cell.isXmasPart2(grid: Grid): Boolean {

    val adjacentMs = grid.findAllDiagonallyAdjacent(this, 'M')

    if (adjacentMs.size != 2) {
        return false
    }

    adjacentMs.forEach { adjacent ->
        val xDistance = this.x - adjacent.x
        val yDistance = this.y - adjacent.y

        if (grid.get(x + xDistance, y + yDistance) != 'S') {
            return false
        }
    }


    return true
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = newGrid()
        grid.fromInput(input)
        val start = grid.findAll('X')
        return start.sumOf { it.countXmas(grid) }
    }

    fun part2(input: List<String>): Int {
        val grid = newGrid()
        grid.fromInput(input)
        val start = grid.findAll('A')
        val ret = start.filter { it.isXmasPart2(grid) }.size
        println(ret)
        return ret
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04/Day04_test")
    check(part1(testInput) == 18)

    val input = readInput("Day04/Day04")
    part1(input).println()

    val testInput2 = readInput("Day04/Day04_test")
    check(part2(testInput2) == 9)
    part2(input).println()
}
