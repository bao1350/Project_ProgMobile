package com.example.project_progmobile.Games
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
import com.example.project_progmobile.R

class ShakeChallengeActivity : ComponentActivity() {

    private lateinit var startButton: Button
    private lateinit var countdownTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var shakeDetector: ShakeDetector
    private var score = 0
    private var countdownTimer: CountDownTimer? = null
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake_challenge)

        startButton = findViewById(R.id.startButton)
        countdownTextView = findViewById(R.id.countdownTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        shakeDetector = ShakeDetector(this)
        shakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake() {
                if (isGameRunning) {
                    score++
                    updateScore()
                }
            }
        })

        startButton.setOnClickListener {
            if (!isGameRunning) {
                startGame()
            }
        }
    }

    private fun startGame() {
        score = 0
        updateScore()
        startButton.visibility = View.INVISIBLE // Cacher le bouton "Start"
        countdownTextView.visibility = TextView.VISIBLE
        scoreTextView.visibility = TextView.VISIBLE
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
        scoreTextView.visibility = View.VISIBLE // Rendre le TextView du score visible
        scoreTextView.gravity = Gravity.CENTER // Centrer le TextView du score
        countdownTextView.text = "Countdown"
        countdownTimer?.cancel()
        shakeDetector.stop()
        startButton.visibility = View.INVISIBLE // Cacher le bouton "Start"
        isGameRunning = false

        val resultIntent = Intent()
        resultIntent.putExtra("score", score)
        setResult(RESULT_OK, resultIntent)
        finish() // Terminer cette activité
    }


    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    override fun onDestroy() {
        super.onDestroy()
        shakeDetector.stop()
    }
}

class ShakeDetector(context: Context) : SensorEventListener {

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
