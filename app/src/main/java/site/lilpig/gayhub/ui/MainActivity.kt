package site.lilpig.gayhub.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import site.lilpig.gayhub.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        am_search_bar.setOnClickListener{
            startActivity(Intent(this,SearchActivity().javaClass))
        }


    }

}
