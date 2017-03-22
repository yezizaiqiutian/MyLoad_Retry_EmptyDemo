package com.gh.myloadretryemptydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gh.pagestate.OnPageStateListener;
import com.gh.pagestate.PageStateManager;

/**
 * @author: gh
 * @description: 在Activity中的测试
 * @date: 2017/3/13 16:39
 * @note:
 */
public class TestActivity extends AppCompatActivity {

    private PageStateManager stateManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        stateManager = PageStateManager.generate(this, new OnPageStateListener() {
            @Override
            public void setRetryEvent(View retryView) {
                TestActivity.this.setRetryEvent(retryView);
            }
        });

        loadData();
    }

    private void loadData() {
        stateManager.showLoading();

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
                double v = Math.random();
                if (v > 0.8)
                {
                    stateManager.showContent();
                } else if (v > 0.4)
                {
                    stateManager.showRetry();
                } else
                {
                    stateManager.showEmpty();
                }
            }
        }.start();
    }

    private void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(TestActivity.this, "retry event invoked", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }
}
