package com.gh.myloadretryemptydemo;

import android.app.Application;

import com.gh.pagestate.PageStateApp;

/**
 * @author: gh
 * @description: TODO(描述)
 * @date: 2017/3/13 14:38
 * @note:
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //注册pagestate
        PageStateApp.init(this);
    }
}
