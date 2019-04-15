package com.henley.gankio.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.henley.gankio.GlideApp;
import com.henley.gankio.R;
import com.henley.gankio.entity.IdleReadingEntity;
import com.henley.gankio.utils.Utility;

/**
 * @author Henley
 * @date 2018/7/16 11:05
 */
public class IdleReadingCategoryView extends LinearLayout {

    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDesc;

    public IdleReadingCategoryView(Context context) {
        this(context, null);
    }

    public IdleReadingCategoryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IdleReadingCategoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        int padding = Utility.dp2px(getContext(), 10);
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);
        this.setPadding(padding, padding, padding, padding);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_view_idle_reading_category, this);
        ivIcon = rootView.findViewById(R.id.category_icon);
        tvName = rootView.findViewById(R.id.category_name);
        tvDesc = rootView.findViewById(R.id.category_desc);
    }

    public void updateSite(IdleReadingEntity.Site site) {
        tvName.setText(site.getName());
        tvDesc.setText(site.getDesc());
        GlideApp.with(getContext())
                .asBitmap()
                .load(site.getIcon())
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .into(new BitmapImageViewTarget(ivIcon));
    }


}
