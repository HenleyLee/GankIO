package com.henley.gankio.adapter;

import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.henley.gankio.GlideApp;
import com.henley.gankio.R;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.listener.OnGankDailyItemClickListener;
import com.henley.gankio.utils.Utility;
import com.henley.gankio.utils.ViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * 每日干货数据Adapter
 *
 * @author Henley
 * @date 2018/7/5 10:12
 */
public class GankDailyAdapter extends CommonAdapter<List<GankEntity>> {

    private OnGankDailyItemClickListener mListener;
    private int padding;
    private LayoutInflater inflater;

    public GankDailyAdapter(Collection<List<GankEntity>> datas) {
        super(datas);
    }

    public void setOnGankDailyItemClickListener(OnGankDailyItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_daily;
    }

    @Override
    public void convert(ViewHolder holder, List<GankEntity> data, int position) {
        if (data != null && !data.isEmpty()) {
            if (inflater == null) {
                padding = Utility.dp2px(getContext(), 10);
                inflater = LayoutInflater.from(getContext());
            }
            String gankType = data.get(0).getType();
            int gankTypeColor = ContextCompat.getColor(getContext(), GankConfig.getTypeColor(gankType));
            holder.setText(R.id.gank_daily_title, gankType);
            holder.setTextColor(R.id.gank_daily_title, gankTypeColor);
            LinearLayout container = holder.getView(R.id.gank_daily_container);
            container.removeAllViews();
            View itemView;
            for (final GankEntity gankEntity : data) {
                String type = gankEntity.getType();
                if (TextUtils.equals(type, GankType.Welfare.getName())) {
                    itemView = generateImageView(gankEntity);
                } else {
                    itemView = generateTextView(gankEntity);
                }
                itemView.setPadding(padding, padding, padding, padding);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onGankDailyItemClick(v, gankEntity);
                        }
                    }
                });
                container.addView(itemView);
            }
        }
    }

    private ImageView generateImageView(GankEntity gankEntity) {
        ImageView imageView = (ImageView) inflater.inflate(R.layout.layout_view_image, null);
        GlideApp.with(getContext())
                .asBitmap()
                .load(gankEntity.getUrl())
                .apply(new RequestOptions()
                        .centerInside()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imageView);
        return imageView;
    }

    private TextView generateTextView(GankEntity gankEntity) {
        TextView textView = (TextView) inflater.inflate(R.layout.layout_view_text, null);
        String content = gankEntity.getTitle() + "  (" + gankEntity.getAuthor() + ")";
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new TextAppearanceSpan(getContext(), R.style.AuthorStyle), gankEntity.getTitle().length(), content.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(builder);
        return textView;
    }

}
