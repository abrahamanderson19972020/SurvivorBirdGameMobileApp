package com.abraham.survivorbirdgame.screens


import com.abraham.survivorbirdgame.*
import com.abraham.survivorbirdgame.managers.GameAssets
import com.abraham.survivorbirdgame.managers.GameUI
import com.abraham.survivorbirdgame.model.Enemy
import com.abraham.survivorbirdgame.model.Operation
import com.abraham.survivorbirdgame.model.Player
import com.abraham.survivorbirdgame.model.Question
import com.abraham.survivorbirdgame.model.QuestionGenerator
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Array

class MathematicGameScreen(private val game:com.badlogic.gdx.Game, private val operation: Operation) : Screen {
    private lateinit var batch: SpriteBatch
    private lateinit var player: Player
    private val enemies = Array<Enemy>()

    private var score = 0
    private var lives = 3
    private var level = 1
    private var questionsAnsweredCorrectly = 0
    private var wrongAnswers = 0

    private lateinit var currentQuestion: Question

    // Background scroll variables
    private var backgroundX1 = 0f
    private var backgroundX2 = 0f
    private val backgroundSpeed = 100f

    private val baseEnemySpeed = 300f
    private val playerBaseSpeed = 400f

    private val speedIncrementPerLevel = 50f

    override fun show() {
        batch = SpriteBatch()
        player = Player(300f, 128f)
        backgroundX1 = 0f
        backgroundX2 = Gdx.graphics.width.toFloat()
        newQuestion()
    }

    private fun newQuestion() {
        currentQuestion = QuestionGenerator.generate(level, operation)
        enemies.clear()

        val options = currentQuestion.answers.shuffled()

        val startY = Gdx.graphics.height / 5f
        val gap = Gdx.graphics.height / 6f

        for ((index, ans) in options.withIndex()) {
            val enemy = Enemy(answer = ans.toString(), textureIndex = MathUtils.random(0, 2), size = 128f)
            val yPos = startY + index * gap
            enemy.init(Gdx.graphics.width.toFloat(), yPos)
            enemies.add(enemy)
        }
    }


    private fun spawnEnemy() {
        val ans = currentQuestion.answers.random()
        val enemy = Enemy(answer = ans.toString(), textureIndex = MathUtils.random(0, 2), size = 128f)
        val yPos = Gdx.graphics.height / 5f * MathUtils.random(1, 4) - 64f
        enemy.init(Gdx.graphics.width.toFloat(), yPos)
        enemies.add(enemy)
    }


    override fun render(delta: Float) {
        // Increase player speed based on level

        player.speed = playerBaseSpeed + (level-1) * speedIncrementPerLevel
        // Scroll background
        backgroundX1 -= backgroundSpeed * delta
        backgroundX2 -= backgroundSpeed * delta
        if (backgroundX1 <= -Gdx.graphics.width) backgroundX1 = backgroundX2 + Gdx.graphics.width
        if (backgroundX2 <= -Gdx.graphics.width) backgroundX2 = backgroundX1 + Gdx.graphics.width

        // Update player & enemies
        player.update(delta)
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val currentSpeed = baseEnemySpeed + (level - 1 * 20f)
            enemy.update(delta, 200f)

            // Remove enemies off screen (optional, if you want them gone if missed)
            if (enemy.rect.x < -enemy.rect.width) {
                iterator.remove()
                continue
            }

            // Collision
            if (enemy.rect.overlaps(player.rect)) {
                if (enemy.answer.toInt() == currentQuestion.correctAnswer) {
                    score += 10
                    questionsAnsweredCorrectly++
                    if (questionsAnsweredCorrectly >= 10 && level < 10) {
                        level++
                        questionsAnsweredCorrectly = 0
                    }
                } else {
                    lives--
                    wrongAnswers++
                }
                if (lives <= 0 || wrongAnswers >= 3) {
                    gameOver()
                    return
                }
                newQuestion() // immediately replace with new 4 options
                return
            }
        }

        // Draw everything
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()

        // Draw scrolling background
        batch.draw(GameAssets.backgroundTexture, backgroundX1, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.draw(GameAssets.backgroundTexture, backgroundX2, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // Draw player & enemies
        player.draw(batch)
        enemies.forEach { it.draw(batch) }

        // Draw UI
        GameUI.draw(batch, score, level, lives, currentQuestion.text)

        batch.end()
    }


    override fun dispose() {
        batch.dispose()
        GameAssets.dispose()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}

    private fun gameOver(){
        game.screen = GameOverScreen(onRestart = {
            score = 0
            lives = 3
            level = 1
            wrongAnswers = 0
            questionsAnsweredCorrectly = 0
            enemies.clear()
            newQuestion()
            game.screen = this
        })
    }
}
