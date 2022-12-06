fun main() {
    val input = readInput("Day06").first()

    println("Part1: ${input.indexOfUnique(4)}")
    println("Part2: ${input.indexOfUnique(14)}")
}

private fun String.indexOfUnique(windowSize: Int): Int {
    var index = windowSize - 1
    while (index < length) {
        if (isLastUnique(windowSize, index)) {
            return index + 1
        }
        index += 1
    }
    return -1
}

private fun String.isLastUnique(windowSize: Int, endIndex: Int): Boolean {
    val set = mutableSetOf<Char>()
    var index = endIndex - windowSize + 1
    while (index <= endIndex) {
        if (!set.add(get(index))) {
            return false
        }
        index += 1
    }
    return true
}
