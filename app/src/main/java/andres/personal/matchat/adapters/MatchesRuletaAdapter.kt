package andres.personal.matchat.adapters

import andres.personal.matchat.GameRuletaLobby
import andres.personal.matchat.R
import andres.personal.matchat.models.GameRuletaModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MatchesRuletaAdapter(val context: Context, val matchList: ArrayList<GameRuletaModel>) :
    RecyclerView.Adapter<MatchesRuletaAdapter.MatchRuletaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchRuletaViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.game_layout, parent, false)
        return MatchRuletaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchRuletaViewHolder, position: Int) {
        val currentGame = matchList[position]

        holder.matchName.text = currentGame.uid

        holder.matchName.setOnClickListener {
            var intent = Intent(context, GameRuletaLobby::class.java)
            intent.putExtra("uid", currentGame.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    class MatchRuletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matchName: Button = itemView.findViewById(R.id.gameName)
    }
}
