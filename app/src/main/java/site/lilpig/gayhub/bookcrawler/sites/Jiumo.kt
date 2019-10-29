package site.lilpig.gayhub.bookcrawler.sites

import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.core.*
import java.util.*

class Jiumo(context: Context): ResourceSite(context){

    fun callback2() = TaskCallback {
        if (it!=null){
            val json = JSONObject(it.body()?.string())
            val sources = json.getJSONArray("sources")
            for (i in 0..sources.length()){
                val source = sources.getJSONObject(i)
                val data = source.getJSONObject("details").getJSONArray("data")
                for (j in 0..data.length()){
                    val bookJson = data.getJSONObject(j)
                    val name = Jsoup.parse(bookJson.getString("title")).text()
                    val des = bookJson.getString("des")
                    val url = arrayOf(bookJson.getString("link"))
                    val urldes = arrayOf("去看看")
                    context.addBook(Book(name,"Unknown",des,"",url,urldes,"鸠摩"))
                }
            }
        }
    }

    override fun start() = post("https://www.jiumodiary.com/init_hubs.php",data = hashMapOf(
        "q" to context.getKeyword(),
        "remote_ip" to "",
        "time_int" to Date().time.toString()
    ))

    override fun callback() = TaskCallback {
        if(it!=null){
            val json = JSONObject(it.body()?.string())
            val id = json.getString("id")
            context.addTask(
                post("https://www2.jiumodiary.com/ajax_fetch_hubs.php",data = hashMapOf("id" to id,"set" to "0")),
                callback2()
            )
        }
    }

}