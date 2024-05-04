package com.example.project_progmobile

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.Games.ClickButtonTraining
import com.example.project_progmobile.Games.Clickbutton
import com.example.project_progmobile.Games.CustomAdapter
import com.example.project_progmobile.Games.DifficultiesMode
import com.example.project_progmobile.Games.GamesQuizzTraining
import com.example.project_progmobile.Games.MotionGame
import com.example.project_progmobile.Games.MotionGameTraining
import com.example.project_progmobile.Games.ReflexGames
import com.example.project_progmobile.Games.ReflexGamesTraining
import com.example.project_progmobile.Games.ShakeChallengeActivity
import com.example.project_progmobile.Games.ShakeChallengeTraining
import com.example.project_progmobile.Games.TickGameActivity
import com.example.project_progmobile.Games.TickGameTraining
data class Game(val name: String, val imageResourceId: Int)
class MiniGamesActivity : ComponentActivity() {
    private val miniGames = arrayListOf(
        Game("Quizz Game", R.drawable.quiz_image),
       Game("Reflex Game", R.drawable.correct_answer_background),
        Game("Tick Game", R.drawable.backgroud_gradient),
        Game("Shake Challenge", R.drawable.background_win),
        Game("Motion Game", R.drawable.button_gradient_green),
        Game("Tap Challenge", R.drawable.button_gradient_orange)
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




