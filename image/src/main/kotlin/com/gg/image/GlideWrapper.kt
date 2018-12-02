package com.gg.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.load.Transformation

/**
 * Creator : GG
 * Time    : 2018/12/2
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

class GlideWrapper {

    var url: String? = null

    var image: ImageView? = null

//    var placeholder: Int = R.drawable.shape_default_rec_bg
//
//    var error: Int = R.drawable.shape_default_rec_bg

    var transform: Transformation<Bitmap>? = null

}

fun load(init: GlideWrapper.() -> Unit) {

    val wrap = GlideWrapper()

    wrap.init()

    execute(wrap)
}

private fun execute(wrap: GlideWrapper) {

    wrap.image?.let {

        var request = it.get(wrap.url)
//                .placeholder(wrap.placeholder).error(wrap.error)

        if (wrap?.transform != null) {

            request.transform(wrap.transform!!)
        }

        request.into(it)

    }

}

