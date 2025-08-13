package com.abraham.survivorbirdgame.model

import com.badlogic.gdx.Screen
import com.badlogic.gdx.math.Rectangle

data class GameButton(
    val bounds:Rectangle,
    val title:String,
    val gameCategory: GameCategory,
    val screenFactory:() -> Screen
)
