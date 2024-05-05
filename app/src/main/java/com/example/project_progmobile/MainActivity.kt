package com.example.project_progmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.project_progmobile.modeSolo.SoloMode
import com.example.project_progmobile.Bluetooth.MainActivityBluetooth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        val btnSolo = findViewById<Button>(R.id.btnSolo)
       //val btnMultiplayer = findViewById<Button>(R.id.btnMultiplayer)
        val btnTraining = findViewById<Button>(R.id.btnTraining)
        val btnMulti = findViewById<Button>(R.id.btnMultiplayer)
        val btnBluetooth = findViewById<Button>(R.id.btnBluetooth)

        // Définition des listeners de clic pour chaque bouton
        btnSolo.setOnClickListener {
            startActivity(Intent(this, SoloMode::class.java))

        }
        btnTraining.setOnClickListener {
            startActivity(Intent(this, MiniGamesActivity::class.java))

        }
        btnMulti.setOnClickListener {


        }
        btnBluetooth.setOnClickListener{
            startActivity(Intent(this, MainActivityBluetooth::class.java))
        }


    }


}