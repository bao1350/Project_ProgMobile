package com.example.project_progmobile.Games

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R
import kotlin.random.Random

class ReflexGamesTraining : ComponentActivity() {

    private lateinit var startButton: Button
    private lateinit var greenButton: Button
    private lateinit var redButton: Button
    private lateinit var btnReturnToHome: Button
    private lateinit var reactionTimeTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var replayButton: Button // Bouton de rejouer
    private var startTime: Long = 0
    private var isReactionTimeRecorded = false
    private var isGameStarted = false
    private var hasLost = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reflex_games)

        startButton = findViewById(R.id.startButton)
        greenButton = findViewById(R.id.greenButton)
        redButton = findViewById(R.id.redButton)
        reactionTimeTextView = findViewById(R.id.reactionTimeTextView)
        timeTextView = findViewById(R.id.timeTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)
        replayButton = findViewById(R.id.replayButton) // Initialiser le bouton de rejouer

        // Afficher les instructions
        instructionsTextView.visibility = View.VISIBLE

        startButton.setOnClickListener {
            startButton.visibility = View.INVISIBLE
            greenButton.visibility = View.INVISIBLE
            redButton.visibility = View.INVISIBLE
            reactionTimeTextView.visibility = View.INVISIBLE
            timeTextView.visibility = View.INVISIBLE
            instructionsTextView.visibility = View.INVISIBLE

            hasLost = false

            // Affichage immédiat du bouton rouge
            redButton.visibility = View.VISIBLE

            // Délai pour l'affichage du bouton vert
            val delayForGreen = Random.nextLong(2000, 5000)
            Handler().postDelayed({
                greenButton.visibility = View.VISIBLE
                startTime = System.currentTimeMillis()
                isReactionTimeRecorded =
                    false // Réinitialiser l'enregistrement du temps de réaction
                isGameStarted = true
                redButton.visibility =
                    View.INVISIBLE // Le bouton rouge disparaît lorsque le vert apparaît
            }, delayForGreen)
        }

        greenButton.setOnClickListener {
            if (!isReactionTimeRecorded && !hasLost) {
                val endTime = System.currentTimeMillis()
                val reactionTime = endTime - startTime
                val seconds = reactionTime / 1000.0
                reactionTimeTextView.visibility = View.VISIBLE
                timeTextView.visibility = View.VISIBLE
                timeTextView.text = "$seconds seconds"
                isReactionTimeRecorded = true // Marquer que le temps de réaction a été enregistré
                // Afficher le bouton de rejouer une fois que le jeu est terminé
                replayButton.visibility = View.VISIBLE
            }
        }

        redButton.setOnClickListener {
            startActivity(Intent(this, ReflexGames::class.java))
        }

        btnReturnToHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        replayButton.setOnClickListener {
            // Réinitialisation du jeu
            resetGame()
        }
    }
    // Méthode pour réinitialiser le jeu
    private fun resetGame() {
        // Réinitialisation des variables de jeu
        hasLost = false
        isReactionTimeRecorded = false
        isGameStarted = false

        // Réinitialisation de l'interface utilisateur
        reactionTimeTextView.visibility = View.INVISIBLE
        timeTextView.visibility = View.INVISIBLE
        instructionsTextView.visibility = View.VISIBLE
        startButton.visibility = View.VISIBLE
        greenButton.visibility = View.INVISIBLE
        redButton.visibility = View.INVISIBLE
        replayButton.visibility = View.INVISIBLE

        // Réinitialisation du texte affiché
        timeTextView.text = ""
    }

}
