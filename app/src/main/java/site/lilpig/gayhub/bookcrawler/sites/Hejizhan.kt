package site.lilpig.gayhub.bookcrawler.sites

import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.SomeUtils
import site.lilpig.gayhub.bookcrawler.core.*
import kotlin.collections.get

class Hejizhan(context:Context) : ResourceSite(context){
    override fun start() = get("http://www.hejizhan.com/bbs/?kw="+context.getKeyword(),head = hashMapOf(SomeUtils.CHROME_USER_AGENT))

    override fun callback() = TaskCallback {
        if(it != null){
            val doc = Jsoup.parse(it.body()?.string())
            val bookItems = doc.getElementsByClass("forum-list").first().getElementsByTag("li")
            for (book in bookItems){
                val nameA = book.getElementsByClass("title").first().getElementsByTag("a").first()
                val name = nameA.text()
                val downloadUrls = arrayOf("http://www.hejizhan.com"+nameA.attr("href"))
                val downloadUrlDes = arrayOf("去万千合集站")
                val desc = book.text()
                val covor = "http://www.hejizhan.com"+book.getElementsByClass("media").first().getElementsByTag("img").first().attr("src")
                context.addBook(Book(name,"Unknow",desc,covor,downloadUrls,downloadUrlDes,"万千合集站"))
            }
        }
    }

}