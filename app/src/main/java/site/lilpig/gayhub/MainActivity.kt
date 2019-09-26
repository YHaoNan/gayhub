package site.lilpig.gayhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import site.lilpig.gayhub.bookcrawler.core.Book
import site.lilpig.gayhub.bookcrawler.core.CrawlerEngine
import site.lilpig.gayhub.bookcrawler.core.OnBookFoundedListener
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.am_result)
        CrawlerEngine("登天的感觉",10, OnBookFoundedListener {
            runOnUiThread(Runnable {
                textView.text = textView.text.toString() + "\n" + it.toString()
            })
        }).start()


    }

}
