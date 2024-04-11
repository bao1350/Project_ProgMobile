package com.example.project_progmobile.Games
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.R
import kotlin.random.Random

class ReflexGames : ComponentActivity() {

    private lateinit var startButton: Button
    private lateinit var greenButton: Button
    private lateinit var redButton: Button
    private lateinit var reactionTimeTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var instructionsTextView: TextView
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

                        val resultIntent = Intent()
                        resultIntent.putExtra("reactionTime", timeTextView.text.toString())
                        setResult(RESULT_OK, resultIntent)
                        finish() // Terminer cette activité

                }

            }
        redButton.setOnClickListener {

            startActivity(Intent(this,ReflexGames::class.java))
        }
    }
        }





