fun main() {
    val input = readInput("Day07")
        .drop(1)
        .filterNot { it.isEmpty() }

    val root = Directory("/")
    var node = root
    input.forEach { row ->
        when {
            row.startsWith(COMMAND_CD) -> {
                val dirName = row.drop(COMMAND_CD.length)
                node = if (dirName == REF_GO_UP) {
                    requireNotNull(node.parent)
                } else {
                    requireNotNull(node.children.find { it.name == dirName } as Directory)
                }
            }

            row.startsWith(COMMAND_LS) -> {
                // Empty ¯\_(ツ)_/¯
            }

            else -> {
                val (size, name) = row.split(" ")
                val child = if (size == TYPE_DIRECTORY) {
                    Directory(name, node)
                } else {
                    File(name, size.toInt())
                }
                node.children.add(child)
            }
        }
    }
    val directories = root.directories

    val part1 = directories
        .filter { it.size < 100_000 }
        .sumOf { it.size }

    println("Part1: $part1")

    val totalSize = root.size
    val freeSpace = 70_000_000 - totalSize
    val needToFree = 30_000_000 - freeSpace
    val part2 = directories
        .filter { it.size > needToFree }
        .minOf { it.size }

    println("Part2: $part2")
}

private sealed interface Node {
    val name: String
    val size: Int
}

private class File(
    override val name: String,
    override val size: Int
) : Node

private class Directory(
    override val name: String,
    val parent: Directory? = null,
    val children: MutableList<Node> = mutableListOf()
) : Node {
    override val size: Int by lazy {
        children.fold(0) { acc, child -> acc + child.size }
    }

    val directories: List<Directory> by lazy {
        children
            .filterIsInstance<Directory>()
            .flatMap { it.directories }
            .plus(this)
    }
}

private const val COMMAND_CD = "$ cd "
private const val COMMAND_LS = "$ ls"
private const val TYPE_DIRECTORY = "dir"
private const val REF_GO_UP = ".."
