package com.gg.baseproject

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.gg.baseproject.bean.User
import com.gg.image.loadCircle
import com.gg.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val user: User by extraDelegate("user", User())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user.userId = "11111111"
        image.loadCircle("https://user-gold-cdn.xitu.io/2018/5/16/16366bf7f9b7bb5d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1")
        image.clickWithTrigger {
            toast("hahah")
            "---".toJson()
            val str = user.toJson()
            Log.d("str", str)
            val newUser = str.toObject<User>()
            Log.d("user", newUser.toString())

        }
        runOnUiThread {

        }
    }

}