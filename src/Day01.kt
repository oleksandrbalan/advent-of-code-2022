fun main() {
    val input = readInput("Day01")
    val calories = input.chunkedBy { it.isEmpty() }
        .map { list -> list.sumOf { it.toLong() } }
        .sortedDescending()
    println("Part1: ${calories.maxOrNull() ?: 0}")
    println("Part2: ${calories.take(3).sum()}")
}

private fun <T> List<T>.chunkedBy(separator: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    val resultItem = mutableListOf<T>()
    forEach { item ->
        if (separator(item)) {
            result.add(resultItem.toList())
            resultItem.clear()
        } else {
            resultItem.add(item)
        }
    }
    result.add(resultItem.toList())
    return result
}
