package andres.personal.matchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class ChatMain : AppCompatActivity() {

    private lateinit var btCrear: Button

    private lateinit var btVer: Button

    private lateinit var btBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        btCrear  = findViewById(R.id.CMBtC)
        btVer    = findViewById(R.id.CMBtV)
        btBack   = findViewById(R.id.CMBack)

        btCrear.setOnClickListener {
            startActivity(Intent(this, CreateChat::class.java))
        }

        btVer.setOnClickListener {
            startActivity(Intent(this, ChatsList::class.java))
        }

        btBack.setOnClickListener{
            finish()
        }
    }
}