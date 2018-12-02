package com.gg.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.gg.image.config.GlideApp
import com.gg.image.config.GlideRequest
import com.gg.utils.dip2px
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *  Creator : GG
 *  Time    : 2017/10/16
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */

fun ImageView.loadUrl(url: String?) {
    get(url)
//                .placeholder(R.drawable.shape_default_rec_bg)
//                .error(R.drawable.shape_default_rec_bg)
            .into(this)
}


fun ImageView.loadRound(url: String?, centerCrop: Boolean = false) {
    get(url).apply {
        //            .placeholder(R.drawable.shape_default_round_bg)
//                .error(R.drawable.shape_default_round_bg)
        if (centerCrop)
            transform(RoundedCornersTransformation(context.dip2px(10), 0))

    }
            .into(this)

}


/**
 * 占位符圆形
 */
fun ImageView.loadCircle(url: Drawable?) {
    get(url)
//                .placeholder(R.drawable.shape_default_circle_bg)
            .apply(RequestOptions.circleCropTransform())
//                .error(R.drawable.shape_default_circle_bg)
            .into(this)
}


fun ImageView.loadCircle(url: String?) {
    get(url)
//                .placeholder(R.drawable.shape_default_circle_bg)
//                .error(R.drawable.shape_default_circle_bg)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
}

fun ImageView.get(url: String?): GlideRequest<Drawable> = GlideApp.with(context).load(url)


fun ImageView.get(url: Drawable?): GlideRequest<Drawable> = GlideApp.with(context).load(url)

