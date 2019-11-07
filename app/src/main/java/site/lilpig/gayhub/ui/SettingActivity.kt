package site.lilpig.gayhub.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import site.lilpig.gayhub.R

class SettingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }
}