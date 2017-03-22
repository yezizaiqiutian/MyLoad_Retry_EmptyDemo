package com.gh.pagestate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author: gh
 * @description: 页面管理   调用类     在原有基础上增加一些功能    无网络判断   设置失败文字  修改初始化入口     空白时文字
 * @date: 2017/3/13 14:36
 * @note:https://github.com/hss01248/PageStateManager
 */

public class PageStateManager2 {

    public static final int NO_LAYOUT_ID = 0;
    public static int PAGESTATE_LOADING_ID = NO_LAYOUT_ID;
    public static int PAGESTATE_RETRY_ID = NO_LAYOUT_ID;
    public static int PAGESTATE_EMPTY_ID = NO_LAYOUT_ID;

    public PageStateLayout mPageStateLayout;

    //【新】
    private static Context appContext;
    private TextView tvError;

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
     * 【新】
     * 初始化重试/加载/空的界面
     * 如果需要后续调用自定义空白msg,错误msg字符串的api,则页面中显示该字符串的textview的id必须为tv_msg_empty,tv_msg_error
     * @param appContext
     * @param layoutLoading
     * @param layoutRetry
     * @param layoutEmpty
     */
    public static void initApp(Context appContext, int layoutLoading, int layoutRetry, int layoutEmpty) {
        PageStateManager2.appContext = appContext;
        if (layoutLoading > 0) {
            PAGESTATE_LOADING_ID = layoutLoading;
        }
        if (layoutRetry > 0) {
            PAGESTATE_RETRY_ID = layoutRetry;
        }
        if (layoutEmpty > 0) {
            PAGESTATE_EMPTY_ID = layoutEmpty;
        }
    }

    /**
     * 【新】
     * 封装后的初始化
     * @param container
     * @param emptMsg
     * @param isShowLoadingOrContent
     * @param retryAction
     * @return
     */
    public static PageStateManager2 init(final Object container, final CharSequence emptMsg, boolean isShowLoadingOrContent, final Runnable retryAction) {
        PageStateManager2 manager2 = generate(container, new OnPageStateListener() {
            @Override
            public void setRetryEvent(View retryView) {
                retryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isNetWorkAvailable(appContext)) {
                            showNoNetWorkDlg(container);
                        } else {
                            retryAction.run();
                        }
                    }
                });
            }

            @Override
            public View generateEmptyLayout() {
                return generateCustomEmptyView(emptMsg);
            }
        });
        if (isShowLoadingOrContent) {
            manager2.showLoading();
        } else {
            manager2.showContent();
        }
        return manager2;
    }

    /**
     * 【新】
     * 判断网络状态
     * @param context
     * @return
     */
    private static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info == null) {
                return false;
            } else {
                if (info.isAvailable()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 【新】
     * 当判断当前手机没有网络时选择是否打开网络设置
     */
    private static AlertDialog showNoNetWorkDlg(final Object container) {
        AlertDialog dialog = null;
        Context context = null;


        if (container instanceof Activity) {
            context = (Activity) container;

        } else if (container instanceof Fragment) {
            context = ((Fragment) container).getActivity();

        } else if (container instanceof View) {
            context = ((View) container).getContext();
        }

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final Activity finalActivity = (Activity) context;

            dialog = builder        //
                    .setTitle("提示")            //
                    .setMessage("当前无网络").setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上

                                //intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            finalActivity.startActivity(intent);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("知道了", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }

    /**
     * 【新】
     * 设置空View的文字
     * @param word
     * @return
     */
    public static View generateCustomEmptyView(CharSequence word) {
        ViewGroup view = (ViewGroup) View.inflate(appContext, PAGESTATE_EMPTY_ID, null);

        TextView textView = (TextView) view.findViewById(R.id.id_tv_msgempty);
        textView.setText(word);
        return view;
    }

    /**
     * 初始化方法，入口
     *
     * @param activityOrFragmentOrView
     * @param listener
     * @return
     */
    public static PageStateManager2 generate(Object activityOrFragmentOrView, OnPageStateListener listener) {
        return new PageStateManager2(activityOrFragmentOrView, listener);
    }

    private PageStateManager2(Object activityOrFragmentOrView, OnPageStateListener listener) {
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
     * 【新】
     * 显示重试 可定义内容
     * @param errorMsg
     */
    public void showRetry(CharSequence errorMsg) {
        if (tvError != null) {
            tvError.setText(errorMsg);
            mPageStateLayout.showRetry();
            return;
        }
        ViewGroup view = (ViewGroup) mPageStateLayout.getRetryView();
        tvError = (TextView) view.findViewById(R.id.id_tv_msgretry);
        tvError.setText(errorMsg);
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
