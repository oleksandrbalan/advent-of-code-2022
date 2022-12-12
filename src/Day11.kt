import java.util.Deque
import java.util.LinkedList

fun main() {
    val part1Monkeys = parseMonkeys()
    part1Monkeys.playWithItems(PART1_ROUNDS) { this / 3 }
    val part1 = part1Monkeys.calculateMonkeyBusiness()

    println("Part2: $part1")

    val part2Monkeys = parseMonkeys()
    val commonTestBy = part2Monkeys.fold(1) { acc, monkey -> acc * monkey.testBy }
    part2Monkeys.playWithItems(PART2_ROUNDS) { this % commonTestBy }
    val part2 = part2Monkeys.calculateMonkeyBusiness()

    println("Part2: $part2")
}

private fun parseMonkeys(): List<Monkey> =
    readInput("Day11")
        .filterNot { it.isEmpty() }
        .chunked(6) { parseMonkey(it) }

private fun List<Monkey>.playWithItems(rounds: Int, adjustWorryLevel: Long.() -> Long) =
    repeat(rounds) {
        forEach { monkey ->
            while (monkey.items.isNotEmpty()) {
                val oldWorryLevel = monkey.items.pop()
                val newWorryLevel = monkey.inspect(oldWorryLevel).adjustWorryLevel()
                val index = monkey.nextMonkey(newWorryLevel)
                this[index].items.push(newWorryLevel)
            }
        }
    }

private fun List<Monkey>.calculateMonkeyBusiness(): Long =
    sortedByDescending { it.inspected }
        .take(2)
        .fold(1L) { acc, monkey -> acc * monkey.inspected }

private fun parseMonkey(info: List<String>): Monkey {
    fun String.parseItems(): LinkedList<Long> =
        LinkedList<Long>().apply {
            addAll(split(":")[1].trim().split(",").map { it.trim().toLong() })
        }

    fun String.parseOperation(): (Long) -> Long {
        val rightSide = split("=")[1].trim().split(" ")
        val operand1 = rightSide[0].toLongOrNull()
        val operand2 = rightSide[2].toLongOrNull()
        return when (rightSide[1]) {
            "+" -> {
                { (operand1 ?: it) + (operand2 ?: it) }
            }

            "*" -> {
                { (operand1 ?: it) * (operand2 ?: it) }
            }

            else -> error("Operation '${rightSide[1]}' is not supported")
        }
    }

    fun String.parseTestBy(): Int =
        split(" ").last().toInt()

    fun List<String>.parseTest(): (Boolean) -> Int {
        val monkeys = takeLast(2).map { it.split(" ").last().toInt() }
        return { if (it) monkeys[0] else monkeys[1] }
    }

    return Monkey(
        items = info[1].parseItems(),
        testBy = info[3].parseTestBy(),
        operation = info[2].parseOperation(),
        test = info.parseTest()
    )
}

private class Monkey(
    val items: Deque<Long>,
    val testBy: Int,
    val operation: (Long) -> Long,
    val test: (Boolean) -> Int,
) {
    var inspected: Long = 0

    fun inspect(item: Long): Long {
        inspected += 1
        return operation(item)
    }

    fun nextMonkey(item: Long): Int = test(item % testBy == 0L)
}

private const val PART1_ROUNDS = 20
private const val PART2_ROUNDS = 10_000
