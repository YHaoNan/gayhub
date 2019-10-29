package site.lilpig.gayhub

import android.app.Application

var app: GayhubApplication? = null
class GayhubApplication: Application() {
    private val isbnSearched = mutableSetOf<String>()
    override fun onCreate() {
        super.onCreate()
        app = this
    }
    fun addISBNToSearchedSet(isbn: String) = isbnSearched.add(isbn)
    fun isISBNSearched(isbn:String) = isbnSearched.contains(isbn)
}