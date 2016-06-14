package com.JSwipeRefreshListView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.JSwipeRefreshListView.adapter.MyAdapter;
import com.SwipeRefreshListView.interfaces.MODE;
import com.SwipeRefreshListView.listener.OnPullDownListener;
import com.SwipeRefreshListView.listener.OnPullUpListener;
import com.SwipeRefreshListView.view.JSwipeRefreshManualListView;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * 手动下拉demo
 */
public class ManualListViewActivity extends AppCompatActivity {

    private JSwipeRefreshManualListView mSwipeRefreshLayout;

    private ListView mListView;

    private Button mBtn;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_listview_layout);

        mSwipeRefreshLayout = (JSwipeRefreshManualListView) findViewById(R.id.id_list_refresh_layout);
        mBtn = (Button) findViewById(R.id.id_list_refresh_btn);
        mListView = mSwipeRefreshLayout.getListView();

        //mSwipeRefreshLayout.setMode(MODE.BOTH);

        mAdapter = new MyAdapter(this);

        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnPullDownListener(new OnPullDownListener() {
            @Override
            public void onRefresh() {
                /*
                   进行下拉刷新的时候 建议把FooterView隐藏掉
                */
                mSwipeRefreshLayout.setMode(MODE.ONLY_DOWN);
                //获取列表模拟数据
                final ArrayList<String> list = getRefeshData();
                //数据获取完成 这里做了3秒的处理
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ManualListViewActivity.this, "下拉刷新>>" + list.size(), Toast.LENGTH_SHORT).show();
                        //如果获取到的数据小于15条的话 去掉FooterView并不允许上拉进行刷新  反之 则显示FooterView 并运行上拉进行刷新
                        //PS 15可以自己进行配置
                        if (list.size() < 15) {
                            mSwipeRefreshLayout.setMode(MODE.ONLY_DOWN);
                        } else {
                            mSwipeRefreshLayout.setMode(MODE.BOTH);
                        }
                        mAdapter.cleanAdapter();

                        mAdapter.setList(list);
                        mSwipeRefreshLayout.pullDownComplete(); //通知下拉刷新完成


                    }
                }, 3000);


            }
        });

        mSwipeRefreshLayout.setOnPullUpListener(new OnPullUpListener() {
            @Override
            public void onLoad() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int ran = new Random().nextInt(2);
                        if (ran == 0) {
                            Log.e("TAG", "还有更多的数据");
                            ArrayList<String> list = getLoadData();
                            mAdapter.addList(list);
                            mSwipeRefreshLayout.pullUpSuccess();
                        } else {
                            int rans =  new Random().nextInt(2);
                            Log.e("TAG","rans>>"+rans);
                            if (rans == 0) {
                                Log.e("TAG","没有更多的数据了");
                                mSwipeRefreshLayout.pullUpSuccess("没有更多的数据了");
                            } else {
                                Log.e("TAG","点击重新加载");
                                mSwipeRefreshLayout.pullUpError();
                            }
                        }
                    }
                }, 3000);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ManualListViewActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

        mSwipeRefreshLayout.autoRefreshing();


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManualListViewActivity.this, "自动下拉刷新", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.autoRefreshing();
            }
        });


    }


    private ArrayList<String> getRefeshData() {
        ArrayList<String> list = new ArrayList<>();
        int random = new Random().nextInt(30);
        for (int i = 0; i < random; i++) {
            list.add(UUID.randomUUID() + "");
        }
        return list;
    }


    private ArrayList<String> getLoadData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(UUID.randomUUID() + "");
        }
        return list;
    }

}
