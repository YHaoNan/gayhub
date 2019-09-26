package site.lilpig.gayhub.bookcrawler.core

enum class Method{
    GET,POST
}

fun get(url: String,head: HashMap<String,String> = HashMap()) = Task(Method.GET,url,head=head)
fun post(url: String,head: HashMap<String,String> = HashMap(),data: HashMap<String,String> = HashMap()) = Task(Method.POST,url,data,head)

data class Task constructor(val method: Method, val url:String, val data: HashMap<String,String> = HashMap(), val head: HashMap<String,String> = HashMap()){

}
