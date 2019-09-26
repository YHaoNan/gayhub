package site.lilpig.gayhub.bookcrawler.core

import okhttp3.*
import java.util.concurrent.Callable

class TaskRequester: Callable<Response>{
    private var task: Task

    constructor(task:Task){
        this.task = task
    }
    override fun call(): Response {
        var client = OkHttpClient()
        var request = getRequestFromTask(task)
        return client.newCall(request).execute()
    }


    private fun getRequestFromTask(task: Task): Request {
        var builderOfHeaders = Headers.Builder()
        task.head.keys.forEach {
            builderOfHeaders.add(it,task.head.get(it))
        }

        var builderOfBody = FormBody.Builder()
        task.data.keys.forEach {
            builderOfBody.add(it,task.data.get(it))
        }
        var method = if (task.method == Method.GET) "GET" else "POST"
        var body: FormBody? = if (task.method == Method.GET) null else builderOfBody.build()
        var request = Request.Builder()
            .url(task.url)
            .method(if (task.method == Method.GET) "GET" else "POST", body)
            .headers(builderOfHeaders.build())
            .build()
        return request
    }

}

