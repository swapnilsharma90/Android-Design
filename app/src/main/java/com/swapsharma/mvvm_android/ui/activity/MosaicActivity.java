package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;

import com.swapsharma.mvvm_android.R;
import com.swapsharma.mvvm_android.network.LoadBitmapsService;
import com.swapsharma.mvvm_android.ui.activity.base.BaseActivity;
import com.swapsharma.mvvm_android.ui.adapter.CustomGridAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MosaicActivity extends BaseActivity implements Handler.Callback {

    private static final String TAG = MosaicActivity.class.getSimpleName();
    @BindView(R.id.gridview)
    GridView grid;

    CustomGridAdapter customGridAdapter;
    ArrayList<Bitmap> smallimages;
    List<String> hexCodes;
    ThreadPoolExecutor executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        grid.setVisibility(View.GONE);
        hexCodes = getIntent().getStringArrayListExtra("HexCodesListExtra");
        executor.execute(new LoadBitmapsService(hexCodes, new Handler(this), this));
        updateProgressBar(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean handleMessage(Message message) {
        smallimages = (ArrayList) message.obj;
        //Getting the grid view and setting an adapter to it
        grid.setVisibility(View.VISIBLE);
        customGridAdapter = new CustomGridAdapter(MosaicActivity.this, smallimages);
        grid.setAdapter(customGridAdapter);
        grid.setNumColumns((int) Math.sqrt(smallimages.size()));
        updateProgressBar(false);
        return false;
    }
}
