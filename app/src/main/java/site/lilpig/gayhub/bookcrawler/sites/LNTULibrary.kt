package site.lilpig.gayhub.bookcrawler.sites

import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.core.*

/**
 * Get result from the library of the lntu.
 *
 * Return the first if have more than one result.
 */
class LNTULibrary (context: Context): ResourceSite(context){
    override fun start() = get("http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")

    override fun callback() = TaskCallback {
        if (it!=null){
            val doc = Jsoup.parse(it.body()?.string())
            val books = doc.getElementsByClass("weui_media_box")
            if(books.size > 0){
                val bookCountE = doc.getElementsByClass("weui_panel_hd").first()
                val firstBook = books.first()
                val covor = firstBook.getElementsByClass("weui_media_appmsg_thumb").first().attr("src")
                val name = firstBook.getElementsByClass("weui_media_title").first().text()
                val desc = firstBook.getElementsByClass("weui_media_desc").first().text() +"\n\n"+ firstBook.getElementsByClass("weui_media_info_meta").first().text() + if (books.size == 1 ) "" else "\n${bookCountE.text()} 点击查看"
                val urls = arrayOf("http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
                val urldes = arrayOf("去官网查看更多")
                context.addBook(Book(name,"Unknown",desc,covor,urls,urldes,"工大图书馆"))
            }
        }
    }
}
