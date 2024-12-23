package day23

import println
import readInput

typealias Network = Map<String, Set<String>>

fun List<String>.parseNetwork(): Network {
    val ret = mutableMapOf<String, HashSet<String>>()
    this.forEach {
        val (first, second) = it.split("-")
        val firstSet = ret.getOrDefault(first, hashSetOf())
        firstSet.add(second)
        ret[first] = firstSet

        val secondSet = ret.getOrDefault(second, hashSetOf())
        secondSet.add(first)
        ret[second] = secondSet
    }
    return ret
}

fun Network.extractTriplets(): Set<Set<String>> {
    val ret = hashSetOf<Set<String>>()

    this.keys.forEach { key ->
        val neighbors = this[key]!!

        neighbors.forEach { neighbor ->
            this[neighbor]!!.filter { it != key }.forEach { third ->
                if (this[third]!!.contains(key)) {
                    ret.add(hashSetOf(key, neighbor, third))
                }
            }
        }
    }

    return ret
}

fun Network.findSubnets(): Set<Set<String>> {
    val visited = HashSet<List<String>>()
    fun helper(key: String, currentSubnet: Set<String>): Set<Set<String>> {
        println("Helper with ($key, $currentSubnet)")
        val neighbors = this[key]!!
        if(visited.contains(currentSubnet.sorted())) {
            return setOf(currentSubnet)
        }
        return if(neighbors.containsAll(currentSubnet)) {
            neighbors.subtract(currentSubnet).flatMap { helper(it, currentSubnet + setOf(key)) }.toSet()
        } else {
            val ret = setOf(currentSubnet)
            visited.add(currentSubnet.sorted())
            ret
        }
    }


    val ret = hashSetOf<Set<String>>()
    this.keys.forEach { key ->
        val neighbors = this[key]!!
        println("Starting key $key, it has neighbors $neighbors")
        ret.addAll(neighbors.flatMap { helper(key, hashSetOf(it)) })
        println("Done with key $key")
    }

    return ret
}

fun main() {
    fun part1(input: List<String>): Int {
        val network = input.parseNetwork()

        val triplets = network.extractTriplets()

        return triplets.count { triplet -> triplet.any { it.startsWith("t") } }
    }

    fun part2(input: List<String>): String {
        val network = input.parseNetwork()
        val subnets = network.findSubnets()
        return subnets.maxBy { it.size }.sorted().joinToString(",")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23/Day23_test")
    check(part1(testInput) == 7)

    val input = readInput("Day23/Day23")
    //part1(input).println()

    check(part2(testInput) == "co,de,ka,ta")
    part2(input).println()
}
