package andres.personal.matchat.adapters


import andres.personal.matchat.R
import andres.personal.matchat.models.User
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AddUserAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<AddUserAdapter.NewUserViewHolder>() {

    //esto lo pones asi porque hay que ponerlo asi no tengo ni idea
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewUserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return NewUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewUserViewHolder, position: Int) {
        val currentUser = userList[position]
        //esto es el nombre que saldra en la vista de MainActivity
        holder.useName.text = currentUser.name
        //si pulsas este nombre en el Mainactivity haz esto
        holder.itemView.setOnClickListener {
            //creo un intent y le pongo la id del usuario actual y el nombre
            var activity = context as Activity
            activity.intent.putExtra("modo","add")
            activity.intent.putExtra("name", currentUser.name)
            activity.intent.putExtra("uid", currentUser.uid)
            activity.intent.putExtra("email", currentUser.email)
            activity.setResult(Activity.RESULT_OK,activity.intent)
            activity.finish()

        }
    }

    //sin palabras lo simple de imaginar que es esto
    override fun getItemCount(): Int {
        return userList.size
    }

    //te hacen hacer una clase por cada clase que quieres poner en tu recycler
    //pon MODELO+ViewHolder y que derive y reciba la mismo y saldras con vida
    class NewUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val useName: TextView = itemView.findViewById(R.id.useName)
    }
}