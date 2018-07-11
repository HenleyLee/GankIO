package com.liyunlong.gankio.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.mvp.BaseObserver;
import com.liyunlong.gankio.rxjava.transformer.ObservableTransformerAsync;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.ShareHelper;
import com.liyunlong.gankio.utils.Utility;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 图片预览页面
 *
 * @author liyunlong
 * @date 2018/7/4 14:10
 */
public class PictureActivity extends BaseActivity {

    private String mUrl;
    private String mTitle;
    private PhotoView ivPicture;
    private SmartRefreshLayout mRefreshLayout;

    public static void startActivity(Activity activity, String title, String url, View sharedElement) {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(GankConfig.GANK_PAGR_TITLE, title);
        intent.putExtra(GankConfig.GANK_PAGE_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (sharedElement != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElement.setTransitionName(GankConfig.IMAGE_TRANSITION_NAME);
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, sharedElement, GankConfig.IMAGE_TRANSITION_NAME).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_picture;
    }

    @Override
    protected String title() {
        return mTitle;
    }

    @Override
    protected void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
        mUrl = intent.getStringExtra(GankConfig.GANK_PAGE_URL);
        mTitle = intent.getStringExtra(GankConfig.GANK_PAGR_TITLE);
        Logger.i("Url = " + mUrl);
    }

    @Override
    protected View getContentView() {
        return ivPicture;
    }

    @Override
    protected void initViews() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        setRefreshLayout(mRefreshLayout);
        ivPicture = findViewById(R.id.iv_picture);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivPicture.setTransitionName(GankConfig.IMAGE_TRANSITION_NAME);
        }
        ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.picture_save_ask)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chechPermission(R.id.picture_download);
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(mUrl)
                    .apply(new RequestOptions()
                            .centerInside()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .priority(Priority.HIGH)
                            .override(Utility.getScreenWidth(getContext()), Integer.MAX_VALUE)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    )
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(ivPicture);
        } else {
            showNetworkErrorLayout();
        }
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, final int itemId) {
        chechPermission(itemId);
        return super.onMenuItemSelected(menuItem, itemId);
    }

    private void chechPermission(final int itemId) {
        new RxPermissions(getContext())
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new BaseObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) { // 用户已经同意该权限
                            if (itemId == R.id.picture_download) {
                                savePicture();
                            } else if (itemId == R.id.picture_share) {
                                sharePicture();
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {// 用户拒绝了该权限，没有选中『不再询问』
                            Snackbar.make(mRefreshLayout, R.string.permission_denied, Snackbar.LENGTH_SHORT).show();
                        } else {// 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.permission_title)
                                    .setMessage(R.string.permission_message)
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Utility.startPackageSettings(getContext());
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                });
    }

    private void savePicture() {
        getDownloadPictureObservable()
                .compose(new ObservableTransformerAsync<File>())
                .subscribe(new BaseObserver<File>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Snackbar.make(mRefreshLayout, R.string.download_failed, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(File file) {
                        Snackbar.make(mRefreshLayout, String.format(Locale.getDefault(), getString(R.string.picture_save_to), file.getParentFile().getAbsolutePath()), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void sharePicture() {
        getDownloadPictureObservable()
                .compose(new ObservableTransformerAsync<File>())
                .subscribe(new BaseObserver<File>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Snackbar.make(mRefreshLayout, R.string.download_failed, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(File file) {
                        Uri uri = ShareHelper.getUriForFileProvider(getContext(), file);
                        ShareHelper.shareImage(getContext(), uri);
                    }
                });
    }

    private Observable<File> getDownloadPictureObservable() {
        return Observable.just(mUrl)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String url) throws Exception {
                        return Glide.with(getContext())
                                .asBitmap()
                                .load(url)
                                .apply(new RequestOptions()
                                        .priority(Priority.HIGH)
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                )
                                .submit()
                                .get();
                    }
                })
                .flatMap(new Function<Bitmap, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Bitmap bitmap) throws Exception {
                        File file = saveBitmapToFile(bitmap);
                        if (file == null || !file.exists()) {
                            return Observable.error(new IllegalArgumentException("The picture failed to save."));
                        }
                        return Observable.just(file);
                    }
                });
    }

    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
            throw new IllegalArgumentException("The picture failed to download.");
        }
        File picturesDir = getPicturesDir();
        if (!picturesDir.exists()) {
            if (!picturesDir.mkdir()) {
                throw new IllegalArgumentException("The directory of " + picturesDir.getAbsolutePath() + " failed to create.");
            }
        }
        String fileName = Utility.getMd5(mUrl) + ".jpg";
        File file = new File(picturesDir, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } finally {
            // 通知图库更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
        return file;
    }

    private File getPicturesDir() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GankConfig.TAG);
    }

    @Override
    public void onBackClick() {
        if (ivPicture != null) {
            if (ivPicture.getScale() > 1) {
                ivPicture.setScale(1);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackClick();
        }
    }

}
