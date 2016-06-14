package com.JSwipeRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button mPullUpListViewBtn;

    private Button mScrollListViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPullUpListViewBtn = (Button) findViewById(R.id.pull_up_btn);

        mScrollListViewBtn = (Button) findViewById(R.id.scroll_btn);


        mPullUpListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ManualListViewActivity.class);
                startActivity(intent);
            }
        });

        mScrollListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AutoListViewActivity.class);
                startActivity(intent);
            }
        });

    }

}
