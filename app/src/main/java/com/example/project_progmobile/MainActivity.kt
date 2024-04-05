package com.example.project_progmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project_progmobile.ui.theme.Project_ProgMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        val btnSolo = findViewById<Button>(R.id.btnSolo)
       //val btnMultiplayer = findViewById<Button>(R.id.btnMultiplayer)
        val btnTraining = findViewById<Button>(R.id.btnTraining)

        // Définition des listeners de clic pour chaque bouton
        btnSolo.setOnClickListener {
            // Démarrage de l'activité SoloModeActivity
            startActivity(Intent(this, MiniGamesActivity::class.java))
        }
        btnTraining.setOnClickListener {
            startActivity(Intent(this, TrainingModeActivity::class.java))

        }

    }


}