package com.abraham.survivorbirdgame.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.abraham.survivorbirdgame.model.Operation
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class MathCategoryScreen(private val game: Game) : Screen {

    data class CategoryCard(
        val operation: Operation,
        val title: String,
        val description: String,
        val image: Texture,
        val bounds: Rectangle
    )

    private lateinit var batch: SpriteBatch
    private lateinit var titleFont: BitmapFont
    private lateinit var descFont: BitmapFont
    private lateinit var cardFont: BitmapFont
    private lateinit var background: Texture

    private val categoryCards = mutableListOf<CategoryCard>()

    override fun show() {
        batch = SpriteBatch()

        // Fonts
        titleFont = BitmapFont().apply {
            data.setScale(6f)
            color = Color.valueOf("#E4004B")
        }
        descFont = BitmapFont().apply {
            data.setScale(2.5f)
            color = Color.valueOf("#541212")
        }
        cardFont = BitmapFont().apply {
            data.setScale(3.5f)
            color = Color.valueOf("#0F0E0E")
        }


        // Background
        background = Texture("images/bg2.jpg")

        // Card dimensions (bigger now)
        val cardWidth = 800f
        val cardHeight = 500f
        val gapX = 200f
        val gapY = 60f

        // Reserve some space at the top for the header (title + subtitle + padding)
        val headerOffset = 1200f
        val headerHeight = 250f

        val totalWidth = (2 * cardWidth) + gapX
        val startX = (Gdx.graphics.width - totalWidth) / 2f
        val startY = Gdx.graphics.height - headerHeight - cardHeight

        val operations = listOf(
            Triple(Operation.ADDITION, "images/addition1.jpg", "Learn how to add numbers together!"),
            Triple(Operation.SUBTRACTION, "images/subtraction1.jpg", "Understand subtraction step by step!"),
            Triple(Operation.MULTIPLICATION, "images/multiplication1.jpg", "Master times tables with fun!"),
            Triple(Operation.DIVISION, "images/division1.jpg", "Divide and conquer math problems!")
        )

        operations.forEachIndexed { index, (op, imagePath, desc) ->
            val row = index / 2
            val col = index % 2

            val x = startX + col * (cardWidth + gapX)
            val y = startY - row * (cardHeight + gapY)

            categoryCards.add(
                CategoryCard(
                    op,
                    op.name.lowercase().replaceFirstChar { it.titlecase() },
                    desc,
                    Texture(imagePath),
                    Rectangle(x, y, cardWidth, cardHeight)
                )
            )
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        // Draw background
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // ===== Global header (top of the screen) =====
        val titleLayout = GlyphLayout(titleFont, "Arithmetic Calculations for Kids")
        val centerX = Gdx.graphics.width / 2f

        val headerTopY = Gdx.graphics.height - 80f  // padding from top

        // Title
        titleFont.draw(batch, titleLayout, centerX - titleLayout.width / 2, headerTopY)

        // Subtitle (with padding below title)
        // ===== Cards (below header) =====
        categoryCards.forEach { card ->
            val bounds = card.bounds

            // Card image (in the middle of card, leaving top & bottom for text)
            batch.draw(
                card.image,
                bounds.x,
                bounds.y + 60f,
                bounds.width,
                bounds.height - 120f
            )

            // Card title (top of card)
            val titleLayoutCard = GlyphLayout(cardFont, card.title)
            val titleX = bounds.x + (bounds.width - titleLayoutCard.width) / 2
            val titleY = bounds.y + bounds.height
            cardFont.draw(batch, titleLayoutCard, titleX, titleY)

        }

        batch.end()

        handleInput()
    }

    private fun handleInput() {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()

            categoryCards.forEach { card ->
                if (card.bounds.contains(Vector2(touchX, touchY))) {
                    game.screen = MathematicGameScreen(game, card.operation)
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
        descFont.dispose()
        cardFont.dispose()
        background.dispose()
        categoryCards.forEach { it.image.dispose() }
    }
}
