package andres.personal.matchat.adapters

import andres.personal.matchat.R
import andres.personal.matchat.models.Message

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //cro estas constantes para saber que si un mensaje es de ese tipo se meta en el if 1 o en 2
    val Recive = 1;
    val Sent = 2;
    //al crearse este objeto comprueba si es de enviar o de recibir y ejecuta ese codigo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View;
        if (viewType.equals(1)) {
            view = LayoutInflater.from(context).inflate(R.layout.recive, parent, false)
            //ojo mira que la clase q muestran es la que hemos creado antes
            return ReciveViewHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if (holder.itemViewType.equals(2)) {

            //como en el if filtra si es sent o recive
            //yo convierto al objeto en un SentViewHolder
            val viewHolder = holder as SentViewHolder
            //pongo el texto del sent a el texto de este mensaje
            holder.sentMessage.text = currentMessage.message
        } else {

            val viewHolder = holder as ReciveViewHolder
            holder.reciveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        //basicamente si el mensaje este que estoy leyendo tiene la misma id de quien lo esta leyendo
        //es un mensaje que enviaste tu
        //por si no se entiendo si la id del enviador fuiste tu el mensaje es tuyo sino es del otro
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return Sent
        } else {
            return Recive
        }
    }
    //paso de explicartelo aprende ingles
    override fun getItemCount(): Int {
        return messageList.size
    }
    //las clases que estas obligado a hacer por cada modelo que quieras usar
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
    }

    class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reciveMessage = itemView.findViewById<TextView>(R.id.reciveMessage)
    }


}