package andres.personal.matchat.adapters

import andres.personal.matchat.Chat
import andres.personal.matchat.GameMain
import andres.personal.matchat.R
import andres.personal.matchat.models.ChatModel
import andres.personal.matchat.models.GameMode
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameAdapter(val context: Context, val gameList: ArrayList<GameMode>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    //esto lo pones asi porque hay que ponerlo asi no tengo ni idea
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.game_layout, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val currentGame = gameList[position]
        //esto es el nombre que saldra en la vista de MainActivity
        holder.gameName.text = currentGame.name

            holder.gameName.setOnClickListener {
                //creo un intent y le pongo la id del usuario actual y el nombre
                var intent = Intent(context, GameMain::class.java)
                intent.putExtra("name", currentGame.name)
                intent.putExtra("uid", currentGame.uid)

                context.startActivity(intent)
            }

    }
    //sin palabras lo simple de imaginar que es esto
    override fun getItemCount(): Int {
        return gameList.size
    }
    //te hacen hacer una clase por cada clase que quieres poner en tu recycler
    //pon MODELO+ViewHolder y que derive y reciba la mismo y saldras con vida
    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName: Button = itemView.findViewById(R.id.gameName)
    }
}
