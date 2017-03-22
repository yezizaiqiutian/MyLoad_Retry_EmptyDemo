package com.gh.pagestate;

import android.view.View;

/**
 * @author: gh
 * @description: 页面管理监听器
 * @date: 2017/3/13 14:44
 * @note:
 */

public abstract class OnPageStateListener {

    /*可以在方法中找到界面的子空间并定义点击事件    如找到button设置点击重试 重写接口的回调方法:*/

    /**
     * 返回加载中View
     *
     * @param loadingView
     */
    public void setLoadingEnvent(View loadingView) {

    }

    /**
     * 返回重试View
     *
     * @param retryView
     */
    public abstract void setRetryEvent(View retryView);

    /**
     * 返回空View
     *
     * @param emptyView
     */
    public void setEmptyEvent(View emptyView) {

    }

    /*如果需要针对单个Activity、Fragment、View定制页面，重写接口的回调方法：*/

    /**
     * 生成加载中ViewId
     *
     * @return 返回NO_LAYOUT_ID为默认值, 代表没有设置ID
     */
    public int generateLoadingLayoutId() {
        return PageStateManager.NO_LAYOUT_ID;
    }

    /**
     * 生成重试ViewId
     *
     * @return 返回NO_LAYOUT_ID为默认值, 代表没有设置ID
     */
    public int generateRetryLayoutId() {
        return PageStateManager.NO_LAYOUT_ID;
    }

    /**
     * 生成空ViewId
     *
     * @return 返回NO_LAYOUT_ID为默认值, 代表没有设置ID
     */
    public int generateEmptyId() {
        return PageStateManager.NO_LAYOUT_ID;
    }

    /**
     * 生成加载中View
     *
     * @return 默认返回Null, 代表没有设置定制界面, 采用统一界面
     */
    public View generateLoadingLayout() {
        return null;
    }

    /**
     * 生成重试View
     *
     * @return 默认返回Null, 代表没有设置定制界面, 采用统一界面
     */
    public View generateRetryLayout() {
        return null;
    }

    /**
     * 生成空View
     *
     * @return 默认返回Null, 代表没有设置定制界面, 采用统一界面
     */
    public View generateEmptyLayout() {
        return null;
    }

    /*判断是否有重试/加载/空的自定义界面*/

    /**
     * 是否设置了自定义的加载页面
     *
     * @return
     */
    public boolean isSetLoadingLayout() {
        //对原有逻辑做了简化
//        if (generateLoadingLayoutId() != PageStateManager.NO_LAYOUT_ID || generateLoadingLayout() != null) {
//            return true;
//        }
//        return false;
        return generateLoadingLayoutId() != PageStateManager.NO_LAYOUT_ID || generateLoadingLayout() != null;
    }

    /**
     * 是否设置了自定义的重试页面
     *
     * @return
     */
    public boolean isSetRetryLayout() {
        return generateRetryLayoutId() != PageStateManager.NO_LAYOUT_ID || generateRetryLayout() != null;
    }

    /**
     * 是否设置了自定义的空页面
     *
     * @return
     */
    public boolean isSetEmptyLayout() {
        return generateEmptyId() != PageStateManager.NO_LAYOUT_ID || generateEmptyLayout() != null;
    }

}
