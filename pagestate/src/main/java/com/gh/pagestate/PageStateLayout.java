package com.gh.pagestate;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author: gh
 * @description: 页面管理View
 * @date: 2017/3/13 15:07
 * @note:
 */

public class PageStateLayout extends FrameLayout{

    /**
     * 加载
     */
    private View mLoadingView;
    /**
     * 重试
     */
    private View mRetryView;
    /**
     * 空
     */
    private View mEmptyView;
    /**
    * 内容
     */
    private View mContentView;

    private LayoutInflater mInflater;

    private static final String TAG = PageStateLayout.class.getSimpleName();

    /*构造方法*/
    public PageStateLayout(Context context) {
        this(context,null);
    }

    public PageStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public PageStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 判断是否为主线程
     * @return
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /*显示相关的View*/
    /**
     * 显示加载
     */
    public void showLoading() {
        if (isMainThread()) {
            showView(mLoadingView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mLoadingView);
                }
            });
        }
    }

    /**
     * 显示重试
     */
    public void showRetry() {
        if (isMainThread()) {
            showView(mRetryView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mRetryView);
                }
            });
        }
    }

    /**
     * 显示空
     */
    public void showEmpty() {
        if (isMainThread()) {
            showView(mEmptyView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mEmptyView);
                }
            });
        }
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (isMainThread()) {
            showView(mContentView);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mContentView);
                }
            });
        }
    }

    /**
     * 具体执行View显示的方法
     * @param view
     */
    private void showView(View view) {
        if (view == null) return;

        if (view == mLoadingView) {
            mLoadingView.setVisibility(VISIBLE);
            if (mRetryView != null)
                mRetryView.setVisibility(GONE);
            if (mEmptyView!=null)
                mEmptyView.setVisibility(GONE);
            if (mContentView!=null)
                mContentView.setVisibility(GONE);
        } else if (view == mRetryView) {
            mRetryView.setVisibility(VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(GONE);
            if (mEmptyView!=null)
                mEmptyView.setVisibility(GONE);
            if (mContentView!=null)
                mContentView.setVisibility(GONE);
        } else if (view == mEmptyView) {
            mEmptyView.setVisibility(VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(GONE);
            if (mRetryView!=null)
                mRetryView.setVisibility(GONE);
            if (mContentView!=null)
                mContentView.setVisibility(GONE);
        } else if (view == mContentView) {
            mContentView.setVisibility(VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(GONE);
            if (mRetryView!=null)
                mRetryView.setVisibility(GONE);
            if (mEmptyView!=null)
                mEmptyView.setVisibility(GONE);
        }
    }

    /*设置重试/加载/空View*/

    /**
     * 设置加载View
     * @param layoutId
     * @return
     */
    public View setLoadingView(int layoutId) {
        return setLoadingView(mInflater.inflate(layoutId, this, false));
    }

    /**
     * 设置重试View
     * @param layoutId
     * @return
     */
    public View setRetryView(int layoutId) {
        return setRetryView(mInflater.inflate(layoutId, this, false));
    }

    /**
     * 设置空View
     * @param layoutId
     * @return
     */
    public View setEmptyView(int layoutId) {
        return setEmptyView(mInflater.inflate(layoutId, this, false));
    }

    /**
     * 设置内容View
     * @param layoutId
     * @return
     */
    public View setContentView(int layoutId) {
        return setContentView(mInflater.inflate(layoutId, this, false));
    }

    /**
     * 设置加载View
     * @return
     */
    public View setLoadingView(View view) {
        View loadingView = mLoadingView;
        if (loadingView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
        return mLoadingView;
    }

    /**
     * 设置重试View
     * @param view
     * @return
     */
    public View setRetryView(View view) {
        View retryView = mRetryView;
        if (retryView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        mRetryView = view;
        return mRetryView;
    }

    /**
     * 设置空View
     * @param view
     * @return
     */
    public View setEmptyView(View view) {
        View emptyView = mEmptyView;
        if (emptyView != null) {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        mEmptyView = view;
        return mEmptyView;
    }

    /**
     * 设置内容View
     * @param view
     * @return
     */
    public View setContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        mContentView = view;
        return mContentView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getRetryView() {
        return mRetryView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getContentView() {
        return mContentView;
    }
}
