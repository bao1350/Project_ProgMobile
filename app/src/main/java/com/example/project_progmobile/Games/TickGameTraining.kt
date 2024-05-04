package com.example.project_progmobile.Games
import android.app.AlertDialog
import android.content.DialogInterface
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
        timerTextView = findViewById(R.id.timerTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)

        timerTextView.visibility = View.INVISIBLE

        startButton.setOnClickListener {
            startGame()
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

        shakeCountTextView.visibility = View.VISIBLE
        timerTextView.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Timer: $secondsRemaining"
            }

            override fun onFinish() {
                gameStarted = false
                shakeCountTextView.text = "Shake count: $shakeCount"
                showScoreDialog()
            }
        }.start()
    }

    private fun resetGame() {
        startActivity(Intent(this, TickGameTraining::class.java))
        finish() // Optionnel : fermer l'activité actuelle pour éviter la superposition d'activités
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (gameStarted) {
            shakeCount++
            shakeCountTextView.text = "Shake count: $shakeCount"
        }
    }

    private fun showScoreDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Le temps est écoulé")
        builder.setMessage("Your score: $shakeCount")
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            resetGame()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
