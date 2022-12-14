fun main() {
    val input = readInput("Day13")
        .filterNot { it.isEmpty() }
        .map { it.parseRoot() }

    val part1 = input
        .chunked(2)
        .mapIndexed { index, (left, right) -> if (left < right) index + 1 else -0 }
        .sum()

    println("Part1: $part1")

    val divider1 = "[[2]]".parseRoot()
    val divider2 = "[[6]]".parseRoot()

    val list = (input + divider1 + divider2).sorted()
    val part2 = (list.indexOf(divider1) + 1) * (list.indexOf(divider2) + 1)

    println("Part2: $part2")
}

private fun String.parseRoot(): Item {
    var index = 0

    fun String.parseItem(): Item {
        if (get(index).isDigit()) {
            var number = ""
            while (get(index).isDigit()) {
                number += get(index)
                index += 1
            }
            return Number(number.toInt())
        }
        if (get(index) == '[') {
            index += 1
            val items = mutableListOf<Item>()
            while (get(index) != ']') {
                val a = parseItem()
                items.add(a)
                if (get(index) == ',') {
                    index += 1
                }
            }
            index += 1
            return ItemList(items)
        }
        return ItemList(emptyList())
    }

    return parseItem()
}

private sealed interface Item : Comparable<Item>

private data class Number(val value: Int) : Item {
    override fun compareTo(other: Item): Int =
        when (other) {
            is Number -> value.compareTo(other.value)
            is ItemList -> ItemList(listOf(this)).compareTo(other)
        }
}

private data class ItemList(val items: List<Item>) : Item {
    override fun compareTo(other: Item): Int =
        when (other) {
            is Number -> this.compareTo(ItemList(listOf(other)))
            is ItemList -> {
                val zip = items.zip(other.items)
                val zipCompare = zip
                    .map { it.first.compareTo(it.second) }
                    .firstOrNull { it != 0 }
                    ?: 0
                if (zipCompare != 0) {
                    zipCompare
                } else {
                    items.size.compareTo(other.items.size)
                }
            }
        }
}
