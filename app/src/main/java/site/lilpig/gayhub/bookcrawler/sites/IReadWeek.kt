package site.lilpig.gayhub.bookcrawler.sites

import okhttp3.Response
import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.core.*
import java.util.regex.Pattern
import kotlin.math.log

private val SITEURL = "http://www.ireadweek.com"
class IReadWeek(context: Context): ResourceSite(context) {
    val logger = Logger(this.javaClass)

    override fun start() = get("http://www.ireadweek.com/index.php?g=portal&m=search&a=index&keyword=${context.getKeyword()}")

    val callback4book = TaskCallback {
        var body = it ?. body() ?. string() ?: return@TaskCallback
        val doc = Jsoup.parse(body)
        val btns = doc.getElementsByClass("downloads")
        var downloadUrls = Array<String>(btns.size,{
            btns.get(it).attr("href")
        })
        var downloadUrlDescriptions = Array<String>(btns.size,{
            btns.get(it).text()
        })
        val img = SITEURL+doc.getElementsByClass("hanghang-shu-content-img").first().child(0).attr("src")
        val content = doc.getElementsByClass("hanghang-shu-content-font").first().getElementsByTag("p")
        val name = content.get(0).text().split("：")[1]
        val author = content.get(1).text().split("：")[1]
        val sb = StringBuffer()
        for (i in 5..content.size - 1){
            sb.append(content.get(i).text()+"\n")
        }
        context.addBook(Book(name,author,sb.toString(),img,downloadUrls,downloadUrlDescriptions,"周读"))
    }

    override fun callback(): TaskCallback {
        return TaskCallback {
            var body = it ?. body() ?. string() ?: return@TaskCallback
            val pattern = Pattern.compile("<a href=\"(\\/index.php\\?m=article&a=index&id=.*?)\">")
            val matcher = pattern.matcher(body)
            while (matcher.find()){
                val url = matcher.group(1) as String
                context.addTask(get(SITEURL+url),callback4book)
            }
        }
    }


}