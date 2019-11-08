package site.lilpig.gayhub.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_setting.*
import site.lilpig.gayhub.R
import site.lilpig.gayhub.adapter.SettingItem
import site.lilpig.gayhub.adapter.SettingListAdapter
import site.lilpig.gayhub.app

class SettingActivity : AppCompatActivity(){
    val settingList = listOf<SettingItem>(
        SettingItem(title = "工大图书馆",subTitle = "开启后返回结果中会有工大图书馆中的藏书情况",isCheckedbox = true,checkedBoxDefaultValue = app?.isGetBookFromLNTULibrary() ?: false),
        SettingItem(title = "关于软件"),
        SettingItem(title = "开源相关")
    )
    val adapter = SettingListAdapter(this,settingList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        adapter.onItemCheckedListener = { i: Int, b: Boolean ->
            when(i){
                settingList[0].settingId -> {
                    app?.setIsGetBookFromLNTULibrary(b)
                }
            }
        }

        adapter.onItemClickListener = {
            when(it){
                settingList[1].settingId -> {
                    val dialog = AlertDialog.Builder(this,R.style.CustomDialog)
                        .setTitle("关于软件")
                        .setMessage("闪电搜书 V1.0.0\n\n一款能轻松从互联网上检索免费电子书籍的软件，本软件不提供书籍存储和书籍阅读功能，搜索结果中的书籍来自互联网。")
                        .setPositiveButton("我知道了", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.dismiss()
                        })
                    dialog.show()
                }
                settingList[2].settingId -> {
                    val dialog = AlertDialog.Builder(this,R.style.CustomDialog)
                        .setTitle("开源相关")
                        .setMessage("软件遵循GPL3.0开源协议开源，不可商用")
                        .setPositiveButton("我知道了", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.dismiss()
                        })
                    dialog.show()
                }
            }
        }

        ast_recycler.layoutManager = LinearLayoutManager(this)
        ast_recycler.adapter = adapter


    }

}