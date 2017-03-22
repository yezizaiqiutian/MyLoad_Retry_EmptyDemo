package com.gh.pagestate;

import android.app.Application;

/**
 * @author: gh
 * @description: 初始化,在Application中调用
 * @date: 2017/3/13 14:24
 * @note: 页面管理  设置重试/加载/空的界面
 * @参考:https://github.com/hongyangAndroid/LoadingAndRetryManager
 * https://github.com/hss01248/PageStateManager
 */

public class PageStateApp {

    private static Application application;

    /**
     * 设置重试/加载/空的界面
     * @param application
     */
    public static void init(Application application) {
        setApplication(application);
        PageStateManager.PAGESTATE_RETRY_ID = R.layout.pagestate_retry;
        PageStateManager.PAGESTATE_LOADING_ID = R.layout.pagrstate_loading;
        PageStateManager.PAGESTATE_EMPTY_ID = R.layout.pagestate_empty;
    }

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        PageStateApp.application = application;
    }
}
