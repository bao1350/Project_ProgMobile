package com.example.project_progmobile

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.Games.DifficultiesMode
import com.example.project_progmobile.Games.MotionGame
import com.example.project_progmobile.Games.ReflexGames
import com.example.project_progmobile.Games.ShakeChallengeActivity
import com.example.project_progmobile.Games.TickGameActivity

class MiniGamesActivity : ComponentActivity() {
    private val miniGames = arrayOf(
        "Games Capitales",
        "Games Reflex",
        "Tick Game ",
        "Shake Challenge ",
        "Sound Game",
        "Circle Challenge"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mini_games)

        val listView: ListView = findViewById(R.id.listView)


        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, miniGames)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this, DifficultiesMode::class.java))
                1->startActivity(Intent(this, ReflexGames::class.java))
                2->startActivity(Intent(this, TickGameActivity::class.java))
                3->startActivity((Intent(this, ShakeChallengeActivity::class.java)))
                //4->startActivity(Intent(this,SoundGame::class.java))
                5->startActivity((Intent(this, MotionGame::class.java)))



            }
        }
    }
}



