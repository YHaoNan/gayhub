package site.lilpig.gayhub.isbn_getter

import android.os.Handler
import okhttp3.*
import org.json.JSONObject
import site.lilpig.gayhub.bookcrawler.SomeUtils
import site.lilpig.gayhub.bookcrawler.core.Book
import java.io.IOException
import java.lang.Exception

private val STATE_SUCCESS = 0
private val STATE_FAILD = 1
class ISBNGetter(private val callback:(isSuccess: Boolean, book: Book?) -> Unit) {

    val handler: Handler = Handler{
        when(it.what){
            STATE_SUCCESS -> {
                callback(true, it.obj as Book)
            }
            STATE_FAILD -> {
                callback(false,null)
            }
        }

        false
    }

    fun getBookByISDN(isbn:String){
        val client = OkHttpClient()
        val req = Request.Builder()
            .url("https://isbn.qiaohaoforever.cn/${isbn}")
            .build()
        client.newCall(req).enqueue(object : Callback{
            override fun onResponse(p0: Call, p1: Response) {
                var book: Book? = null
                try {
                    val jsonString = p1.body()?.string()
                    val bookJson = JSONObject(jsonString)
                    book = Book(bookJson.getString("title"),bookJson.getString("author"),bookJson.getString("summary"),bookJson.getString("image"),
                        arrayOf(),
                        arrayOf(),"")
                }catch (e: Exception){

                }
                val message = handler.obtainMessage()
                message.what = if (book == null) STATE_FAILD else STATE_SUCCESS
                message.obj = book
                handler.sendMessage(message)
            }

            override fun onFailure(p0: Call, p1: IOException) {
                val message = handler.obtainMessage()
                message.what = STATE_FAILD
                handler.sendMessage(message)
            }

        })
    }
}