package com.example.project_progmobile.Games
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.MainActivity
import com.example.project_progmobile.R

class ShakeChallengeTraining : ComponentActivity() {

    private lateinit var startButton: Button
    private lateinit var countdownTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var shakeDetector: ShakeDetector1
    private lateinit var btnReturnToHome: Button
    private  lateinit var instructionsTextView : TextView
    private lateinit var btnReplay : Button
    private var score = 0
    private var countdownTimer: CountDownTimer? = null
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake_challenge)

        // Initialisation des vues
        startButton = findViewById(R.id.startButton)
        countdownTextView = findViewById(R.id.countdownTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        btnReturnToHome = findViewById(R.id.btnReturnToHome)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        btnReplay = findViewById(R.id.btnReplay)

        // Initialisation du détecteur de secousses
        shakeDetector = ShakeDetector1(this)
        shakeDetector.setOnShakeListener(object : ShakeDetector1.OnShakeListener {
            override fun onShake() {
                if (isGameRunning) {
                    score++
                    updateScore()
                }
            }
        })

        // Configuration du clic sur le bouton "Start"
        startButton.setOnClickListener {
            if (!isGameRunning) {
                startGame()
            }
        }

        // Configuration du clic sur le bouton "Retour à l'accueil"
        btnReturnToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Configuration du clic sur le bouton "Rejouer"
        btnReplay.setOnClickListener {
            restartGame()
        }
    }

    private fun restartGame() {
        // Rendre le bouton "Rejouer" invisible
        btnReplay.visibility = View.INVISIBLE
        // Redémarrer le jeu
        startGame()
    }

    private fun startGame() {
        score = 0
        updateScore()
        startButton.visibility = View.INVISIBLE // Cacher le bouton "Start"
        countdownTextView.visibility = TextView.VISIBLE
        scoreTextView.visibility = TextView.VISIBLE
        instructionsTextView.visibility=TextView.INVISIBLE
        isGameRunning = true

        // Démarre le compte à rebours de 30 secondes
        countdownTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countdownTextView.text = "Temps restant: $secondsLeft s"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()

        // Démarre la détection de secousses
        shakeDetector.start()
    }

    private fun endGame() {
        countdownTextView.visibility = View.INVISIBLE
        scoreTextView.visibility = View.VISIBLE
        scoreTextView.gravity = Gravity.CENTER
        countdownTextView.text = "Countdown"
        countdownTimer?.cancel()
        shakeDetector.stop()
        startButton.visibility = View.INVISIBLE
        isGameRunning = false

        // Afficher le score final dans une fenêtre de dialogue
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Fin du jeu")
        alertDialog.setMessage("Votre score final est de $score points.")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            // Réinitialiser le jeu
            restartGame()
        }
        alertDialog.setOnCancelListener {
            // Réinitialiser le jeu
            restartGame()
        }
        alertDialog.show()
    }



    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    override fun onDestroy() {
        super.onDestroy()
        shakeDetector.stop()
    }
}

class ShakeDetector1(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = null
    private var lastUpdateTime: Long = 0
    private var shakeThreshold = 500 // Seuil de secousse ajustable
    private var listener: OnShakeListener? = null

    init {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        this.listener = listener
    }

    fun start() {
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val currentTime = System.currentTimeMillis()
                val timeDifference = currentTime - lastUpdateTime
                if (timeDifference > shakeThreshold) {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH
                    if (acceleration > 2f) {
                        // Détection de secousse
                        listener?.onShake()
                        lastUpdateTime = currentTime
                    }
                }
            }
        }
    }

    interface OnShakeListener {
        fun onShake()
    }
}
