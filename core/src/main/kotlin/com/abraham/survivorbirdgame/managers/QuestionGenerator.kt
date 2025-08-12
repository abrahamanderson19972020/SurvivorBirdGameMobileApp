package com.abraham.survivorbirdgame.managers


import com.badlogic.gdx.math.MathUtils

data class Question(val text: String, val correctAnswer: String, val answers: List<String>)

object QuestionGenerator {
    fun generate(level: Int): Question {
        val num1 = MathUtils.random(1, 10 * level)
        val num2 = MathUtils.random(1, 10 * level)
        val isAddition = MathUtils.randomBoolean()
        val correctAnswer = if (isAddition) (num1 + num2).toString() else (num1 - num2).toString()
        val questionText = if (isAddition) "$num1 + $num2 = ?" else "$num1 - $num2 = ?"

        val answers = mutableListOf(correctAnswer)
        while (answers.size < 4) {
            val wrongAnswer = (correctAnswer.toInt() + MathUtils.random(-10, 10)).toString()
            if (wrongAnswer != correctAnswer && wrongAnswer !in answers) {
                answers.add(wrongAnswer)
            }
        }
        answers.shuffle()

        return Question(questionText, correctAnswer, answers)
    }
}
