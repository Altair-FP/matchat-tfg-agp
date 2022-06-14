package andres.personal.matchat

import andres.personal.matchat.models.RolHasMenuModel
import andres.personal.matchat.models.User
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Principal : AppCompatActivity() {

    private lateinit var texto: TextView
    private lateinit var btPlay: Button
    private lateinit var btChat: Button
    private lateinit var btAdmin: Button
    private lateinit var btSuper: Button

    //como el proyecto esta hecho con firebase necesito logear con auth
    private lateinit var auth: FirebaseAuth

    //la referencia para leer los datos
    private lateinit var ref: DatabaseReference

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        texto = findViewById(R.id.lTv2)

        btPlay = findViewById(R.id.prinBtPlay)

        btChat = findViewById(R.id.prinBtChat)

        btAdmin = findViewById(R.id.prinBtAdmin)

        btSuper = findViewById(R.id.prinBtSuper)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            //por si de alguna forma magica se puede entrar sin estar autorizado, te expulso
            finish()
        }
        ref =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference()

        getUserData()

        btChat.setOnClickListener {
            startActivity(Intent(this, ChatMain::class.java))
        }
        btPlay.setOnClickListener {
            startActivity(Intent(this, GameList::class.java))
        }
        btAdmin.setOnClickListener{
            startActivity(Intent(this,AdminMain::class.java))
        }
        btSuper.setOnClickListener {

        }



    }

    override fun onBackPressed() {
        var builder: AlertDialog.Builder  = AlertDialog.Builder(this)

        builder.setIcon(R.drawable.ic_baseline_android_24)

        builder.setTitle("Cerrar Sesion")

        builder.setMessage("Estas seguro de que quieres cerrar sesion")

        builder.setPositiveButton("Cerrar Sesion"){d,w ->
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        builder.setNeutralButton("No"){d,w ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getUserData(){

        ref.child("users").orderByChild("email").equalTo(auth.currentUser?.email).limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                //si los datos de la base de datos cambian
                //la variable snapshot son los datos de la query
                override fun onDataChange(snapshot: DataSnapshot) {
                    var currentUser: User? = null
                    for (postSnapshot in snapshot.children) {
                        currentUser = postSnapshot.getValue(User::class.java)

                    }
                    if (currentUser?.name != null)
                        texto.text = "Bienvenido " + currentUser.name

                    if(currentUser?.rolid != null){
                        getMenus(currentUser.rolid!!)
                    }
                }

                //funcion obligatoria a este listener
                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun getMenus(rolid: String){
        println("El rolid de ahora es "+rolid )
        var currentRHM: RolHasMenuModel?
        var listRHM: ArrayList<RolHasMenuModel> = ArrayList()

        ref.child("RolHasMenu").orderByChild("rolid").equalTo(rolid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listRHM.clear()
                    for (postSnapshot in snapshot.children) {
                        currentRHM = postSnapshot.getValue(RolHasMenuModel::class.java)

                        if (currentRHM != null){
                            listRHM.add(currentRHM!!)
                        }

                    }

                    for (rHM in listRHM){
                        println("el menu id de ahora es "+rHM.menuid)
                        when(rHM.menuid){
                            "1"->{
                                btChat.setVisibility(View.VISIBLE)
                            }

                            "2"->{
                                btPlay.setVisibility(View.VISIBLE)
                            }
                            "3"->{
                                btAdmin.setVisibility(View.VISIBLE)
                            }
                            "4"->{
                                btSuper.setVisibility(View.VISIBLE)
                            }
                            else->{}
                        }
                    }

                }

                //funcion obligatoria a este listener
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}