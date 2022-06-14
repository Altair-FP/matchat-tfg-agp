package andres.personal.matchat

import andres.personal.matchat.adapters.ChatAdapter
import andres.personal.matchat.models.ChatModel
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


class ChatsList : AppCompatActivity() {

    private lateinit var chatName: EditText

    private lateinit var send: ImageView
    //recibo los objetos de la vista
    private lateinit var chatRecyclerView: RecyclerView

    //la lista me sirve para crear el recycler view
    private lateinit var chatList: ArrayList<ChatModel>

    //los malditos recyclerView necesitan un adapter
    private lateinit var adapter: ChatAdapter

    //pos el auth
    private lateinit var auth: FirebaseAuth

    //la referencia para leer los datos
    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats_list)

        chatList = ArrayList()
        //creo el adaptador que he hecho en otra clase
        adapter = ChatAdapter(this, chatList)
        auth = FirebaseAuth.getInstance()
        //obtengo el recicler
        chatRecyclerView = findViewById(R.id.CLRv)
        chatName = findViewById(R.id.CLchat)
        send = findViewById(R.id.CLSend)
        //obtengo la referencia
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()
        //al recycler view le pongo un linearLayoutManager para que se alineen los objetos verticalmente
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        //le doy el adaptador que necesita
        chatRecyclerView.adapter = adapter

        getChats()

        send.setOnClickListener{
            if (chatName.text.toString().trim() == "")
                getChats()
            else
                getChats(chatName.text.toString())
            chatName.setText("")
        }
    }
    private fun getChats(name:String ){
        ref.child("newChats").orderByChild("name").equalTo(name).addValueEventListener(object : ValueEventListener {
            //si los datos de la base de datos cambian
            //la variable snapshot son los datos de la query
            override fun onDataChange(snapshot: DataSnapshot) {
                //limpio la lista que ya tenia
                chatList.clear()
                //creo la variable que usare para leer cada usuario
                var currentChat: ChatModel?
                //le hago un for each y los meto en la lista siempre y cuando no sean tu mismo
                //ya que no tiene sentido que puedas mandarte mensajes a ti mismo
                for (postSnapshot in snapshot.children) {
                    currentChat = postSnapshot.getValue(ChatModel::class.java)
                    if (currentChat?.users != null) {
                        if (currentChat.users!!.contains(auth.currentUser?.uid!!)) {
                            chatList.add(currentChat!!)
                        }
                    }
                }
                //avisale al adaptador que los datos han cambiado para que se refresque
                adapter.notifyDataSetChanged()
            }

            //funcion obligatoria a este listener
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun getChats(){
        ref.child("newChats").addValueEventListener(object : ValueEventListener {
            //si los datos de la base de datos cambian
            //la variable snapshot son los datos de la query
            override fun onDataChange(snapshot: DataSnapshot) {
                //limpio la lista que ya tenia
                chatList.clear()
                //creo la variable que usare para leer cada usuario
                var currentChat: ChatModel?
                //le hago un for each y los meto en la lista siempre y cuando no sean tu mismo
                //ya que no tiene sentido que puedas mandarte mensajes a ti mismo
                for (postSnapshot in snapshot.children) {
                    currentChat = postSnapshot.getValue(ChatModel::class.java)
                    if (currentChat?.users != null) {
                        if (currentChat.users!!.contains(auth.currentUser?.uid!!)) {
                            chatList.add(currentChat!!)
                        }
                    }
                }
                //avisale al adaptador que los datos han cambiado para que se refresque
                adapter.notifyDataSetChanged()
            }

            //funcion obligatoria a este listener
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}