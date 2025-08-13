package com.abraham.survivorbirdgame.screens

import com.abraham.survivorbirdgame.model.GameButton
import com.abraham.survivorbirdgame.model.GameCategory
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
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class HomeScreen(private val game: Game): Screen {

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer

    // List of carousel cards
    private val cards = mutableListOf<GameCard>()

    // Carousel scrolling
    private var scrollX = 0f
    private var startTouchX = 0f
    private var isDragging = false

    data class GameCard(
        val title: String,
        val description: String,
        val image: Texture,
        val screenFactory: () -> Screen
    )

    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(2f)
        shapeRenderer = ShapeRenderer()

        // Add your game cards here
        cards.add(GameCard(
            "Math Master",
            "Solve fun math challenges!",
            Texture("images/math_game.png"),
            { MathCategoryScreen(game) }
        ))

        cards.add(GameCard(
            "Catch Kenny",
            "Catch Kenny before he escapes!",
            Texture("images/catch_kenny.jpeg"),
            { CatchKennyScreen(game) }
        ))

        // More games can be added here
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.5f, 0f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        handleInput()

        val cardWidth = 500f
        val cardHeight = 400f
        val cardGap = 50f
        val centerX = Gdx.graphics.width / 2f
        val centerY = Gdx.graphics.height / 2f

        // Draw cards
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        cards.forEachIndexed { index, card ->
            val x = centerX - cardWidth / 2 + (index * (cardWidth + cardGap)) - scrollX
            val y = centerY - cardHeight / 2

            // Only draw cards visible on screen for performance
            if (x + cardWidth > 0 && x < Gdx.graphics.width) {
                shapeRenderer.color = Color.WHITE
                shapeRenderer.rect(x, y, cardWidth, cardHeight)
            }
        }
        shapeRenderer.end()

        // Draw card images and text
        batch.begin()
        cards.forEachIndexed { index, card ->
            val x = centerX - cardWidth / 2 + (index * (cardWidth + cardGap)) - scrollX
            val y = centerY - cardHeight / 2

            if (x + cardWidth > 0 && x < Gdx.graphics.width) {
                // Draw game image
                batch.draw(card.image, x + 20f, y + cardHeight / 3f, cardWidth - 40f, cardHeight / 2f)

                // Draw title
                val titleLayout = GlyphLayout(font, card.title)
                font.color = Color.BLACK
                font.draw(batch, titleLayout, x + (cardWidth - titleLayout.width) / 2f, y + cardHeight - 40f)

                // Draw description
                val descLayout = GlyphLayout(font, card.description)
                font.draw(batch, descLayout, x + (cardWidth - descLayout.width) / 2f, y + 40f)
            }
        }
        batch.end()
    }

    private fun handleInput() {
        // Drag to scroll carousel
        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            if (!isDragging) {
                startTouchX = touchX
                isDragging = true
            } else {
                val delta = startTouchX - touchX
                scrollX += delta
                startTouchX = touchX
            }
        } else {
            isDragging = false
        }

        // Tap to select a card
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()

            val cardWidth = 500f
            val cardHeight = 400f
            val cardGap = 50f
            val centerX = Gdx.graphics.width / 2f
            val centerY = Gdx.graphics.height / 2f

            cards.forEachIndexed { index, card ->
                val x = centerX - cardWidth / 2 + (index * (cardWidth + cardGap)) - scrollX
                val y = centerY - cardHeight / 2
                val rect = Rectangle(x, y, cardWidth, cardHeight)
                if (rect.contains(touchX, touchY)) {
                    game.screen = card.screenFactory()
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        batch.dispose()
        font.dispose()
        shapeRenderer.dispose()
        cards.forEach { it.image.dispose() }
    }
}
