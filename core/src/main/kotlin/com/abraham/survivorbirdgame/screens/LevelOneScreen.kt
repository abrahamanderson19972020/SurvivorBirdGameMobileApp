package com.abraham.survivorbirdgame.screens

// Importing necessary LibGDX classes
import com.abraham.survivorbirdgame.model.Enemy
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.utils.viewport.ScreenViewport


class LevelOneScreen : Screen {
    private lateinit var stage: Stage
    private lateinit var skin: Skin
    private lateinit var startButton: TextButton
    private lateinit var exitButton: TextButton
    private lateinit var gameOverLabel: Label
    private var gameOverUIInitialized = false

    // Game rendering tools
    private lateinit var batch: SpriteBatch
    private lateinit var background: Texture
    private lateinit var bird: Texture

    // Bird position on screen
    private var birdX = 100f
    private var birdY = 200f

    // Bird movement speeds
    private val birdSpeedX = 100f
    private val birdSpeedY = 200f

    // Scale factor to shrink the bird image
    private val birdScale = 0.2f

    // Background scrolling variables
    private var bgX1 = 0f
    private var bgX2 = 0f
    private var bgSpeed = 100f
    private var bgWidth = 0f

    // Text display: score and level
    private lateinit var font: BitmapFont
    private var score = 0
    private var level = 1

    // Enemies
    private lateinit var enemyTextures: List<Texture>
    private val enemies = mutableListOf<Enemy>()
    private var enemySpawnTimer = 0f
    private val spawnInterval = 2f // New enemy every 2 seconds

    // Life system (3 hearts)
    private lateinit var heartTexture: Texture
    private var lives: Int = 3
    private var isGameOver: Boolean = false
    private var gameOverFont: BitmapFont = BitmapFont()
    private var isPlaying:Boolean = true
    private var justRestarted = false

    override fun show() {
        // Initialize the game objects when the screen is shown
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(3f)
        font.color = Color.BLACK

        gameOverFont.data.setScale(6f)
        gameOverFont.color = Color.RED

        // Load enemy textures
        enemyTextures = listOf(
            Texture(Gdx.files.internal("images/enemy1.png")),
            Texture(Gdx.files.internal("images/enemy2.png")),
            Texture(Gdx.files.internal("images/enemy3.png"))
        )

        // Load heart and background images
        heartTexture = Texture(Gdx.files.internal("images/heart.png"))
        background = Texture(Gdx.files.internal("images/background1.png"))
        bird = Texture(Gdx.files.internal("images/plane1.png"))

        // Set background width for scrolling logic
        bgWidth = background.width.toFloat()
        bgX2 = bgWidth
        refreshParameters()
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage

// Use LibGDX's built-in skin or your own (if you have one in assets)
        skin = Skin(Gdx.files.internal("uiskin.json"))  // Make sure uiskin.json is in assets

// Game Over Label
        gameOverLabel = Label("Game Over", LabelStyle(BitmapFont(), Color.RED))
        gameOverLabel.setFontScale(3f)

// Buttons
        startButton = TextButton("Start Again", skin)
        exitButton = TextButton("Exit Game", skin)

        startButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                refreshParameters()
            }
        })

        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })

