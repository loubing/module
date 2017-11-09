package com.example.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;
    private LoadMoreAdapter loadMoreAdapter;
    private List<String> dataList = new ArrayList<>();
    private LoadMoreWrapper loadMoreWrapper;
    private Button mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        mShare = (Button) findViewById(R.id.btn_share);
        init();
    }

    private void init() {
        getData();
        //封装后代码
        LoadMoreWrapperAdapter loadMoreWrapperAdapter = new LoadMoreWrapperAdapter(dataList);
        loadMoreWrapper = new LoadMoreWrapper(loadMoreWrapperAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(loadMoreWrapper);
        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreAdapter.LOADING);
                if (dataList.size() < 52) {
                    // 模拟获取网络数据，延时1s
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            loadMoreWrapper.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                        }
                    }, 5000);
                } else {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreAdapter.LOADING_END);
                }
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePop();
            }
        });
    }

    private static final String APP_ID="WX8888888";
    private IWXAPI api;
    private void showSharePop() {
        api= WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);

        WXTextObject textObj=new WXTextObject();
        textObj.text="abc";
        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=textObj;
        msg.description="abc";
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=String.valueOf(System.currentTimeMillis());
        req.message=msg;
        api.sendReq(req);
    }

    private void getData() {
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            dataList.add(String.valueOf(letter));
            letter++;
        }
    }

}
