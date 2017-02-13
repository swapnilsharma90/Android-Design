package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.swapsharma.mvvm_android.R;
import com.swapsharma.mvvm_android.network.Tile;
import com.swapsharma.mvvm_android.ui.activity.base.BaseActivity;
import com.swapsharma.mvvm_android.util.DialogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MosaicActivity extends BaseActivity implements MainMvpView ,Handler.Callback {

    private static final String TAG = MosaicActivity.class.getSimpleName();
    @BindView(R.id.gridview)
    GridView grid;
    //@Inject
    ChunksAdapter chunksAdapter;
    ArrayList<Bitmap> smallimages = new ArrayList<Bitmap>();
    List<String> hexCodes;
    ThreadPoolExecutor executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);
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
        executor.execute(new LoadChunksThread(hexCodes, new Handler(this),this));
        //createPhotoMosaic(hexCodes);
        updateProgressBar(true);
    }

    @Override
    public void showTiles() {

    }

    @Override
    public void showTilesWithColor(List<Tile> tilesList) {

    }

    /***** MVP View methods implementation *****/
    //show tiles ...divided on image
    //show tiles fetched from server
    //// TODO: 2/2/17  remove these
    @Override
    public void showTilesWithColor() {
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_tiles))
                .show();
    }

    @Override
    public void showTile(Bitmap tile) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateProgressBar(boolean shouldShow) {
        ProgressBar progressView = (ProgressBar) findViewById(R.id.progressBar);
        if (null != progressView) {
            if (shouldShow) {
                progressView.setVisibility(View.VISIBLE);
            } else {
                progressView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        smallimages.clear();
        smallimages.addAll((ArrayList) message.obj);
        //Getting the grid view and setting an adapter to it
        grid.setVisibility(View.VISIBLE);
        chunksAdapter = new ChunksAdapter(MosaicActivity.this, smallimages);
        grid.setAdapter(chunksAdapter);
        grid.setNumColumns((int) Math.sqrt(smallimages.size()));
        updateProgressBar(false);
        return false;
    }
}

