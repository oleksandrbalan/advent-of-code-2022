import kotlin.math.abs

fun main() {
    val input = readInput("Day09")
        .filterNot { it.isEmpty() }
        .map {
            val (direction, count) = it.split(" ")
            List(count.toInt()) { direction }
        }
        .flatten()

    val part1Rope = Array(2) { Coordinate(0, 0) }
    val part1Positions = part1Rope.move(input)
    val part1 = part1Positions.toSet().size
    println("Part1: $part1")

    val part2Rope = Array(10) { Coordinate(0, 0) }
    val part2Positions = part2Rope.move(input)
    val part2 = part2Positions.toSet().size
    println("Part2: $part2")
}

private fun Array<Coordinate>.move(directions: List<String>): List<Coordinate> {
    val tailPositions = mutableListOf<Coordinate>()
    tailPositions.add(last())
    directions.forEach { direction ->
        this[0] = this[0].move(direction)
        (1 until size).forEach {
            this[it] = this[it].keepUp(this[it - 1])
        }
        tailPositions.add(last())
    }
    return tailPositions
}

private fun Coordinate.move(direction: String): Coordinate =
    when (direction) {
        "L" -> Coordinate(x - 1, y)
        "R" -> Coordinate(x + 1, y)
        "U" -> Coordinate(x, y + 1)
        "D" -> Coordinate(x, y - 1)
        else -> error("Direction is not supported: $direction")
    }

private fun Coordinate.keepUp(head: Coordinate): Coordinate {
    val dx = head.x - x
    val dy = head.y - y
    if (abs(dx) <= 1 && abs(dy) <= 1) return this
    return Coordinate(x + dx.coerceIn(-1, 1), y + dy.coerceIn(-1, 1))
}

private data class Coordinate(val x: Int, val y: Int)
