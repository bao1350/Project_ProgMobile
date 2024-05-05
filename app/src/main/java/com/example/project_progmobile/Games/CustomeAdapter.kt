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

        val gameNameTextView1 = itemView!!.findViewById<TextView>(R.id.gameNameTextView1)
        val gameImageView1 = itemView.findViewById<ImageView>(R.id.gameImageView1)
        val gameNameTextView2 = itemView!!.findViewById<TextView>(R.id.gameNameTextView2)
        val gameImageView2 = itemView.findViewById<ImageView>(R.id.gameImageView2)
        val gameNameTextView3 = itemView!!.findViewById<TextView>(R.id.gameNameTextView3)
        val gameImageView3 = itemView.findViewById<ImageView>(R.id.gameImageView3)
        val gameNameTextView4 = itemView!!.findViewById<TextView>(R.id.gameNameTextView4)
        val gameImageView4 = itemView.findViewById<ImageView>(R.id.gameImageView4)
        val gameNameTextView5 = itemView!!.findViewById<TextView>(R.id.gameNameTextView5)
        val gameImageView5 = itemView.findViewById<ImageView>(R.id.gameImageView5)



        game?.let {
            gameNameTextView1.text = it.name
            gameImageView1.setImageResource(it.imageResourceId)
            gameNameTextView2.text = it.name
            gameImageView2.setImageResource(it.imageResourceId)
            gameNameTextView3.text = it.name
            gameImageView3.setImageResource(it.imageResourceId)
            gameNameTextView4.text = it.name
            gameImageView4.setImageResource(it.imageResourceId)
            gameNameTextView5.text = it.name
            gameImageView5.setImageResource(it.imageResourceId)


        }

        return itemView
    }
}
