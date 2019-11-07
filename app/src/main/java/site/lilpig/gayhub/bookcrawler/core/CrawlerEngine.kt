package site.lilpig.gayhub.bookcrawler.core

import android.os.Handler
import android.util.Log
import site.lilpig.gayhub.bookcrawler.sites.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.log

class CrawlerEngine(private val keyword:String, private val maxWaitTime:Long,private val maxTaskCount: Int,private val callback: OnBookFoundedListener) : Context{
    val ADD_BOOK = 0
    val CLOSE = 1

    val handler = Handler{
        when(it.what){
            ADD_BOOK ->
                callback.onFound(it.obj as Book)
            CLOSE ->
                callback.onTaskOver()
        }
        false
    }

    val sites =listOf<ResourceSite>(
        IReadWeek(this),
        Epubw(this),
        PanSoSo(this),
        Hejizhan(this),
        Book118(this),
        Jiumo(this),
        LNTULibrary(this)
    )

    val logger = Logger(this.javaClass)

    val taskPool: ExecutorService

    @Volatile
    var isClosed = false

    init {
        logger.v("Init crawler engine...")
        this.taskPool = Executors.newFixedThreadPool(this.maxTaskCount)
    }

    fun start(){
        Timer(true).schedule(object : TimerTask() {
            override fun run() {
                close()
            }
        },maxWaitTime)

        logger.v("Call default task of sites...")
        for(site in sites){
            addTask(site.start(),site.callback())
        }
    }
    fun close(){
        if (!isClosed){
            logger.v("Call close method...")
            taskPool.shutdownNow()
            val message = handler.obtainMessage()
            message.what = CLOSE
            handler.sendMessage(message)
            isClosed = true
        }
    }

    override fun addTask(task: Task, callback: TaskCallback):Boolean {
        if (isClosed)return false
        logger.v("Added a task : ${task.url}")
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
        return true
    }

    override fun getKeyword(): String {
        return keyword
    }
    @Synchronized override fun addBook(book: Book) {
        logger.v("Added a book : ${book}")
        val message = handler.obtainMessage()
        message.what = ADD_BOOK
        message.obj = book
        handler.sendMessage(message)
    }

}