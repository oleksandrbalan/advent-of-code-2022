data class IntMatrix(
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
                append(get(row, column))
            }
            appendLine()
        }
    }
}

fun IntMatrix.asSequence(): Sequence<Int> = sequence {
    repeat(rows) { row ->
        repeat(columns) { column ->
            yield(get(row, column))
        }
    }
}
