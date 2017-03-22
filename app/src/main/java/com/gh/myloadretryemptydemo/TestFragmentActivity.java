package com.gh.myloadretryemptydemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * @author: gh
 * @description: TODO(描述)
 * @date: 2017/3/13 16:39
 * @note:
 */
public class TestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.id_rl_fragment_container);

        if (fragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.id_rl_fragment_container, new NormalFragment()).commit();
        }
    }

}
