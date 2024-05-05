package com.example.project_progmobile.modeMulti

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.Bluetooth.BluetoothConnectionService
import com.example.project_progmobile.Bluetooth.MainActivityBluetooth
import com.example.project_progmobile.Games.Clickbutton
import com.example.project_progmobile.Games.GamesQuizz
import com.example.project_progmobile.Games.GamesQuizzHard
import com.example.project_progmobile.Games.GamesQuizzMedium
import com.example.project_progmobile.Games.MotionGame
import com.example.project_progmobile.Games.ReflexGames
import com.example.project_progmobile.Games.ShakeChallengeActivity
import com.example.project_progmobile.Games.TickGameActivity
import com.example.project_progmobile.R
import java.nio.charset.Charset


private lateinit var bluetoothConnectionService: BluetoothConnectionService

class ModeMulti  : ComponentActivity() {
    private lateinit var btnStartChallenge: Button
    private lateinit var btnNextChallenge: Button
    private lateinit var textViewExplanation: TextView
    private lateinit var textViewTitle: TextView
    private var selectedGame: Class<*>? = null
    private lateinit var mediaPlayerLoss: MediaPlayer
    private lateinit var mediaPlayerWin: MediaPlayer
    private var successfulChallenges = 0

    private val availableGames = mutableListOf<Class<*>>(
        GamesQuizz::class.java,
        ReflexGames::class.java,
        TickGameActivity::class.java,
        ShakeChallengeActivity::class.java,
        Clickbutton::class.java,
        MotionGame::class.java,
        GamesQuizzHard::class.java,
        GamesQuizzMedium::class.java
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mode_multi)
        mediaPlayerWin = MediaPlayer.create(this, R.raw.win)
        mediaPlayerLoss=MediaPlayer.create(this,R.raw.loss)
        btnStartChallenge = findViewById(R.id.btnStartChallenge)
        btnNextChallenge = findViewById(R.id.btnNextChallenge)
        textViewExplanation = findViewById(R.id.textViewExplanation)
        val btnBluetooth = findViewById<Button>(R.id.btnBluetooth)
        textViewTitle = findViewById(R.id.textViewTitle)

        bluetoothConnectionService = BluetoothConnectionService(this)


        btnStartChallenge.setOnClickListener {
            val startMsg = "start_mode_multi"
            val bytes: ByteArray = startMsg.toByteArray(Charset.defaultCharset())
            bluetoothConnectionService.write(bytes)
            startRandomChallenge()
            btnStartChallenge.isEnabled = false // Désactiver le bouton de démarrage pendant le défi
        }

        btnBluetooth.setOnClickListener{
           startActivity(Intent(this, MainActivityBluetooth::class.java))
        }

        btnNextChallenge.setOnClickListener {
            startRandomChallenge()
            btnNextChallenge.visibility = View.INVISIBLE // Cacher le bouton après le clic
            textViewExplanation.text = "" // Effacer le message d'explication
        }
    }

    private fun startRandomChallenge() {
        // Vérifier s'il reste des jeux disponibles

        // Sélectionner un jeu aléatoire parmi les jeux disponibles restants
        selectedGame = availableGames.random()
        if(selectedGame == GamesQuizz::class.java){
            availableGames.remove(GamesQuizzMedium::class.java)
            availableGames.remove(GamesQuizzHard::class.java)
        }else if(selectedGame ==GamesQuizzMedium::class.java){
            availableGames.remove(GamesQuizz::class.java)
            availableGames.remove(GamesQuizzHard::class.java)
        }else if(selectedGame == GamesQuizzHard::class.java){
            availableGames.remove(GamesQuizz::class.java)
            availableGames.remove(GamesQuizzMedium::class.java)
        }else{
            availableGames.remove(selectedGame)
        }

        // Retirer le jeu sélectionné de la liste des jeux disponibles


        startActivityForResult(Intent(this, selectedGame), REQUEST_CODE_CHALLENGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHALLENGE && resultCode == RESULT_OK) {
            val score = data?.getIntExtra("score", 0) ?: 0
            val score1 = data?.getLongExtra("score1", 0L) ?: 0L

            when (selectedGame) {
                GamesQuizz::class.java -> {
                    if (score > 12000) {
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
                ReflexGames::class.java -> {
                    if (score1 < 500) {
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
                Clickbutton::class.java -> {
                    if(score >12){
                        showNextChallengeButton()
                    } else {
                        handleGameLoss()
                    }
                }
                MotionGame::class.java ->{
                    if(score>5){
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
            mediaPlayerWin.start()
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
        mediaPlayerLoss.start()

    }

    companion object {
        private const val REQUEST_CODE_CHALLENGE = 100
    }

}