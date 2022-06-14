package andres.personal.matchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class AdminMain : AppCompatActivity() {


    private lateinit var btBorrar: Button

    private lateinit var btVer: Button

    private lateinit var btBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        btBorrar  = findViewById(R.id.ABtBr)
        btVer    = findViewById(R.id.ABtE)
        btBack   = findViewById(R.id.ABack)

        btBorrar.setOnClickListener {
            startActivity(Intent(this, BorrarGame::class.java))
        }

        btVer.setOnClickListener {
            startActivity(Intent(this, AdminChatsList::class.java))
        }

        btBack.setOnClickListener{
            finish()
        }
    }
}