package com.gh.pagestate;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: gh
 * @description: 页面管理   调用类
 * @date: 2017/3/13 14:36
 * @note:
 */

public class PageStateManager {

    public static final int NO_LAYOUT_ID = 0;
    public static int PAGESTATE_LOADING_ID = NO_LAYOUT_ID;
    public static int PAGESTATE_RETRY_ID = NO_LAYOUT_ID;
    public static int PAGESTATE_EMPTY_ID = NO_LAYOUT_ID;

    public PageStateLayout mPageStateLayout;

    /**
     * 默认监听
     * 什么都不做
     */
    public OnPageStateListener DEFAULT_LISTENER = new OnPageStateListener() {
        @Override
        public void setRetryEvent(View retryEvent) {

        }
    };

    /**
     * 初始化方法，入口
     *
     * @param activityOrFragmentOrView
     * @param listener
     * @return
     */
    public static PageStateManager generate(Object activityOrFragmentOrView, OnPageStateListener listener) {
        return new PageStateManager(activityOrFragmentOrView, listener);
    }

    private PageStateManager(Object activityOrFragmentOrView, OnPageStateListener listener) {
        if (listener == null) listener = DEFAULT_LISTENER;

        //需要替换的View的父View
        ViewGroup contentParent = null;
        Context context;
        if (activityOrFragmentOrView instanceof Activity) {
            Activity activity = (Activity) activityOrFragmentOrView;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (activityOrFragmentOrView instanceof Fragment) {
            Fragment fragment = (Fragment) activityOrFragmentOrView;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
        } else if (activityOrFragmentOrView instanceof View) {
            View view = (View) activityOrFragmentOrView;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)");
        }
//        //父控件View的个数
//        int childCount = contentParent.getChildCount();
        //显示内容的View在父控件中是第几个    如果不是在View中， 都是第0个
        int index = 0;
        //显示内容的View
        View oldContent;
        if (activityOrFragmentOrView instanceof View) {
            //父控件View的个数
            int childCount = contentParent.getChildCount();
            oldContent = (View) activityOrFragmentOrView;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        PageStateLayout pageStateLayout = new PageStateLayout(context);
        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(pageStateLayout, index, lp);
        //设置内容View
        pageStateLayout.setContentView(oldContent);
        //设置界面
        setupLoadingLayout(listener, pageStateLayout);
        setupRetryLayout(listener, pageStateLayout);
        setupEmptLayout(listener, pageStateLayout);
        //设置回调方法
        listener.setLoadingEnvent(pageStateLayout.getLoadingView());
        listener.setRetryEvent(pageStateLayout.getRetryView());
        listener.setEmptyEvent(pageStateLayout.getEmptyView());

        mPageStateLayout = pageStateLayout;
    }

    /*设置重试/加载/空的界面  是用默认的还是定制的在这里找到*/

    /**
     * 设置加载Layout
     *
     * @param listener
     * @param pageStateLayout
     */
    private void setupLoadingLayout(OnPageStateListener listener, PageStateLayout pageStateLayout) {
        if (listener.isSetLoadingLayout()) {
            //已经定制了单独的界面
            int layoutId = listener.generateLoadingLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                //设置的ID
                pageStateLayout.setLoadingView(layoutId);
            } else {
                //设置View
                pageStateLayout.setLoadingView(listener.generateLoadingLayout());
            }
        } else {
            //没有定制页面    使用默认的
            if (PAGESTATE_LOADING_ID != NO_LAYOUT_ID) {
                pageStateLayout.setLoadingView(PAGESTATE_LOADING_ID);
            }
        }
    }

    /**
     * 设置重试Layout
     *
     * @param listener
     * @param pageStateLayout
     */
    private void setupRetryLayout(OnPageStateListener listener, PageStateLayout pageStateLayout) {
        if (listener.isSetRetryLayout()) {
            //已经定制了单独的界面
            int layoutId = listener.generateRetryLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                //设置的ID
                pageStateLayout.setRetryView(layoutId);
            } else {
                //设置View
                pageStateLayout.setRetryView(listener.generateRetryLayout());
            }
        } else {
            //没有定制页面    使用默认的
            if (PAGESTATE_RETRY_ID != NO_LAYOUT_ID) {
                pageStateLayout.setRetryView(PAGESTATE_RETRY_ID);
            }
        }
    }

    /**
     * 设置空Layout
     *
     * @param listener
     * @param pageStateLayout
     */
    private void setupEmptLayout(OnPageStateListener listener, PageStateLayout pageStateLayout) {
        if (listener.isSetEmptyLayout()) {
            //已经定制了单独的界面
            int layoutId = listener.generateEmptyId();
            if (layoutId != NO_LAYOUT_ID) {
                //设置的ID
                pageStateLayout.setEmptyView(layoutId);
            } else {
                //设置View
                pageStateLayout.setEmptyView(listener.generateEmptyLayout());
            }
        } else {
            //没有定制页面    使用默认的
            if (PAGESTATE_EMPTY_ID != NO_LAYOUT_ID) {
                pageStateLayout.setEmptyView(PAGESTATE_EMPTY_ID);
            }
        }
    }

    /*显示哪个界面*/

    /**
     * 显示加载
     */
    public void showLoading() {
        mPageStateLayout.showLoading();
    }

    /**
     * 显示重试
     */
    public void showRetry() {
        mPageStateLayout.showRetry();
    }

    /**
     * 显示空
     */
    public void showEmpty() {
        mPageStateLayout.showEmpty();
    }

    /**
     * 显示内容
     */
    public void showContent() {
        mPageStateLayout.showContent();
    }
}
