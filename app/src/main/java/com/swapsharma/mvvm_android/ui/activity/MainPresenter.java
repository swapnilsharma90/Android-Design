package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;

import com.swapsharma.mvvm_android.di.ConfigPersistent;
import com.swapsharma.mvvm_android.manager.DataManager;
import com.swapsharma.mvvm_android.ui.activity.base.BasePresenter;
import com.swapsharma.mvvm_android.util.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void loadTiles(String hexCode) {
        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getTiles(hexCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                        System.out.println("1111...d");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("11111....f...."+e.toString());


                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(Bitmap tile) {
                        System.out.println("11111....g");

                        if (null != tile) {
                            getMvpView().showTile(tile);
                        } else {
                            getMvpView().showTilesWithColor();
                        }
                    }
                });
    }
}
