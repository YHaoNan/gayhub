# Lightning Search

You can read others version of this document, such as [简体中文](./README-ZHS.md)...

Lightning Search(Gayhub) is a tool can search e-book from internet on Android platfrom.

[To src](https://gitee.com/YHaoNan/gayhub/tree/master/app/src/main)

[Source of the front-end](https://github.com/YHaoNan/lightning-search-fe)

## Add a book spider
In this section, I'll lead you create a simple spider that search the book in the web page of the LNTU library step by step.

All the book spider is in the [site.lilpig.gayhub.bookcrawler.sites]() package. All of these are child class of the ResourceSite. So. Your spider must extend from the ResourceSite. 

```kotlin
class LNTULibrary (context: Context): ResourceSite(context){

    override fun start() {}

    override fun callback() {}

}
```
The constructure method of the `ResourceSite` receive a `Context` object.It's not `android.content.Context`. It's `site.lilpig.gayhub.bookcrawler.core.Context`. There is all the code of this class below:

```kotlin
interface Context{
    fun addBook(book: Book)
    fun addTask(task: Task, callback: TaskCallback): Boolean
    fun getKeyword(): String
}
```

You can add a book, add a task, get search keyword by `context`.

ResoucreSite has two abstract method, `start` return a `Task` object. You can think of a task as a http request. `CrawlerEngine` will handle it shortly. `callback` return a `TaskCallback` object, likewise , you can think of it as a response handler.

Now , we override the start method.
```kotlin
override fun start(): Task{
    return Task(Method.GET,"http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
}
```

We return a `Task` in start method above.The first parameter is request method. It should be `Method.GET` or `Method.POST`. The second parameter is request url. There are two other parameters you can set. They're `head` and `data`. They are HashMap. Defualt to empty hashmap. 


The code above looks very fussy. But we provide a easy way to sovle it.

```kotlin
override fun start() = get("http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
```

`get` method allow you to create a `Task` by url. Likewise , There is a `post` method you can use to create a post request.

Ok~ Now , Let's see the `callback` method.

`callback` method return a implementation of the `TaskCallback`. Here is the code of `TaskCallback`:

```java
public interface TaskCallback {
    void done(@Nullable Response response);
}
```

The `TaskCallback` you return in the `callback` method will called when `CrawlerEngine` processed the task you return in the `start` method.

Let's try it.

```kotlin
override fun callback() = TaskCallback {
    if (it!=null){
        System.out.println(it.body?.string())
    }
}
```

If anything correct, you'll see the html code of the LNTU library in your console.

`response` maybe null, so you must check it in your code.

Finally, don't write any platform related code in your spider to make sure your spider can run correctly on other platform.

Here is all the code of LNTU library spider:
```kotlin
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

```


