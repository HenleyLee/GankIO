package com.henley.gankio.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henley.gankio.http.HttpException;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPViewHelper;
import com.henley.gankio.mvp.IPresenter;
import com.henley.gankio.mvp.MVPViewHelper;
import com.henley.gankio.statuslayout.OnRetryActionListener;
import com.henley.gankio.statuslayout.StatusLayoutManager;
import com.henley.gankio.utils.ReflexHelper;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Fragment基类
 *
 * @author Henley
 * @date 2018/7/3 17:14
 */
public abstract class BaseFragment<Presenter extends IPresenter> extends Fragment implements IMVPViewHelper, OnRetryActionListener {

    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity activity;
    protected View rootView;
    private boolean isViewCreated; // rootView是否初始化的标志，防止回调函数在rootView为空的时候触发
    protected boolean isFragmentVisible; // 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
    private MVPViewHelper mvpViewHelper;
    protected Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            handleBundle(bundle); // 处理Bundle(主要用来获取其中携带的参数)
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (rootView == null) {
            activity = (BaseActivity) getActivity();
            int contentLayoutID = getContentLayoutID();
            if (contentLayoutID == 0) {
                return super.onCreateView(inflater, container, savedInstanceState);
            } else {
                rootView = inflater.inflate(contentLayoutID, null);
                initTitleBar(rootView);         // 初始化titleBar
                initViews(rootView);            // 初始化View
                initComponents();               // 初始化系统组件(广播接收器和服务)
            }
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
            // java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewCreated) { // 判断rootView是否已经初始化
            isViewCreated = true;
            if (getUserVisibleHint()) {
                isFragmentVisible = true;
                onFragmentVisibleChange(true);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }
        if (isVisibleToUser) {
            isFragmentVisible = true;
            onFragmentVisibleChange(true);
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i(this.getClass().getName());
    }

    @Override
    public Context getContext() {
        Context context = super.getContext();
        if (context == null) {
            context = getActivity();
        }
        if (context == null && rootView != null) {
            context = rootView.getContext();
        }
        if (context == null && getContentView() != null) {
            context = getContentView().getContext();
        }
        return context != null ? context : super.getContext();
    }

    public MVPViewHelper getMvpViewHelper() {
        if (mvpViewHelper == null) {
            mvpViewHelper = new MVPViewHelper(getContext());
        }
        return mvpViewHelper;
    }

    /**
     * 返回Presenter对象
     */
    public Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter == null) {
            throw new NullPointerException("Please check the generic Fragment.");
        }
        return mPresenter;
    }

    /**
     * 返回根布局View
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 判断当前Fragment的根布局是否初始化
     */
    public boolean isViewCreated() {
        return isViewCreated;
    }

    /**
     * 判断当前Fragment是否可见
     */
    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * 处理Bundle(主要用来获取其中携带的参数)
     */
    protected void handleBundle(@NonNull Bundle bundle) {
    }

    /**
     * 初始化titleBar(需要调用{@link Fragment#setHasOptionsMenu(boolean)})
     */
    protected void initTitleBar(View rootView) {
    }

    /**
     * 加载Fragment的布局
     */
    protected abstract int getContentLayoutID();

    /**
     * 初始化View
     */
    protected abstract void initViews(View rootView);

    /**
     * 检查创建StatusLayoutManager实例并添加到MVPViewHelper中
     */
    protected void checkStatusLayoutManager() {
        if (getMvpViewHelper().getStatusLayoutManager() == null && getContentView() != null) {
            getMvpViewHelper().setStatusLayoutManager(createStatusLayoutManager());
        }
    }

    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 创建Presenter实例
     */
    protected Presenter createPresenter() {
        Presenter presenter = ReflexHelper.getTypeInstance(this, 0);
        if (presenter != null && presenter instanceof BasePresenter) {
            presenter.attachView(this);
        }
        return presenter;
    }

    /**
     * 创建StatusLayoutManager实例(内容布局必须有父布局，不能是Fragment的根布局)
     */
    protected StatusLayoutManager createStatusLayoutManager() {
        Context context = getContext();
        if (context == null) {
            context = getContentView().getContext();
        }
        return StatusLayoutManager.create(context)
                .setContentLayout(getContentView())
                .setOnRetryActionListener(this);
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
     * 查找View
     */
    protected <ViewType extends View> ViewType findViewById(@IdRes int id) {
        return findViewById(rootView, id);
    }

    /**
     * 查找View
     */
    protected <ViewType extends View> ViewType findViewById(View view, @IdRes int id) {
        return view.findViewById(id);
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
     * 当前{@link #Fragment}可见状态发生变化时会回调该方法
     * <br/>如果当前{@link #Fragment}是第一次加载，等待{@link #onViewCreated(View, Bundle)}后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     *
     * @param isVisible true表示{@link #Fragment}有不可见变为可见；false表示{@link #Fragment}有可见变为不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: " + this.getClass().getName());
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
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
