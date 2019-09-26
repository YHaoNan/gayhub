package site.lilpig.gayhub.bookcrawler.sites

import org.jsoup.Jsoup
import site.lilpig.gayhub.bookcrawler.core.*
import kotlin.collections.get


class Epubw (context: Context): ResourceSite(context) {
    private val logger = Logger(this.javaClass)

    private val domain = "https://epubw.com"

    override fun start() = get(domain+"/?s="+context.getKeyword())

    override fun callback() = TaskCallback {
        var body = it?.body()?.string()?:return@TaskCallback
        var doc = Jsoup.parse(body)
        var articles = doc.getElementsByClass("equal").first().getElementsByTag("article")
        articles.forEach({
            val url = it.getElementsByClass("thumbnail").first().attr("href")
            val imgUrl = it.getElementsByClass("img-thumbnail").first().attr("src")
            val captions = it.getElementsByClass("caption").first().getElementsByTag("p")
            val name = captions[0].getElementsByTag("a").text()
            val author = captions[1].getElementsByTag("a").text()
            context.addBook(Book(name,author,"No Description",imgUrl, arrayOf(url), arrayOf("去epubw"),"EpubW"))
        })
    }
}