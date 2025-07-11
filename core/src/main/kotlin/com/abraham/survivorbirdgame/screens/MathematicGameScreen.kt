package com.abraham.survivorbirdgame.screens


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.graphics.g2d.BitmapFont

class MathematicGameScreen : Screen {
    private lateinit var batch: SpriteBatch
    private lateinit var playerTexture: Texture
    private lateinit var backgroundTexture: Texture
    private lateinit var heartTexture: Texture
    private lateinit var enemy1Texture: Texture
    private lateinit var enemy2Texture: Texture
    private lateinit var enemy3Texture: Texture
    private lateinit var font: BitmapFont

    private lateinit var player: Rectangle
    private val enemies = Array<Rectangle>()
    private val enemyAnswers = Array<String>()
    private var backgroundX1 = 0f
    private var backgroundX2 = 0f
    private var score = 0
    private var lives = 3
    private var level = 1
    private var lastQuestionTime = 0L
    private var question = ""
    private var correctAnswer = ""
    private val playerSpeed = 300f
    private val enemySpeed = 200f
    private val backgroundSpeed = 100f
    private val questionInterval = 5000000000L // 5 seconds in nanoseconds
    private val planeSize = 128f // Reduced size for player and enemy planes
    private lateinit var fontBig:BitmapFont
    private lateinit var fontSmall:BitmapFont

    override fun show() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.color = com.badlogic.gdx.graphics.Color.BLACK
        fontBig = BitmapFont()
        fontBig.data.setScale(8f)
        fontBig.color = Color.BLACK

        fontSmall = BitmapFont()
        fontSmall.data.setScale(3.5f)
        fontSmall.color = Color.BLACK

        // Load textures
        playerTexture = Texture(Gdx.files.internal("images/plane1.png"))
        backgroundTexture = Texture(Gdx.files.internal("images/background1.png"))
        heartTexture = Texture(Gdx.files.internal("images/heart.png"))
        enemy1Texture = Texture(Gdx.files.internal("images/enemy1.png"))
        enemy2Texture = Texture(Gdx.files.internal("images/enemy2.png"))
        enemy3Texture = Texture(Gdx.files.internal("images/enemy3.png"))

        // Initialize player
        player = Rectangle(50f, Gdx.graphics.height / 2f - planeSize / 2, planeSize, planeSize)

        // Initialize background
        backgroundX1 = 0f
        backgroundX2 = Gdx.graphics.width.toFloat()

        // Generate first question
        generateQuestion()
    }

    override fun render(delta: Float) {
        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update background
        backgroundX1 -= backgroundSpeed * delta
        backgroundX2 -= backgroundSpeed * delta
        if (backgroundX1 <= -Gdx.graphics.width) backgroundX1 = backgroundX2 + Gdx.graphics.width
        if (backgroundX2 <= -Gdx.graphics.width) backgroundX2 = backgroundX1 + Gdx.graphics.width

        // Update player position
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.y += playerSpeed * delta
            if (player.y > Gdx.graphics.height - player.height) player.y = Gdx.graphics.height - player.height
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.y -= playerSpeed * delta
            if (player.y < 0) player.y = 0f
        }

        if (Gdx.input.isTouched) {
            val touchY = Gdx.input.y.toFloat()
            val newY = Gdx.graphics.height - touchY - player.height / 2  // Invert Y coordinate
            player.y = newY.coerceIn(0f, Gdx.graphics.height - player.height)
        }


        // Update enemies
        val iterator = enemies.iterator()
        val answerIterator = enemyAnswers.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val answer = answerIterator.next()
            enemy.x -= enemySpeed * delta
            if (enemy.x < -planeSize) {
                iterator.remove()
                answerIterator.remove()
            }
            if (enemy.overlaps(player)) {
                if (answer == correctAnswer) {
                    score += 10
                    if (score >= level * 50) level++
                } else {
                    lives--
                    if (lives <= 0) {
                        // Game over logic (restart)
                        lives = 3
                        score = 0
                        level = 1
                    }
                }
                iterator.remove()
                answerIterator.remove()
                generateQuestion()
            }
        }

        // Generate new question if needed
        if (enemies.size == 0) {
            generateQuestion()
        }


        // Draw everything
        batch.begin()
        // Draw background
        batch.draw(backgroundTexture, backgroundX1, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.draw(backgroundTexture, backgroundX2, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        // Draw player
        batch.draw(playerTexture, player.x, player.y, planeSize, planeSize)
        // Draw enemies with answers (using fontBig for bigger answers)
        for (i in 0 until enemies.size) {
            val enemy = enemies[i]
            val texture = when (i % 3) {
                0 -> enemy1Texture
                1 -> enemy2Texture
                else -> enemy3Texture
            }
            batch.draw(texture, enemy.x, enemy.y, planeSize, planeSize)
            fontBig.draw(batch, enemyAnswers[i], enemy.x + planeSize + 20f, enemy.y + planeSize - 20f)
        }

// Draw UI (Level, Score, Lives) with fontSmall
        val uiX = 40f
        var uiY = Gdx.graphics.height - 80f

        fontSmall.draw(batch, "Level: $level", uiX, uiY)
        uiY -= 50f
        fontSmall.draw(batch, "Score: $score", uiX, uiY)
        uiY -= 60f
        val heartSize = 70f
        for (i in 0 until lives) {
            batch.draw(heartTexture, uiX + i * (heartSize + 10f), uiY - heartSize, heartSize, heartSize)
        }

// Draw question in big font, centered near top
        fontBig.draw(batch, question, Gdx.graphics.width / 2f - 150f, Gdx.graphics.height - 50f)

        batch.end()
    }

    private fun generateQuestion() {
        enemies.clear()
        enemyAnswers.clear()

        // Generate simple addition or subtraction question
        val num1 = MathUtils.random(1, 10 * level)
        val num2 = MathUtils.random(1, 10 * level)
        val isAddition = MathUtils.randomBoolean()
        question = if (isAddition) "$num1 + $num2 = ?" else "$num1 - $num2 = ?"
        correctAnswer = if (isAddition) (num1 + num2).toString() else (num1 - num2).toString()

        // Generate answers (1 correct, 3 wrong)
        val answers = mutableListOf(correctAnswer)
        while (answers.size < 4) {
            val wrongAnswer = (correctAnswer.toInt() + MathUtils.random(-10, 10)).toString()
            if (wrongAnswer != correctAnswer && wrongAnswer !in answers) {
                answers.add(wrongAnswer)
            }
        }
        answers.shuffle()

        // Spawn enemies
        val screenHeight = Gdx.graphics.height
        for (i in 0 until 4) {
            val enemyY = screenHeight / 5f * (i + 1) - planeSize / 2
            enemies.add(Rectangle(Gdx.graphics.width.toFloat(), enemyY, planeSize, planeSize))
            enemyAnswers.add(answers[i])
        }

        lastQuestionTime = TimeUtils.nanoTime()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        batch.dispose()
        playerTexture.dispose()
        backgroundTexture.dispose()
        heartTexture.dispose()
        enemy1Texture.dispose()
        enemy2Texture.dispose()
        enemy3Texture.dispose()
        font.dispose()
    }
}
