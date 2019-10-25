package site.lilpig.gayhub.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pure_text.*
import site.lilpig.gayhub.R

class PureTextActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pure_text)
        val text:String = intent.getStringExtra("text")
        apt_text.text = text
    }
}