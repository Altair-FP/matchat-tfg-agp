package andres.personal.matchat

import andres.personal.matchat.models.GameModel
import andres.personal.matchat.models.GameRuletaModel
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

//la clase date puede crear una fecha en base a milisegundos,
// y los meses son 0-11
class GameMain : AppCompatActivity() {

    private lateinit var btCrear: Button

    private lateinit var btUnirse: Button

    private lateinit var modeUid: String

    private lateinit var modeName: String

    private lateinit var uid: String

    private lateinit var btBack: ImageView

    private lateinit var tv: TextView

    private lateinit var ref: DatabaseReference

    private lateinit var auth: FirebaseAuth

    private lateinit var fCreacion: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_main)

        btCrear = findViewById(R.id.GMBtC)
        btUnirse = findViewById(R.id.GMBtJ)
        btBack = findViewById(R.id.GMBack)
        tv = findViewById(R.id.GMTv)

        fCreacion = Calendar.getInstance().getTime().getTime().toString()

        auth = FirebaseAuth.getInstance()

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        modeUid = intent.getStringExtra("uid").orEmpty()

        modeName = intent.getStringExtra("name").orEmpty()

        if (modeName.trim() != "") {
            tv.setText(modeName.trim())
        }
        btCrear.setOnClickListener {
            CrearPartida()
            if (modeUid == "1") {
                actualizarRuleta(this)
            } else {
                Toast.makeText(
                    applicationContext,
                    "El modo de juego no esta habilitado para jugar!!",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

        btUnirse.setOnClickListener {
            if (modeUid == "1") {
                startActivity(Intent(this, GameListRuleta::class.java))
            }
        }

        btBack.setOnClickListener {
            finish()
        }
    }

    private fun CrearPartida() {
        ref.child("games").push()
            .setValue(GameModel(modeUid, fCreacion, auth.currentUser?.uid.toString()))
    }

    private fun actualizarRuleta(gameMain: GameMain) {
        var currentGame: GameRuletaModel? = null
        var primeraVuelta = true
        ref.child("games").orderByChild("fecCreacion").equalTo(fCreacion).limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        currentGame = postSnapshot.getValue(GameRuletaModel::class.java)
                        if (currentGame != null &&
                            postSnapshot.key != "" &&
                            postSnapshot.key != null
                        ) {
                            currentGame?.uid = postSnapshot.key
                            uid = postSnapshot.key.toString()
                            currentGame?.enable = true
                            currentGame?.turno = ((1..6).random()).toString()
                        }
                    }
                    if (currentGame != null && currentGame?.uid != null && primeraVuelta) {
                        primeraVuelta = false
                        ref.child("games").child(currentGame?.uid!!).setValue(currentGame)
                            .addOnCompleteListener {
                                var i = Intent(gameMain, GameRuletaLobby::class.java)
                                i.putExtra("uid", uid)
                                startActivity(i)
                                finish()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}