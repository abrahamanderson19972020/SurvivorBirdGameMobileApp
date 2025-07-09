package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.graphics.Texture

data class Enemy(
    val texture: Texture,
    var x: Float,
    var y: Float,
    val width: Float,
    val height: Float,
    var passed: Boolean = false
)
