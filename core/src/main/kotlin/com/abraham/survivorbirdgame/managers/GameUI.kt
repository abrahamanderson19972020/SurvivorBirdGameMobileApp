package com.abraham.survivorbirdgame.managers


import com.badlogic.gdx.graphics.g2d.SpriteBatch

object GameUI {
    fun draw(batch: SpriteBatch, score: Int, level: Int, lives: Int, question: String) {
        val uiX = 40f
        var uiY = com.badlogic.gdx.Gdx.graphics.height - 80f

        GameAssets.fontSmall.draw(batch, "Level: $level", uiX, uiY)
        uiY -= 50f
        GameAssets.fontSmall.draw(batch, "Score: $score", uiX, uiY)

        uiY -= 60f
        val heartSize = 70f
        for (i in 0 until lives) {
            batch.draw(GameAssets.heartTexture, uiX + i * (heartSize + 10f), uiY - heartSize, heartSize, heartSize)
        }

        GameAssets.fontBig.draw(batch, question, com.badlogic.gdx.Gdx.graphics.width / 2f - 150f, com.badlogic.gdx.Gdx.graphics.height - 50f)
    }
}
