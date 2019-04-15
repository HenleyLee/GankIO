package com.henley.gankio.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 分享工具类
 *
 * @author Henley
 * @date 2018/7/4 13:48
 */
public class ShareHelper {

    /**
     * 分享文本
     *
     * @param context
     * @param content
     */
    public static void shareText(Context context, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享图片
     *
     * @param context
     * @param uri
     */
    public static void shareImage(Context context, Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 根据{@link File}对象通过{@link FileProvider}生成对应的{@link Uri}对象
     *
     * @param context 上下文
     * @param file    File对象
     */
    public static Uri getUriForFileProvider(Context context, File file) {
        String authority = String.format("%s.fileprovider", context.getPackageName());
        return FileProvider.getUriForFile(context, authority, file);
    }

}
