package com.example.project_progmobile

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.Games.ClickButtonTraining
import com.example.project_progmobile.Games.CustomAdapter
import com.example.project_progmobile.Games.DifficultiesMode
import com.example.project_progmobile.Games.MotionGameTraining
import com.example.project_progmobile.Games.ReflexGamesTraining
import com.example.project_progmobile.Games.ShakeChallengeTraining
import com.example.project_progmobile.Games.TickGameTraining
data class Game(val name: String, val imageResourceId: Int)
class MiniGamesActivity : ComponentActivity() {
    private val miniGames = arrayListOf(
        Game("Quizz Game", R.drawable.quizz_image),
       Game("Reflex Game", R.drawable.reflex_image),
        Game("Tick Game", R.drawable.tickgame_image),
        Game("Shake Challenge", R.drawable.shake_image),
        Game("Motion Game", R.drawable.motion_game_image),
        Game("Tap Challenge", R.drawable.click_button_image)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mini_games)

        val listView: ListView = findViewById(R.id.listView)

        val adapter = CustomAdapter(this, miniGames)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this, DifficultiesMode::class.java))
                1 -> startActivity(Intent(this, ReflexGamesTraining::class.java))
                2 -> startActivity(Intent(this, TickGameTraining::class.java))
                3 -> startActivity(Intent(this, ShakeChallengeTraining::class.java))
                4 -> startActivity(Intent(this, MotionGameTraining::class.java))
                5 -> startActivity(Intent(this, ClickButtonTraining::class.java))
            }
        }
    }
}




