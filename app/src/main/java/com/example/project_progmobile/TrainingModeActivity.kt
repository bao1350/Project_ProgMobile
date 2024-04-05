package com.example.project_progmobile
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity

class TrainingModeActivity : ComponentActivity() {
    private val capitals = listOf(
        "France - Paris",
        "Allemagne - Berlin",
        "Espagne - Madrid",
        "Royaume-Uni - Londres",
        "Italie - Rome",
        "Canada - Ottawa",
        "Australie - Canberra",
        "Inde - New Delhi",
        "Brésil - Brasilia",
        "Japon - Tokyo",
        "Mongolie - Oulan-Bator",
        "Islande - Reykjavik",
        "Djibouti - Djibouti",
        "Bhoutan - Thimphou",
        "Suriname - Paramaribo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_mode)

        val listView = findViewById<ListView>(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, capitals)
        listView.adapter = adapter
    }
}

