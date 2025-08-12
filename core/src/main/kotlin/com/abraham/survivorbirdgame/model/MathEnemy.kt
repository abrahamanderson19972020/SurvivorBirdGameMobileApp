package com.abraham.survivorbirdgame.model
import com.badlogic.gdx.math.MathUtils

class MathEnemy(
    question: String,
    correctAnswer: Int,
    textureIndex: Int,
    size: Float
) : Enemy(correctAnswer.toString(), textureIndex, size) {

    val questionText: String = question
    val correctAnswerValue: Int = correctAnswer
    val options: List<Int>

    init {
        // Generate 2 wrong answers near the correct one
        val wrongAnswers = mutableSetOf<Int>()
        while (wrongAnswers.size < 2) {
            val wrong = MathUtils.random(correctAnswer - 10, correctAnswer + 10)
            if (wrong != correctAnswer) wrongAnswers.add(wrong)
        }
        options = (wrongAnswers + correctAnswer).shuffled()
    }
}


