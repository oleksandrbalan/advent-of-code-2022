import kotlin.math.abs

fun main() {
    val input = readInput("Day10")
        .filterNot { it.isEmpty() }
        .map {
            when {
                it.startsWith(COMMAND_ADDX) -> listOf(0, it.split(" ")[1].toInt())
                it.startsWith(COMMAND_NOOP) -> listOf(0)
                else -> error("Command not supported")
            }
        }
        .flatten()
        .scan(1) { acc, i -> acc + i }

    val part1 = CYCLES.sumOf { it * input[it - 1] }
    println("Part1: $part1")

    println("Part2:")
    val width = 40
    val height = 6
    repeat(height) { row ->
        repeat(width) { column ->
            val index = row * width + column
            val position = input[index]
            val pixel = if (abs(position - column) <= 1) PIXEL_WHITE else PIXEL_DARK
            print(pixel)
        }
        println()
    }
}

private const val COMMAND_ADDX = "addx"
private const val COMMAND_NOOP = "noop"
private const val PIXEL_WHITE = "#"
private const val PIXEL_DARK = "."

private val CYCLES = listOf(20, 60, 100, 140, 180, 220)
