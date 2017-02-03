package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.swapsharma.mvvm_android.R;
import com.swapsharma.mvvm_android.network.Tile;
import com.swapsharma.mvvm_android.ui.activity.base.BaseActivity;
import com.swapsharma.mvvm_android.util.ColorUtil;
import com.swapsharma.mvvm_android.util.DialogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class MainActivity extends BaseActivity implements MainMvpView, Handler.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.pickedIv)
    ImageView ivPickedImage;
    @BindView(R.id.pickImageBtn)
    Button pickImageBtn;

//    @BindView(R.id.createMosiacBtn)
//    Button createMosaicBtn;

    @BindView(R.id.gridview)
    GridView grid;
    List<Target> targets;
    int rows, cols;
    private Subscription subscription;
    @Inject
    MainPresenter mMainPresenter;
    //@Inject
//    ChunksAdapter chunksAdapter;
    ArrayList<Bitmap> smallimages;
    ArrayList<Bitmap> bitmaps;
    ThreadPoolExecutor executor;
    ArrayList<Bitmap> myList;

    private Handler handler2;


    private Boolean isLoaded = false;


    List<String> hexCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        grid.setVisibility(View.GONE);
//        grid.setTag(target);
        pickImageBtn.setOnClickListener(view -> pickImageFromSource(Sources.GALLERY));
        if (RxImagePicker.with(this).getActiveSubscription() != null) {
            RxImagePicker.with(this).getActiveSubscription().subscribe(this::onImagePicked);
        }
        mMainPresenter.attachView(this);
        //code for fetching image from gallery goes in mainpresenter
//        mMainPresenter.loadTiles();


        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );


    }

    private void pickImageFromSource(Sources source) {
        subscription = RxImagePicker.with(this).requestImage(source)
                .flatMap(uri -> RxImageConverters.uriToBitmap(MainActivity.this, uri))
                .subscribe(this::onImagePicked, throwable -> {
                    Toast.makeText(MainActivity.this, String.format("Error: %s", throwable), Toast.LENGTH_LONG).show();
                });
    }

    private void onImagePicked(Object result) {
        Toast.makeText(this, String.format("Result: %s", result), Toast.LENGTH_LONG).show();
        if (result instanceof Bitmap) {
            //todo use picasso with callback
            Bitmap scaledBitmapx = Bitmap.createScaledBitmap((Bitmap) result, 350, 350, true);
            ivPickedImage.setImageBitmap(scaledBitmapx);
            executor.execute(new LongThread(ivPickedImage, 3200, new Handler(this)));
            rows = cols = (int) Math.sqrt(3200);
//
        } else {
            //todo not needed
        }
    }

//        new AsyncTask<List<String>, Void, List<Bitmap>>() {
//            @Override
//            protected List<Bitmap> doInBackground(List<String>... params) {
//                try {
//                    List<Bitmap> bitmaps = new ArrayList<Bitmap>();
//                    for (int i = 0; i < params[0].size(); ++i) {
//
//                        System.out.println("....."+i+"...."+params[0].get(i));
//                        bitmaps.add(Picasso.with(MainActivity.this).load(params[0].get(i)).get());
//                    }
//                    return bitmaps;
//                } catch (IOException e) {
//                    return null;
//                }
//            }
//            @Override
//            public void onPostExecute(List<Bitmap> bitmaps) {
//                if (bitmaps != null) {
//                    // Do stuff with your loaded bitmaps
//                    smallimages.clear();
//                    //call webservice for each tile and get reslut add result in the list
//                    //working part
//                    smallimages.addAll(bitmaps);
//                    chunksAdapter.notifyDataSetChanged();
//                }
//            }
//        }.execute(hexCodes);


    @Override
    public void showTiles() {

    }

    @Override
    public void showTilesWithColor(List<Tile> tilesList) {
        System.out.println("11111...");


    }

    /***** MVP View methods implementation *****/
    //show tiles ...divided on image
    //show tiles fetched from server
    //// TODO: 2/2/17  remove these
    @Override
    public void showTilesWithColor() {
        System.out.println("1111...a");

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
        mMainPresenter.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }

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

    public void getHexCodes() throws ExecutionException, InterruptedException {
        // do something long
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //new Thread(new Task()).start();
        TaskCallable task = new TaskCallable ();
        Future<List<String>> future = executor.submit(task);
        hexCodes = future.get();
    }

    @Override
    public boolean handleMessage(Message message) {
        myList = (ArrayList) message.obj;

        ivPickedImage.setVisibility(View.GONE);
        //Getting the grid view and setting an adapter to it
        grid.setVisibility(View.VISIBLE);
        ChunksAdapter chunksAdapter = new ChunksAdapter(this, myList);
        grid.setAdapter(chunksAdapter);
        grid.setNumColumns((int) Math.sqrt(myList.size()));
        //find average color
        try {
            getHexCodes();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bitmaps = new ArrayList<Bitmap>();
        targets = new ArrayList<Target>();
        for (int i = 0; i < hexCodes.size(); i++) {
            final int k = i;
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.i("Targets", "Loaded: " + k);
                    //targets.remove(this);
                    bitmaps.add(bitmap);
                    updateProgressBar(false);
                    targets.add(this);
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    targets.remove(this);
                    updateProgressBar(true);
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.i("Targets", "Preparing: " + k);
                }
            };
//            targets.add(target);
            Picasso.with(this)
                    .load(hexCodes.get(i)) // Start loading the current target
                    .resize(4, 4)
                    .into(target);
        }
        myList.addAll(bitmaps);
//
//        //Getting the grid view and setting an adapter to it
        chunksAdapter.notifyDataSetChanged();
        grid.invalidateViews();
        return true;
    }
    class TaskCallable implements Callable {
        @Override
        public List<String> call() throws Exception {
            String url = "http://10.0.2.2:8765/color/32/32/";
            hexCodes = new ArrayList<>();
            for (int i = 0; i < rows * cols; i++) {
                final int value = i;
                try {
                    String hex = Integer.toString
                            (ColorUtil.calculateAverageColor((Bitmap) grid.getItemAtPosition(i), 4), 16);
                    //remove - if contains
                    String hexCode = hex.replaceAll("-", "");
                    hexCodes.add(url.concat(hexCode));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return hexCodes;
        }

    }
}

