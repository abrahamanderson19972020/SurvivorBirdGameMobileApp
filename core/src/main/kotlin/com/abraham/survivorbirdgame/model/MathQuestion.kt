package com.abraham.survivorbirdgame.model

import kotlin.random.Random


object QuestionGenerator {
    fun generate(level: Int, operation: Operation): Question {
        val maxNumber = when (level) {
            1 -> 9
            2 -> 19
            3 -> 99
            4 -> 199
            5 -> 999
            6 -> 1999
            7 -> 9999
            8 -> 19999
            9 -> 99999
            10 -> 199999
            else -> 9
        }

        val a = Random.nextInt(1, maxNumber + 1)
        val b = Random.nextInt(1, maxNumber + 1)

        return when (operation) {
            Operation.ADDITION -> {
                val correct = a + b
                val wrong = generateWrongAnswers(correct)
                Question("$a + $b = ?", correct, wrong + correct)
            }
            Operation.SUBTRACTION -> {
                val (x, y) = if (a >= b) Pair(a, b) else Pair(b, a) // avoid negative answers
                val correct = x - y
                val wrong = generateWrongAnswers(correct)
                Question("$x - $y = ?", correct, wrong + correct)
            }
            Operation.MULTIPLICATION -> {
                val correct = a * b
                val wrong = generateWrongAnswers(correct)
                Question("$a x $b = ?", correct, wrong + correct)
            }
            Operation.DIVISION -> {
                // For division, ensure divisible numbers
                val correct = if (b != 0) a else 1
                val dividend = correct * b
                val divisor = b
                val wrong = generateWrongAnswers(correct)
                Question("$dividend / $divisor = ?", correct, wrong + correct)
            }
        }
    }

    private fun generateWrongAnswers(correct: Int): List<Int> {
        val wrongAnswers = mutableSetOf<Int>()
        while (wrongAnswers.size < 3) {
            val wrong = correct + Random.nextInt(-10, 11)
            if (wrong != correct && wrong >= 0) wrongAnswers.add(wrong)
        }
        return wrongAnswers.toList()
    }
}
