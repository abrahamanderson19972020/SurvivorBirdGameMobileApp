package com.abraham.survivorbirdgame.model


import com.abraham.survivorbirdgame.managers.GameAssets
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Player(
    private var x: Float,
    private var y: Float
) {
    val width = 256f
    val height = 128f
    var speed = 300f
    val rect = Rectangle(x, y, width, height)

    fun update(delta: Float) {
        var moveX = 0f
        var moveY = 0f

        // Keyboard controls
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveY += speed * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveY -= speed * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveX -= speed * delta
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveX += speed * delta
        }

        // Touch input controls
        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat() // invert Y axis

            val centerX = x + width / 2
            val centerY = y + height / 2

            val dx = touchX - centerX
            val dy = touchY - centerY

            val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

            if (distance > 5) { // small deadzone to avoid jitter
                val dirX = (dx / distance).toFloat()
                val dirY = (dy / distance).toFloat()

                // Calculate speed multipliers based on direction
                val horizontalMultiplier = if (dirX < 0) 1.5f else 1.0f  // faster backward
                val verticalMultiplier = if (dirY != 0f) 1.2f else 1.0f  // slightly faster vertical

                moveX += dirX * speed * delta * horizontalMultiplier
                moveY += dirY * speed * delta * verticalMultiplier
            }
        }

        // Update position with combined movement
        x += moveX
        y += moveY

        // Clamp position to screen bounds
        x = x.coerceIn(0f, Gdx.graphics.width - width)
        y = y.coerceIn(0f, Gdx.graphics.height - height)

        // Update collision rectangle position
        rect.setPosition(x, y)
    }

    fun updateSpeed(newSpeed: Float) {
        speed = newSpeed
    }

    fun draw(batch: SpriteBatch) {
        batch.draw(GameAssets.playerTexture, x, y, width, height)
    }
}
