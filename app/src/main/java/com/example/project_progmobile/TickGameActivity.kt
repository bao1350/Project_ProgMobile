package com.example.project_progmobile
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class TickGameActivity : ComponentActivity() {

    private lateinit var instructionsTextView: TextView
    private lateinit var shakeCountTextView: TextView
    private lateinit var startButton: Button
    private var shakeCount = 0
    private var gameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tick_game)

        instructionsTextView = findViewById(R.id.instructionsTextView)
        shakeCountTextView = findViewById(R.id.shakeCountTextView)
        startButton = findViewById(R.id.startButton)

        // Configuration du clic sur le bouton "Start"
        startButton.setOnClickListener {
            startGame()
        }
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

        // Lancer un compte à rebours de 10 secondes
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Ne rien faire à chaque tick du compte à rebours
            }

            override fun onFinish() {
                gameStarted = false
                shakeCountTextView.text = "Game over! Shake count: $shakeCount"
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
