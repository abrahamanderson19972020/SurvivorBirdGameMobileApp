package com.abraham.survivorbirdgame.model

import com.abraham.survivorbirdgame.managers.GameAssets
import com.badlogic.gdx.graphics.Texture


import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

open class Enemy(
    val answer: String,
    textureIndex: Int,
    private val size: Float
) {
    val rect = Rectangle()
    private val texture = GameAssets.enemyTextures[textureIndex]

    fun init(x: Float, y: Float) {
        rect.set(x, y, size, size)
    }

    fun update(delta: Float, speed: Float) {
        rect.x -= speed * delta
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(texture, rect.x, rect.y, rect.width, rect.height)
        GameAssets.fontBig.draw(batch, answer, rect.x + size + 20f, rect.y + size - 20f)
    }
}
