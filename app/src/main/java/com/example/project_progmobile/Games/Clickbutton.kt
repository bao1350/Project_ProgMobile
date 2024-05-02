package com.example.project_progmobile.Games
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.R
import kotlin.random.Random

class Clickbutton : ComponentActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnClickHere: Button
    private lateinit var tvInstructions: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private var score = 0
    private lateinit var timer: CountDownTimer
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_button)

        btnPlay = findViewById(R.id.btnPlay)
        btnClickHere = findViewById(R.id.btnClickHere)
        tvInstructions = findViewById(R.id.tvInstructions)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)

        // Obtenir les dimensions de l'écran
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        btnPlay.setOnClickListener {
            startGame()
        }

        btnClickHere.setOnClickListener {
            increaseScore()
            moveButtonRandomly()
        }
    }

    private fun startGame() {
        score = 0
        tvInstructions.text = "Cliquez sur le bouton pour gagner des points !"
        btnPlay.visibility = View.INVISIBLE
        btnClickHere.visibility = View.VISIBLE

        // Timer pour le jeu
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "Temps restant: ${(millisUntilFinished / 1000)}"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    private fun endGame() {
        timer.cancel()
        btnPlay.visibility = View.VISIBLE
        btnClickHere.visibility = View.INVISIBLE
        tvInstructions.text = "Votre score : $score. Cliquez sur Jouer pour rejouer."
    }

    private fun increaseScore() {
        score++
        tvScore.text = "Score: $score"
    }

    private fun moveButtonRandomly() {
        // Obtenir les paramètres de mise en page du bouton
        val layoutParams = btnClickHere.layoutParams as RelativeLayout.LayoutParams

        // Calculer les coordonnées aléatoires dans toute la plage de l'écran
        val randomX = Random.nextInt(0, screenWidth - btnClickHere.width)
        val randomY = Random.nextInt(0, screenHeight - btnClickHere.height)

        // Ajuster les coordonnées si elles dépassent les limites de l'écran
        val adjustedX = if (randomX + btnClickHere.width > screenWidth) screenWidth - btnClickHere.width else randomX
        val adjustedY = if (randomY + btnClickHere.height > screenHeight) screenHeight - btnClickHere.height else randomY

        // Définir les nouvelles coordonnées pour le bouton
        layoutParams.leftMargin = adjustedX
        layoutParams.topMargin = adjustedY

        // Appliquer les nouveaux paramètres de mise en page
        btnClickHere.layoutParams = layoutParams
    }


}
