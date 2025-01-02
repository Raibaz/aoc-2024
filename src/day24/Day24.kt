package day24

import println
import readInput
import kotlin.math.pow

fun List<String>.parseInitial(): Map<String, Boolean> =
    this.associate { row ->
        val split = row.split(": ")
        split[0] to (split[1] == "1")
    }

fun String.computeOutput(values: Map<String, Boolean>): Pair<String, Boolean>? {
    val split = this.split(" ")
    val input1 = split[0]
    val input2 = split[2]
    val function = split[1]
    val output = split[4]

    if (!values.containsKey(input1) || !values.containsKey(input2)) {
        return null
    }

    val value1 = values[input1]!!
    val value2 = values[input2]!!
    val outputValue = when(function) {
        "AND" -> value1 && value2
        "OR" -> value1 || value2
        "XOR" -> value1.xor(value2)
        else -> throw Exception("Unsupported function $function")
    }

    return output to outputValue
}

fun List<String>.computeAllValues(values: MutableMap<String, Boolean>): Map<String, Boolean> {
    val instructions = this.toMutableList()
    while(instructions.isNotEmpty()) {
        val instruction = instructions.removeFirst()
        val computed = instruction.computeOutput(values)
        if (computed != null) {
            values[computed.first] = computed.second
        } else {
            instructions.add(instruction)
        }
    }
    return values
}

fun Long.decimalToBinary(): String {
    val intList = mutableListOf<Long>()
    var decimalNumber = this

    while (decimalNumber > 0) {
        intList.add(decimalNumber % 2)
        decimalNumber /= 2
    }
    return intList.reversed().joinToString("")
}

fun Map<String, Boolean>.getNumber(prefix: String): String {
    val keys = filter { it.key.startsWith(prefix) }.keys.sorted().reversed()
    return keys.joinToString("") { if (this[it]!!) "1" else "0" }
}

fun main() {
    fun part1(input: List<String>): Long {
        val values = input.takeWhile { it.isNotEmpty() }.parseInitial().toMutableMap()
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1).toMutableList()

        val computedValues = instructions.computeAllValues(values)

        val binary = computedValues.getNumber("z")

        return binary.toLong(2)
    }

    fun part2(input: List<String>): String {
        val values = input.takeWhile { it.isNotEmpty() }.parseInitial().toMutableMap()
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1).toMutableList()

        val x = values.getNumber("x").toLong(2)
        val y = values.getNumber("y").toLong(2)

        val expectedZ = (x + y).decimalToBinary()

        val initialComputed = instructions.computeAllValues(values)
        val initialZ = initialComputed.getNumber("z")

        return ""
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24/Day24_test")
    //check(part1(testInput) == 4L)
    val testInput2 = readInput("Day24/Day24_test2")
    //check(part1(testInput2) == 2024L)

    val input = readInput("Day24/Day24")
    //part1(input).println()

    //check(part2(testInput) == "co,de,ka,ta")
    part2(input).println()
}
