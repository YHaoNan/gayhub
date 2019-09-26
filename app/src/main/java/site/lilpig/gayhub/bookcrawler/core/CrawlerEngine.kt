package site.lilpig.gayhub.bookcrawler.core

import android.util.Log
import site.lilpig.gayhub.bookcrawler.sites.Epubw
import site.lilpig.gayhub.bookcrawler.sites.IReadWeek
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log

class CrawlerEngine(private val keyword:String, private val maxTaskCount: Int,private val callback: OnBookFoundedListener) : Context{

    val logger = Logger(this.javaClass)
    val taskPool: ExecutorService
    val bookList: ArrayList<Book>

    val sites = listOf<ResourceSite>(
        IReadWeek(this),
        Epubw(this)
    )
    init {
        logger.i("Init crawler engine...")
        this.taskPool = Executors.newFixedThreadPool(this.maxTaskCount)
        this.bookList = ArrayList()
    }

    fun start(){
        logger.i("Call default task of sites...")
        for(site in sites){
            addTask(site.start(),site.callback())
        }
    }

    override fun addTask(task: Task, callback: TaskCallback) {
        logger.i("Added a task : ${task.url}")
        var future = taskPool.submit(TaskRequester(task))
        Thread(){
            kotlin.run {
                try {
                    callback.done(future.get())
                }catch (e: Exception){
                    logger.e(e)
                }
            }
        }.start()
    }

    override fun getKeyword(): String {
        return keyword
    }
    @Synchronized override fun addBook(book: Book) {
        this.bookList.add(book)
        logger.i("Added a book : ${book}")
        callback.onFound(book)
    }

}