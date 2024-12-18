package day17

import println
import readInput
import kotlin.math.pow

data class State(val a: Long, val b: Long, val c: Long, val instructionPointer: Int, val output: MutableList<Long> = mutableListOf())

fun State.execute(program: List<Int>): State {
    if (instructionPointer > program.size - 2) {
        return State(a, b, c, -1, output)
    }

    val opcode = program[instructionPointer]
    val operand = program[instructionPointer + 1]

    val operandValue = when(operand) {
        in (0..3) -> operand.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> operand.toLong()
    }

    val ret = when(opcode) {
        0 -> State((a / 2.0.pow(operandValue.toDouble())).toLong(), b, c, instructionPointer + 2, output)
        1 -> State(a, b xor operand.toLong(), c, instructionPointer + 2, output)
        2 -> State(a, operandValue % 8, c, instructionPointer + 2, output)
        3 -> if (a == 0L) {
            State(a, b, c, instructionPointer + 2, output)
        } else {
            State(a, b, c, operand, output)
        }
        4 -> State(a, b xor c, c, instructionPointer + 2, output)
        5 -> {
            output.add(operandValue % 8)
            State(a, b, c, instructionPointer + 2, output)
        }
        6 -> State(a, (a / 2.0.pow(operandValue.toDouble())).toLong(), c, instructionPointer + 2, output)
        7 -> State(a, b, (a / 2.0.pow(operandValue.toDouble())).toLong(), instructionPointer + 2, output)
        else -> throw IllegalArgumentException("Unsupported opcode $opcode")
    }
    //println("Opcode = $opcode, operand = $operand, next state = $ret")
    return ret
}

fun List<String>.parseInput(): Pair<State, List<Int>> {
    val a = this.first().replace("Register A: ", "").toLong()
    val b = this.drop(1).first().replace("Register B: ", "").toLong()
    val c = this.drop(2).first().replace("Register C: ", "").toLong()

    val program = this.drop(4).first().replace("Program: ", "").split(",").map { it.toInt() }

    return State(a, b, c, 0) to program
}

fun runProgram(initialState: State, program: List<Int>): State {
    var currentState = initialState
    while(currentState.instructionPointer != -1) {
        currentState = currentState.execute(program)
    }
    return currentState
}

fun findCandidateBits(regA: Long, expectedOutput: Long, state: State, program: List<Int>): List<Long> = buildList {
    for (bits in 0b000L..0b111L) {
        val candidate = (regA shl 3) or bits
        val output = runProgram(State(candidate, state.b, state.c, 0), program).output
        val isMatch = output.firstOrNull() == expectedOutput
        if (isMatch) add(candidate)
    }
}

fun main() {
    fun part1(input: List<String>): String {
        val (initialState, program) = input.parseInput()
        val endState = runProgram(initialState, program)
        return endState.output.joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val (initialState, program) = input.parseInput()

        return program
            .asReversed()
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap {
                    val candidate = findCandidateBits(it, instruction.toLong(), initialState, program)
                    println(candidate)
                    candidate
                }
            }
            .minOrNull() ?: -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17/Day17_test")
    //check(part1(testInput) == "4635635210")

    val input = readInput("Day17/Day17")
    //part1(input).println()

    val testInput2 = readInput("Day17/Day17_test1")
    //check(part2(testInput2) == 117440)
    part2(input).println()
}
