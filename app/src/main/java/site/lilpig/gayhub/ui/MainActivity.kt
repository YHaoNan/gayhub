package site.lilpig.gayhub.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import noclay.im.zxing.activity.CaptureActivity
import site.lilpig.gayhub.R
import site.lilpig.gayhub.app
import site.lilpig.gayhub.isbn_getter.ISBNGetter
import site.lilpig.gayhub.utils.isMatches
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private var clipboardManager: ClipboardManager? = null
    private val REQUEST_SCANED = 666
    private val PATTERN_ISBN = "^\\d{13}\$"
    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility =
             View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        am_search_bar.setOnClickListener{
            startActivity(Intent(this,SearchActivity().javaClass))
        }
        am_quick_bar.setOnClickListener {
            searchByISBN(clipboardManager!!.text.toString())
        }
        am_scan_qr.setOnClickListener{
            if (!hasPermission()){
                AlertDialog.Builder(this)
                    .setTitle("需要授权")
                    .setMessage("如果宁使用扫描ISBN功能的话，那宁得给瓦个权限...")
                    .setNegativeButton("去授权", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                        ActivityCompat.requestPermissions(this,permissions,0)
                    }).show()
            }else
                startActivityForResult(Intent(this,CaptureActivity().javaClass),REQUEST_SCANED)
        }
        am_setting.setOnClickListener {
            startActivity(Intent(this,SettingActivity().javaClass))
        }

    }

    fun hasPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        var hasSomePermissionDenied = false
        permissions.forEach outside@{
            if (ContextCompat.checkSelfPermission(this,it) != PackageManager.PERMISSION_GRANTED){
                hasSomePermissionDenied = true
                return@outside
            }
        }
        return !hasSomePermissionDenied
    }
    private fun searchByISBN(isbn: String){
        app?.addISBNToSearchedSet(isbn)
        am_quick_bar.visibility = View.GONE
        ISBNGetter({isSuccess, book ->
            if (isSuccess){
                BookInfoFromISBNDialog(this,book!!).show()
            }else{
                Toast.makeText(this,"未通过ISBN查询到书籍",Toast.LENGTH_SHORT).show()
            }
        }).getBookByISDN(isbn)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SCANED){
            val bundle = data?.getExtras()
            val scanResult = bundle?.getString("result")
            if (scanResult!= null && scanResult.isMatches(PATTERN_ISBN)){
                searchByISBN(scanResult)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onResume() {
        super.onResume()
        if (clipboardManager == null) clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var text: String = clipboardManager!!.text.toString()
        if (text.isMatches(PATTERN_ISBN) && ! (app?.isISBNSearched(text) ?: false)){
            am_quick_bar.text = "宁复制了ISBN号：${text}，点击查找该书"
            am_quick_bar.visibility = View.VISIBLE
        }
    }
}