// Table layout
        val table = Table()
        table.setFillParent(true)
        table.center()
        table.add(gameOverLabel).padBottom(40f).row()
        table.add(startButton).width(300f).height(80f).padBottom(20f).row()
        table.add(exitButton).width(300f).height(80f).row()

        stage.addActor(table)
    }

    override fun render(delta: Float) {
        // Game logic during Game Over
        if (isGameOver) {
            ScreenUtils.clear(0f, 0f, 0f, 1f)
            stage.act(Gdx.graphics.deltaTime)
            stage.draw()
            handleGameOverInput()
            return
        }

        // Handle player input (arrow keys or touch)
        handleInput(delta)

        // Scroll background images
        updateBackground(delta)

        // Clear screen before drawing
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        batch.begin()

        // Draw both backgrounds side-by-side for infinite scroll
        batch.draw(background, bgX1, 0f, bgWidth, Gdx.graphics.height.toFloat())
        batch.draw(background, bgX2, 0f, bgWidth, Gdx.graphics.height.toFloat())

        // Draw the bird with scaling
        val birdWidth = bird.width * birdScale
        val birdHeight = bird.height * birdScale
        batch.draw(bird, birdX, birdY, birdWidth, birdHeight)

        // Spawn enemies based on timer
        enemySpawnTimer += delta
        if (enemySpawnTimer >= spawnInterval) {
            spawnEnemy()
            enemySpawnTimer = 0f
        }

        // Collision detection
        checkCollisions()

        if (isPlaying && !justRestarted) {
            score += (10 * delta).toInt()
        }

        // Draw score and level
        val startX = 50f
        val startY = Gdx.graphics.height - 50f
        val lineSpacing = 50f
        font.draw(batch, "Level: $level", startX, startY)
        font.draw(batch, "Score: $score", startX, startY - lineSpacing)

        // ✅ DRAW LIVES (hearts) BELOW score, with more space
        val heartY = startY - lineSpacing * 3f  // Adjusted Y position for hearts
        val heartSize = 48f  // or 40f if you prefer slightly larger
        val heartSpacing = 50f  // more spacing to match larger size
        for (i in 0 until lives) {
            batch.draw(heartTexture, startX + i * heartSpacing, heartY, heartSize, heartSize)
        }

        // Move and draw all enemies
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            enemy.x -= bgSpeed * delta

            batch.draw(enemy.texture, enemy.x, enemy.y, enemy.width, enemy.height)

            // Increase score when enemy passes the bird
            if (!enemy.passed && enemy.x + enemy.width < birdX) {
                enemy.passed = true
                score += 1
            }

            // Remove off-screen enemies
            if (enemy.x + enemy.width < 0) {
                iterator.remove()
            }
        }
        if (justRestarted) justRestarted = false
        batch.end()
    }
    private fun refreshParameters(){
        score = 0
        lives = 3
        isGameOver = false
        isPlaying = true
        justRestarted = true
        birdY = 200f
        birdX = 100f
        enemies.clear()
        bgX1 = 0f
        bgX2 = bgWidth
        enemySpawnTimer = 0f
    }

    // Handle up/down controls or touch input
    private fun handleInput(delta: Float) {
        // If using keyboard
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            birdY += birdSpeedY * delta
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            birdY -= birdSpeedY * delta
        }

        // If using touch or mouse — follow Y position of the input
        if (Gdx.input.isTouched) {
            val touchY = Gdx.input.y.toFloat()
            val targetY = Gdx.graphics.height - touchY  // Convert from screen to game coords

            // Smoothly move toward the target Y
            val diff = targetY - birdY
            birdY += diff * 5f * delta  // The factor (5f) controls smoothness/speed
        }

        // Keep the bird within screen boundaries
        birdY = birdY.coerceIn(0f, Gdx.graphics.height - bird.height * birdScale)
    }

    // Move background to create scrolling effect
    private fun updateBackground(delta: Float) {
        bgX1 -= bgSpeed * delta
        bgX2 -= bgSpeed * delta

        // Reset background positions when they move off-screen
        if (bgX1 + bgWidth <= 0) {
            bgX1 = bgX2 + bgWidth
        }

        if (bgX2 + bgWidth <= 0) {
            bgX2 = bgX1 + bgWidth
        }
    }

    // Spawn a random enemy at the right edge, random Y
    private fun spawnEnemy() {
        val texture = enemyTextures.random()
        val scale = 0.3f
        val width = texture.width * scale
        val height = texture.height * scale
        val x = Gdx.graphics.width.toFloat()
        val y = (0..(Gdx.graphics.height - height.toInt())).random().toFloat()

        enemies.add(Enemy(texture, x, y, width, height))
    }

    // Check if the bird has collided with any enemies
    private fun checkCollisions() {
        val birdRect = com.badlogic.gdx.math.Rectangle(birdX, birdY, bird.width * birdScale, bird.height * birdScale)
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val enemyRect = com.badlogic.gdx.math.Rectangle(enemy.x, enemy.y, enemy.width, enemy.height)
            if (birdRect.overlaps(enemyRect)) {
                iterator.remove()
                lives--
                if (lives <= 0) {
                    isGameOver = true
                    isPlaying = false
                }
            }
        }
    }

    // Handle keys during game over screen
    private fun handleGameOverInput() {
        // Start game again from scratch
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            lives = 3
            score = 0
            enemies.clear()
            birdY = 200f
            isGameOver = false
            isPlaying = true
            justRestarted = true
            bgX1 = 0f
            bgX2 = bgWidth
        }

        // Quit the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }


    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}

    // Called when screen is no longer visible
    override fun hide() {
        dispose()
    }

    // Clean up resources to avoid memory leaks
    override fun dispose() {
        batch.dispose()
        background.dispose()
        bird.dispose()
        font.dispose()
        enemyTextures.forEach { it.dispose() }
        heartTexture.dispose()
        stage.dispose()
        skin.dispose()
    }
}
