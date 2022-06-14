package andres.personal.matchat

import andres.personal.matchat.adapters.GameAdapter
import andres.personal.matchat.adapters.MatchesRuletaAdapter
import andres.personal.matchat.models.GameMode
import andres.personal.matchat.models.GameRuletaModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameListRuleta : AppCompatActivity() {

    private lateinit var gameUid: EditText

    private lateinit var send: ImageView

    private lateinit var recyclerView: RecyclerView

    private lateinit var gameList: ArrayList<GameRuletaModel>

    private lateinit var adapter: MatchesRuletaAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list_ruleta)


        gameList = ArrayList()

        adapter = MatchesRuletaAdapter(this, gameList)
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.GLRRv)
        gameUid = findViewById(R.id.GLRchat)
        send = findViewById(R.id.GLRSend)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        getGames()

        send.setOnClickListener {
            if (gameUid.text.toString().trim() == "")
                getGames()
            else
                getGames(gameUid.text.toString().trim())
            gameUid.setText("")
        }

    }

    private fun getGames(){
        ref.child("games").orderByChild("enable").equalTo(true).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                gameList.clear()
                var currentGame: GameRuletaModel?

                for (postSnapshot in snapshot.children) {
                    currentGame = postSnapshot.getValue(GameRuletaModel::class.java)
                    if (!(currentGame?.users?.contains(";"))!! && (currentGame?.gameModeid == "1")) {
                        gameList.add(currentGame!!)
                        println(gameList.size)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getGames(uid:String){
/*        ref.child("games").orderByChild("enable").equalTo(true).orderByChild("gameModeid").equalTo("1").addValueEventListener(object :
            ValueEventListener {
*/
        ref.child("games").orderByChild("uid").equalTo(uid).limitToFirst(1).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gameList.clear()
                var currentGame: GameRuletaModel?

                for (postSnapshot in snapshot.children) {
                    currentGame = postSnapshot.getValue(GameRuletaModel::class.java)
                    if (!(currentGame?.users?.contains(";"))!! && (currentGame?.gameModeid == "1") && currentGame.enable) {
                        gameList.add(currentGame!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}