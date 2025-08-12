package com.abraham.survivorbirdgame

import com.abraham.survivorbirdgame.screens.MathematicGameScreen
import com.badlogic.gdx.Game

class Main : Game() {
    override fun create() {
        setScreen(MathematicGameScreen(this))
    }
}
