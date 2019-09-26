package site.lilpig.gayhub.bookcrawler.core

abstract class ResourceSite(val context: Context){
    abstract fun start(): Task
    abstract fun callback(): TaskCallback
}