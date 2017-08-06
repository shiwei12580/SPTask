package org.wei.sptask;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wei.sptask.appService.network.AsyncException;
import org.wei.sptask.appService.network.AsyncListener;
import org.wei.sptask.appService.network.AsyncToken;
import org.wei.sptask.appService.network.RequestManager;
import org.wei.sptask.appService.network.RequestManagerServiceConnection;
import org.wei.sptask.appService.network.request.PSIReadingRequest;
import org.wei.sptask.appService.network.response.GetPSIDataResponse;
import org.wei.sptask.data.ApiStatus;
import org.wei.sptask.data.ItemsData;
import org.wei.sptask.ui.DividerItemDecoration;
import org.wei.sptask.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryDataActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    private PSIHistoryDataAdapter mAdapter;
    private SwipeRefreshLayout mRefresher;
    private RequestManagerServiceConnection mServiceConnection;
    private List<ItemsData> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mData = new ArrayList<ItemsData>();
        mAdapter = new PSIHistoryDataAdapter(mData);
        mServiceConnection = RequestManager.register(this);
        mRecycler.setAdapter(mAdapter);

        mRefresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(mRefreshListener);
        mRefresher.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.red,
                R.color.yellow);

    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getPSIHistoryData();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mRefresher.setRefreshing(true);
        getPSIHistoryData();
    }



    private void getPSIHistoryData() {
        Date now = new Date();
        PSIReadingRequest request = new PSIReadingRequest(null, DateUtils.getDateString(now));

        mServiceConnection.processRequest(request, new AsyncListener<GetPSIDataResponse>() {
            @Override
            public void onResponse(GetPSIDataResponse response, AsyncToken token, AsyncException exception) {
                if (response != null && exception == null) {
                    if (response.getApiStatus().getStatus().equals(ApiStatus.HEALTH)) {

                        List<ItemsData> data = response.getItemData();
                        mData.clear();
                        mData.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mRefresher.setRefreshing(false);
            }
        });
    }



}
