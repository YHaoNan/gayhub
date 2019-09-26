package site.lilpig.gayhub.bookcrawler.core

data class Book(val name: String, val author: String, val description: String, val covorUrl: String,
                val downloadUrls: Array<String>, val downloadUrlDescriptions: Array<String>, val from: String)


