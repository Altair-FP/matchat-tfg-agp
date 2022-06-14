package andres.personal.matchat

import andres.personal.matchat.adapters.GameLobbyAdapter
import andres.personal.matchat.models.GameRuletaModel
import andres.personal.matchat.models.User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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


class GameRuletaLobby : AppCompatActivity() {

    private lateinit var gameRecyclerView: RecyclerView

    private lateinit var userList: ArrayList<User>

    private lateinit var adapter: GameLobbyAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var ref: DatabaseReference

    private lateinit var iniciar: Button

    private lateinit var gameUid: String

    private  var currentGame: GameRuletaModel? = null

    private lateinit var partidaText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ruleta_lobby)

        userList = ArrayList()

        adapter = GameLobbyAdapter(this, userList)

        auth = FirebaseAuth.getInstance()

        gameRecyclerView = findViewById(R.id.GRLRv)

        iniciar = findViewById(R.id.GRLbt)

        partidaText = findViewById(R.id.GRLsal)

        gameUid = intent.getStringExtra("uid").orEmpty()

        partidaText.setText("Partida: "+gameUid)

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        gameRecyclerView.layoutManager = LinearLayoutManager(this)
        //le doy el adaptador que necesita
        gameRecyclerView.adapter = adapter

        getGame(this)

        iniciar.setOnClickListener {
            if (currentGame != null) {
                currentGame?.empezada = true
                var i = Intent(this,GameRuleta::class.java)
                ref.child("games").child(currentGame?.uid!!).setValue(currentGame).addOnCompleteListener {
                    i.putExtra("uid",currentGame?.uid)
                    i.putExtra("turno",currentGame?.turno)
                    for (u in userList) {
                        if (u?.uid== auth.currentUser?.uid){
                            i.putExtra("fUid",u?.uid)
                            i.putExtra("fName",u.name)
                        }else{
                            i.putExtra("sUid",u?.uid)
                            i.putExtra("sName",u.name)
                        }
                    }
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    private fun getGame(gameRuletaLobby: GameRuletaLobby) {
        currentGame = null
        var usuarios: ArrayList<String> = ArrayList()
        ref.child("games").orderByChild("uid").equalTo(gameUid).limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuarios.clear()
                    for (postSnapshot in snapshot.children) {
                        currentGame = postSnapshot.getValue(GameRuletaModel::class.java)
                    }
                    if (currentGame?.users?.contains(";")!!) {
                        usuarios = currentGame?.users?.split(";") as ArrayList<String>
                        if (usuarios.get(0) == auth.currentUser?.uid) {
                            iniciar.setEnabled(true)
                        }else if (currentGame?.empezada != null){

                            var i = Intent(gameRuletaLobby,GameRuleta::class.java)

                            i.putExtra("uid",currentGame?.uid)
                            i.putExtra("turno",currentGame?.turno)

                            for (u in userList) {
                                if (u?.uid != auth.currentUser?.uid){
                                    i.putExtra("fUid",u?.uid)
                                    i.putExtra("fName",u.name)
                                }else{
                                    i.putExtra("sUid",u?.uid)
                                    i.putExtra("sName",u.name)
                                }
                            }
                            startActivity(i)
                            finish()

                        }

                    } else if (!currentGame?.users?.contains(auth.currentUser?.uid!!)!!) {
                        currentGame?.users += ";" + auth.currentUser?.uid
                        ref.child("games").child(currentGame?.uid!!).setValue(currentGame)
                    } else {
                        usuarios.add(currentGame?.users!!)
                    }
                    getUsuarios(usuarios)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun getUsuarios(usuarios: ArrayList<String>) {
        var currentUser: User?
        ref.child("users").orderByChild("uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    currentUser = postSnapshot.getValue(User::class.java)
                    if (usuarios.contains(currentUser?.uid.toString())) {

                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}