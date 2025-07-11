package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class AnswerBird(
    private val texture: Texture,
    var x: Float,
    var y: Float,
    val answer: Int,
    val isCorrect: Boolean
) {
    val bounds = Rectangle(x, y, texture.width.toFloat(), texture.height.toFloat())

    fun update(delta: Float) {
        x -= 200 * delta
        bounds.setPosition(x, y)
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, x, y)
        val font = BitmapFont()
        font.draw(batch, answer.toString(), x + 20f, y + texture.height + 20f)
    }
}
