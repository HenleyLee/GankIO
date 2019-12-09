package com.henley.gankio;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;

import androidx.annotation.NonNull;

/**
 * Glide全局配置
 *
 * @author Henley
 * @date 2018/7/13 13:38
 */
@GlideModule
public class GankAppGlideModule extends AppGlideModule {

    private static final String DISK_CACHE_NAME = "image";
    private static final int DISK_SIZE = 200 * 1024 * 1024; // 200MB

    @Override
    public boolean isManifestParsingEnabled() {
        return false; // 禁用清单解析
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        // 全局设置ViewTaget的tagId
        ViewTarget.setTagId(R.id.glide_tag_id);

        // 设置磁盘缓存大小和位置
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, DISK_CACHE_NAME, DISK_SIZE)); // 磁盘缓存

        // 设置内存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .setBitmapPoolScreens(3)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize())); // 设置缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize())); // 设置图片池大小

        //设置图片格式
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .disallowHardwareConfig()); // 设置图片格式(默认为PREFER_RGB_565)
        // 设置日志级别
        builder.setLogLevel(Log.INFO);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }

}
