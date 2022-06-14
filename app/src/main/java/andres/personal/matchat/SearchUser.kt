package andres.personal.matchat

import andres.personal.matchat.adapters.AddUserAdapter
import andres.personal.matchat.models.User
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


class SearchUser : AppCompatActivity() {

    private lateinit var usuName: EditText

    private lateinit var userRecyclerView: RecyclerView

    //creo las dependencias del recycler
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: AddUserAdapter

    //pillo lo de fire base
    private lateinit var auth: FirebaseAuth

    private lateinit var ref: DatabaseReference

    private lateinit var btSend: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        usuName = findViewById(R.id.SUName)

        btSend = findViewById(R.id.SUBtSend)


        userList = ArrayList()
        //creo el adaptador que he hecho en otra clase
        userAdapter = AddUserAdapter(this, userList)

        auth = FirebaseAuth.getInstance()

        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        userRecyclerView = findViewById(R.id.mRv)

        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userRecyclerView.adapter = userAdapter

        btSend.setOnClickListener {
            if (usuName.text.toString().trim() != "" && usuName.text.toString() != null) {
                setUserInAdapter()
            }else{
                setAllUsers()
            }
        }
        setAllUsers()
    }

    private fun setUserInAdapter() {
        ref.child("users").orderByChild("name").equalTo(usuName.text.toString().trim())
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    userList.clear()

                    var currentUser: User?

                    for (postSnapshot in snapshot.children) {

                        currentUser = postSnapshot.getValue(User::class.java)
                        //no puedes agregarte a ti mismo fiera
                        if (!currentUser?.uid.equals(auth.currentUser?.uid)) {
                            userList.add(currentUser!!)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun setAllUsers() {
        ref.child("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    var currentUser: User?
                    for (postSnapshot in snapshot.children) {
                        currentUser = postSnapshot.getValue(User::class.java)
                        //no puedes agregarte a ti mismo fiera
                        if (!currentUser?.uid.equals(auth.currentUser?.uid)) {
                            userList.add(currentUser!!)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}