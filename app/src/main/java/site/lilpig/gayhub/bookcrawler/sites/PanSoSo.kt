package site.lilpig.gayhub.bookcrawler.sites

import android.util.Log
import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.SomeUtils
import site.lilpig.gayhub.bookcrawler.core.*

class PanSoSo(context: Context): ResourceSite(context){
    override fun start() = get("http://www.pansoso.com/zh/"+context.getKeyword(), hashMapOf(SomeUtils.CHROME_USER_AGENT))

    override fun callback() = TaskCallback {
        if (it != null){
            val document = Jsoup.parse(it.body()?.string())
            val psss = document.getElementsByClass("pss")
            for (pss in psss){
                val a = pss.getElementsByTag("a").first()
                val name = a.text()
                if (name.indexOf("pdf")!=-1 || name.indexOf("mobi")!=-1 || name.indexOf("txt") != -1 || name.indexOf("epub") != -1 || name.indexOf("awz3")!=-1){
                    val downloadUrls = arrayOf(a.attr("href"))
                    val downloadUrlsDes = arrayOf("去盘搜搜")
                    val des = pss.getElementsByClass("des").first().text()
                    context.addBook(Book(name,"Unknown",des,"",downloadUrls,downloadUrlsDes,"盘搜搜"))
                }
            }
        }

    }

}