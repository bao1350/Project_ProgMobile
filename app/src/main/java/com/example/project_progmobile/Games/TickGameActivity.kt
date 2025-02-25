package com.example.project_progmobile.Games

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R

class TickGameActivity : ComponentActivity() {

    private lateinit var instructionsTextView: TextView
    private lateinit var shakeCountTextView: TextView
    private lateinit var startButton: Button
    private var shakeCount = 0
    private var gameStarted = false
    private lateinit var timerTextView: TextView
    private lateinit var btnReturnToHome: Button
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tick_game)

        instructionsTextView = findViewById(R.id.instructionsTextView)
        shakeCountTextView = findViewById(R.id.shakeCountTextView)
        startButton = findViewById(R.id.startButton)
        timerTextView = findViewById(R.id.timerTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)

        // Configuration du clic sur le bouton "Start"
        startButton.setOnClickListener {
            startGame()
        }
        btnReturnToHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun startGame() {
        shakeCount = 0
        gameStarted = true

        // Rendre le TextView et le bouton invisibles
        instructionsTextView.visibility = View.INVISIBLE
        startButton.visibility = View.INVISIBLE

        // Afficher le compteur de secousses
        shakeCountTextView.visibility = View.VISIBLE
        shakeCountTextView.text = "Shake count: $shakeCount"
        timerTextView.visibility = View.VISIBLE
        // Lancer un compte à rebours de 10 secondes

        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Timer: $secondsRemaining"
            }

            override fun onFinish() {

                val resultIntent = Intent()
                resultIntent.putExtra("score", shakeCount)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                gameStarted = false
                shakeCountTextView.text = "Shake count: $shakeCount"
            }
        }.start()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        // Vérifier si le jeu est en cours et autoriser les secousses
        if (gameStarted) {
            // Incrémenter le compteur de secousses
            shakeCount++
            shakeCountTextView.text = "Shake count: $shakeCount"
        }
    }
}
