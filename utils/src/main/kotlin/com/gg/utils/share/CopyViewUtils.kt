package com.gg.utils.share

import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Environment
import android.util.Log
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri
import android.provider.MediaStore
import java.io.*
import android.graphics.Bitmap.CompressFormat
import android.os.Build
import android.os.Parcelable
import kotlin.collections.ArrayList
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.opengl.ETC1.getWidth
import android.graphics.Color.parseColor
import android.opengl.ETC1.getHeight
import android.widget.ScrollView
import com.socks.library.KLog


/**
 * Creator : GG
 * Date    : 2018/5/24
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
object CopyViewUtils {


    /**
     * 生成图片核心代码
     */
    fun generateBitmap(context: Context, view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        //图片的宽度为屏幕宽度，高度为wrap_content
        view.measure(View.MeasureSpec.makeMeasureSpec(context.resources.displayMetrics.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        //放置mView
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)
        return view.drawingCache
    }


    /**
     * 截取scrollview的屏幕
     * @param scrollView
     * @return
     */
    fun getBitmapByView(scrollView: ScrollView): Bitmap {
        var h = 0
        var bitmap: Bitmap? = null
        // 获取scrollview实际高度
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"))
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h,
                Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }

    fun noticeGallery(context: Context, filePath: String, name: String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver, filePath, name, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$filePath")))
    }


    fun saveImageToGallery(context: Context, bmp: Bitmap?, fileName: String = System.currentTimeMillis().toString() + ".png"): File {

        if (bmp == null)
            throw RuntimeException("传入的bitmap不能为空")

        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "QiYuan" + File.separator + "picture")
        if (!appDir.exists()) {
            if (!appDir.mkdirs())
                KLog.w("创建文件夹失败")
        }

        val file = File(appDir, fileName)
        if (file.exists()) {
            return file
        }

        try {
            val fos = FileOutputStream(file)
            bmp.compress(CompressFormat.PNG, 80, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

//         其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    file.absolutePath, fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // Save the screenshot to the MediaStore
//        val values = ContentValues()
//        val resolver = context.contentResolver
//        values.put(MediaStore.Images.ImageColumns.DATA, file.absolutePath)
//        values.put(MediaStore.Images.ImageColumns.TITLE, fileName)
//        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
//        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png")
//        values.put(MediaStore.Images.ImageColumns.WIDTH, bmp.width)
//        values.put(MediaStore.Images.ImageColumns.HEIGHT, bmp.height)
//        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//
//        val out = resolver.openOutputStream(uri!!)
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
//        out!!.flush()
//        out.close()

        // update file size in the database
//        values.clear()
//        values.put(MediaStore.Images.ImageColumns.SIZE, File(file.absolutePath).length())
//        resolver.update(uri, values, null, null)
        // 最后通知图库更新
//        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://${file.absolutePath}")))
        noticeGallery(context, file.absolutePath, fileName)
        return file
    }


    fun saveImage(bmp: Bitmap, fileName: String = System.currentTimeMillis().toString() + ".png"): File {
        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "QiYuan" + File.separator + "picture")
        val hideDir = File(appDir.absolutePath + File.separator + ".nomedia")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        if (!hideDir.exists()) {
            try {
                hideDir.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val file = File(appDir, fileName)
        if (file.exists()) {
            return file
        }

        try {
            val fos = FileOutputStream(file)
            bmp.compress(CompressFormat.PNG, 80, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    fun hideImage(file: File): Boolean {
        val hideFile = File(file.name.replace(".png", ""))
        return file.renameTo(hideFile)
    }

    fun hideImage(context: Context, uri: Uri): Boolean {
        val file = File(ShareFileUtil.getFileRealPath(context, uri))
        return hideImage(file)
    }


    fun startShareImage(activity: Activity, list: ArrayList<Uri>, description: String = "") {
        Log.w("uri=========", list.toString())
        //过滤出需要分享到对应的平台：微信好友、朋友圈、QQ好友。  可自行修改
        val targetApp = arrayOf("com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mobileqq.activity.JumpActivity")
        /** * 分享图片 */
//        val bitmap = getImageFromAssetsFile(activity, "img_share.jpg")  //从assets目录中取到对应的文件，文件名自行修改
//        val localImage = saveBitmap(bitmap!!, "share.jpg")    //分享前，需要先将图片存在本地（记得添加权限），文件名自行修改
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.type = "image/*" //设置分享内容的类型：图片
        shareIntent.putExtra(Intent.EXTRA_STREAM, list)
        try {
            val resInfo = activity.packageManager.queryIntentActivities(shareIntent, 0)
            if (!resInfo.isEmpty()) {
                val targetedShareIntents = ArrayList<Intent>()
                for (info in resInfo) {
                    val targeted = Intent(Intent.ACTION_SEND_MULTIPLE)
                    targeted.type = "image/*"  //设置分享内容的类型
                    val activityInfo = info.activityInfo
                    //如果还需要分享至其它平台，可以打印出具体信息，然后找到对应的Activity名称，填入上面的数组中即可
//                  println("package = ${activityInfo.packageName}, activity = ${activityInfo.name}")

                    //进行过滤（只显示需要分享的平台）
//                    if (targetApp.any { it == activityInfo.name }) {
                    val comp = ComponentName(activityInfo.packageName, activityInfo.name)
                    targeted.component = comp
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        targeted.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    targeted.putExtra("Kdescription", description)
                    targeted.putExtra(Intent.EXTRA_STREAM, list)
                    targetedShareIntents.add(targeted)
//                    }
                }
                val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "选择要分享到的平台")
                if (chooserIntent != null) {
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
                    activity.startActivity(chooserIntent)
                }
            }
        } catch (e: Exception) {
            Log.e("------", "Unable to share image,  logs : " + e.toString())
        }
    }


    /**
     *
     * @param src 原图片
     * @param watermark  要打的水印图片
     * @return Bitmap 打好水印的图片
     */
    fun createBitmap(src: Bitmap?, watermark: Bitmap?): Bitmap {
        if (src == null) {
            throw RuntimeException("原图片不能为空")
        }
        val srcWidth = src.width
        val srcHeight = src.height

        val waterWidth = watermark!!.width
        val waterHeight = watermark.height
        //create the new blank bitmap
        val newb = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)//创建一个新的和src长度宽度一样的位图
        val cv = Canvas(newb)
        cv.drawBitmap(src, 0f, 0f, null)//在0,0坐标开始画入src
        /*Paint paint = new Paint();
        paint.setColor(Color.RED);*/
        if (watermark != null) {
            cv.drawBitmap(watermark, (srcWidth - waterWidth).toFloat(), (srcHeight - waterHeight).toFloat(), null)//在src的右下解画入水印图片
            //cv.drawText("HELLO",srcWidth-waterWidth,srcHeight-waterHeight, paint);//这是画入水印文字，在画文字时，需要指定paint
        }
        cv.save(Canvas.ALL_SAVE_FLAG)//保存
        cv.restore()//存储
        return newb
    }

    /**
     *	给图片添加水印，返回添加好图片的水印，需要一个已经下载完毕的图片对象
     */
//    public static File Watermark(File file)
//    {
//        //把要添加水印的图片读入到内存中，注意，这里图片不能太大哟。
//        Bitmap bitmap = BitmapFactory . decodeFile (file.getAbsolutePath());
//        //创建画布，因为Android不支持对原图的修改，所以创建一个新的bitmap对象来操作
//        Bitmap newBitmap = Bitmap . createBitmap (bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newBitmap);
//        canvas.drawBitmap(bitmap, 0, 0, null);//把原图画作为背景画在新的画布上
//        // 把要添加的水印图片读到内存中来
//        Bitmap watermarkImg = getWatermarkImg (newBitmap.getWidth() > newBitmap.getHeight()
//        ? newBitmap.getHeight() : newBitmap.getWidth());
//        Paint paint = new Paint();
//        canvas.drawBitmap(watermarkImg, margin
//                , (newBitmap.getHeight() - watermarkImg.getHeight() - margin), paint);
//        //添加文字水印
//        paint.setColor(Color.rgb(0xff, 0xff, 0xff));
//        paint.setTextSize(watermarkImg.getHeight() / 4);
//        canvas.drawText("要添加的水印文字", margin * 2 + watermarkImg.getWidth(),
//                newBitmap.getHeight() - (watermarkImg.getHeight() / 2) - margin, paint);
//        //canvas.restore(); android 6.0 Bug 在github上面有人讨论，如果是android 6.0，使用这个会出问题！
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        //最后，就是把添加水印完成图片写到sd卡中去了
//        try {
//            file.delete(); //删除老的没有水印的图片
//            FileOutputStream fos = new FileOutputStream(file);
//            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//        }
//        return file;
//    }

    fun watermark(src: Bitmap, watermarkBitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawBitmap(src, 0f, 0f, null)
        val watermark = getWatermarkImg(if (newBitmap.width > newBitmap.height) newBitmap.height else newBitmap.width, watermarkBitmap)
        val paint = Paint()
        canvas.drawBitmap(watermark, (newBitmap.width - watermark!!.width).toFloat(), (newBitmap.height - watermark.height).toFloat(), paint)
        canvas.save(Canvas.ALL_SAVE_FLAG)
        canvas.restore()
        return newBitmap
    }


    /**
     * 获取要加载到图片上的水印图片
     *
     * @return
     */
    private fun getWatermarkImg(width: Int, bitmap: Bitmap): Bitmap? {
        var imgWatermark: Bitmap? = null
        try {
            imgWatermark = bitmap
            //将水印图片缩放至图片的7/1宽度的高宽
            val matrix = Matrix()
            val scale = width * 1.0f / 7f / imgWatermark.width.toFloat()
            matrix.postScale(scale, scale, imgWatermark.width * 1.0f / 2, imgWatermark.height * 1.0f / 2)
            imgWatermark = Bitmap.createBitmap(imgWatermark, 0, 0,
                    imgWatermark.width, imgWatermark.height, matrix, true)
        } catch (e: IOException) {
            imgWatermark = null
            e.printStackTrace()
        }

        return imgWatermark
    }


}