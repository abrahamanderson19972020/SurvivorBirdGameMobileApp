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
import com.badlogic.gdx.utils.Align

class HomeScreen(private val game: Game): Screen {

    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer

    private lateinit var titleFont:BitmapFont
    private lateinit var taglineFont:BitmapFont
    private lateinit var descFont:BitmapFont

    private lateinit var background:Texture
    private lateinit var cardBackground:Texture
    private lateinit var leftArrow:Texture
    private lateinit var rightArrow:Texture

    private val cards = mutableListOf<GameCard>()

    private var scrollX = 0f
    private var startTouchX = 0f
    private var isDragging = false

    private val cardWidth = 650f
    private val cardHeight = 600f
    private val cardGap = 70f
    private var maxScroll = 0f
    private val leftPadding = 200f // Consistent left padding
    private val arrowVerticalOffset = 100f // CHANGE: Added vertical offset to avoid status bar/camera notch

    data class GameCard(
        val title: String,
        val description: String,
        val image: Texture,
        val screenFactory: () -> Screen
    )

    override fun show() {
        batch = SpriteBatch()
        titleFont = BitmapFont().apply {
            data.setScale(7f)
            color = Color.valueOf("#000000")
        }
        taglineFont = BitmapFont().apply {
            data.setScale(4f)
            color = Color.valueOf("#FF731D")
        }
        descFont = BitmapFont().apply {
            data.setScale(2.5f)
            color = Color.valueOf("#E0E0E0")
        }

        shapeRenderer = ShapeRenderer()

        background = Texture("images/home_bg7.jpg")
        leftArrow = Texture("images/arrow_left.png")
        rightArrow = Texture("images/arrow_right.png")

        cards.add(GameCard("Math Master", "Solve fun math challenges!", Texture("images/math_game.png")) { MathCategoryScreen(game) })
        cards.add(GameCard("Catch Kenny", "Catch Kenny before he escapes!", Texture("images/catch_kenny.jpeg")) { CatchKennyScreen(game) })
        cards.add(GameCard("Bird Escape", "Dodge and survive!", Texture("images/bird_escape.png")) { BirdEscapeScreen(game) })
        cards.add(GameCard("Memory Match", "Test your memory!", Texture("images/memory_match.png")) { MemoryMatchScreen(game) })
        cards.add(GameCard("Puzzle Quest", "Solve tricky puzzles!", Texture("images/puzzle_quest.jpg")) { PuzzleQuestScreen(game) })

        maxScroll = ((cards.size * (cardWidth + cardGap)) - cardGap) - (Gdx.graphics.width - leftPadding)
        if (maxScroll < 0) maxScroll = 0f
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.5f, 0f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        handleInput()

        val renderCardWidth = 600f
        val renderCardHeight = 500f
        val renderCardGap = 80f
        val centerX = Gdx.graphics.width / 2f
        val centerY = Gdx.graphics.height / 2f

        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        val paddingBetweenTitleAndTagline = 80f
        val paddingBetweenTaglineAndDesc = 80f
        val titleLayout = GlyphLayout(titleFont, "BrainQuest Adventures for Kids")
        val taglineLayout = GlyphLayout(taglineFont, "Fun challenges. Smart thinking. Big smiles.")
        var currentY = Gdx.graphics.height - 80f
        titleFont.draw(batch, titleLayout, centerX - titleLayout.width / 2, currentY)
        currentY -= titleLayout.height + paddingBetweenTitleAndTagline
        taglineFont.draw(batch, taglineLayout, centerX - taglineLayout.width / 2, currentY)
        currentY -= taglineLayout.height + paddingBetweenTaglineAndDesc

        // Draw cards with consistent left padding
        cards.forEachIndexed { index, card ->
            val x = leftPadding + index * (renderCardWidth + renderCardGap) - scrollX
            val y = centerY - renderCardHeight / 2

            if (x + renderCardWidth > 0 && x < Gdx.graphics.width) {
                batch.draw(card.image, x, y, renderCardWidth, renderCardHeight)
            }
        }
        // Navigation arrows with adjusted positions to avoid camera/status bar
        batch.draw(leftArrow, 100f, centerY - 50f + arrowVerticalOffset, 100f, 100f) // CHANGE: Moved left arrow to x=100f and added vertical offset
        batch.draw(rightArrow, Gdx.graphics.width - 150f, centerY - 50f + arrowVerticalOffset, 100f, 100f) // CHANGE: Added vertical offset to right arrow

        batch.end()
    }

    private fun handleInput() {
        // Drag to scroll carousel
        val inputCardWidth = 600f
        val inputCardGap = 80f
        val maxScroll = (cards.size * (inputCardWidth + inputCardGap)) - inputCardWidth
        scrollX = scrollX.coerceIn(0f, maxScroll)

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

            // Left arrow with adjusted position
            if (Rectangle(100f, Gdx.graphics.height / 2f - 50f + arrowVerticalOffset, 100f, 100f).contains(touchX, touchY)) { // CHANGE: Updated left arrow rectangle to match new position
                scrollX -= inputCardWidth + inputCardGap
                if (scrollX < 0) scrollX = 0f
            }
            // Right arrow with adjusted position
            if (Rectangle(Gdx.graphics.width - 150f, Gdx.graphics.height / 2f - 50f + arrowVerticalOffset, 100f, 100f).contains(touchX, touchY)) { // CHANGE: Updated right arrow rectangle
                scrollX += inputCardWidth + inputCardGap
            }

            scrollX = scrollX.coerceIn(0f, maxScroll)

            // Tap on card to select
            cards.forEachIndexed { index, card ->
                val x = leftPadding + index * (inputCardWidth + inputCardGap) - scrollX
                val y = Gdx.graphics.height / 2f - cardHeight / 2
                if (Rectangle(x, y, inputCardWidth, cardHeight).contains(touchX, touchY)) {
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
        titleFont.dispose()
        taglineFont.dispose()
        descFont.dispose()
        shapeRenderer.dispose()
        background.dispose()
        cardBackground.dispose()
        leftArrow.dispose()
        rightArrow.dispose()
        cards.forEach { it.image.dispose() }
    }
}
