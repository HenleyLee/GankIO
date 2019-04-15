package com.henley.gankio.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 安全的Handler(解决Handler导致的内存泄漏)
 *
 * @author Henley
 * @date 2018/7/13 16:38
 */
public final class SafetyHandler extends Handler {

    private final WeakReference<Delegate> mWeakReference;

    public static SafetyHandler create(Delegate delegate) {
        return new SafetyHandler(delegate);
    }

    private SafetyHandler(Delegate delegate) {
        this.mWeakReference = new WeakReference<>(delegate);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Delegate delegate = getDelegate();
        if (delegate == null) {
            clear();
            return;
        }
        delegate.handleMessage(msg.what, msg);
    }

    public Delegate getDelegate() {
        return mWeakReference.get();
    }

    /**
     * 清空当前Handler队列所有消息并移除WeakReference持有的Delegate引用
     */
    public void clear() {
        removeCallbacksAndMessages(null);
        mWeakReference.clear();
    }

    public interface Delegate {
        void handleMessage(int what, Message msg);
    }

}
