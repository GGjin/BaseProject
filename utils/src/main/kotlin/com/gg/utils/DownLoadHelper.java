package com.gg.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.anpxd.framelibrary.net.widget.OkHttpFactory;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Creator : GG
 * Time    : 2017/12/11
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class DownLoadHelper {


    public static void downloadImage(Context context, String url) {

        if (!FileUtil.isSdCardAvailable()) {
            Toast.makeText(context, "SD卡未插入", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            downloadAsFile(context, url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(context, R.string.save_image_success, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(context, R.string.save_image_fail, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String filePath) {
                            KLog.w("path", filePath);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            values.put(MediaStore.MediaColumns.DATA, filePath);
                            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(context, R.string.save_image_fail, Toast.LENGTH_SHORT).show();
        }
    }


    public static Observable<String> downloadAsFile(Context context, String url) {
        return downloadAsFile(context, url, FileUtil.getNameFromUrl(url));
    }

    public static Observable<String> downloadAsFile(Context context, String url, String fileName) {
        return Observable.create(subscriber -> {
            OkHttpClient client = OkHttpFactory.INSTANCE.getClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String imagePath = FileUtil.getTempFilePathWithSpecifiedName(context, fileName);
                    InputStream inputStream = response.body().byteStream();
                    OutputStream outputStream = new FileOutputStream(new File(imagePath));

                    int count;
                    byte[] buffer = new byte[1024];
                    while ((count = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, count);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    subscriber.onNext(imagePath);
                    subscriber.onComplete();
                } else {
                    subscriber.onError(new Exception("Unknown Error"));
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public void notify19(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(file);
            scanIntent.setData(contentUri);
            context.sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            context.sendBroadcast(intent);
        }
    }
}
