import java.util.LinkedList

fun main() {
    val input = readInput("Day12")
        .filterNot { it.isEmpty() }
        .map { it.toCharArray() }

    val rows = input.size
    val columns = input.first().size

    var s = Vertex(0, 0)
    var e = Vertex(0, 0)
    repeat(rows) { i ->
        repeat(columns) { j ->
            if (input[i][j] == 'S') {
                input[i][j] = 'a'
                s = Vertex(i, j)
            }
            if (input[i][j] == 'E') {
                input[i][j] = 'z'
                e = Vertex(i, j)
            }
        }
    }

    val graph = Graph()
    graph.init(input)

    val distances = graph.vertices
        .associateWith { Int.MAX_VALUE }
        .toMutableMap()

    distances[e] = 0
    val queue = LinkedList<Vertex>()
    queue.add(e)
    while (queue.isNotEmpty()) {
        val vertex = queue.pop()
        graph.getNeighbours(vertex).forEach {
            val oldDistance = distances[it] ?: 0
            val newDistance = (distances[vertex] ?: 0) + 1
            if (newDistance < oldDistance) {
                distances[it] = newDistance
                queue.add(it)
            }
        }
    }

    val part1 = distances[s]
    println("Part1: $part1")

    val part2 = distances
        .filterKeys { input[it.row][it.column] == 'a' }
        .minOf { it.value }
    println("Part2: $part2")
}

private fun Graph.init(input: List<CharArray>) {
    val rows = input.size
    val columns = input.first().size

    repeat(rows) { i ->
        repeat(columns) { j ->
            val vertex = Vertex(i, j)
            buildList {
                if (i > 0) add(Vertex(i - 1, j))
                if (j > 0) add(Vertex(i, j - 1))
                if (i < rows - 1) add(Vertex(i + 1, j))
                if (j < columns - 1) add(Vertex(i, j + 1))
            }.filter {
                val from = input[i][j]
                val to = input[it.row][it.column]
                (from.code - to.code) <= 1
            }.forEach {
                add(vertex, it)
            }
        }
    }
}

private data class Vertex(val row: Int, val column: Int)

private class Graph {
    private val neighbours = mutableMapOf<Vertex, MutableList<Vertex>>()

    val vertices: List<Vertex> get() = neighbours.keys.toList()

    fun add(from: Vertex, to: Vertex) {
        val list = neighbours.getOrPut(from) { mutableListOf() }
        list.add(to)
    }

    fun getNeighbours(vertex: Vertex): List<Vertex> =
        requireNotNull(neighbours[vertex])
}
