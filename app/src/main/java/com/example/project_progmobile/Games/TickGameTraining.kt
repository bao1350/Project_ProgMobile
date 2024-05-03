package com.example.project_progmobile.Games

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R

class TickGameTraining : ComponentActivity() {

    private lateinit var instructionsTextView: TextView
    private lateinit var shakeCountTextView: TextView
    private lateinit var startButton: Button
    private lateinit var replayButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var btnReturnToHome: Button
    private var shakeCount = 0
    private var gameStarted = false
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tick_game)

        instructionsTextView = findViewById(R.id.instructionsTextView)
        shakeCountTextView = findViewById(R.id.shakeCountTextView)
        startButton = findViewById(R.id.startButton)
        replayButton = findViewById(R.id.replayButton)
        timerTextView = findViewById(R.id.timerTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)

        replayButton.visibility = View.INVISIBLE
        timerTextView.visibility = View.INVISIBLE

        startButton.setOnClickListener {
            startGame()
        }

        replayButton.setOnClickListener {
            resetGame()
        }

        btnReturnToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun startGame() {
        shakeCount = 0
        gameStarted = true

        instructionsTextView.visibility = View.INVISIBLE
        startButton.visibility = View.INVISIBLE
        replayButton.visibility = View.INVISIBLE

        shakeCountTextView.visibility = View.VISIBLE
        timerTextView.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Timer: $secondsRemaining"
            }

            override fun onFinish() {
                gameStarted = false
                shakeCountTextView.text = "Shake count: $shakeCount"
                replayButton.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun resetGame() {
        shakeCount = 0
        shakeCountTextView.text = "Shake count: $shakeCount"
        replayButton.visibility = View.INVISIBLE
        startGame()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (gameStarted) {
            shakeCount++
            shakeCountTextView.text = "Shake count: $shakeCount"
        }
    }
}
