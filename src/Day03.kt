import kotlin.math.log2

fun main() {
    val input = readInput("Day03")
        .filterNot { it.isEmpty() }

    val part1 = input.sumOf {
        val first = it.substring(0, it.length / 2)
        val second = it.substring(it.length / 2, it.length)
        val result = first.content and second.content
        log2(result.toDouble()).toInt() + 1
    }
    println("Part1: $part1")

    val part2 = input.windowed(3, 3).sumOf { (elf1, elf2, elf3) ->
        val result = elf1.content and elf2.content and elf3.content
        log2(result.toDouble()).toInt() + 1
    }
    println("Part2: $part2")
}

private val Char.priority: Int
    get() = if (isLowerCase()) code - 96 else code - 38

private val String.content: Long
    get() {
        var result = 0L
        forEach {
            val bit = 1L shl (it.priority - 1)
            result = result or bit
        }
        return result
    }
