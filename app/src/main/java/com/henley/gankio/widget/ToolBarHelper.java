package com.henley.gankio.widget;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.henley.gankio.R;

import java.lang.reflect.Field;

/**
 * ToolBar工具类
 *
 * @author Henley
 * @date 2018/7/3 16:58
 */
public class ToolBarHelper {

    /**
     * Toolbar对象
     */
    private AppCompatActivity mActivity;
    /**
     * 视图容器
     */
    private FrameLayout mContentView;
    /**
     * Toolbar对象
     */
    private Toolbar mToolBar;
    private LayoutInflater mInflater;
    /**
     * 两个属性：
     * <br/>1.Toolbar是否悬浮在窗口之上
     * <br/>2.Toolbar的高度获取
     */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };
    private int topMargin;

    /**
     * 构造方法(在{@link AppCompatActivity#setContentView(int)}方法之后调用)
     * <br/>注意：视图容器中需包含{@link Toolbar}，且控件id为toolbar
     */
    public ToolBarHelper(AppCompatActivity activity) {
        this.mActivity = activity;
        this.mToolBar = mActivity.findViewById(R.id.toolbar);
        if (mToolBar != null) {
            mActivity.setSupportActionBar(mToolBar);
            mToolBar.setContentInsetsRelative(0, 0);
        }
    }

    /**
     * 构造方法(在{@link AppCompatActivity#setContentView(int)}方法之前调用)
     * <br/>注意：视图容器中无需包含{@link Toolbar}
     */
    public ToolBarHelper(AppCompatActivity activity, @LayoutRes int layoutId) {
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(mActivity);
        if (layoutId != 0) {
            initAttrs();
            initContentView(); // 初始化视图容器
            initCustomView(mInflater, layoutId); // 初始化自定义布局
        }
        initToolBar(mInflater); // 初始化ToolBar
    }

    /**
     * 初始化视图容器
     */
    private void initContentView() {
        mContentView = new FrameLayout(mActivity);// 直接创建一个帧布局，作为视图容器的父容器
        mContentView.setId(R.id.root_content);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mContentView.setFitsSystemWindows(true);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_toolbar, mContentView);// 通过inflater获取Toolbar的布局文件
        mToolBar = view.findViewById(R.id.toolbar);
        if (mToolBar != null) {
            mActivity.setSupportActionBar(mToolBar);
            mToolBar.setContentInsetsRelative(0, 0);
        }
    }

    /**
     * 初始化自定义布局
     */
    private void initCustomView(LayoutInflater inflater, @LayoutRes int layoutId) {
        View view = inflater.inflate(layoutId, null);// 通过inflater获取自定义的布局文件
        FrameLayout.LayoutParams customParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        customParams.topMargin = topMargin;
        mContentView.addView(view, customParams);// 将自定义布局添加到视图容器中

    }

    private void initAttrs() {
        TypedArray typedArray = mActivity.getTheme().obtainStyledAttributes(ATTRS);
        try {
            int actionBarSize = (int) mActivity.getResources().getDimension(R.dimen.actionBarSize);
            boolean overly = typedArray.getBoolean(0, false); // 获取主题中定义的悬浮标志
            int toolBarSize = (int) typedArray.getDimension(1, actionBarSize); // 获取主题中定义的Toolbar的高度
            topMargin = overly ? 0 : toolBarSize;// 如果是悬浮状态，则不需要设置间距
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * 获取视图容器
     */
    public FrameLayout getContentView() {
        return mContentView;
    }

    /**
     * 获取Toolbar
     */
    public Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * 设置Toolbar的悬浮菜单
     */
    public void showOverflowMenu(int overflowMenuResId) {
        if (mToolBar != null) {
            mToolBar.showOverflowMenu();
            LayoutInflater.from(mActivity).inflate(overflowMenuResId, mToolBar);
        }
    }

    /**
     * 设置Toolbar为白色
     */
    public void setToolbarWhite() {
        if (mToolBar != null) {
            setToolbarSmooth();
            mToolBar.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 设置Toolbar平滑过渡(去掉阴影)
     */
    public void setToolbarSmooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolBar.setElevation(0);
            mToolBar.setTranslationZ(0);
        } else {
            ViewCompat.setElevation(mToolBar, 0);
            ViewCompat.setTranslationZ(mToolBar, 0);
        }
    }

    /**
     * 初始化标题栏
     *
     * @param title    标题
     * @param subtitle 副标题
     */
    public void initTitle(CharSequence title, CharSequence subtitle) {
        if (mToolBar != null) {
            ActionBar actionBar = mActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false); // 是否显示应用程序标题，默认为true
            }
            if (!TextUtils.isEmpty(title)) {
                mToolBar.setTitle(title); // 系统标题样式
                mToolBar.setSubtitle(subtitle);
            }
        }
    }

    /**
     * 初始化NavigationIcon
     *
     * @param navigationResId 导航按钮图标资源id
     * @param hideHomeUp      是否隐藏左上角的图标
     */
    public void initNavigation(int navigationResId, boolean hideHomeUp) {
        if (mToolBar != null) {
            ActionBar actionBar = mActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(!hideHomeUp); // 是否显示应用程序图标，默认为true，对应id为android.R.id.home
                actionBar.setHomeButtonEnabled(!hideHomeUp); // 是否将应用程序图标转变成可点击的按钮，默认为false。如果设置了setDisplayHomeAsUpEnabled为true，则该设置自动为 true。
                // 注意：setHomeButtonEnabled和setDisplayShowHomeEnabled共同起作用
                // 如果setHomeButtonEnabled设成false，即使setDisplayShowHomeEnabled设成true，图标也不能点击
                actionBar.setDisplayHomeAsUpEnabled(!hideHomeUp); // 在应用程序图标的左边显示一个向左的箭头，默认为false。
            }
            if (!hideHomeUp) {
                if (navigationResId != 0) {
                    mToolBar.setNavigationIcon(navigationResId); // 设置导航按钮图标
                } else {
//                    mToolBar.setNavigationIcon(R.drawable.ic_back); // 设置默认导航按钮图标
                }
            }
        }
    }

    /**
     * 设置Toolbar的标题(居中显示)
     */
    public void setToolBarTitle(int titleResId) {
        if (titleResId == 0) {
            return;
        }
        setToolBarTitle(mActivity.getString(titleResId));
    }

    /**
     * 设置Toolbar的标题(居中显示)
     */
    public void setToolBarTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        Toolbar.LayoutParams params = getToolBarLayoutParams();
        params.gravity = Gravity.CENTER;
        TextView tvTtitle = new TextView(mActivity);
        tvTtitle.setGravity(Gravity.CENTER);
        tvTtitle.setTextColor(Color.WHITE);
        tvTtitle.setTextSize(18);
        tvTtitle.setText(title);
        mToolBar.addView(tvTtitle, params);
    }

    /**
     * 设置Toolbar的Logo(居中显示)
     */
    public void setToolBarLogo(int logoResId) {
        if (logoResId == 0) {
            return;
        }
        Toolbar.LayoutParams params = getToolBarLayoutParams();
        params.gravity = Gravity.CENTER;
        params.topMargin = 10;
        params.bottomMargin = 10;
        ImageView ivIcon = new ImageView(mActivity);
        ivIcon.setImageResource(logoResId);
        mToolBar.addView(ivIcon, params);
    }

    /**
     * 设置Toolbar的自定义View
     */
    public void addToolBarCustomView(int layoutResId, int gravity) {
        if (layoutResId == 0) {
            return;
        }
        View view = LayoutInflater.from(mActivity).inflate(layoutResId, null);
        addToolBarCustomView(view, gravity);
    }

    /**
     * 设置Toolbar的自定义View
     */
    public void addToolBarCustomView(View view, int gravity) {
        if (view != null) {
            Toolbar.LayoutParams params = getToolBarLayoutParams();
            if (gravity <= 0) {
                gravity = Gravity.NO_GRAVITY;
            }
            params.gravity = gravity;
            mToolBar.addView(view, params);
        }
    }

    /**
     * 获取标题TextView
     */
    public TextView getTitleTextView() {
        try {
            Field field = Toolbar.class.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            return (TextView) field.get(mToolBar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回Toolbar的LayoutParams对象
     */
    @NonNull
    private static Toolbar.LayoutParams getToolBarLayoutParams() {
        return new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置Toolbar的自定义View
     */
    public static void addToolBarCustomView(Toolbar toolbar, View view, int gravity) {
        if (view != null) {
            Toolbar.LayoutParams params = getToolBarLayoutParams();
            if (gravity <= 0) {
                gravity = Gravity.NO_GRAVITY;
            }
            params.gravity = gravity;
            toolbar.addView(view, params);
        }
    }


}
