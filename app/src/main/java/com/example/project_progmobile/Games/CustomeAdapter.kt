package com.example.project_progmobile.Games
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.project_progmobile.Game
import com.example.project_progmobile.R

class CustomAdapter(context: Context, games: List<Game>) :
    ArrayAdapter<Game>(context, 0, games) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val game = getItem(position)

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        }

        val gameNameTextView = itemView!!.findViewById<TextView>(R.id.gameNameTextView)
        val gameImageView = itemView.findViewById<ImageView>(R.id.gameImageView)

        game?.let {
            gameNameTextView.text = it.name
            gameImageView.setImageResource(it.imageResourceId)
        }

        return itemView
    }
}
