package site.lilpig.gayhub

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

var app: GayhubApplication? = null
class GayhubApplication: Application() {
    private val isbnSearched = mutableSetOf<String>()
    private lateinit var sp: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        app = this
        sp = getSharedPreferences("lightning", Context.MODE_PRIVATE)
    }
    fun addISBNToSearchedSet(isbn: String) = isbnSearched.add(isbn)
    fun isISBNSearched(isbn:String) = isbnSearched.contains(isbn)

    fun getLastKeyword() = sp.getString("lastSearch",null)
    fun setLastKeyword(keyword: String) = sp.edit().putString("lastSearch",keyword).commit()

    fun isGetBookFromLNTULibrary() = sp.getBoolean("getBookFromLNTULibrary",false)
    fun setIsGetBookFromLNTULibrary(bol: Boolean) = sp.edit().putBoolean("getBookFromLNTULibrary",bol).commit()


}