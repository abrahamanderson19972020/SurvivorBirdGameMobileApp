package com.abraham.survivorbirdgame.screens


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.utils.viewport.ScreenViewport


class GameOverScreen(
    private val onRestart: () -> Unit
):Screen {
    private val stage = Stage(ScreenViewport())
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    private val font = BitmapFont()


    override fun show(){
        Gdx.input.inputProcessor = stage
        val table = Table()
        table.setFillParent(true)
        table.center()
        val binFont = skin.getFont("default-font")
        binFont.data.setScale(3f)
        val noBackgroundStyle = TextButton.TextButtonStyle()
        noBackgroundStyle.font = skin.getFont("default-font")
        noBackgroundStyle.font.data.setScale(3f)
        noBackgroundStyle.up = null
        noBackgroundStyle.down = null
        val restartButton = TextButton("RESTART GAME", noBackgroundStyle)
        val exitButton = TextButton("EXIT GAME", noBackgroundStyle)
        val labelStyle = LabelStyle(font, Color.RED)
        val gameOverLabel = Label("GAME OVER", labelStyle)
        gameOverLabel.setFontScale(5f)
        restartButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onRestart()
            }
        })
        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })
        table.add(gameOverLabel).colspan(2).padBottom(40f).row()
        table.add(restartButton).width(300f).height(80f).padRight(20f)
        table.add(exitButton).width(300f).height(80f)
        stage.addActor(table)
      }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        dispose()
    }
    override fun dispose() {
        stage.dispose()
        skin.dispose()
        font.dispose()
    }
}
