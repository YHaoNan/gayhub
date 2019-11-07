package site.lilpig.gayhub.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import site.lilpig.gayhub.R
import site.lilpig.gayhub.bookcrawler.core.Book
import site.lilpig.gayhub.utils.ResourceSiteUtils
import site.lilpig.gayhub.utils.similarity
import java.util.regex.Pattern

class BookListAdapter(val keyword: String, val context: Context, val datas: ArrayList<Book>) : RecyclerView.Adapter<BookViewHolder>(){
    val similarityArray = ArrayList<Float>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookViewHolder(LayoutInflater.from(context).inflate(R.layout.item_booklist,parent,false))

    var onItemClickListener: AdapterView.OnItemClickListener? = null
    var onItemLongClickListener: AdapterView.OnItemLongClickListener? = null

    @Synchronized fun addBook(book: Book){
        val similarity = if(book.from == "工大图书馆") -1f else book.name.similarity(keyword) * ResourceSiteUtils.weightMap[book.from]!!
        Log.i("BookListAdapter","keyword: "+keyword+", bookname: "+book.name+", from: "+book.from+", similarity: "+similarity)
        val i = findPosition(similarity)
        Log.i("BookListAdapter",similarityArray.toString())
        datas.add(i,book)
        notifyItemInserted(i)
        notifyItemRangeChanged(i,datas.size-i)
    }
    private fun findPosition(v: Float): Int {
        if (similarityArray.size == 0){
            similarityArray.add(0,v)
            return 0
        }
        for(index in 0..similarityArray.size - 1){
            val curS=similarityArray[index]
            if (v<=curS){
                similarityArray.add(index,v)
                return index
            }else if(index==similarityArray.size - 1){
                similarityArray.add(index+1,v)
                return index+1
            }
        }
        return 0
    }
    @Synchronized fun getBook(pos: Int) = datas.get(pos)

    @Synchronized override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = datas[position]
        val booknameSpannable = SpannableString(book.name)
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#ef9056"))
        val pattern = Pattern.compile(keyword.toLowerCase())
        val matcher = pattern.matcher(book.name.toLowerCase())
        while (matcher.find()){
            booknameSpannable.setSpan(foregroundColorSpan,matcher.start(),matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.bookName.text = booknameSpannable
        holder.bookDescription.text = book.description
        holder.authorAndFrom.text = "${book.author} 来自: ${book.from}"
        holder.libraryFlag.visibility = if (book.from == "工大图书馆") View.VISIBLE else View.GONE
        Glide.with(context)
            .load(book.covorUrl)
            .apply(RequestOptions().placeholder(R.drawable.nocovor).error(R.drawable.nocovor).centerCrop())
            .into(holder.bookCovor)
        holder.root.setOnClickListener {
            onItemClickListener?.onItemClick(null,holder.root,position,0)
        }
        holder.root.setOnLongClickListener{
            onItemLongClickListener?.onItemLongClick(null,holder.root,position,0)
            true
        }
    }


}
class BookViewHolder(val root : View): RecyclerView.ViewHolder(root){
    val authorAndFrom: TextView
    val bookName: TextView
    val bookCovor: ImageView
    val bookDescription: TextView
    val libraryFlag: ImageView
    init {
        authorAndFrom = root.findViewById(R.id.ibl_author)
        bookName = root.findViewById(R.id.ibl_name)
        bookCovor = root.findViewById(R.id.ibl_img)
        bookDescription = root.findViewById(R.id.ibl_description)
        libraryFlag = root.findViewById(R.id.ibl_library)
    }
}