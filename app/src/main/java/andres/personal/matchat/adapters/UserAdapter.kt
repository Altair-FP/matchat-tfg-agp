package andres.personal.matchat.adapters

import andres.personal.matchat.CreateChat
import andres.personal.matchat.DelUser
import andres.personal.matchat.R
import andres.personal.matchat.models.User
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//creo el adaptador y le pongo la clase de la que tiene que derivar, no voy a mentir no lo entiendo muy bien
//pero si tu buscas como hacer un RecyclerView todos hacen adaptador asi que tu haces adpatador y te callas
class UserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    //esto lo pones asi porque hay que ponerlo asi no tengo ni idea
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        //esto es el nombre que saldra en la vista de MainActivity
        holder.useName.text = currentUser.name

        holder.itemView.setOnClickListener {
            userList.removeAt(position)
            notifyDataSetChanged()
        }
    }
    //sin palabras lo simple de imaginar que es esto
    override fun getItemCount(): Int {
        return userList.size
    }
    //te hacen hacer una clase por cada clase que quieres poner en tu recycler
    //pon MODELO+ViewHolder y que derive y reciba la mismo y saldras con vida
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val useName: TextView = itemView.findViewById(R.id.useName)
    }
}