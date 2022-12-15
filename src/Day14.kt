import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day14")
        .filterNot { it.isEmpty() }
        .map { row -> row.split(" -> ").map { Point.from(it) } }

    val part1 = input.sandSimulation()
    println("Part1: $part1")

    val bounds = input.flatten().getBounds()
    val height = bounds.to.y + 2
    val bottomPath = listOf(listOf(Point(500 - height, height), Point(500 + height, height)))
    val part2 = (input + bottomPath).sandSimulation()
    println("Part2: $part2")
}

private fun List<List<Point>>.sandSimulation(): Int {
    val bounds = flatten().getBounds()

    val rows = bounds.to.y + 1
    val columns = bounds.to.x - bounds.from.x + 1

    val start = Point(500 - bounds.from.x, 0)

    val matrix = IntMatrix(rows, columns)
    forEach { path ->
        val points = path.map { Point(it.x - bounds.from.x, it.y) }
        matrix.fill(points)
    }

    var sand = start
    while (true) {
        sand = try {
            matrix.move(sand)
        } catch (e: NoSuchElementException) {
            matrix.set(sand.y, sand.x, FLAG_SAND)
            if (sand == start) {
                break
            }
            start
        } catch (e: IndexOutOfBoundsException) {
            break
        }
    }

    return matrix.asSequence().count { it == FLAG_SAND }
}

private fun IntMatrix.fill(path: List<Point>) {
    path.windowed(2).forEach { (p1, p2) ->
        val minX = min(p1.x, p2.x)
        repeat(abs(p2.x - p1.x) + 1) {
            set(p1.y, minX + it, FLAG_ROCK)
        }
        val minY = min(p1.y, p2.y)
        repeat(abs(p2.y - p1.y) + 1) {
            set(minY + it, p1.x, FLAG_ROCK)
        }
    }
}

private fun IntMatrix.move(point: Point): Point =
    sequenceOf(
        Point(point.x, point.y + 1),
        Point(point.x - 1, point.y + 1),
        Point(point.x + 1, point.y + 1),
    ).first { (x, y) -> get(y, x) == 0 }

private fun List<Point>.getBounds(): Bounds {
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    var maxY = Int.MIN_VALUE
    forEach {
        minX = min(minX, it.x)
        maxX = max(maxX, it.x)
        minY = min(minY, it.y)
        maxY = max(maxY, it.y)
    }
    return Bounds(Point(minX, minY), Point(maxX, maxY))
}

private data class Bounds(val from: Point, val to: Point)

private data class Point(val x: Int, val y: Int) {
    companion object {
        fun from(value: String): Point {
            val data = value.split(",").map { it.toInt() }
            return Point(data[0], data[1])
        }
    }
}

private const val FLAG_ROCK = 1
private const val FLAG_SAND = 2
