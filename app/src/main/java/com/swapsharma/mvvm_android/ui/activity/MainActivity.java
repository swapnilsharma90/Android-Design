package com.swapsharma.mvvm_android.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.swapsharma.mvvm_android.R;
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
    @BindView(R.id.createMosiacBtn)
    Button createMosaicBtn;
    @BindView(R.id.gridview)
    GridView grid;
    int rows, cols;
    private Subscription subscription;
    @Inject
    MainPresenter mMainPresenter;
    //@Inject
    ChunksAdapter chunksAdapter;
    ThreadPoolExecutor executor;
    ArrayList<Bitmap> myList;
    private Handler handler2;
    private Boolean isTiled = false;
    List<String> hexCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        grid.setVisibility(View.GONE);
        pickImageBtn.setOnClickListener(view -> mMainPresenter.createTiledImage(Sources.GALLERY));
        createMosaicBtn.setOnClickListener(view -> mMainPresenter.createPhotoMosaic());

        if (RxImagePicker.with(this).getActiveSubscription() != null) {
            RxImagePicker.with(this).getActiveSubscription().subscribe(this::onImagePicked);
        }
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }


    private void createPhotoMosaic() {
        if (!isTiled) {
            Toast.makeText(MainActivity.this, "pls tile first", Toast.LENGTH_SHORT).show();
        } else {
            //find average color
            try {
                getHexCodes();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void pickImageFromSource(Sources source) {
        subscription = RxImagePicker.with(this).requestImage(source)
                .flatMap(uri -> RxImageConverters.uriToBitmap(MainActivity.this, uri))
                .subscribe(this::onImagePicked, throwable -> {
                    Toast.makeText(MainActivity.this, String.format("Error: %s", throwable), Toast.LENGTH_LONG).show();
                });
    }

    private void onImagePicked(Object result) {
        Toast.makeText(this, String.format("Please wait ", result), Toast.LENGTH_LONG).show();
        if (result instanceof Bitmap) {
            //todo use picasso with callback

            updateProgressBar(true);
            Bitmap scaledBitmapx = Bitmap.createScaledBitmap((Bitmap) result, 350, 350, true);
            ivPickedImage.setImageBitmap(scaledBitmapx);
            executor.execute(new LongThread(scaledBitmapx, 1500, new Handler(this)));
            rows = cols = (int) Math.sqrt(1500);

        } else {
            //todo not needed
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        myList = (ArrayList) message.obj;

        ivPickedImage.setVisibility(View.GONE);
        //Getting the grid view and setting an adapter to it
        grid.setVisibility(View.VISIBLE);
        chunksAdapter = new ChunksAdapter(this, myList);
        grid.setAdapter(chunksAdapter);
        grid.setNumColumns((int) Math.sqrt(myList.size()));
        updateProgressBar(false);
        isTiled = true;
        return true;
    }


    /***** MVP View methods implementation *****/

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_tiles))
                .show();
    }

    @Override
    public void showMosaicImage() {
        createPhotoMosaic();
    }

    @Override
    public void showTiledImage() {

        pickImageFromSource(Sources.GALLERY);

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
        TaskCallable task = new TaskCallable();
        Future<List<String>> future = executor.submit(task);
        hexCodes = future.get();

        Intent i = new Intent(MainActivity.this, MosaicActivity.class);
        i.putStringArrayListExtra("HexCodesListExtra", (ArrayList<String>) hexCodes);
        startActivity(i);

    }

    class TaskCallable implements Callable {
        @Override
        public List<String> call() throws Exception {
            String url = "http://10.0.2.2:8765/color/8/8/";
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