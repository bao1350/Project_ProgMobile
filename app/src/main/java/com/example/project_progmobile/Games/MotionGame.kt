package com.example.project_progmobile.Games

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.R

class MotionGame : ComponentActivity() {

    private lateinit var player: ImageView
    private lateinit var layout: FrameLayout
    private lateinit var handler: Handler
    private var obstacleSpeed: Int = 10
    private var obstacleInterval: Long = 2000 // Interval between obstacle spawn in milliseconds
    private var obstacleSize: Int = 100 // Size of the obstacle ImageView
    private val obstacles = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_games)

        player = findViewById(R.id.player)
        layout = findViewById(R.id.layout)
        handler = Handler()

        player.setOnTouchListener { view, event ->
            movePlayer(view, event)
            true
        }

        // Start spawning obstacles
        handler.postDelayed(obstacleRunnable, obstacleInterval)
    }

    private val obstacleRunnable: Runnable = object : Runnable {
        override fun run() {
            // Generate obstacles continuously
            generateObstacle()

            // Schedule the next obstacle
            handler.postDelayed(this, obstacleInterval)
        }
    }

    private fun generateObstacle() {
        // Create a new obstacle
        val obstacle = createObstacle()
        obstacles.add(obstacle)

        // Position the obstacle at the top of the screen
        val randomX = (0 until layout.width - obstacleSize).random()
        obstacle.x = randomX.toFloat()
        obstacle.y = -obstacleSize.toFloat()

        // Move the obstacle downwards
        moveObstacle(obstacle)
    }

    private fun createObstacle(): ImageView {
        val obstacle = ImageView(this)
        obstacle.setImageResource(R.drawable.obstacle) // Load the obstacle drawable
        obstacle.layoutParams = FrameLayout.LayoutParams(obstacleSize, obstacleSize)
        layout.addView(obstacle)
        return obstacle
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

    private fun moveObstacle(obstacle: ImageView) {
        obstacle.animate()
            .translationYBy(layout.height.toFloat()) // Move the obstacle to the bottom of the screen
            .setDuration(obstacleSpeed.toLong())
            .withEndAction {
                // Check collision with player
                if (isCollision(player, obstacle)) {
                    // Game over logic
                    obstacles.remove(obstacle)
                    layout.removeView(obstacle)
                } else {
                    // Reset obstacle position at the top of the screen
                    val randomX = (0 until layout.width - obstacleSize).random()
                    obstacle.x = randomX.toFloat()
                    obstacle.y = -obstacleSize.toFloat()
                    // Continue moving the obstacle downwards
                    moveObstacle(obstacle)
                }
            }
            .start()
    }

    private fun isCollision(player: ImageView, obstacle: ImageView): Boolean {
        return player.x < obstacle.x + obstacle.width &&
                player.x + player.width > obstacle.x &&
                player.y < obstacle.y + obstacle.height &&
                player.y + player.height > obstacle.y
    }
}
