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

ResoucreSite has two abstract method, `start` return a `Task` object. You can compare a task to a http request. `CrawlerEngine` will handle it shortly. `callback` return a `TaskCallback` object, likewise , you can compare it to a response handler.

Now , we override the start method.
```kotlin
override fun start(): Task{
    return Task(Method.GET,"http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
}
```

The code above looks very fussy. But we provide a easy way to sovle it.

```kotlin
override fun start() = get("http://tsg.lntu.edu.cn/m/weixin/wsearch.php?q=${context.getKeyword()}&t=any")
```

老子有些编不下去了

## Rebuild or use this project in your product
