
fun main() {

    data class Enabler(val index: Int = 0, val enabled: Boolean = true)

    fun String.multiply() =  replace("mul(", "")
        .replace(")", "")
        .split(",")
        .map { num -> num.toLong() }
        .reduce { acc, cur -> acc * cur}

    fun part1(input: List<String>): Long {
        val regex = Regex("mul\\([0-9]+?,[0-9]+?\\)")
        val concatInput = input.joinToString("")
        return regex.findAll(concatInput).sumOf { it.value.multiply() }
    }

    fun part2(input: List<String>): Long {
        val mulRegex = Regex("mul\\([0-9]+?,[0-9]+?\\)")
        val enablersRegex = Regex("do[n't]*+\\(\\)")
        val concatInput = input.joinToString("")

        val enablers = sequenceOf(
                sequenceOf((Enabler())),
                enablersRegex.findAll(concatInput).map { Enabler(it.range.first, it.value == "do()") },
                sequenceOf(Enabler(concatInput.length, false))
        ).flatten().zipWithNext()

        val mults = mulRegex.findAll(concatInput).map { it.range.first to it.value }.toMap()

        return enablers.filter { it.first.enabled }.sumOf { (start, end) ->
            (start.index..end.index)
                .map { index -> mults.getOrDefault(index, "") }
                .filter { it.isNotEmpty() }
                .sumOf { it.multiply() }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03/Day03_test")
    check(part1(testInput) == 161L)

    val input = readInput("Day03/Day03")
    part1(input).println()

    val testInput2 = readInput("Day03/Day03_test2")
    check(part2(testInput2) == 48L)
    part2(input).println()
}
