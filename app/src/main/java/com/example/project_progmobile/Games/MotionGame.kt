package com.example.project_progmobile.Games
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.project_progmobile.R

class MotionGame : ComponentActivity() {

    private lateinit var player: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_games)

        player = findViewById(R.id.player)
        player.setOnTouchListener { view, event ->
            movePlayer(view, event)
            true
        }
    }

    private fun movePlayer(view: View, event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val newX = event.rawX - view.width / 2
                val newY = event.rawY - view.height / 2
                view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            }
        }
    }
}
