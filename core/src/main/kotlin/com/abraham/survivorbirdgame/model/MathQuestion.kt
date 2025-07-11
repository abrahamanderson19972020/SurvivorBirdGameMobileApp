package com.abraham.survivorbirdgame.model

data class MathQuestion(val question: String, val correctAnswer: Int, val wrongAnswers: List<Int>) {
    fun getShuffledAnswers(): List<Int> {
        return (wrongAnswers + correctAnswer).shuffled()
    }

    companion object {
        fun generate(level: Int): MathQuestion {
            val a = (1..level * 5).random()
            val b = (1..level * 5).random()
            val correct = a + b
            val wrong = mutableSetOf<Int>()

            while (wrong.size < 3) {
                val wrongAnswer = correct + (-3..3).random()
                if (wrongAnswer != correct && wrongAnswer >= 0) wrong.add(wrongAnswer)
            }

            return MathQuestion("$a + $b = ?", correct, wrong.toList())
        }
    }
}
