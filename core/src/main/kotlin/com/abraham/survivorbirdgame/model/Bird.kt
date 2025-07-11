package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Bird(private val texture: Texture) {
    var x = 100f
    var y = 300f
    private val speed = 300f
    val bounds = Rectangle(x, y, texture.width.toFloat(), texture.height.toFloat())

    fun update(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += speed * delta
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= speed * delta
        bounds.setPosition(x, y)
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, x, y)
    }
}
