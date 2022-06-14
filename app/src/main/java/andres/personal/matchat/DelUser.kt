package andres.personal.matchat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class DelUser : AppCompatActivity() {

    private lateinit var usuName: TextView

    private lateinit var btSi: Button

    private lateinit var btNo:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_user)

        usuName = findViewById(R.id.DUtV)

        btSi    = findViewById(R.id.DUbtYes)

        btNo    = findViewById(R.id.DUbtNo)


        usuName.setText(intent.getStringExtra("name"))

        btSi.setOnClickListener {
            intent.putExtra("uid",intent.getStringExtra("uid"))
            intent.putExtra("modo","del")
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        btNo.setOnClickListener {
            //comprobar si hay que probar eso
            finish()
        }

    }
}