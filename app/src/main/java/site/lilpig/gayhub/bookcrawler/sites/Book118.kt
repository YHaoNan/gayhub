package site.lilpig.gayhub.bookcrawler.sites

import android.util.Log
import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.core.*
import java.net.URLEncoder
import java.nio.charset.Charset

class Book118(context: Context) : ResourceSite(context){
    override fun start() = get("http://www.book118.com/search.asp?m=2&word="+URLEncoder.encode(context.getKeyword(),"gb2312"))

    private fun toGB2312(string: String): String{
        return String(string.toByteArray(Charsets.UTF_8), Charset.forName("gb2312"))
    }
    override fun callback() = TaskCallback {
        if (it!=null){
            val doc = Jsoup.parse(it?.body()?.string())
            val contT = doc.getElementsByClass("contentT")
            val bookLis = contT.first().getElementsByTag("li")
            for (book in bookLis){
                val a = book.getElementsByTag("a").first()
                val name = a.text()
                val des = book.getElementsByClass("p2").first().text()
                val urls = arrayOf("http://www.book118.com"+a.attr("href"))
                val urldes = arrayOf("去book118")
                val covor = ""
                val book = Book(name,"Unknown",des,covor,urls,urldes,"book118")
                context.addBook(book)
            }
        }
    }

}