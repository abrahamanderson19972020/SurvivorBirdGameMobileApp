package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.math.Rectangle

data class Button(
    val bounds: Rectangle,
    val text: String,
    val operation: Operation
)
