# 闪电搜书
你可以阅读该文档的其他版本：[English](./README.md)

闪电搜书原Gayhub是一款Android平台下的搜书工具。

[到src](https://gitee.com/YHaoNan/gayhub/tree/master/app/src/main)

[前端源码](https://github.com/YHaoNan/lightning-search-fe)

## 添加书籍爬虫
在这个部分，我将带领你一步步的创建一个简单的，在工大图书管的网页版中搜索书籍的爬虫。

所有的书籍爬虫都在[site.lilpig.gayhub.bookcrawler.sites]()包中。它们都是ResourceSite的子类。所以，你的爬虫必须继承自ResourceSite。

```kotlin
class LNTULibrary (context: Context): ResourceSite(context){

    override fun start() {}

    override fun callback() {}

}
```

`ResourceSite`的构造方法接收一个`Context`对象，不过不是`android.content.Context`,而是`site.lilpig.gayhub.bookcrawler.core.Context`。以下是这个类的代码：

```kotlin
interface Context{
    fun addBook(book: Book)
    fun addTask(task: Task, callback: TaskCallback): Boolean
    fun getKeyword(): String
}
```

你可以通过`context`添加一本书籍、一个任务，或者获取用户搜索的关键字。

ResourceSite有两个抽象方法，`start`返回一个`Task`对象。你可以把它看做一个http请求。`CrawlerEngine`稍后会处理它。`callback`返回一个`TaskCallback`对象，同样的，你可以把它看做一个响应处理器。

现在，我们重写start方法。
```kotlin
override fun start(): Task{
    return Task(Method.GET,"http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
}
```

我们在上面的`start`方法中返回了一个`Task`。第一个参数是请求方法，可以是`Method.GET`或`Method.POST`。第二个参数是请求URL。你还可以设置另外两个参数，分别是`head`和`data`，它们都是HashMap，默认为空hashmap。

上面的代码看起来很繁琐，但我们提供了一个简单的方式。

```kotlin
override fun start() = get("http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
```

`get`方法允许你通过url创建一个`Task`，同样的，还有一个`post`方法你可以用来创建post请求。

好的~现在，我们看看`callback`方法。

`callback`方法返回一个`TaskCallback`的实现。这是`TaskCallback`的代码：

```java
public interface TaskCallback {
    void done(@Nullable Response response);
}
```

你在`callback`方法中返回的`TaskCallback`将在`CrawlerEngine`处理完你在`start`方法中返回的`Task`后被调用。

我们试试。


```kotlin
override fun callback() = TaskCallback {
    if (it!=null){
        System.out.println(it.body?.string())
    }
}
```

如果一切正常，你将在控制台看到辽宁工大图书馆的html代码。

`response`可能是null，所以你必须在代码里检查它。

最后，不要在你的spider中写任何平台相关的代码以保证你的spider可以在其他平台上正确运行。

下面是辽宁工大图书馆爬虫的所有代码：
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

