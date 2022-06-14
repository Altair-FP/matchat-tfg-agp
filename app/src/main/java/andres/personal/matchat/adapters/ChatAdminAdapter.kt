package andres.personal.matchat.adapters

import andres.personal.matchat.ChatAdmin
import andres.personal.matchat.R
import andres.personal.matchat.models.ChatModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ChatAdminAdapter(val context: Context, val chatList: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatAdminAdapter.ChatAdminViewHolder>() {

    //esto lo pones asi porque hay que ponerlo asi no tengo ni idea
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdminViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false)
        return ChatAdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdminViewHolder, position: Int) {
        val currentChat = chatList[position]
        //esto es el nombre que saldra en la vista de MainActivity
        holder.chatName.text = currentChat.name
        //si pulsas este nombre en el Mainactivity haz esto
        holder.itemView.setOnClickListener {
            //creo un intent y le pongo la id del usuario actual y el nombre
            var intent = Intent(context, ChatAdmin::class.java)
            intent.putExtra("name", currentChat.name)
            intent.putExtra("uid", currentChat.uid)
            //inicia el chat con este tio
            context.startActivity(intent)

        }
    }

    //sin palabras lo simple de imaginar que es esto
    override fun getItemCount(): Int {
        return chatList.size
    }

    //te hacen hacer una clase por cada clase que quieres poner en tu recycler
    //pon MODELO+ViewHolder y que derive y reciba la mismo y saldras con vida
    class ChatAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chatName)
    }
}