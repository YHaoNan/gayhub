package site.lilpig.gayhub.ui

import android.content.Context
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class ListDialog(context: Context): BaseDialog(context){
    lateinit var datas: Array<String>
    lateinit var itemClickListener: AdapterView.OnItemClickListener
    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(listView)

    }

    override fun show() {
        if (datas == null) return
        listView = ListView(context)
        var adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,datas)
        listView.adapter = adapter
        listView.onItemClickListener = itemClickListener
        super.show()
    }
}