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

class HomeScreen(private val game: Game): Screen {

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var titleFont:BitmapFont
    private lateinit var background:Texture
    private lateinit var cardBackground:Texture
    private lateinit var leftArrow:Texture
    private lateinit var rightArrow:Texture

    // List of carousel cards
    private val cards = mutableListOf<GameCard>()

    // Carousel scrolling
    private var scrollX = 0f
    private var startTouchX = 0f
    private var isDragging = false

    private val cardWidth = 500f
    private val cardHeight = 400f
    private val cardGap = 50f
    private var maxScroll = 0f

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
        titleFont = BitmapFont()
        titleFont.data.setScale(3f)

        shapeRenderer = ShapeRenderer()

        background = Texture("images/home_bg.png")
        cardBackground = Texture("images/card_bg.png")
        leftArrow = Texture("images/arrow_left.png")
        rightArrow = Texture("images/arrow_right.png")

        cards.add(GameCard("Math Master", "Solve fun math challenges!", Texture("images/math_game.png")) { MathCategoryScreen(game) })
        cards.add(GameCard("Catch Kenny", "Catch Kenny before he escapes!", Texture("images/catch_kenny.jpeg")) { CatchKennyScreen(game) })
        cards.add(GameCard("Bird Escape", "Dodge and survive!", Texture("images/bird_escape.png")) { BirdEscapeScreen(game) })
        cards.add(GameCard("Survivor Mode", "Endless flying challenge!", Texture("images/survivor_mode.png")) { SurvivorModeScreen(game) })
        cards.add(GameCard("Memory Match", "Test your memory!", Texture("images/memory_match.png")) { MemoryMatchScreen(game) })
        cards.add(GameCard("Puzzle Quest", "Solve tricky puzzles!", Texture("images/puzzle_quest.jpg")) { PuzzleQuestScreen(game) })

        maxScroll = ((cards.size * (cardWidth + cardGap)) - cardGap) - Gdx.graphics.width
        if (maxScroll < 0) maxScroll = 0f
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

        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // Draw cards
        cards.forEachIndexed { index, card ->
            val x = index * (cardWidth + cardGap) - scrollX // CHANGE: left-aligned carousel
            val y = centerY - cardHeight / 2

            if (x + cardWidth > 0 && x < Gdx.graphics.width) {
                batch.draw(card.image, x, y, cardWidth, cardHeight)
            }
        }
        // Navigation arrows - ADDED
        val centerYArrow = Gdx.graphics.height / 2f - 50f
        batch.draw(leftArrow, 50f, centerY - 50f, 100f, 100f)
        batch.draw(rightArrow, Gdx.graphics.width - 150f, centerY - 50f, 100f, 100f)

        batch.end()
    }

    private fun handleInput() {
        // Drag to scroll carousel
        val cardWidth = 500f
        val cardGap = 50f
        val maxScroll = (cards.size * (cardWidth + cardGap)) - cardWidth // ADDED
        scrollX = scrollX.coerceIn(0f, maxScroll) // ADDED

        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            if (!isDragging) {
                startTouchX = touchX
                isDragging = true
            } else {
                val delta = startTouchX - touchX
                scrollX += delta
                scrollX = scrollX.coerceIn(0f, maxScroll)
                startTouchX = touchX
            }
        } else {
            isDragging = false
        }

        // Tap to select a card
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()

            // Left arrow scroll
            if (Rectangle(50f, Gdx.graphics.height / 2f - 50f, 100f, 100f).contains(
                    touchX,
                    touchY
                )
            ) {
                scrollX -= cardWidth + cardGap // CHANGE: scroll left one card
            }

            // Right arrow scroll
            if (Rectangle(
                    Gdx.graphics.width - 150f,
                    Gdx.graphics.height / 2f - 50f,
                    100f,
                    100f
                ).contains(touchX, touchY)
            ) {
                scrollX += cardWidth + cardGap // CHANGE: scroll right one card
            }

            scrollX = scrollX.coerceIn(0f, maxScroll) // CHANGE: clamp scrollX

            // CHANGE: tap on card to select
            cards.forEachIndexed { index, card ->
                val x = index * (cardWidth + cardGap) - scrollX
                val y = Gdx.graphics.height / 2f - cardHeight / 2
                if (Rectangle(x, y, cardWidth, cardHeight).contains(touchX, touchY)) {
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
        background.dispose()
        cardBackground.dispose()
        leftArrow.dispose()
        rightArrow.dispose()
        cards.forEach { it.image.dispose() }
    }
}
