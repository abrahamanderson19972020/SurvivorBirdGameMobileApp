package com.abraham.survivorbirdgame.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.abraham.survivorbirdgame.managers.GameAssets
import com.abraham.survivorbirdgame.managers.GameUI
import com.abraham.survivorbirdgame.model.Button
import com.abraham.survivorbirdgame.model.Enemy
import com.abraham.survivorbirdgame.model.Operation
import com.abraham.survivorbirdgame.model.Player
import com.abraham.survivorbirdgame.model.Question
import com.abraham.survivorbirdgame.model.QuestionGenerator
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

class MathCategoryScreen(private val game: Game) : Screen {
    private val operations = Operation.values()
    private val buttons = mutableListOf<Button>()
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer

    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(3f)
        shapeRenderer = ShapeRenderer()

        val buttonWidth = 350f
        val buttonHeight = 150f
        val gap = 50f
        val startY = Gdx.graphics.height / 2f + ((operations.size - 1) * (buttonHeight + gap) / 2f)

        operations.forEachIndexed { index, op ->
            val x = (Gdx.graphics.width - buttonWidth) / 2f
            val y = startY - index * (buttonHeight + gap)
            buttons.add(Button(Rectangle(x, y, buttonWidth, buttonHeight), op.name, op))
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.5f, 0f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        buttons.forEach { button ->
            val hovered = button.bounds.contains(Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat()))
            shapeRenderer.color = if (hovered) Color.DARK_GRAY else Color.LIGHT_GRAY
            shapeRenderer.rect(button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height)
        }
        shapeRenderer.end()

        batch.begin()
        buttons.forEach { button ->
            val layout = GlyphLayout(font, button.text)
            val textX = button.bounds.x + (button.bounds.width - layout.width) / 2f
            val textY = button.bounds.y + (button.bounds.height + layout.height) / 2f
            font.draw(batch, layout, textX, textY)
        }
        batch.end()

        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            buttons.forEach { button ->
                if (button.bounds.contains(touchX, touchY)) {
                    game.screen = MathematicGameScreen(game, button.operation)
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
    }
}
