package com.abraham.survivorbirdgame.model


import com.abraham.survivorbirdgame.managers.GameAssets
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Player(
    private val speed: Float,
    private val size: Float
) {
    val rect = Rectangle(50f, Gdx.graphics.height / 2f - size / 2, size, size)

    fun update(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            rect.y = (rect.y + speed * delta).coerceAtMost(Gdx.graphics.height - rect.height)

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            rect.y = (rect.y - speed * delta).coerceAtLeast(0f)

        if (Gdx.input.isTouched) {
            val newY = Gdx.graphics.height - Gdx.input.y - rect.height / 2
            rect.y = newY.coerceIn(0f, Gdx.graphics.height - rect.height)
        }
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(GameAssets.playerTexture, rect.x, rect.y, rect.width, rect.height)
    }
}
