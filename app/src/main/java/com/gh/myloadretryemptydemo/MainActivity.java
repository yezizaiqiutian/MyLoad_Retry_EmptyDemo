package com.gh.myloadretryemptydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView id_list;
    private List<String> mDatas = Arrays.asList("LoadingAndRetry in Activity", "LoadingAndRetry in Fragment", "LoadingAndRetry in Any View");
    private Class[] mClazz = new Class[]{TestActivity.class, TestFragmentActivity.class, TestAnyViewTestActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id_list = (ListView) findViewById(R.id.id_list);
        id_list.setAdapter(new ArrayAdapter<String>(this,R.layout.item_list,R.id.id_tv,mDatas));
        id_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position+1>mClazz.length) return;
                Intent intent = new Intent(MainActivity.this, mClazz[position]);
                startActivity(intent);
            }
        });
    }
}
