package site.lilpig.gayhub.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import site.lilpig.gayhub.R
import site.lilpig.gayhub.bookcrawler.core.Book

class BookAdapter(val context: Context, val datas: ArrayList<Book>): BaseAdapter(){

    class ViewHolder{
        lateinit var covor: ImageView
        lateinit var name: TextView
        lateinit var author: TextView
        lateinit var description: TextView
        lateinit var from: TextView
        lateinit var downloadGroup: RadioGroup
    }
    fun addBook(book: Book){
        datas.add(book)
        notifyDataSetChanged()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1?:LayoutInflater.from(context).inflate(R.layout.item_booklist,null)
        var vh: ViewHolder = if (p1 == null){
            var holder = ViewHolder()
            holder.name = view.findViewById(R.id.ibl_name)
            holder.author = view.findViewById(R.id.ibl_author)
            holder.covor = view.findViewById(R.id.ibl_img)
            holder.description = view.findViewById(R.id.ibl_description)
            holder.downloadGroup = view.findViewById(R.id.ibl_downloads)
            holder.from = view.findViewById(R.id.ibl_from)
            view.tag = holder
            holder
        }else view.getTag() as ViewHolder

        var book = datas.get(p0)
        vh.name.text = book.name
        vh.from.text = book.from
        vh.author.text = book.author
        Glide.with(context).load(book.covorUrl).into(vh.covor)
        vh.description.text = book.description
        var i = 0
        if (vh.downloadGroup.getTag() == null){
            book.downloadUrls.forEach {
                var link = it
                var radio = RadioButton(context)
                radio.text = book.downloadUrlDescriptions[i++]
                radio.setOnClickListener({
                    var uri = Uri.parse(link)
                    var intent = Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent)
                })
                radio.buttonDrawable = null
                vh.downloadGroup.addView(radio)
            }
            vh.downloadGroup.setTag(true)
        }

        return view

    }

    override fun getItem(p0: Int): Any {
        return datas.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return datas.size
    }

}