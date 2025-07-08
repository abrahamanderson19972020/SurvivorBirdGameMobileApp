package com.abraham.survivorbirdgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class LevelOneScreen : Screen {
    private lateinit var batch: SpriteBatch
    private lateinit var background: Texture

    override fun show() {
        batch = SpriteBatch()
        background = Texture(Gdx.files.internal("images/background1.png"))
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {
        dispose()
    }

    override fun dispose() {
        batch.dispose()
        background.dispose()
    }
}
