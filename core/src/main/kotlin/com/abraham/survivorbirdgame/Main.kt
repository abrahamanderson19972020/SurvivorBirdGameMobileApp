package com.abraham.survivorbirdgame

import com.abraham.survivorbirdgame.screens.LevelOneScreen
import com.badlogic.gdx.Game

class Main : Game() {
    override fun create() {
        setScreen(LevelOneScreen())
    }
}
