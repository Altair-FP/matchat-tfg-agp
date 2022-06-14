package andres.personal.matchat.adapters

import andres.personal.matchat.GameRuletaLobby
import andres.personal.matchat.Login
import andres.personal.matchat.R
import andres.personal.matchat.models.GameRuletaModel
import andres.personal.matchat.models.MatchMove
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class AdminDeleteRuletaAdapter(val context: Context, val matchList: ArrayList<GameRuletaModel>) :
    RecyclerView.Adapter<AdminDeleteRuletaAdapter.AdminDeleteRuletaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDeleteRuletaViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.game_layout, parent, false)
        return AdminDeleteRuletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminDeleteRuletaViewHolder, position: Int) {
        val currentGame = matchList[position]

        holder.matchName.text = currentGame.uid

        holder.matchName.setOnClickListener {
            var builder: AlertDialog.Builder  = AlertDialog.Builder(context)

            builder.setIcon(R.drawable.ic_baseline_android_24)

            builder.setTitle("Borrar Registro")

            builder.setMessage("Estas seguro de que quieres borrar todos los movimientos y la partida "+currentGame.uid)

            builder.setPositiveButton("Eliminar Todo"){d,w ->

                var ref: DatabaseReference =
                    Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference()

                ref.child("matchMoves").orderByChild("gameid").equalTo(currentGame.uid).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (postSnapshot in snapshot.children) {
                            postSnapshot.getRef().removeValue()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                ref.child("games").orderByChild("uid").equalTo(currentGame.uid).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postSnapshot in snapshot.children) {
                            postSnapshot.getRef().removeValue()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                matchList.removeAt(position)
                notifyDataSetChanged()

            }
            builder.setNeutralButton("No"){d,w ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    class AdminDeleteRuletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matchName: Button = itemView.findViewById(R.id.gameName)
    }
}
