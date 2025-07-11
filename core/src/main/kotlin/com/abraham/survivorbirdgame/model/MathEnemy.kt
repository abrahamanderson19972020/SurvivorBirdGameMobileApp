package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.graphics.Texture

class MathEnemy(
    texture: Texture,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    val question: String,
    val answer: Int
) : Enemy(texture, x, y, width, height) {
    val options: List<Int>

    init {
        // Generate 2 wrong answers
        val wrongAnswers = mutableSetOf<Int>()
        while (wrongAnswers.size < 2) {
            val wrong = (answer - 10..answer + 10).random()
            if (wrong != answer) wrongAnswers.add(wrong)
        }
        options = (wrongAnswers + answer).shuffled()
    }
}

