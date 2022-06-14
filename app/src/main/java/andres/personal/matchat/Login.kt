package andres.personal.matchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var pas: EditText
    private lateinit var btSend: ImageButton
    private lateinit var btNew: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var btRec: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.logEmail)
        pas = findViewById(R.id.logPas)
        btSend = findViewById(R.id.logBtSend)
        btNew = findViewById(R.id.logBtNew)
        btRec = findViewById(R.id.logBtrec)
        btSend.setOnClickListener {
            if (email.text.toString().isNotEmpty() && pas.text.toString().isNotEmpty()) {
                login(email.text.toString(), pas.text.toString())
            } else {
                Toast.makeText(applicationContext, "Fiera rellena los campos!!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        btNew.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        btRec.setOnClickListener{
            startActivity(Intent(this, RecuperarCuenta::class.java))
        }
    }
    private fun login(correo: String, contraseña: String) {
        auth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this)
            { tarea ->
                if (tarea.isSuccessful) {
                    if (chekear()) {
                        startActivity(Intent(this, Principal::class.java))
                        finish()
                    }
                } else if (tarea.exception is FirebaseAuthInvalidCredentialsException) {
                    println("La exepcion es :"+tarea.exception)
                    Toast.makeText(
                        applicationContext,
                        "Los datos no coinciden con ningun usuario!!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (tarea.exception is FirebaseAuthInvalidUserException){
                    Toast.makeText(
                        applicationContext,
                        "El usuario esta Desactivado, Habla con algun Admin!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun chekear(): Boolean {
        var check: Boolean?;
        check = auth.currentUser?.isEmailVerified
        if (check != null) {
            if (!check) {
                Toast.makeText(
                    applicationContext,
                    "El email no esta verificado miralo!!",
                    Toast.LENGTH_LONG
                ).show()

            }
            return check
        }

        return false

    }
}