package com.henley.gankio.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.henley.gankio.R;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPViewHelper;
import com.henley.gankio.mvp.IPresenter;
import com.henley.gankio.mvp.MVPViewHelper;
import com.henley.gankio.statuslayout.OnRetryActionListener;
import com.henley.gankio.statuslayout.StatusLayoutManager;
import com.henley.gankio.utils.ReflexHelper;
import com.henley.gankio.utils.UltimateBar;
import com.henley.gankio.widget.ToolBarHelper;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.disposables.Disposable;

/**
 * Activity基类
 *
 * @author Henley
 * @date 2018/7/3 16:47
 */
public abstract class BaseActivity<Presenter extends IPresenter> extends AppCompatActivity implements IMVPViewHelper, OnRetryActionListener {

    protected final String TAG = this.getClass().getSimpleName();
    protected Menu menu;
    protected BaseActivity mContext;
    protected boolean isDestroyed;
    protected boolean isWindowAttached;
    protected MVPViewHelper mvpViewHelper;
    protected Presenter mPresenter;
    protected ToolBarHelper toolBarHelper;
    private Disposable subscribe;
    private boolean isActivityVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        beforeSuper();                              // 初始化(super.onCreate(savedInstanceState)之前调用)
        super.onCreate(savedInstanceState);
        init(savedInstanceState);                   // 初始化
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);                   // 处理Intent(主要用来获取其中携带的参数)
        }
        if (getContentLayoutId() != 0) {            // 判断是否有布局文件
            setContentView(getContentLayoutId());   // 加载页面布局
            initViews();                            // 初始化View
        }
        initComponents();                           // 初始化系统组件(广播接收器和服务)
        loadData();                                 // 加载数据
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (hideToolBar()) {
            super.setContentView(layoutResID);
            toolBarHelper = new ToolBarHelper(this);
        } else {
            toolBarHelper = new ToolBarHelper(this, layoutResID);
            super.setContentView(toolBarHelper.getContentView());
        }
        toolBarHelper.initTitle(title(), subtitle());
        toolBarHelper.initNavigation(navigationIcon(), hideHomeUp());
        initToolBar(toolBarHelper);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            handleIntent(intent);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isWindowAttached = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() != 0) {
            getMenuInflater().inflate(getMenuRes(), menu);
            this.menu = menu;
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != 0) {
            if (itemId == android.R.id.home) {
                onBackClick();
            } else {
                return onMenuItemSelected(item, itemId);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(this.getClass().getName());
        isActivityVisible = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }

    /**
     * 返回Menu对象
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * 返回上下文
     */
    @Override
    public BaseActivity getContext() {
        return mContext;
    }

    /**
     * 返回Toolbar对象
     */
    public Toolbar getToolBar() {
        if (toolBarHelper != null) {
            return toolBarHelper.getToolBar();
        }
        return null;
    }

    /**
     * 返回Presenter
     */
    protected Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new NullPointerException("Please check the generic Activity.");
        }
        return mPresenter;
    }

    /**
     * 初始化(在{@code super.onCreate(savedInstanceState)}之前调用)
     */
    protected void beforeSuper() {

    }

    /**
     * 初始化
     */
    protected void init(Bundle savedInstanceState) {
        if (useTintStatusBar()) {
            initStatusBar(); // 初始化透明栏效果(国内一般叫沉浸式状态栏)
        }
    }

    public boolean isWindowAttached() {
        return isWindowAttached;
    }

    /**
     * 处理Intent(主要用来获取其中携带的参数，不要在此方法中操作UI)
     */
    protected void handleIntent(@NonNull Intent intent) {
    }

    /**
     * 加载页面布局
     */
    protected abstract int getContentLayoutId();

    /**
     * 加载菜单布局
     */
    protected int getMenuRes() {
        return 0;
    }

    /**
     * 菜单项选择事件
     */
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        return true;
    }

    /**
     * 设置标题
     */
    protected abstract String title();

    /**
     * 设置副标题
     */
    protected String subtitle() {
        return null;
    }

    /**
     * 是否隐藏ToolBar
     */
    protected boolean hideToolBar() {
        return false;
    }

    /**
     * 是否隐藏返回按钮
     */
    protected boolean hideHomeUp() {
        return false;
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean useTintStatusBar() {
        return true;
    }

    /**
     * 初始化透明栏(国内一般叫沉浸式状态栏)效果
     */
    protected void initStatusBar() {
        UltimateBar.newColorBuilder()
                .statusColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                .build(getContext())
                .apply();   // 状态栏颜色
    }

    /**
     * 初始化ToolBar
     */
    protected void initToolBar(ToolBarHelper toolBarHelper) {

    }

    /**
     * 初始化View
     */
    protected abstract void initViews();

    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 检查创建StatusLayoutManager实例并添加到MVPViewHelper中
     */
    protected void checkStatusLayoutManager() {
        if (getMvpViewHelper().getStatusLayoutManager() == null && getContentView() != null) {
            getMvpViewHelper().setStatusLayoutManager(createStatusLayoutManager());
        }
    }

    /**
     * 创建Presenter实例
     */
    @SuppressWarnings("Unchecked")
    protected Presenter createPresenter() {
        Presenter presenter = ReflexHelper.getTypeInstance(this, 0);
        if (presenter != null && presenter instanceof BasePresenter) {
            presenter.attachView(this);
        }
        return presenter;
    }

    /**
     * 创建StatusLayoutManager实例
     */
    protected StatusLayoutManager createStatusLayoutManager() {
        return StatusLayoutManager.create(getContext())
                .setContentLayout(getContentView())
                .setOnRetryActionListener(this);
    }

    public MVPViewHelper getMvpViewHelper() {
        if (mvpViewHelper == null) {
            mvpViewHelper = new MVPViewHelper(getContext());
        }
        return mvpViewHelper;
    }

    /**
     * 获取内容布局(用于创建StatusLayoutManager实例，内容布局必须有父布局，不能是Fragment的根布局)
     */
    protected View getContentView() {
        return null;
    }

    @Override
    public void onRetryAction(View view) {
        loadData();
    }

    /**
     * 将{@link Fragment}添加到{@link BaseActivity}中，同时添加到回退栈中
     */
    public void pushFragmentToBackStack(Fragment fragment) {
        pushFragmentToBackStack(getFragmentContainerId(), fragment);
    }

    /**
     * 将{@link Fragment}添加到{@link BaseActivity}中，同时添加到回退栈中
     */
    public void pushFragmentToBackStack(@IdRes int containerId, Fragment fragment) {
        pushFragmentToBackStack(containerId, fragment, null);
    }

    /**
     * 将{@link Fragment}添加到{@link BaseActivity}中，同时添加到回退栈中
     */
    public void pushFragmentToBackStack(@IdRes int containerId, Fragment fragment, String tag) {
        if (fragment != null) {
            if (tag == null) {
                tag = fragment.getClass().getName();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
            if (fragmentByTag == null) {
                if (fragmentManager.getBackStackEntryCount() == 0) {
                    transaction.add(containerId, fragment, tag);
                } else {
                    transaction.replace(containerId, fragment, tag);
                }
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // Fragment切换效果
                transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            } else {
                fragmentManager.popBackStack(tag, 0); // 弹出在它上面的所有fragment，并显示对应fragment
            }
        }
    }

    /**
     * 将{@link Fragment}弹出回退栈
     */
    public void popFragmentFromBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 返回添加{@link Fragment}的容器Id
     */
    protected int getFragmentContainerId() {
        return 0;
    }

    /**
     * 初始化系统组件(广播接收器和服务)
     */
    protected void initComponents() {
        registerReceivers();
        startServices();
    }

    /**
     * 注册广播接收器
     */
    protected void registerReceivers() {
    }

    /**
     * 开启服务
     */
    protected void startServices() {
    }

    /**
     * 设置Logo
     */
    protected int icon() {
        return 0;
    }

    /**
     * 设置左侧icon
     */
    protected int navigationIcon() {
        return 0;
    }


    @Override
    public final void onBackPressed() {
        onBackClick();
    }

    /**
     * 返回事件处理
     */
    public void onBackClick() {
        super.onBackPressed();
    }

    @Override
    public void onDetachedFromWindow() {
        isWindowAttached = false;
        super.onDetachedFromWindow();
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            return isDestroyed;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceivers();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        isDestroyed = true;
        if (subscribe != null) {
            subscribe.dispose();
        }
        super.onDestroy();
    }

    /**
     * 反注册广播接收器
     */
    protected void unregisterReceivers() {

    }

    @Override
    public void handleException(HttpException exception) {
        getMvpViewHelper().handleException(exception);
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        getMvpViewHelper().setRefreshLayout(refreshLayout);
    }

    @Override
    public void showContentLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showContentLayout();
    }

    @Override
    public void showEmptyLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showEmptyLayout();
    }

    @Override
    public void showLoadingLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showLoadingLayout();
    }

    @Override
    public void showErrorLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showErrorLayout();
    }

    @Override
    public void showNetworkErrorLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showNetworkErrorLayout();
    }

    @Override
    public void showNetworkPoorLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().showNetworkPoorLayout();
    }

    @Override
    public void restoreLayout() {
        checkStatusLayoutManager();
        getMvpViewHelper().restoreLayout();
    }

}
