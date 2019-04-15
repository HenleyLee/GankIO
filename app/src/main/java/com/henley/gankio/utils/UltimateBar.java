package com.henley.gankio.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 状态栏工具类
 *
 * @author Henley
 * @date 2018/7/3 17:12
 */
public class UltimateBar {

    private static final int TYPE_COLOR = 1;
    private static final int TYPE_TRANSPARENT = 2;
    private static final int TYPE_IMMERSION = 3;
    private static final int TYPE_DRAWER = 4;
    private static final int TYPE_HIDE = 5;
    private Activity activity;
    private int type;

    private HideBuilder hideBuilder;
    private ColorBuilder colorBuilder;
    private DrawerBuilder drawerBuilder;
    private ImmersionBuilder immersionBuilder;
    private TransparentBuilder transparentBuilder;

    public UltimateBar(Activity activity) {
        this.activity = activity;
    }

    private UltimateBar(Activity activity, ColorBuilder builder) {
        this.activity = activity;
        this.type = TYPE_COLOR;
        this.colorBuilder = builder;
    }

    private UltimateBar(Activity activity, TransparentBuilder builder) {
        this.activity = activity;
        this.type = TYPE_TRANSPARENT;
        this.transparentBuilder = builder;
    }

    private UltimateBar(Activity activity, ImmersionBuilder builder) {
        this.activity = activity;
        this.type = TYPE_IMMERSION;
        this.immersionBuilder = builder;
    }

    private UltimateBar(Activity activity, DrawerBuilder builder) {
        this.activity = activity;
        this.type = TYPE_DRAWER;
        this.drawerBuilder = builder;
    }

    private UltimateBar(Activity activity, HideBuilder builder) {
        this.activity = activity;
        this.type = TYPE_HIDE;
        this.hideBuilder = builder;
    }

    public static ColorBuilder newColorBuilder() {
        return new ColorBuilder();
    }

    public static TransparentBuilder newTransparentBuilder() {
        return new TransparentBuilder();
    }

    public static ImmersionBuilder newImmersionBuilder() {
        return new ImmersionBuilder();
    }

    public static DrawerBuilder newDrawerBuilder() {
        return new DrawerBuilder();
    }

    public static HideBuilder newHideBuilder() {
        return new HideBuilder();
    }

    public void apply() {
        if (type == TYPE_COLOR) {
            if (colorBuilder.darkMode) {
                setStatusBarDarkMode(activity);
            }
            setColorBar(colorBuilder.statusColor, colorBuilder.statusDepth,
                    colorBuilder.applyNav, colorBuilder.navColor, colorBuilder.navDepth);
        } else if (type == TYPE_TRANSPARENT) {
            if (transparentBuilder.darkMode) {
                setStatusBarDarkMode(activity);
            }
            setTransparentBar(transparentBuilder.statusColor, transparentBuilder.statusAlpha,
                    transparentBuilder.applyNav, transparentBuilder.navColor, transparentBuilder.navAlpha);
        } else if (type == TYPE_IMMERSION) {
            if (immersionBuilder.darkMode) {
                setStatusBarDarkMode(activity);
            }
            setTransparentBar(Color.TRANSPARENT, 0, immersionBuilder.applyNav,
                    Color.TRANSPARENT, 0);
        } else if (type == TYPE_DRAWER) {
            if (drawerBuilder.darkMode) {
                setStatusBarDarkMode(activity);
            }
            setColorBarForDrawer(drawerBuilder.statusColor, drawerBuilder.statusDepth,
                    drawerBuilder.applyNav, drawerBuilder.navColor, drawerBuilder.navDepth);
        } else if (type == TYPE_HIDE) {
            setHideBar(hideBuilder.applyNav);
        }
    }

