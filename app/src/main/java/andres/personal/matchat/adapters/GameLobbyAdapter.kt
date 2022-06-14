package andres.personal.matchat.adapters

import andres.personal.matchat.R
import andres.personal.matchat.models.User
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class GameLobbyAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<GameLobbyAdapter.GameLobbyViewHolder>() {

    //esto lo pones asi porque hay que ponerlo asi no tengo ni idea
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLobbyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return GameLobbyViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameLobbyViewHolder, position: Int) {
        val currentUser = userList[position]
        //esto es el nombre que saldra en la vista de MainActivity
        holder.userName.text = currentUser.name
    }

    //sin palabras lo simple de imaginar que es esto
    override fun getItemCount(): Int {
        return userList.size
    }

    //te hacen hacer una clase por cada clase que quieres poner en tu recycler
    //pon MODELO+ViewHolder y que derive y reciba la mismo y saldras con vida
    class GameLobbyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.useName)
    }
}
