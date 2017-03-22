package com.gh.myloadretryemptydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gh.pagestate.OnPageStateListener;
import com.gh.pagestate.PageStateManager;

/**
 * @author: gh
 * @description: TODO(描述)
 * @date: 2017/3/13 17:32
 * @note:
 */
public class NormalFragment extends Fragment {

    PageStateManager mLoadingAndRetryManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        mLoadingAndRetryManager = PageStateManager.generate(this, new OnPageStateListener()
        {
            @Override
            public void setRetryEvent(View retryView)
            {
                NormalFragment.this.setRetryEvent(retryView);
            }
        });

        mLoadingAndRetryManager.showLoading();
    }


    public void setRetryEvent(View retryView)
    {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "retry event invoked", Toast.LENGTH_SHORT).show();
                mLoadingAndRetryManager.showLoading();
                loadData();
            }
        });
    }

    private void loadData()
    {


        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                if (Math.random() > 0.6)
                {
                    mLoadingAndRetryManager.showContent();
                } else
                {
                    mLoadingAndRetryManager.showRetry();
                }
            }
        }.start();
    }

}
