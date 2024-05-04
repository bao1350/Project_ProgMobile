package com.example.project_progmobile.Games


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R
import kotlin.random.Random

class Clickbutton : ComponentActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnClickHere: Button
    private lateinit var btnReturnToHome: Button
    private lateinit var tvInstructions: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private var score = 0
    private lateinit var timer: CountDownTimer
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_button)

        btnPlay = findViewById(R.id.btnPlay)
        btnClickHere = findViewById(R.id.btnClickHere)
        tvInstructions = findViewById(R.id.tvInstructions)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)

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

        btnReturnToHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    private fun startGame() {
        score = 0
        tvInstructions.text = "Cliquez sur le bouton pour gagner des points !"
        btnPlay.visibility = View.INVISIBLE
        btnClickHere.visibility = View.VISIBLE
        tvInstructions.visibility=View.INVISIBLE
        btnReturnToHome.visibility=View.INVISIBLE

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
        btnReturnToHome.visibility = View.VISIBLE
        tvInstructions.text = "Votre score : $score. Cliquez sur Jouer pour rejouer."
        val resultIntent = Intent()
        resultIntent.putExtra("score", score)
        setResult(RESULT_OK, resultIntent)
        finish()
        // Afficher une fenêtre contextuelle avec le score

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

        // Définir les nouvelles coordonnées pour le bouton
        layoutParams.leftMargin = randomX
        layoutParams.topMargin = randomY

        // Appliquer les nouveaux paramètres de mise en page
        btnClickHere.layoutParams = layoutParams
    }

    private fun showScoreDialog(score: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Score")
        builder.setMessage("Vous avez obtenu un score de $score")
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        alertDialog = builder.create()
        alertDialog.show()
    }
}
