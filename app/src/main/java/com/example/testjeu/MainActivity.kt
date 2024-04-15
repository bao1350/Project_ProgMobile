package com.example.testjeu

import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Timer
import java.util.TimerTask


class MainActivity : ComponentActivity() {

    private lateinit var bonbons: ImageView
    private var score = 0
    private lateinit var timer: Timer

    private var vx = 0f
    private var vy = 0f
    private var dt = 0.01f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attraper_les_bonbons)

        bonbons = findViewById(R.id.bonbons)

        // Définir la position initiale des bonbons
        bonbons.x = 0f
        bonbons.y = 0f

        vx = 0f
        vy = 0f
        dt = 0.01f

        // Définir le score initial
        score = 0


        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val width = size.x
        val height = size.y


        // Démarrer le timer
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                // Déplacer les bonbons aléatoirement
                bonbons.x += (Math.random() * 10).toFloat()
                bonbons.y += (Math.random() * 10).toFloat()

                // Vérifier si les bonbons ont été touchés
                if (bonbons.isPressed) {
                    // Augmenter le score
                    score++

                    // Déplacer les bonbons à une nouvelle position
                    bonbons.x = 0f
                    bonbons.y = 0f
                }

                // Déterminer la direction du rebondissement
                if (bonbons.x > width) {
                    bonbons.x = width - 1f
                    vx *= -1
                } else if (bonbons.x < 0) {
                    bonbons.x = 1f
                    vx *= -1
                }
                if (bonbons.y > height) {
                    bonbons.y = height - 1f
                    vy *= -1
                } else if (bonbons.y < 0) {
                    bonbons.y = 1f
                    vy *= -1
                }

                // Mettre à jour la position du bonbon
                bonbons.x += vx * dt
                bonbons.y += vy * dt
            }
        }, 0, 100)


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Détecter le toucher sur l'écran
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Vérifier si les bonbons ont été touchés
            if (bonbons.isPressed) {
                // Augmenter le score
                score++

                // Déplacer les bonbons à une nouvelle position
                bonbons.x = 0f
                bonbons.y = 0f
            }
        }
        return super.onTouchEvent(event)
    }

    fun afficherScore(view: View) {
        // Afficher le score dans une Toast
        Toast.makeText(this, "Score : $score", Toast.LENGTH_SHORT).show()
    }
}
