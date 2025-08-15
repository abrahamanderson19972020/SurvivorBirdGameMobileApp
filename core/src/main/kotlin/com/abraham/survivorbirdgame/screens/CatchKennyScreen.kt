package com.abraham.survivorbirdgame.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.TimeUtils


class CatchKennyScreen(private val game: Game):Screen {
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var titleFont: BitmapFont
    private lateinit var kennyTexture:Texture
    private lateinit var shapeRenderer: ShapeRenderer
    private var kennyRect = Rectangle()
    private var score = 0
    private var level = 1
    private val maxLevel = 10
    private val scorePerLevel = 10
    private var moveInterval = 1f
    private var lastMoveTime = 0L
    private var timeLeft = 200f
    private val borderInset = 150f
    private var kennyScale = 1f
    private var kennyTouchedTime = 0L
    private val kennyTouchDuration = 200L
    private val titlePaddingTop = 40f



    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(3f)
        titleFont = BitmapFont().apply {
            data.setScale(4f)
            color = Color.valueOf("#EE4540")
        }
        kennyTexture = Texture("images/catch_kenny.jpeg")
        shapeRenderer = ShapeRenderer()
        kennyRect.setSize(250f, 250f)
        moveKenny()
        lastMoveTime = TimeUtils.millis()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        timeLeft -= delta
        if (timeLeft <= 0) {
            gameOver()
        }

        // Move Kenny periodically
        if (TimeUtils.timeSinceMillis(lastMoveTime) > (moveInterval * 1000)) {
            moveKenny()
            lastMoveTime = TimeUtils.millis()
        }

        handleInput()

        // Draw boundary area
        Gdx.gl.glLineWidth(3f)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLACK // black border

        shapeRenderer.rect(
            borderInset, // left padding
            borderInset, // bottom padding
            Gdx.graphics.width.toFloat() - borderInset * 2, // width with padding on both sides
            Gdx.graphics.height.toFloat() - borderInset * 2 // height with padding on top and bottom
        )

        shapeRenderer.end()
        Gdx.gl.glLineWidth(1f)

        // Draw background blocks for score, level, timer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0.85f, 0.85f, 0.85f, 1f)
        shapeRenderer.rect(40f, Gdx.graphics.height - 80f, 220f, 70f) // SCORE
        shapeRenderer.rect(40f, Gdx.graphics.height - 140f, 220f, 70f) // LEVEL
        shapeRenderer.rect(Gdx.graphics.width - 270f, Gdx.graphics.height - 80f, 230f, 50f) // TIMER
        shapeRenderer.end()

        // Gradually reset scale after touch
        val elapsed = TimeUtils.timeSinceMillis(kennyTouchedTime)
        if (elapsed < kennyTouchDuration) {
            // Kenny stays scaled up
        } else {
            kennyScale = 1f
        }

        batch.begin()  // <--- must begin before any draw()

// Draw Kenny with scaling effect
        val centerX = kennyRect.x + kennyRect.width / 2
        val centerY = kennyRect.y + kennyRect.height / 2
        batch.draw(
            kennyTexture,
            centerX - kennyRect.width / 2 * kennyScale,
            centerY - kennyRect.height / 2 * kennyScale,
            kennyRect.width * kennyScale,
            kennyRect.height * kennyScale
        )

// Draw UI text
        font.color = Color.BLACK
        font.draw(batch, "SCORE: $score", 150f, Gdx.graphics.height - 50f)
        font.draw(batch, "LEVEL: $level", 150f, Gdx.graphics.height - 100f)
        font.draw(batch, "TIMER: ${timeLeft.toInt()}", Gdx.graphics.width - 380f, Gdx.graphics.height - 50f)
        val titleLayout = GlyphLayout(titleFont, "CATCH KENNY")
        titleFont.draw(
            batch,
            titleLayout,
            (Gdx.graphics.width - titleLayout.width) / 2f,
            Gdx.graphics.height - titlePaddingTop
        )

        batch.end()  // <--- end after all drawing

    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()

            if (kennyRect.contains(touchX, touchY)) {
                score++
                kennyScale = 1.3f
                kennyTouchedTime = TimeUtils.millis()
                if ((score / scorePerLevel) + 1 > level && level < maxLevel) {
                    level++
                    moveInterval = 1f - (level - 1) * 0.1f
                }
            }
        }
    }

    private fun moveKenny() {
        val maxX = Gdx.graphics.width - borderInset - kennyRect.width
        val maxY = Gdx.graphics.height - borderInset - kennyRect.height
        val minX = borderInset
        val minY = borderInset

        kennyRect.setPosition(
            MathUtils.random(minX, maxX),
            MathUtils.random(minY, maxY)
        )
    }

    private fun gameOver() {
        // Simple restart: go back to home screen
        game.screen = HomeScreen(game)
    }


    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        titleFont.dispose()
        kennyTexture.dispose()
    }
}
