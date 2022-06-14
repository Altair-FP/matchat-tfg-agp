package andres.personal.matchat

import andres.personal.matchat.models.User
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var bt: ImageButton
    private lateinit var email: EditText
    private lateinit var pas: EditText
    private lateinit var pas2: EditText
    private lateinit var nick: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var nameList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        database =
            Firebase.database("https://kt-login-d3cda-default-rtdb.europe-west1.firebasedatabase.app/")
        ref = database.getReference()
        nameList = ArrayList()
        getNames()
        supportActionBar?.hide()
        back = findViewById(R.id.regBack)
        email = findViewById(R.id.regEmail)
        bt = findViewById(R.id.regBt)
        pas = findViewById(R.id.regPas)
        pas2 = findViewById(R.id.regPas2)
        nick = findViewById(R.id.regNick)
        back.setOnClickListener {
            finish()
        }
        bt.setOnClickListener {

            if (sonIguales(pas.text.toString(), pas2.text.toString()) &&
                checkear(pas.text.toString(), email.text.toString(), nick.text.toString())
            ) {

                registrar(email.text.toString(), pas.text.toString())

            }
        }
    }

    private fun registrar(textCorreo: String, textContraseña: String) {
        auth.createUserWithEmailAndPassword(textCorreo, textContraseña).addOnCompleteListener(this)
        { tarea ->
            if (tarea.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Revisa tu correo, verficalo!!",
                    Toast.LENGTH_LONG
                ).show()
                ref.child("users").child(auth.currentUser?.uid!!)
                    .setValue(User(nick.text.toString().trim(), textCorreo, auth.currentUser?.uid!!))
                    .addOnCompleteListener {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener(this) { tarea ->
                                if (tarea.isSuccessful) {
                                    finish()
                                }

                            }
                    }
            } else if (tarea.exception is FirebaseAuthUserCollisionException) {
                Toast.makeText(
                    applicationContext,
                    "El correo ya esta registradp!!",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (tarea.exception is FirebaseAuthInvalidCredentialsException) {

                Toast.makeText(
                    applicationContext,
                    "alguna credencial esta mal puesta!!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Error desconocido, hackeaste el sistema?!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkear(textContraseña: String, textCorreo: String, nick: String): Boolean {
        var mensaje = ""
        var patronPasword =
            Regex("^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,}\$")
        var patronEmail = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")

        if (!textContraseña.matches(patronPasword))
            mensaje += " El patron de la contraseña no coincide (2 mayusculas , 3 minusculas , 2 numeros y un caracter especial)"
        if (!textCorreo.matches(patronEmail))
            mensaje += " El patron del correo no coincide"
        if (nick.trim().isEmpty())
            mensaje += " El nick esta Vacio"
        if (nick.trim().length > 10)
            mensaje += " El nick tiene como maximo una longitud de 10 caracteres"
        if (nameList.contains(nick.trim()))
            mensaje += " El nick no puede estar repetido"
        if (mensaje.trim() != "") {
            Toast.makeText(
                applicationContext,
                mensaje,
                Toast.LENGTH_LONG
            ).show()
        }

        return mensaje.trim() == ""
    }

    private fun sonIguales(textContraseña: String, textRepiteContraseña: String): Boolean {
        if (!textContraseña.equals(textRepiteContraseña)) {
            Toast.makeText(applicationContext, "Las contraseñas no coinciden!!", Toast.LENGTH_LONG)
                .show()
        }
        return textContraseña.equals(textRepiteContraseña);
    }

    private fun getNames() {
        ref.child("users").orderByChild("name").addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                nameList.clear()
                var currentUser: User?

                for (postSnapshot in snapshot.children) {
                    currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser?.name != null) {
                        nameList.add(currentUser.name!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}