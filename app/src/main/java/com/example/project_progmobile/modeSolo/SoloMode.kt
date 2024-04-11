package com.example.project_progmobile.modeSolo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.Games.GamesCapitales1
import com.example.project_progmobile.Games.ShakeChallengeActivity
import com.example.project_progmobile.Games.TickGameActivity
import com.example.project_progmobile.Games.ReflexGames
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R

class SoloMode : ComponentActivity() {

    private lateinit var btnStartChallenge: Button
    private lateinit var btnNextChallenge: Button
    private lateinit var textViewExplanation: TextView
    private lateinit var textViewTitle: TextView
    private var selectedGame: Class<*>? = null

    private var successfulChallenges = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_mode)

        btnStartChallenge = findViewById(R.id.btnStartChallenge)
        btnNextChallenge = findViewById(R.id.btnNextChallenge)
        textViewExplanation = findViewById(R.id.textViewExplanation)
        textViewTitle = findViewById(R.id.textViewTitle)

        btnStartChallenge.setOnClickListener {
            startRandomChallenge()
            btnStartChallenge.isEnabled = false // Désactiver le bouton de démarrage pendant le défi
        }

        btnNextChallenge.setOnClickListener {
            startRandomChallenge()
            btnNextChallenge.visibility = View.INVISIBLE // Cacher le bouton après le clic
            textViewExplanation.text = "" // Effacer le message d'explication
        }
    }


         val availableGames = mutableListOf<Class<*>>(
            GamesCapitales1::class.java,
            ReflexGames::class.java,
            TickGameActivity::class.java,
            ShakeChallengeActivity::class.java
        )
        //selectedGame = availableGames.random()
        //startActivityForResult(Intent(this, selectedGame), REQUEST_CODE_CHALLENGE)
    private fun startRandomChallenge() {
        // Vérifier s'il reste des jeux disponibles

        // Sélectionner un jeu aléatoire parmi les jeux disponibles restants
        selectedGame = availableGames.random()

        // Retirer le jeu sélectionné de la liste des jeux disponibles
        availableGames.remove(selectedGame)

        startActivityForResult(Intent(this, selectedGame), REQUEST_CODE_CHALLENGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHALLENGE && resultCode == RESULT_OK) {
            val score = data?.getIntExtra("score", 0) ?: 0
            val reactionTime = data?.getLongExtra("reactionTime", 0) ?: 0

            when (selectedGame) {
                GamesCapitales1::class.java -> {
                    if (score > 10000) {
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
                ReflexGames::class.java -> {
                    if (reactionTime < 400) {
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
                TickGameActivity::class.java -> {
                    if (score > 50) {
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
                ShakeChallengeActivity::class.java -> {
                    if (score > 10) {
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
            }
        }
    }

    private fun showNextChallengeButton() {
        successfulChallenges++
        if (successfulChallenges >= 3) {
            // Afficher l'écran de victoire lorsque le joueur réussit trois défis
            setContentView(R.layout.win_screen)
            return
        }

        // Afficher le bouton pour le défi suivant comme avant
        btnNextChallenge.visibility = View.VISIBLE
        textViewExplanation.text = "Vous avez réussi le défi! Passez au suivant."
        btnStartChallenge.visibility = View.INVISIBLE
        textViewExplanation.visibility = View.VISIBLE
        btnNextChallenge.visibility = View.VISIBLE
        textViewTitle.visibility = View.INVISIBLE
    }

    private fun handleGameLoss() {
        // Afficher l'écran de fin du jeu avec le texte "Game Over"
        setContentView(R.layout.game_over_screen)
    }

    companion object {
        private const val REQUEST_CODE_CHALLENGE = 100
    }
}
