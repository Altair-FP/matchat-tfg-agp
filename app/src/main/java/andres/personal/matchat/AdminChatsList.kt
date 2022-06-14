package andres.personal.matchat

import andres.personal.matchat.adapters.ChatAdapter
import andres.personal.matchat.adapters.ChatAdminAdapter
import andres.personal.matchat.models.ChatModel
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

class AdminChatsList : AppCompatActivity() {

    private lateinit var chatName: EditText

    private lateinit var send: ImageView
    //recibo los objetos de la vista
    private lateinit var chatRecyclerView: RecyclerView

    //la lista me sirve para crear el recycler view
    private lateinit var chatList: ArrayList<ChatModel>

    //los malditos recyclerView necesitan un adapter
    private lateinit var adapter: ChatAdminAdapter

    //pos el auth
    private lateinit var auth: FirebaseAuth

    //la referencia para leer los datos
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_chats_list)

        chatList = ArrayList()
        adapter = ChatAdminAdapter(this, chatList)
        auth = FirebaseAuth.getInstance()
        chatRecyclerView = findViewById(R.id.ACLRv)
        chatName = findViewById(R.id.ACLchat)
        send = findViewById(R.id.ACLSend)
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
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
        ref.child("newChats").orderByChild("name").equalTo(name).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                var currentChat: ChatModel?
                for (postSnapshot in snapshot.children) {
                    currentChat = postSnapshot.getValue(ChatModel::class.java)
                    if (currentChat?.users != null) {
                            chatList.add(currentChat!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun getChats(){
        ref.child("newChats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                var currentChat: ChatModel?
                for (postSnapshot in snapshot.children) {
                    currentChat = postSnapshot.getValue(ChatModel::class.java)
                    if (currentChat?.users != null) {
                            chatList.add(currentChat!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }




}