package andres.personal.matchat

import andres.personal.matchat.models.GameRuletaModel
import andres.personal.matchat.models.MatchMove
import andres.personal.matchat.models.User
import andres.personal.matchat.models.UserDTO
import android.os.Bundle
import android.widget.Button
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
import kotlin.collections.ArrayList

class GameRuleta : AppCompatActivity() {
    private lateinit var ref: DatabaseReference

    private lateinit var auth: FirebaseAuth

    private lateinit var gameUid: String

    private lateinit var fPlayer: UserDTO

    private lateinit var sPlayer: UserDTO

    private lateinit var turnoFinal: String

    private lateinit var tituloPartida: TextView

    private lateinit var turnoText: TextView

    private lateinit var turnoInsulto: TextView

    private lateinit var disparar: Button

    private var turnoActual: Int = 1

    private var turnoJugador1 = true

    private lateinit var turnoList: ArrayList<MatchMove>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_ruleta)

        turnoList = ArrayList()

        gameUid = intent.getStringExtra("uid").orEmpty()

        turnoFinal = intent.getStringExtra("turno").orEmpty()

        fPlayer = UserDTO(
            intent.getStringExtra("fName").orEmpty(),
            intent.getStringExtra("fUid").orEmpty()
        )

        sPlayer = UserDTO(
            intent.getStringExtra("sName").orEmpty(),
            intent.getStringExtra("sUid").orEmpty()
        )

        auth = FirebaseAuth.getInstance()

        disparar = findViewById(R.id.GRRBt)

        tituloPartida = findViewById(R.id.GRRTv)

        turnoText = findViewById(R.id.GRRTv2)

        turnoInsulto = findViewById(R.id.GRRTv3)

        if (sPlayer?.uid.toString() == auth.currentUser?.uid.toString()) {
            disparar.setEnabled(false)
        }

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        tituloPartida.setText("Partida: " + gameUid)

        turnoText.setText("Turno " + turnoActual)

        turnoInsulto.setText(fPlayer.name + " No seas Cobarde")

        disparar.setOnClickListener {
            var currentMove: MatchMove
            var winnerId: String
            if (turnoActual.toString() != turnoFinal) {
                currentMove =
                    MatchMove("apreto el gatillo", gameUid, auth.currentUser?.uid, 10, turnoActual)
                ref.child("matchMoves").push().setValue(currentMove)
            } else {
                updateGame()
                currentMove = MatchMove("died", gameUid, auth.currentUser?.uid, -20, turnoActual)
                ref.child("matchMoves").push().setValue(currentMove)
                if (fPlayer?.uid.toString() == auth.currentUser?.uid.toString()) {
                    winnerId = sPlayer.uid.toString()
                } else {
                    winnerId = fPlayer.uid.toString()
                }
                currentMove = MatchMove("won", gameUid, winnerId, -20, turnoActual)
                ref.child("matchMoves").push().setValue(currentMove).addOnCompleteListener{}
            }
        }
        getTurnos()
    }

    private fun getTurnos() {
        ref.child("matchMoves").orderByChild("gameid").equalTo(gameUid)
            .addValueEventListener(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    turnoList.clear()
                    println("entra en GetTurnos")
                    var currentMove: MatchMove? = null

                    for (postSnapshot in snapshot.children) {
                        currentMove = postSnapshot.getValue(MatchMove::class.java)
                        turnoList.add(currentMove!!)
                    }
                    if (turnoList.size - turnoActual == 0 && currentMove?.desc != "died") {
                        pasarTurno()
                    } else if (turnoList.size - turnoActual == 1 && currentMove?.desc == "won") {
                        if (!turnoJugador1) {
                            terminarPartida(fPlayer)
                        } else {
                            terminarPartida(sPlayer)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun pasarTurno() {
        //reproducir sonido
        println("entro a pasar turno")
        turnoActual++

        turnoJugador1 = !turnoJugador1

        if (!turnoJugador1) {
            if (sPlayer?.uid.toString() == auth.currentUser?.uid.toString()) {
                disparar.setEnabled(true)
            } else {
                disparar.setEnabled(false)
            }
            turnoText.setText("Turno " + turnoActual)

            turnoInsulto.setText(sPlayer.name + " No seas Cobarde")
        } else {
            if (fPlayer?.uid.toString() == auth.currentUser?.uid.toString()) {
                disparar.setEnabled(true)
            } else {
                disparar.setEnabled(false)
            }
            turnoText.setText("Turno " + turnoActual)
            turnoInsulto.setText(sPlayer.name + " No seas Cobarde")
        }
    }

    private fun terminarPartida(winner: UserDTO) {
        println("entro a terminar la partida")
        Toast.makeText(
            applicationContext,
            "El Jugador " + winner.name + " ha ganado",
            Toast.LENGTH_LONG
        )
            .show()
        finish()
    }

    private fun updateGame() {
        var primeraVuelta = true
        ref.child("games").orderByChild("uid").equalTo(gameUid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                turnoList.clear()
                println("entra en GetTurnos")
                var currentGame: GameRuletaModel? = null

                for (postSnapshot in snapshot.children) {
                    currentGame = postSnapshot.getValue(GameRuletaModel::class.java)
                }
                if (currentGame!=null&&primeraVuelta){
                    primeraVuelta = false
                    currentGame.fecFin = Calendar.getInstance().getTime().getTime().toString()
                    currentGame.enable = false
                    ref.child("games").child(currentGame?.uid!!).setValue(currentGame)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}