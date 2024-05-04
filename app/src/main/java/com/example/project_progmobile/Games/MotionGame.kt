package com.example.project_progmobile.Games

import android.app.Activity
import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R
import kotlin.random.Random

class MotionGame : ComponentActivity() {

    private lateinit var player: ImageView
    private lateinit var goal: ImageView
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var btnReturnToHome: Button
    private  lateinit var startButton : Button
    private lateinit var instructionsTextView:TextView

    private var score = 0
    private var timeLeftInMillis: Long = 10000
    private var timer: CountDownTimer? = null

    private val obstacleSize = 50 // Taille de l'obstacle (largeur ou hauteur)
    private val numObstacles = 5 // Nombre d'obstacles dans le niveau

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_games)

        player = findViewById(R.id.player)
        goal = findViewById(R.id.goal)
        scoreTextView = findViewById(R.id.scoreTextView)
        timerTextView = findViewById(R.id.timerTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)
        startButton =findViewById(R.id.startButton)
        instructionsTextView=findViewById(R.id.instructionsTextView)

        updateScore()
        updateTimer()

        player.setOnTouchListener { view, event ->
            movePlayer(view, event)
            checkCollisions()
            checkGoalReached()
            true
        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            startGame()
        }

        btnReturnToHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }
    private fun startGame() {
        score = 0
        timeLeftInMillis = 10000
        updateScore()
        updateTimer()
        instructionsTextView.visibility=View.INVISIBLE
        startButton.visibility=View.INVISIBLE
        player.visibility = View.VISIBLE
        goal.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
        timerTextView.visibility = View.VISIBLE

        // Rendre les obstacles visibles
        findViewById<ImageView>(R.id.obstacle1).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.obstacle2).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.obstacle3).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.obstacle4).visibility = View.VISIBLE
        findViewById<ImageView>(R.id.obstacle5).visibility = View.VISIBLE


        startTimer()
        resetGoalPosition()
        resetObstaclesPosition()
    }
    private fun movePlayer(view: View, event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val newX = event.rawX - view.width / 2
                val newY = event.rawY - view.height / 2
                view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            }
        }
    }

    private fun checkCollisions() {
        val playerRect = RectF(player.x, player.y, player.x + player.width, player.y + player.height)
        val obstacles = mutableListOf(findViewById<ImageView>(R.id.obstacle1), findViewById(R.id.obstacle2), findViewById(R.id.obstacle3), findViewById(R.id.obstacle4), findViewById(R.id.obstacle5))
        obstacles.forEachIndexed { index, obstacle ->
            val obstacleRect = RectF(obstacle.x, obstacle.y, obstacle.x + obstacleSize, obstacle.y + obstacleSize)
            if (playerRect.intersect(obstacleRect)) {
                resetObstaclePosition(obstacle)
                decrementScore()
            }
        }
    }

    private fun checkGoalReached() {
        val playerRect = RectF(player.x, player.y, player.x + player.width, player.y + player.height)
        val goalRect = RectF(goal.x, goal.y, goal.x + goal.width, goal.y + goal.height)

        if (playerRect.intersect(goalRect)) {
            incrementScore()
            resetGoalPosition()
            resetObstaclesPosition()
        }
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    private fun incrementScore() {
        score++
        updateScore()
    }

    private fun decrementScore() {
        score -= 5
        if (score < 0) score = 0
        updateScore()
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = "Time left: $timeLeftFormatted"
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                val resultIntent = Intent()
                resultIntent.putExtra("score", score)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }.start()
    }

    private fun resetGoalPosition() {
        var newGoalPosition: Pair<Float, Float>
        do {
            val newGoalX = Random.nextInt(0, 951).toFloat() // Nouvelle valeur aléatoire entre 0 et 950
            val newGoalY = Random.nextInt(0, 751).toFloat() // Nouvelle valeur aléatoire entre 0 et 750
            newGoalPosition = Pair(newGoalX, newGoalY)
        } while (isPlayerCollidingWithGoal(newGoalPosition) || isPlayerCollidingWithObstacle(newGoalPosition))

        goal.animate()
            .x(newGoalPosition.first)
            .y(newGoalPosition.second)
            .setDuration(0)
            .start()
    }

    private fun resetObstaclesPosition() {
        val obstacles = mutableListOf(findViewById<ImageView>(R.id.obstacle1), findViewById(R.id.obstacle2), findViewById(R.id.obstacle3), findViewById(R.id.obstacle4), findViewById(R.id.obstacle5))
        val goalPosition = Pair(goal.x, goal.y)
        val playerPosition = Pair(player.x, player.y)
        obstacles.forEach { obstacle ->
            var newObstaclePosition: Pair<Float, Float>
            do {
                newObstaclePosition = getRandomPosition(obstacles, goalPosition, playerPosition)
            } while (isPlayerCollidingWithObstacle(newObstaclePosition) || isPlayerCollidingWithGoal(newObstaclePosition) || isObstacleAtTopLeft(newObstaclePosition, playerPosition) || isObstacleAtPlayer(obstacle))

            obstacle.animate()
                .x(newObstaclePosition.first)
                .y(newObstaclePosition.second)
                .setDuration(0)
                .start()
        }
    }

    private fun isObstacleAtPlayer(obstacle: ImageView): Boolean {
        val playerRect = RectF(player.x, player.y, player.x + player.width, player.y + player.height)
        val obstacleRect = RectF(obstacle.x, obstacle.y, obstacle.x + obstacle.width, obstacle.y + obstacle.height)
        return playerRect.intersect(obstacleRect)
    }

    private fun isObstacleAtTopLeft(obstaclePosition: Pair<Float, Float>, playerPosition: Pair<Float, Float>): Boolean {
        return obstaclePosition.first < playerPosition.first + 100 && obstaclePosition.second < playerPosition.second + 100
    }

    private fun isPlayerCollidingWithObstacle(obstaclePosition: Pair<Float, Float>): Boolean {
        val playerRect = RectF(player.x, player.y, player.x + player.width, player.y + player.height)
        val obstacleRect = RectF(obstaclePosition.first, obstaclePosition.second, obstaclePosition.first + obstacleSize, obstaclePosition.second + obstacleSize)
        return playerRect.intersect(obstacleRect)
    }

    private fun isPlayerCollidingWithGoal(obstaclePosition: Pair<Float, Float>): Boolean {
        val goalRect = RectF(goal.x, goal.y, goal.x + goal.width, goal.y + goal.height)
        val obstacleRect = RectF(obstaclePosition.first, obstaclePosition.second, obstaclePosition.first + obstacleSize, obstaclePosition.second + obstacleSize)
        return goalRect.intersect(obstacleRect)
    }

    private fun getRandomPosition(obstacles: MutableList<ImageView>, greenPosition: Pair<Float, Float>, playerPosition: Pair<Float, Float>): Pair<Float, Float> {
        val newPosition = Pair(Random.nextInt(0, 951).toFloat(), Random.nextInt(0, 751).toFloat())

        // Vérifier la distance entre la nouvelle position et le joueur
        val distanceToPlayer = Math.sqrt(Math.pow((newPosition.first - playerPosition.first).toDouble(), 2.0) + Math.pow((newPosition.second - playerPosition.second).toDouble(), 2.0))

        // Vérifier la distance entre la nouvelle position et le but
        val distanceToGoal = Math.sqrt(Math.pow((newPosition.first - greenPosition.first).toDouble(), 2.0) + Math.pow((newPosition.second - greenPosition.second).toDouble(), 2.0))

        // Vérifier si la nouvelle position est trop proche du joueur ou du but
        if (distanceToPlayer < obstacleSize * 3 || distanceToGoal < obstacleSize * 3) {
            // Si la nouvelle position est trop proche, générer une nouvelle position récursivement
            return getRandomPosition(obstacles, greenPosition, playerPosition)
        }

        // S'il n'y a pas de problème de proximité, retourner la nouvelle position
        return newPosition
    }

    private fun resetObstaclePosition(obstacle: ImageView) {
        val newObstacleX = Random.nextInt(0, 951).toFloat() // Nouvelle valeur aléatoire entre 0 et 950
        val newObstacleY = Random.nextInt(0, 751).toFloat() // Nouvelle valeur aléatoire entre 0 et 750
        obstacle.animate()
            .x(newObstacleX)
            .y(newObstacleY)
            .setDuration(0)
            .start()
    }
}
