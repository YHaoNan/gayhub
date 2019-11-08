package site.lilpig.gayhub.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri

object SomeUtil{

    fun openInBrowser(context: Context, url: String){
        var uri = Uri.parse(url)
        var intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
