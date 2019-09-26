package site.lilpig.gayhub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import site.lilpig.gayhub.R
import site.lilpig.gayhub.adapter.BookAdapter
import site.lilpig.gayhub.bookcrawler.core.CrawlerEngine
import site.lilpig.gayhub.bookcrawler.core.OnBookFoundedListener
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private lateinit var bookList: ListView
    private lateinit var searchInput: EditText
    private lateinit var search: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bookList = findViewById(R.id.am_list)
        searchInput = findViewById(R.id.am_edit)
        search = findViewById(R.id.am_search)
        search.setOnClickListener({
            var bookAdapter = BookAdapter(this, ArrayList())
            bookList.adapter = bookAdapter
            CrawlerEngine(searchInput.text?.toString() ?: throw IllegalArgumentException("输入参数不完整"),10, OnBookFoundedListener {
                runOnUiThread(Runnable {
                    bookAdapter.addBook(it)
                })
            }).start()
        })


    }

}