    /**
     * StatusBar and navigationBar color
     *
     * @param statusColor StatusBar color
     * @param statusDepth StatusBar color depth
     * @param navColor    NavigationBar color
     * @param navDepth    NavigationBar color depth
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBar(@ColorInt int statusColor, int statusDepth,
                            @ColorInt int navColor, int navDepth) {
        setColorBar(statusColor, statusDepth, true, navColor, navDepth);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBar(@ColorInt int statusColor, @ColorInt int navColor) {
        setColorBar(statusColor, 0, navColor, 0);
    }


    /**
     * StatusBar and NavigationBar hide
     *
     * @param applyNav apply NavigationBar
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setHideBar(boolean applyNav) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            if (applyNav) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
        }
    }


    /**
     * StatusBar and NavigationBar color
     *
     * @param statusColor StatusBar color
     * @param statusDepth StatusBar color depth
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navDepth    NavigationBar color depth (applyNav = true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setColorBar(@ColorInt int statusColor, int statusDepth, boolean applyNav,
                             @ColorInt int navColor, int navDepth) {
        int realStatusDepth = limitDepthOrAlpha(statusDepth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            window.setStatusBarColor(finalStatusColor);
            if (applyNav) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(finalNavColor);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(createStatusBarView(activity, finalStatusColor));
            if (applyNav && navigationBarExist(activity)) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(activity, finalNavColor));
            }
            setRootView(activity, true);
        }
    }


    /**
     * StatusBar and NavigationBar transparent
     *
     * @param statusColor StatusBar color
     * @param statusAlpha StatusBar alpha
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navAlpha    NavigationBar alpha (applyNav == true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTransparentBar(@ColorInt int statusColor, int statusAlpha, boolean applyNav,
                                   @ColorInt int navColor, int navAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int finalStatusColor = statusColor == 0 ? Color.TRANSPARENT :
                    Color.argb(limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                            Color.green(statusColor), Color.blue(statusColor));
            window.setStatusBarColor(finalStatusColor);
            if (applyNav) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                int finalNavColor = navColor == 0 ? Color.TRANSPARENT :
                        Color.argb(limitDepthOrAlpha(navAlpha), Color.red(navColor),
                                Color.green(navColor), Color.blue(navColor));
                window.setNavigationBarColor(finalNavColor);
            }
            decorView.setSystemUiVisibility(option);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalStatusColor = statusColor == 0 ? Color.TRANSPARENT :
                    Color.argb(limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                            Color.green(statusColor), Color.blue(statusColor));
            decorView.addView(createStatusBarView(activity, finalStatusColor));
            if (applyNav && navigationBarExist(activity)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                int finalNavColor = navColor == 0 ? Color.TRANSPARENT :
                        Color.argb(limitDepthOrAlpha(navAlpha), Color.red(navColor),
                                Color.green(navColor), Color.blue(navColor));
                decorView.addView(createNavBarView(activity, finalNavColor));
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setColorBarForDrawer(@ColorInt int statusColor, int statusDepth, boolean applyNav,
                                      @ColorInt int navColor, int navDepth) {
        int realStatusDepth = limitDepthOrAlpha(statusDepth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            window.setStatusBarColor(Color.TRANSPARENT);

            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            decorView.addView(createStatusBarView(activity, finalStatusColor), 0);
            if (applyNav && navigationBarExist(activity)) {
                window.setNavigationBarColor(Color.TRANSPARENT);
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                decorView.addView(createNavBarView(activity, finalNavColor), 1);
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            decorView.addView(createStatusBarView(activity, finalStatusColor), 0);
            if (applyNav && navigationBarExist(activity)) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                decorView.addView(createNavBarView(activity, finalNavColor), 1);
            }
        }
    }


    private int limitDepthOrAlpha(int depthOrAlpha) {
        if (depthOrAlpha < 0) {
            return 0;
        }
        if (depthOrAlpha > 255) {
            return 255;
        }
        return depthOrAlpha;
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     */
    public static void setStatusBarDarkMode(Activity activity) {
        Window window = activity.getWindow();
        if (Helper.isFlyme4Later()) {
            Helper.setStatusBarDarkModeForFlyme4(window, true);
        } else if (Helper.isMIUI6Later()) {
            Helper.setStatusBarDarkModeForMIUI6(window, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Helper.setStatusBarDarkModeForM(window);
        }
    }

    //------------------------->

    private View createStatusBarView(Context context, @ColorInt int color) {
        View statusBarView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private View createNavBarView(Context context, @ColorInt int color) {
        View navBarView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getNavigationHeight(context));
        params.gravity = Gravity.BOTTOM;
        navBarView.setLayoutParams(params);
        navBarView.setBackgroundColor(color);
        return navBarView;
    }


    private boolean navigationBarExist(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }


    @ColorInt
    private int calculateColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    private void setRootView(Activity activity, boolean fit) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fit);
                ((ViewGroup) childView).setClipToPadding(fit);
            }
        }
    }


    private int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    private int getNavigationHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    public static class ColorBuilder {

        @ColorInt
        private int statusColor;
        private int statusDepth;
        private boolean applyNav;
        private boolean darkMode;

        @ColorInt
        private int navColor;
        private int navDepth;

        public ColorBuilder statusColor(@ColorInt int statusColor) {
            this.statusColor = statusColor;
            return this;
        }

        public ColorBuilder statusDepth(int statusDepth) {
            this.statusDepth = statusDepth;
            return this;
        }

        public ColorBuilder applyNav(boolean applyNav) {
            this.applyNav = applyNav;
            return this;
        }

        public ColorBuilder darkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public ColorBuilder navColor(@ColorInt int navColor) {
            this.navColor = navColor;
            return this;
        }

        public ColorBuilder navDepth(int navDepth) {
            this.navDepth = navDepth;
            return this;
        }

        public UltimateBar build(Activity activity) {
            return new UltimateBar(activity, this);
        }

    }

    public static class TransparentBuilder {

        @ColorInt
        private int statusColor;
        private int statusAlpha;
        private boolean applyNav;
        private boolean darkMode;

        @ColorInt
        private int navColor;
        private int navAlpha;

        public TransparentBuilder statusColor(@ColorInt int statusColor) {
            this.statusColor = statusColor;
            return this;
        }

        public TransparentBuilder statusAlpha(int statusAlpha) {
            this.statusAlpha = statusAlpha;
            return this;
        }

        public TransparentBuilder applyNav(boolean applyNav) {
            this.applyNav = applyNav;
            return this;
        }

        public TransparentBuilder darkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public TransparentBuilder navColor(@ColorInt int navColor) {
            this.navColor = navColor;
            return this;
        }

        public TransparentBuilder navAlpha(int navAlpha) {
            this.navAlpha = navAlpha;
            return this;
        }

        public UltimateBar build(Activity activity) {
            return new UltimateBar(activity, this);
        }

    }

    public static class ImmersionBuilder {

        private boolean applyNav;
        private boolean darkMode;

        public ImmersionBuilder applyNav(boolean applyNav) {
            this.applyNav = applyNav;
            return this;
        }

        public ImmersionBuilder darkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public UltimateBar build(Activity activity) {
            return new UltimateBar(activity, this);
        }

    }

    public static class DrawerBuilder {

        @ColorInt
        private int statusColor;
        private int statusDepth;
        private boolean applyNav;
        private boolean darkMode;

        @ColorInt
        private int navColor;
        private int navDepth;

        public DrawerBuilder statusColor(@ColorInt int statusColor) {
            this.statusColor = statusColor;
            return this;
        }

        public DrawerBuilder statusDepth(int statusDepth) {
            this.statusDepth = statusDepth;
            return this;
        }

        public DrawerBuilder darkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public DrawerBuilder applyNav(boolean applyNav) {
            this.applyNav = applyNav;
            return this;
        }

        public DrawerBuilder navColor(@ColorInt int navColor) {
            this.navColor = navColor;
            return this;
        }

        public DrawerBuilder navDepth(int navDepth) {
            this.navDepth = navDepth;
            return this;
        }

        public UltimateBar build(Activity activity) {
            return new UltimateBar(activity, this);
        }

    }

    public static class HideBuilder {

        private boolean applyNav;

        public HideBuilder applyNav(boolean applyNav) {
            this.applyNav = applyNav;
            return this;
        }

        public UltimateBar build(Activity activity) {
            return new UltimateBar(activity, this);
        }

    }

    private static final class Helper {

        /**
         * 判断是否Flyme4以上
         */
        private static boolean isFlyme4Later() {
            return Build.FINGERPRINT.contains("Flyme_OS_4")
                    || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                    || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
        }

        /**
         * 判断是否为MIUI6以上
         */
        private static boolean isMIUI6Later() {
            try {
                Class<?> clz = Class.forName("android.os.SystemProperties");
                Method mtd = clz.getMethod("get", String.class);
                String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
                val = val.replaceAll("[vV]", "");
                int version = Integer.parseInt(val);
                return version >= 6;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * android 6.0设置字体颜色
         */
        @TargetApi(Build.VERSION_CODES.M)
        private static void setStatusBarDarkModeForM(Window window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }

        /**
         * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
         * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
         */
        private static boolean setStatusBarDarkModeForFlyme4(Window window, boolean dark) {
            boolean result = false;
            if (window != null) {
                try {
                    WindowManager.LayoutParams e = window.getAttributes();
                    Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                    Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                    darkFlag.setAccessible(true);
                    meizuFlags.setAccessible(true);
                    int bit = darkFlag.getInt(null);
                    int value = meizuFlags.getInt(e);
                    if (dark) {
                        value |= bit;
                    } else {
                        value &= ~bit;
                    }

                    meizuFlags.setInt(e, value);
                    window.setAttributes(e);
                    result = true;
                } catch (Exception var8) {
                    Logger.e("StatusBar", "setStatusBarDarkIcon: failed");
                }
            }

            return result;
        }

        /**
         * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
         * http://dev.xiaomi.com/doc/p=4769/
         */
        private static void setStatusBarDarkModeForMIUI6(Window window, boolean darkmode) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
