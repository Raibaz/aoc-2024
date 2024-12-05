typealias Rule = Map<Int, List<Int>>

fun main() {

    fun List<String>.parseRules(): Pair<Rule, Rule> {
        val mustPreceed = mutableMapOf<Int, MutableList<Int>>()
        val mustFollow = mutableMapOf<Int, MutableList<Int>>()

        this.forEach { rule ->
            val split = rule.split("|")
            val first = split[0].toInt()
            val second = split[1].toInt()

            val firstList = mustPreceed.getOrDefault(first, mutableListOf())
            firstList.add(second)
            mustPreceed[first] = firstList

            val secondList = mustFollow.getOrDefault(second, mutableListOf())
            secondList.add(first)
            mustFollow[second] = secondList
        }

        return mustPreceed to mustFollow
    }

    fun String.toIntList(): List<Int> {
        return this.split(",").map { it.toInt() }
    }
    
    fun List<Int>.findFirstBrokenRule(mustPreceed: Rule, mustFollow: Rule): Pair<Int, Int>? {
        this.forEachIndexed { index, i ->
            val before = this.take(index)
            val after = this.drop(index + 1)

            val brokenFollower = after.find { mustFollow.getOrDefault(i, listOf()).contains(it) }
            if (brokenFollower != null) {
                return i to brokenFollower
            }

            val brokenPreceeding = before.find { mustPreceed.getOrDefault(i, listOf()).contains(it) }
            if (brokenPreceeding != null) {
                return i to brokenPreceeding
            }
        }

        return null
    }

    fun List<Int>.swap(pair: Pair<Int, Int>): List<Int> {
        val firstIndex = this.indexOf(pair.first)
        val secondIndex = this.indexOf(pair.second)

        val mutable = this.toMutableList()
        mutable[firstIndex] = pair.second
        mutable[secondIndex] = pair.first

        return mutable.toList()
    }

    fun List<Int>.fix(mustPreceed: Rule, mustFollow: Rule): List<Int> {

        var current = this
        var toSwap = findFirstBrokenRule(mustPreceed, mustFollow)
        while(toSwap != null) {
            current = current.swap(toSwap)
            toSwap = current.findFirstBrokenRule(mustPreceed, mustFollow)
        }

        return current
    }

    fun part1(input: List<String>): Int {
        val inputRules = input.takeWhile { it.isNotEmpty() }
        val inputUpdates = input.dropWhile { it.isNotEmpty() }.drop(1)

        val (mustPreceed, mustFollow) = inputRules.parseRules()

       return inputUpdates
            .map { it.toIntList() }
            .filter { it.findFirstBrokenRule(mustPreceed, mustFollow) == null }
            .sumOf { it[(it.size / 2)] }
    }

    fun part2(input: List<String>): Int {
        val inputRules = input.takeWhile { it.isNotEmpty() }
        val inputUpdates = input.dropWhile { it.isNotEmpty() }.drop(1)

        val (mustPreceed, mustFollow) = inputRules.parseRules()
        return inputUpdates
            .map { it.toIntList() }
            .filter { it.findFirstBrokenRule(mustPreceed, mustFollow) != null }
            .map { it.fix(mustPreceed, mustFollow) }
            .sumOf{ it[it.size / 2] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05/Day05_test")
    check(part1(testInput) == 143)

    val input = readInput("Day05/Day05")
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}
