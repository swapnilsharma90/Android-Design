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
import android.widget.Toast;

import com.swapsharma.mvvm_android.R;
import com.swapsharma.mvvm_android.model.Sources;
import com.swapsharma.mvvm_android.network.SplitImageService;
import com.swapsharma.mvvm_android.presenter.MainPresenter;
import com.swapsharma.mvvm_android.ui.activity.base.BaseActivity;
import com.swapsharma.mvvm_android.ui.adapter.CustomGridAdapter;
import com.swapsharma.mvvm_android.util.ColorUtil;
import com.swapsharma.mvvm_android.util.DialogFactory;
import com.swapsharma.mvvm_android.util.RxImageConverters;
import com.swapsharma.mvvm_android.util.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class MainActivity extends BaseActivity implements MainMvpView, Handler.Callback {

    @BindView(R.id.pickedIv)
    ImageView ivPickedImage;
    @BindView(R.id.pickImageBtn)
    Button pickImageBtn;
    @BindView(R.id.createMosiacBtn)
    Button createMosaicBtn;
    @BindView(R.id.gridview)
    GridView grid;

    @Inject
    MainPresenter mainPresenter;
    private CustomGridAdapter customGridAdapter;

    private int rows;
    private int cols;
    private Subscription subscription;
    ThreadPoolExecutor executor;
    ArrayList<Bitmap> tileList;
    private Boolean isTiled = false;
    List<String> hexCodes;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainPresenter.attachView(this);

        grid.setVisibility(View.GONE);
        pickImageBtn.setOnClickListener(view -> mainPresenter.createTiledImage(Sources.GALLERY));
        createMosaicBtn.setOnClickListener(view -> mainPresenter.createPhotoMosaic());

        if (RxImagePicker.with(this).getActiveSubscription() != null) {
            RxImagePicker.with(this).getActiveSubscription().subscribe(this::onImagePicked);
        }
        executor = RxUtil.getExecutor();
    }

    private void createPhotoMosaic() {
        if (!isTiled) {
            Toast.makeText(MainActivity.this, "pls tile first", Toast.LENGTH_SHORT).show();
        } else {
            //find average color

            updateProgressBar(true);
            try {
                getHexCodes();
            } catch (ExecutionException e) {
                updateProgressBar(false);

                e.printStackTrace();
            } catch (InterruptedException e) {
                updateProgressBar(false);

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
        updateProgressBar(true);
        if (result instanceof Bitmap) {
            Bitmap scaledBitmapx = Bitmap.createScaledBitmap((Bitmap) result, 350, 350, true);
            ivPickedImage.setImageBitmap(scaledBitmapx);
            executor.execute(new SplitImageService(scaledBitmapx, 3000, new Handler(this)));
            rows = cols = (int) Math.sqrt(3000);

        } else {
            Toast.makeText(this, "some error occoured !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        tileList = (ArrayList) message.obj;

        ivPickedImage.setVisibility(View.GONE);
        //Getting the grid view and setting an adapter to it
        grid.setVisibility(View.VISIBLE);
        customGridAdapter = new CustomGridAdapter(this, tileList);
        grid.setAdapter(customGridAdapter);
        grid.setNumColumns((int) Math.sqrt(tileList.size()));
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
        mainPresenter.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void getHexCodes() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        TaskCallable task = new TaskCallable();
        Future<List<String>> future = executor.submit(task);
        hexCodes = future.get();
        updateProgressBar(false);

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
