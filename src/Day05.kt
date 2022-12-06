import java.util.Deque
import java.util.LinkedList

fun main() {
    val (movesInput, shipInput) = readInput("Day05")
        .partition { it.startsWith("move") }

    val stacks = shipInput.first().length / 4 + 1
    val shipCargo = shipInput
        .takeWhile { it.trim().startsWith("[") }
        .map { row -> row.chunked(4) { it[1] } }
        .reversed()

    val moves = movesInput
        .map { command -> command.split(" ").mapNotNull { it.toIntOrNull() } }

    val shipPart1 = Ship(List(stacks) { LinkedList() }).apply {
        init(shipCargo)

        moves.forEach { (count, from, to) ->
            moveByOne(count, from - 1, to - 1)
        }
    }
    println("Part1: ${shipPart1.peek()}")

    val shipPart2 = Ship(List(stacks) { LinkedList() }).apply {
        init(shipCargo)

        moves.forEach { (count, from, to) ->
            moveMany(count, from - 1, to - 1)
        }
    }
    println("Part2: ${shipPart2.peek()}")
}

private data class Ship(
    val stacks: List<Deque<Char>>
) {
    fun init(cargo: List<List<Char>>) {
        cargo.forEach { crates ->
            repeat(stacks.size) {
                val crate = crates[it]
                if (crate != ' ') {
                    push(it, crate)
                }
            }
        }
    }

    fun push(to: Int, crate: Char) {
        stacks[to].push(crate)
    }

    fun moveByOne(count: Int, from: Int, to: Int) {
        repeat(count) {
            val crate = stacks[from].pop()
            stacks[to].push(crate)
        }
    }

    fun moveMany(count: Int, from: Int, to: Int) {
        val temporaryStack = LinkedList<Char>()
        repeat(count) {
            val crate = stacks[from].pop()
            temporaryStack.push(crate)
        }
        repeat(count) {
            val crate = temporaryStack.pop()
            stacks[to].push(crate)
        }
    }

    fun peek(): String =
        stacks.map { it.peek() }.joinToString("")
}

