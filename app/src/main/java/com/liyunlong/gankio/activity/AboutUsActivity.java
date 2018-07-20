package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.liyunlong.gankio.BuildConfig;
import com.liyunlong.gankio.GlideApp;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.adapter.MultiItemTypeAdapter;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.delegate.ItemViewDelegate;
import com.liyunlong.gankio.listener.OnItemClickListener;
import com.liyunlong.gankio.utils.ShareHelper;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.utils.ViewHolder;
import com.liyunlong.gankio.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于我们页面
 *
 * @author liyunlong
 * @date 2018/7/19 16:18
 */
public class AboutUsActivity extends BaseActivity implements OnItemClickListener {

    private MultiItemTypeAdapter<Object> mAboutAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected String title() {
        return getString(R.string.page_title_about);
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_about;
    }

    @Override
    protected void initViews() {
        RecyclerView recyclerView = findViewById(R.id.about_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(getContext(), R.color.about_item_line)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAboutAdapter = new MultiItemTypeAdapter<>(getAboutItems());
        mAboutAdapter.addItemViewDelegate(new AboutAppInfoDelegate());
        mAboutAdapter.addItemViewDelegate(new AboutCategoryDelegate());
        mAboutAdapter.addItemViewDelegate(new AboutContentDelegate());
        mAboutAdapter.addItemViewDelegate(new AboutDeveloperDelegate());
        mAboutAdapter.addItemViewDelegate(new AboutLicenseDelegate());
        mAboutAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAboutAdapter);
    }

    private List<Object> getAboutItems() {
        List<Object> items = new ArrayList<>();
        items.add(new AppInfo(BuildConfig.VERSION_NAME));
        items.add(new Category(getString(R.string.about_introduce_title)));
        items.add(new Content(new StringBuilder(getString(R.string.about_introduce_content1)).append("\n\n").append(getString(R.string.about_introduce_content2))));
        items.add(new Category(getString(R.string.about_developer_title)));
        items.add(new Developer("lyl873825813", "前端开发者", "https://avatars3.githubusercontent.com/u/11828300?s=100&v=4", "https://github.com/lyl873825813"));
        items.add(new Developer("daimajia", "后端开发者", "https://avatars3.githubusercontent.com/u/2503423?s=100&v=4", "https://github.com/daimajia"));
        items.add(new Category(getString(R.string.about_licenses_title)));
        items.add(new License("Gson", "google", License.APACHE_2, "https://github.com/google/gson"));
        items.add(new License("Glide", "bumptech", License.APACHE_2, "https://github.com/bumptech/glide"));
        items.add(new License("Retrofit", "Square", License.APACHE_2, "https://github.com/square/retrofit"));
        items.add(new License("Okhttp3", "Square", License.APACHE_2, "https://github.com/square/okhttp"));
        items.add(new License("Rxjava2", "ReactiveX", License.APACHE_2, "https://github.com/ReactiveX/RxJava"));
        items.add(new License("RxAndroid", "ReactiveX", License.APACHE_2, "https://github.com/ReactiveX/RxAndroid"));
        items.add(new License("PhotoView", "chrisbanes", License.APACHE_2, "https://github.com/chrisbanes/PhotoView"));
        items.add(new License("RxPermissions", "tbruyelle", License.APACHE_2, "https://github.com/tbruyelle/RxPermissions"));
        items.add(new License("SmartRefreshLayout", "scwang90", License.APACHE_2, "https://github.com/scwang90/SmartRefreshLayout"));
        items.add(new License("Logger", "orhanobut", License.APACHE_2, "https://github.com/orhanobut/logger"));
        return items;
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.about_share) {
            ShareHelper.shareText(getContext(), getString(R.string.share_message));
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        Object item = mAboutAdapter.getItem(adapterPosition);
        if (item instanceof Developer) {
            Utility.openWebKit(getContext(), ((Developer) item).url);
        } else if (item instanceof License) {
            Utility.openWebKit(getContext(), ((License) item).url);
        }
    }

    private static class AppInfo {

        private String versionName;

        private AppInfo(@NonNull String versionName) {
            this.versionName = versionName;
        }
    }

    private static class Category {

        private String value;

        private Category(@NonNull String value) {
            this.value = value;
        }
    }

    private static class Content {

        private CharSequence content;

        private Content(@NonNull CharSequence content) {
            this.content = content;
        }
    }

    private static class Developer {

        private String name;
        private String duty;
        private String header;
        private String url;

        private Developer(@NonNull String author, @NonNull String duty, @NonNull String header, @Nullable String url) {
            this.name = author;
            this.duty = duty;
            this.header = header;
            this.url = url;
        }
    }

    private static class License {

        public static final String MIT = "MIT License";
        public static final String APACHE_2 = "Apache Software License 2.0";
        public static final String GPL_V3 = "GNU general public license Version 3";

        private String name;
        private String author;
        private String type;
        private String url;

        private License() {
        }

        private License(@NonNull String name, @NonNull String author, @NonNull String type, @NonNull String url) {
            this.name = name;
            this.author = author;
            this.type = type;
            this.url = url;
        }
    }

    private static class AboutAppInfoDelegate implements ItemViewDelegate<Object> {

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_about_appinfo;
        }

        @Override
        public boolean isForViewType(Object data, int position) {
            return data instanceof AppInfo;
        }

        @Override
        public void convert(ViewHolder holder, Object data, int position) {
            AppInfo appInfo = (AppInfo) data;
            holder.setText(R.id.appinfo_version, String.format("V%s", appInfo.versionName));
        }
    }

    private static class AboutCategoryDelegate implements ItemViewDelegate<Object> {

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_about_category;
        }

        @Override
        public boolean isForViewType(Object data, int position) {
            return data instanceof Category;
        }

        @Override
        public void convert(ViewHolder holder, Object data, int position) {
            Category category = (Category) data;
            holder.setText(R.id.category_name, category.value);
        }
    }

    private static class AboutContentDelegate implements ItemViewDelegate<Object> {

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_about_content;
        }

        @Override
        public boolean isForViewType(Object data, int position) {
            return data instanceof Content;
        }

        @Override
        public void convert(ViewHolder holder, Object data, int position) {
            Content content = (Content) data;
            holder.setText(R.id.card_content, content.content);
        }
    }

    private static class AboutDeveloperDelegate implements ItemViewDelegate<Object> {

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_about_developer;
        }

        @Override
        public boolean isForViewType(Object data, int position) {
            return data instanceof Developer;
        }

        @Override
        public void convert(ViewHolder holder, Object data, int position) {
            Developer developer = (Developer) data;
            holder.setText(R.id.developer_name, developer.name);
            holder.setText(R.id.developer_duty, developer.duty);
            ImageView ivHeader = holder.getView(R.id.developer_header);
            GlideApp.with(holder.getContext())
                    .asBitmap()
                    .load(developer.header)
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    )
                    .into(new BitmapImageViewTarget(ivHeader));
        }
    }

    private static class AboutLicenseDelegate implements ItemViewDelegate<Object> {

        @Override
        public int getItemLayoutID() {
            return R.layout.layout_item_about_license;
        }

        @Override
        public boolean isForViewType(Object data, int position) {
            return data instanceof License;
        }

        @Override
        public void convert(ViewHolder holder, Object data, int position) {
            License license = (License) data;
            holder.setText(R.id.license_content, license.name + " - " + license.author);
            holder.setText(R.id.license_url, license.url);
            holder.setText(R.id.license_type, license.type);
        }
    }

}
