package com.abraham.survivorbirdgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.graphics.GL20

class LevelOneScreen : Screen {

    private lateinit var batch: SpriteBatch
    private lateinit var background: Texture
    private lateinit var bird: Texture

    private var birdX = 100f
    private var birdY = 200f

    private val birdSpeedX = 100f
    private val birdSpeedY = 200f

    private val birdScale = 0.2f // 20% of original size

    private var bgX1 = 0f      // position of the first background
    private var bgX2 = 0f      // position of the second background
    private var bgSpeed = 100f // same as birdSpeedX

    private var bgWidth = 0f   // will be set to background texture width

    // Adding Text for User Score and Level
    private lateinit var font:BitmapFont
    private var score = 0
    private var level = 1

    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(3f)
        font.color = Color.BLACK

        background = Texture(Gdx.files.internal("images/background1.png"))
        bird = Texture(Gdx.files.internal("images/plane1.png"))

        bgWidth = background.width.toFloat()
        bgX2 = bgWidth // place second background right after the first one
    }

    override fun render(delta: Float) {
        handleInput(delta)
        updateBackground(delta)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        score += (10 * delta).toInt()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        batch.begin()

        // ✅ First draw backgrounds
        batch.draw(background, bgX1, 0f, bgWidth, Gdx.graphics.height.toFloat())
        batch.draw(background, bgX2, 0f, bgWidth, Gdx.graphics.height.toFloat())

// ✅ Then draw bird
        val birdWidth = bird.width * birdScale
        val birdHeight = bird.height * birdScale
        batch.draw(bird, birdX, birdY, birdWidth, birdHeight)

// ✅ Then draw text on top of everything
        val startX = 20f
        val startY = Gdx.graphics.height - 20f
        val lineSpacing = 50f

        font.draw(batch, "Level: $level", startX, startY)
        font.draw(batch, "Score: $score", startX, startY - lineSpacing)
        batch.end()
    }

    private fun handleInput(delta: Float) {
        // Update Y position based on input
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched && Gdx.input.y < Gdx.graphics.height / 2)) {
            birdY += birdSpeedY * delta
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched && Gdx.input.y >= Gdx.graphics.height / 2)) {
            birdY -= birdSpeedY * delta
        }

        // Keep bird inside screen vertically
        birdY = birdY.coerceIn(0f, Gdx.graphics.height - bird.height * birdScale)
    }

    private fun updateBackground(delta: Float) {
        // Move both backgrounds leftward (to simulate bird moving right)
        bgX1 -= bgSpeed * delta
        bgX2 -= bgSpeed * delta

        // If one background scrolls off-screen, reset it to the right
        if (bgX1 + bgWidth <= 0) {
            bgX1 = bgX2 + bgWidth
        }

        if (bgX2 + bgWidth <= 0) {
            bgX2 = bgX1 + bgWidth
        }
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
        bird.dispose()
        font.dispose()
    }
}
