package andres.personal.matchat

import andres.personal.matchat.adapters.MessageAdapter
import andres.personal.matchat.models.ChatModel
import andres.personal.matchat.models.Message
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

class ChatAdmin : AppCompatActivity() {

    //recivo lo de la vista
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var title: TextView
    private lateinit var back: ImageView

    //creo las dependencias del recycler
    private lateinit var listMessages: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter

    //pillo lo de fire base
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    private lateinit var  chatid:String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_admin)

        chatid = intent.getStringExtra("uid")!!

        auth = FirebaseAuth.getInstance()
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        supportActionBar?.hide()
        messageRecyclerView = findViewById(R.id.chatR)
        back = findViewById(R.id.chatBack)
        title = findViewById(R.id.chatTitle)

        title.text = intent.getStringExtra("name")

        listMessages = ArrayList()
        messageAdapter = MessageAdapter(this, listMessages)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        back.setOnClickListener {
            finish();
        }

        getMessages()
    }
    private fun getMessages(){
        ref.child("messages").orderByChild("chatid").equalTo(chatid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessages.clear()
                var message: Message?
                var i = 1
                for (postSnapshot in snapshot.children) {

                    message = postSnapshot.getValue(Message::class.java)
                    listMessages.add(message!!);
                }
                //notifica que los datos cambiaron
                messageRecyclerView.adapter?.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}