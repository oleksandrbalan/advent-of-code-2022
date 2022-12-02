fun main() {
    val input = readInput("Day02")
        .map {
            val options = it.split(" ")
            options[0] to options[1]
        }

    val roundsPart1 = input
        .map { (first, second) ->
            Round(
                your = Option.from(second),
                opponent = Option.from(first),
            )
        }

    println("Part1: ${roundsPart1.sumOf { it.score }}")

    val roundsPart2 = input
        .map { (first, second) ->
            val result = Result.from(second)
            val opponent = Option.from(first)
            Round(
                your = Option.from(result, opponent),
                opponent = opponent,
            )
        }

    println("Part1: ${roundsPart2.sumOf { it.score }}")
}

private data class Round(
    val your: Option,
    val opponent: Option,
) {

    private val result: Result =
        when {
            your == opponent -> Result.Draw
            your.wins() == opponent -> Result.Win
            else -> Result.Lose
        }

    val score: Int = your.score + result.score
}

private enum class Option(val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    fun wins(): Option = get { it - 1 }

    fun loses(): Option = get { it + 1 }

    private fun get(op: (Int) -> Int): Option {
        val size = values().size
        return Option.values()[(op(ordinal) + size) % size]
    }

    companion object {
        fun from(char: String): Option =
            when (char) {
                "A", "X" -> Rock
                "B", "Y" -> Paper
                "C", "Z" -> Scissors
                else -> error("Option `$char` is not supported \uD83D\uDE40")
            }

        fun from(result: Result, opponent: Option): Option =
            when (result) {
                Result.Draw -> opponent
                Result.Lose -> opponent.wins()
                Result.Win -> opponent.loses()
            }
    }
}

private enum class Result(val score: Int) {
    Lose(0),
    Draw(3),
    Win(6);

    companion object {
        fun from(char: String): Result =
            when (char) {
                "X" -> Lose
                "Y" -> Draw
                "Z" -> Win
                else -> error("Result `$char` is not supported \uD83D\uDE40")
            }
    }
}
