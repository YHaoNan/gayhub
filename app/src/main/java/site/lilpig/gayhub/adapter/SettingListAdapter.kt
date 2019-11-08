package site.lilpig.gayhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_setting.view.*
import site.lilpig.gayhub.R

var _currentId : Int = 0
var currentId : Int = _currentId
    @Synchronized get() = ++_currentId

data class SettingItem(val settingId:Int = currentId,val title: String,val subTitle:String?=null,val isCheckedbox: Boolean = false,val checkedBoxDefaultValue: Boolean  = false){}

class SettingListAdapter(val context: Context,val items: List<SettingItem>) : RecyclerView.Adapter<SettingListAdapter.SettingViewHolder>(){

    var onItemCheckedListener: ((id: Int, isChecked: Boolean) -> Unit)? = null
    var onItemClickListener: ((id: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        return SettingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_setting,null))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val currentItem = items[position]

        holder.title.text = currentItem.title
        holder.subTitle.visibility = if (currentItem.subTitle == null) View.GONE else View.VISIBLE
        holder.subTitle.text = currentItem.subTitle

        if (currentItem.isCheckedbox){
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.isChecked = currentItem.checkedBoxDefaultValue
            holder.checkBox.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                onItemCheckedListener?.invoke(currentItem.settingId,b)
            }
            holder.itemView.setOnClickListener {
                if (onItemCheckedListener != null){
                    val isChecked = holder.checkBox.isChecked
                    onItemCheckedListener?.invoke(currentItem.settingId,!isChecked)
                    holder.checkBox.isChecked = !isChecked
                }
            }
        }else{
            holder.checkBox.visibility = View.GONE
            holder.itemView.setOnClickListener{
                onItemClickListener?.invoke(currentItem.settingId)
            }
        }
    }

    inner class SettingViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView
        val subTitle: TextView
        val checkBox: Switch
        init {
            title = view.findViewById(R.id.isb_title)
            subTitle = view.findViewById(R.id.isb_subtitle)
            checkBox = view.findViewById(R.id.isb_checkbox)
        }

    }
}