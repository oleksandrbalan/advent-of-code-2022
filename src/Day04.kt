fun main() {
    val input = readInput("Day04")
        .filterNot { it.isEmpty() }
        .map {
            val (elf1, elf2) = it.split(",")
            elf1.range to elf2.range
        }


    val part1 = input.count { (range1, range2) ->
        range1.contains(range2) || range2.contains(range1)
    }

    println("Part1: $part1")

    val part2 = input.count { (range1, range2) ->
        range1.first in range2 || range2.first in range1
    }

    println("Part2: $part2")
}

private val String.range: IntRange
    get() = split("-")
        .map { it.toInt() }
        .let { it[0]..it[1] }

private fun IntRange.contains(other: IntRange): Boolean =
    first <= other.first && last >= other.last


