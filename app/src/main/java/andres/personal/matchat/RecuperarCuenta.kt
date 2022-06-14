package andres.personal.matchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RecuperarCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText

    private lateinit var bt: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_cuenta)
        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.recEmail)

        bt = findViewById(R.id.recBt)

        bt.setOnClickListener {
            if (verificar()){
                auth.sendPasswordResetEmail(email.text.toString().trim()).addOnCompleteListener {
                    if (it.isComplete){
                        Toast.makeText(
                            applicationContext,
                            "Se ha enviado un correo de recuperacion de contraseña a "+email.text.toString().trim(),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }

    }
    private fun verificar(): Boolean {
        var mensaje = ""
        var patronEmail = Regex("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$")
        if (!email.text.toString().trim().matches(patronEmail))
            mensaje += " El patron del correo no coincide"

        if (mensaje.trim() != "") {
            Toast.makeText(
                applicationContext,
                mensaje,
                Toast.LENGTH_LONG
            ).show()
        }

        return mensaje.trim() == ""
    }
}