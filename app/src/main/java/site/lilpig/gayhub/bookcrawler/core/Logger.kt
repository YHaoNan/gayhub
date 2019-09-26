package site.lilpig.gayhub.bookcrawler.core

import android.util.Log

class Logger<T>(val clz:Class<T>){
    fun v(message: Any){
        Log.v(clz.name,message.toString())
    }
    fun i(message: Any){
        Log.i(clz.name,message.toString())
    }
    fun d(message: Any){
        Log.d(clz.name,message.toString())
    }
    fun w(message: Any){
        Log.w(clz.name,message.toString())
    }

    fun e(message: Any){
        Log.e(clz.name,message.toString())
    }
}