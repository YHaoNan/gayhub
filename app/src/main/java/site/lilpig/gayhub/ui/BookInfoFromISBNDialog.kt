package site.lilpig.gayhub.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_bottom_pop.*
import kotlinx.android.synthetic.main.item_booklist.*
import site.lilpig.gayhub.R
import site.lilpig.gayhub.bookcrawler.core.Book


class BookInfoFromISBNDialog(context: Context, private val book: Book) : Dialog(context,R.style.BottomDialogTheme){
    init {
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(R.style.bottom_pop_anim)
        window?.getDecorView()?.setPadding(0, 0, 0, 0)
        val layoutParams = window?.getAttributes()
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.setAttributes(layoutParams)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_bottom_pop)
        dbp_name.text = book.name
        dbp_author.text = "作者：${book.author}"
        Glide.with(context).load(book.covorUrl).into(dbp_img)
        dbp_description.text = book.description
        dbp_description.movementMethod = ScrollingMovementMethod.getInstance()
        dbp_search.setOnClickListener{
            dismiss()
            val intent = Intent(context,SearchActivity().javaClass)
            intent.putExtra("keyword",book.name)
            context.startActivity(intent)
        }
    }


}