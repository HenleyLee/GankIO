package com.liyunlong.gankio.utils;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.liyunlong.gankio.gank.GankConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 工具类
 *
 * @author liyunlong
 * @date 2018/7/4 10:03
 */
public class Utility {

    /**
     * 获取SharedPreferences对象
     */
    public static SharedPreferences getPreference(Context context) {
        return context.getApplicationContext().
                getSharedPreferences(GankConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * 获得设备屏幕宽度(单位：px)
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 在浏览器中打开一个Url
     */
    public static void openWebKit(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (NullPointerException e) {
            Toast.makeText(context, "Url为空", Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "您的设备没有安装浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将文字复制到剪切板
     */
    public static boolean copy(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
            return true;
        }
        return false;
    }

    /**
     * 获取String的MD5值
     */
    public static String getMd5(String content) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bufs = md5.digest(content.getBytes());
        StringBuilder builder = new StringBuilder(40);
        for (byte buf : bufs) {
            if ((buf & 0XFF) >> 4 == 0) {
                builder.append("0").append(Integer.toHexString(buf & 0xff));
            } else {
                builder.append(Integer.toHexString(buf & 0xff));
            }
        }
        return builder.toString();
    }

    /**
     * 启动应用设置页面
     */
    public static void startPackageSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
