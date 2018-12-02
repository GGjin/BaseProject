package com.gg.baseproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gg.image.loadCircle
import com.gg.utils.clickWithTrigger
import com.gg.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image.loadCircle("https://user-gold-cdn.xitu.io/2018/5/16/16366bf7f9b7bb5d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1")
        image.clickWithTrigger {
            toast("hahah")
        }
    }
}
