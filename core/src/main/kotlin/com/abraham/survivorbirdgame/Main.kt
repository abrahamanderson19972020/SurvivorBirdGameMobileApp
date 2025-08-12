package com.abraham.survivorbirdgame

import com.abraham.survivorbirdgame.model.Operation
import com.abraham.survivorbirdgame.screens.HomeScreen
import com.abraham.survivorbirdgame.screens.MathematicGameScreen
import com.badlogic.gdx.Game

class Main : Game() {
    override fun create() {
        setScreen(HomeScreen(this))
    }
}
