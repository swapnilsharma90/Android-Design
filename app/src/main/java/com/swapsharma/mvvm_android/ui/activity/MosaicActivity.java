package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.swapsharma.mvvm_android.R;
import com.swapsharma.mvvm_android.network.Tile;
import com.swapsharma.mvvm_android.ui.activity.base.BaseActivity;
import com.swapsharma.mvvm_android.util.DialogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MosaicActivity extends BaseActivity implements MainMvpView {

    private static final String TAG = MosaicActivity.class.getSimpleName();
    @BindView(R.id.gridview)
    GridView grid;
    //@Inject
    ChunksAdapter chunksAdapter;
    ArrayList<Bitmap> smallimages = new ArrayList<Bitmap>();
    List<String> hexCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);
        ButterKnife.bind(this);
        grid.setVisibility(View.GONE);

        hexCodes = getIntent().getStringArrayListExtra("HexCodesListExtra");
        createPhotoMosaic(hexCodes);


        updateProgressBar(true);
    }

    private void createPhotoMosaic(List<String> hexCodes) {
            new AsyncTask<List<String>, Void, List<Bitmap>>() {
                @Override
                protected List<Bitmap> doInBackground(List<String>... params) {
                    try {
                        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        for (int i = 0; i < params[0].size(); ++i) {
                            System.out.println("....." + i + "...." + params[0].get(i));
                            bitmaps.add(Picasso.with(MosaicActivity.this).load(params[0].get(i)).resize(8, 8).get());
                        }
                        return bitmaps;
                    } catch (IOException e) {
                        return null;
                    }
                }

                @Override
                public void onPostExecute(List<Bitmap> bitmaps) {
                    if (bitmaps != null) {
                        // Do stuff with your loaded bitmaps
                        smallimages.clear();
                        //call webservice for each tile and get reslut add result in the list
                        //working part
                        smallimages.addAll(bitmaps);
                        //Getting the grid view and setting an adapter to it
                        grid.setVisibility(View.VISIBLE);
                        chunksAdapter = new ChunksAdapter(MosaicActivity.this, smallimages);
                        grid.setAdapter(chunksAdapter);
                        grid.setNumColumns((int) Math.sqrt(smallimages.size()));

                        updateProgressBar(false);

                    }
                }
            }.execute(hexCodes);

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
}

