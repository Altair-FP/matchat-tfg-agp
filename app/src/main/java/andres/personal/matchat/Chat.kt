package andres.personal.matchat

import andres.personal.matchat.adapters.MessageAdapter
import andres.personal.matchat.models.Message
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
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

class Chat : AppCompatActivity() {
    //recivo lo de la vista
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var msg: EditText
    private lateinit var sendButton: ImageView
    private lateinit var title: TextView
    private lateinit var back: ImageView

    //creo las dependencias del recycler
    private lateinit var listMessages: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter

    //pillo lo de fire base
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatid = intent.getStringExtra("uid")

        auth = FirebaseAuth.getInstance()
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()
        //esta es la id de quien envia el mensaje osea yo
        val senderUid = auth.currentUser?.uid
        //la odio a muerte
        supportActionBar?.hide()

        //pillo las vistas
        messageRecyclerView = findViewById(R.id.chatR)
        msg = findViewById(R.id.chatMsg)
        sendButton = findViewById(R.id.chatBt)
        back = findViewById(R.id.chatBack)
        title = findViewById(R.id.chatTitle)
        //pillo el nombre del chat para ponerlo arriba como en wasa
        title.text = intent.getStringExtra("name")
        //dependencia del recycler ZZZ
        listMessages = ArrayList()
        messageAdapter = MessageAdapter(this, listMessages)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter
        //me mata la actividad y va a la anterior osea
        back.setOnClickListener {
            finish();
        }

        sendButton.setOnClickListener {
            if (msg.text.toString().trim() != "") {
                //creo el mensaje
                val message = Message(msg.text.toString().trim(), senderUid, chatid)
                //y lo guardo en la base de datos
                ref.child("messages").push().setValue(message)
            }
            //reseteo el texto del input de los mensajes por razones de usabilidad
            msg.setText("")
        }
        //recibo los datos de los mensajes entre ese usuario y yo
        ref.child("messages").orderByChild("chatid").equalTo(chatid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //limpio la lista
                listMessages.clear()
                //la variable de los mensajes que voy a leer
                var message: Message?
                //pongo un contador para saber cuantas vueltas me daba
                //esto es culpa mia de tener mal hecha la base de datos
                //ya solo sirve para ver qu funciona correctamente
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