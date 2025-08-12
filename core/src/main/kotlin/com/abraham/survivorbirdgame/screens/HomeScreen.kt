package com.abraham.survivorbirdgame.screens

import com.abraham.survivorbirdgame.model.Button
import com.abraham.survivorbirdgame.model.Operation
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class HomeScreen(private val game:Game):Screen {

    private lateinit var batch: SpriteBatch
    private lateinit var font:BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer
    private val buttons = mutableListOf<Button>()


    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(3f)
        font.color = Color.SKY
        shapeRenderer = ShapeRenderer()

        // Create buttons for each operation
        val ops = listOf(
            Pair("ADDITION", Operation.ADDITION),
            Pair("SUBTRACTION", Operation.SUBTRACTION),
            Pair("MULTIPLICATION", Operation.MULTIPLICATION),
            Pair("DIVISION", Operation.DIVISION)
        )

        val buttonWidth = 350f
        val buttonHeight = 150f
        val gap = 50f
        val startY = Gdx.graphics.height/ 2f + ((ops.size - 1) * (buttonHeight + gap) / 2f)

        ops.forEachIndexed { index, (text, op) ->
            val x = (Gdx.graphics.width - buttonWidth) / 2f
            val y = startY - index * (buttonHeight + gap)
            buttons.add(Button(Rectangle(x, y, buttonWidth, buttonHeight), text, op))
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        buttons.forEach { button ->
            shapeRenderer.color = if (button.bounds.contains(Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())))
                com.badlogic.gdx.graphics.Color.DARK_GRAY else com.badlogic.gdx.graphics.Color.LIGHT_GRAY
            shapeRenderer.rect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height)
        }
        shapeRenderer.end()

        batch.begin()
        buttons.forEach { button ->
            val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, button.text)
            val textX = button.bounds.x + (button.bounds.width - layout.width) / 2f
            val textY = button.bounds.y + (button.bounds.height + layout.height) / 2f
            font.draw(batch, layout, textX, textY)
        }
        batch.end()

        // Handle input - on touch down, check if inside button bounds
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            buttons.forEach { button ->
                if (button.bounds.contains(touchX, touchY)) {
                    // Start game with selected operation
                    // Replace YourGameScreen with your actual screen class that accepts operation
                    game.screen = MathematicGameScreen(game, button.operation)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        shapeRenderer.dispose()
    }
}
