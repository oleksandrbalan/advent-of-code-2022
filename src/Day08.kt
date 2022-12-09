fun main() {
    val input = readInput("Day08")
        .filterNot { it.isEmpty() }

    val rows = input.size
    val columns = input.first().length
    val matrix = IntMatrix(rows, columns) { row, column ->
        input[row][column].digitToInt()
    }

    // Matrix with 0 and 1, where 1 represents that the tree is visible
    val visibilityMatrix = IntMatrix(rows, columns)

    // Fill the new matrix with 4 double cycles, one for each side
    // The complexity then should be 4*n*m ~ O(n*m),
    // where n is count of rows and m is a count of columns
    computeVisibility(matrix, visibilityMatrix, Traverse.LeftToRight(rows, columns))
    computeVisibility(matrix, visibilityMatrix, Traverse.RightToLeft(rows, columns))
    computeVisibility(matrix, visibilityMatrix, Traverse.TopToBottom(rows, columns))
    computeVisibility(matrix, visibilityMatrix, Traverse.BottomToTop(rows, columns))

    val part1 = visibilityMatrix.asSequence().count { it > 0 }
    println("Part1: $part1")

    // Matrix with scenic score of the tree
    val scenicScoreMatrix = IntMatrix(rows, columns) { _, _ -> 1 }

    // Fill the new matrix with 4 double cycles, one for each side
    // The complexity then should be 4*n*m*h ~ O(n*m),
    // where n is count of rows and m is a count of columns, and h (constant = 9) is a max height
    // of the tree
    computeHeights(matrix, scenicScoreMatrix, Traverse.LeftToRight(rows, columns))
    computeHeights(matrix, scenicScoreMatrix, Traverse.RightToLeft(rows, columns))
    computeHeights(matrix, scenicScoreMatrix, Traverse.TopToBottom(rows, columns))
    computeHeights(matrix, scenicScoreMatrix, Traverse.BottomToTop(rows, columns))

    val part2 = scenicScoreMatrix.asSequence().max()
    println("Part2: $part2")
}

private fun computeVisibility(
    from: IntMatrix,
    to: IntMatrix,
    traverse: Traverse,
) {
    repeat(traverse.outer) { i ->
        var max = -1
        repeat(traverse.inner) { j ->
            val (pi, pj) = traverse.projection(i, j)
            val value = from.get(pi, pj)
            if (value > max) {
                max = value
                to.set(pi, pj, 1)
            }
        }
    }
}

private fun computeHeights(
    from: IntMatrix,
    to: IntMatrix,
    traverse: Traverse,
) {
    repeat(traverse.outer) { i ->
        val heightMap = Array(10) { -1 }
        repeat(traverse.inner) { j ->
            val (pi, pj) = traverse.projection(i, j)
            val value = from.get(pi, pj)
            val distance = heightMap[value].let { if (it >= 0) j - it else j }
            heightMap.fill(value, j)
            to.set(pi, pj, to.get(pi, pj) * distance)
        }
    }
}

private data class IntMatrix(
    val rows: Int,
    val columns: Int,
    val init: (Int, Int) -> Int = { _, _ -> 0 }
) {
    private val data: Array<IntArray> = Array(rows) { row ->
        IntArray(columns) { column ->
            init(row, column)
        }
    }

    fun get(row: Int, column: Int): Int =
        data[row][column]

    fun set(row: Int, column: Int, value: Int) {
        data[row][column] = value
    }

    override fun toString(): String = buildString {
        repeat(rows) { row ->
            repeat(columns) { column ->
                appendLine("$row x $column = ${get(row, column)}")
            }
        }
    }
}

private fun IntMatrix.asSequence(): Sequence<Int> = sequence {
    repeat(rows) { row ->
        repeat(columns) { column ->
            yield(get(row, column))
        }
    }
}

private fun Array<Int>.fill(to: Int, value: Int) {
    repeat(to + 1) {
        set(it, value)
    }
}

private sealed class Traverse(
    val outer: Int,
    val inner: Int,
    val projection: (Int, Int) -> Pair<Int, Int>,
) {
    class LeftToRight(rows: Int, columns: Int) : Traverse(
        outer = rows,
        inner = columns,
        projection = { i, j -> i to j }
    )

    class RightToLeft(rows: Int, columns: Int) : Traverse(
        outer = rows,
        inner = columns,
        projection = { i, j -> i to columns - j - 1 }
    )

    class TopToBottom(rows: Int, columns: Int) : Traverse(
        outer = columns,
        inner = rows,
        projection = { i, j -> j to i }
    )

    class BottomToTop(rows: Int, columns: Int) : Traverse(
        outer = columns,
        inner = rows,
        projection = { i, j -> columns - j - 1 to i }
    )
}
