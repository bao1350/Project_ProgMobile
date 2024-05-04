package com.example.project_progmobile.Games

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.project_progmobile.R


class DifficultiesMode : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulties_mode)

        // Bouton Facile
        val btnEasy = findViewById<Button>(R.id.btnEasy)
        btnEasy.setOnClickListener {
            startGame("easy")
        }

        // Bouton Moyen
        val btnMedium = findViewById<Button>(R.id.btnMedium)
        btnMedium.setOnClickListener {
            startGame("medium")
        }

        // Bouton Difficile
        val btnHard = findViewById<Button>(R.id.btnHard)
        btnHard.setOnClickListener {
            startGame("hard")
        }
    }

    private fun startGame(difficultyMode: String) {
        val intent = Intent(this, GamesQuizzTraining::class.java).apply {
            putExtra("difficulty_mode", difficultyMode)
        }
        startActivity(intent)
    }

}
