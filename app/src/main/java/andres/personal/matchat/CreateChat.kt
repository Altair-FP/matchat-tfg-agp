package andres.personal.matchat

import andres.personal.matchat.adapters.UserAdapter
import andres.personal.matchat.models.ChatModel
import andres.personal.matchat.models.User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import java.util.*

class CreateChat : AppCompatActivity() {

    //recibo los objetos de la vista
    private lateinit var chatRecyclerView: RecyclerView

    //la lista me sirve para crear el recycler view
    private lateinit var userList: ArrayList<User>

    //los malditos recyclerView necesitan un adapter
    private lateinit var adapter: UserAdapter

    //pos el auth
    private lateinit var auth: FirebaseAuth

    //la referencia para leer los datos
    private lateinit var ref: DatabaseReference

    private lateinit var btAdd: Button

    private lateinit var btsend: ImageButton

    private lateinit var chatName: EditText

    private lateinit var usu: User

    private var responseLauncher =
        registerForActivityResult(StartActivityForResult()) { resultado ->
            if (resultado.resultCode == RESULT_OK) {

                if (resultado.data?.getStringExtra("uid") != "" &&
                    resultado.data?.getStringExtra("name") != "" &&
                    resultado.data?.getStringExtra("email") != ""
                ) {
                    usu = User(
                        resultado.data?.getStringExtra("name"),
                        resultado.data?.getStringExtra("email"),
                        resultado.data?.getStringExtra("uid")
                    )
                    var existe = false
                    for (p in userList) {
                        if (p.uid.equals(usu.uid)) {
                            existe = true
                            break
                        }
                    }
                    if (resultado.data?.getStringExtra("modo") == "add") {
                        if (!existe) {
                            userList.add(usu)
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "El usuario ya estaba incluido!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        /* no me funciona esto, era la idea optimizada pero no funciona no se por que
                        if (!userList.contains(usu)) {
                            userList.add(usu)
                            adapter.notifyDataSetChanged()
                        } */
                    }


                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_chat)

        userList = ArrayList()
        //creo el adaptador que he hecho en otra clase
        adapter = UserAdapter(this, userList)
        auth = FirebaseAuth.getInstance()
        //obtengo el recicler
        chatRecyclerView = findViewById(R.id.mRv)
        //obtengo la referencia
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()
        //al recycler view le pongo un linearLayoutManager para que se alineen los objetos verticalmente
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        //le doy el adaptador que necesita
        chatRecyclerView.adapter = adapter


        btAdd = findViewById(R.id.CCBtAdd)

        btsend = findViewById(R.id.CCBtSend)

        chatName = findViewById(R.id.CCtV)

        btAdd.setOnClickListener {
            var intent = Intent(this, SearchUser::class.java)
            responseLauncher.launch(intent)
        }

        btsend.setOnClickListener {
            if (userList.count() > 0 && chatName.text.toString().trim() != "") {
                //pongo el or empty porque sino seria un STRING? y necesito un STRING y siempre va
                //a estar logeado
                var users = auth.currentUser?.uid + ";"

                for ((i, u) in userList.withIndex()) {
                    users += u.uid
                    if (i < userList.size - 1) {
                        users += ";"
                    }
                }
                addToDatabase(chatName.text.toString().trim(), users)
            } else if (userList.count() == 0) {
                Toast.makeText(
                    applicationContext,
                    "tienes que agregar almenos 1 usuario",
                    Toast.LENGTH_LONG
                ).show()
            } else if (chatName.text.toString().trim() == "") {
                Toast.makeText(
                    applicationContext,
                    "el chat tiene que tener titulo",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun addToDatabase(nombre: String, users: String) {
        var time = Calendar.getInstance().getTime().getTime().toString()
        ref.child("newChats").push().setValue(ChatModel(nombre, users, time))
        actualizarChat(nombre, users, time)
    }

    private fun actualizarChat(nombre: String, users: String, time: String) {
        var primeraVuelta = true
        var currentChat: ChatModel? = null
        println("entro en actualizar chat")
        ref.child("newChats").orderByChild("fcreate").equalTo(time).limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (postSnapshot in snapshot.children) {
                        currentChat = postSnapshot.getValue(ChatModel::class.java)
                        println("El postSnashot.key es "+postSnapshot.key.toString())
                        if (currentChat?.fCreate != null && postSnapshot.key.toString() != null) {
                            currentChat?.uid = postSnapshot.key.toString()
                        }
                    }

                    println("current chat uid: "+currentChat?.uid)

                    if (currentChat?.uid != null && primeraVuelta) {
                        println("entro en primeraVuelta")
                        primeraVuelta = false
                        ref.child("newChats").child(currentChat?.uid!!).setValue(currentChat)
                            .addOnCompleteListener {
                                if (it.isComplete) {
                                    Toast.makeText(
                                        applicationContext,
                                        "el chat ha sido creado",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "el chat no ha sido creado",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                }

                //funcion obligatoria a este listener
                override fun onCancelled(error: DatabaseError) {
                }

            })

    }
}