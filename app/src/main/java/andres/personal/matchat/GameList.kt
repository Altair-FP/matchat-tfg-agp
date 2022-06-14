package andres.personal.matchat

import andres.personal.matchat.adapters.GameAdapter
import andres.personal.matchat.models.ChatModel
import andres.personal.matchat.models.GameMode
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameList : AppCompatActivity() {

    private lateinit var gameName: EditText

    private lateinit var send: ImageView

    private lateinit var gameRecyclerView: RecyclerView

    private lateinit var gameList: ArrayList<GameMode>

    private lateinit var adapter: GameAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        gameList = ArrayList()

        adapter = GameAdapter(this, gameList)
        auth = FirebaseAuth.getInstance()

        gameRecyclerView = findViewById(R.id.GLRv)
        gameName = findViewById(R.id.GLchat)
        send = findViewById(R.id.GLSend)

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        gameRecyclerView.layoutManager = LinearLayoutManager(this)
        //le doy el adaptador que necesita
        gameRecyclerView.adapter = adapter

        getGames()

        send.setOnClickListener {
            if (gameName.text.toString().trim() == "")
                getGames()
            else
                getGames(gameName.text.toString().trim())
            gameName.setText("")
        }


    }

    private fun getGames(name: String) {
        ref.child("gameModes").orderByChild("name").equalTo(name).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                gameList.clear()
                var currentGame: GameMode?

                for (postSnapshot in snapshot.children) {
                    currentGame = postSnapshot.getValue(GameMode::class.java)
                    if (currentGame?.uid != null) {
                        gameList.add(currentGame!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getGames() {
        ref.child("gameModes").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                gameList.clear()
                var currentGame: GameMode?

                for (postSnapshot in snapshot.children) {
                    currentGame = postSnapshot.getValue(GameMode::class.java)
                    if (currentGame?.uid != null) {
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