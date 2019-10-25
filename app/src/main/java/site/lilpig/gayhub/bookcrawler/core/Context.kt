package site.lilpig.gayhub.bookcrawler.core

interface Context{
    fun addBook(book: Book)
    fun addTask(task: Task, callback: TaskCallback): Boolean
    fun getKeyword(): String
}