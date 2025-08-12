package com.abraham.survivorbirdgame.managers


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.Color

object GameAssets {
    val playerTexture = Texture(Gdx.files.internal("images/plane1.png"))
    val backgroundTexture = Texture(Gdx.files.internal("images/background1.png"))
    val heartTexture = Texture(Gdx.files.internal("images/heart.png"))
    val enemyTextures = listOf(
        Texture(Gdx.files.internal("images/enemy1.png")),
        Texture(Gdx.files.internal("images/enemy2.png")),
        Texture(Gdx.files.internal("images/enemy3.png"))
    )

    val fontSmall = BitmapFont().apply {
        data.setScale(3.5f)
        color = Color.BLACK
    }

    val fontBig = BitmapFont().apply {
        data.setScale(8f)
        color = Color.BLACK
    }

    fun dispose() {
        playerTexture.dispose()
        backgroundTexture.dispose()
        heartTexture.dispose()
        enemyTextures.forEach { it.dispose() }
        fontSmall.dispose()
        fontBig.dispose()
    }
}
