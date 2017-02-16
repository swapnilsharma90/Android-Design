package com.swapsharma.mvvm_android.presenter;

import com.swapsharma.mvvm_android.di.scopes.ConfigPersistent;
import com.swapsharma.mvvm_android.model.Sources;
import com.swapsharma.mvvm_android.ui.activity.MainMvpView;

import javax.inject.Inject;

import rx.Subscription;


@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private Subscription mSubscription;

    @Inject
    public MainPresenter() {
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

    public void createPhotoMosaic() {
        getMvpView().showMosaicImage();
    }

    public void createTiledImage(Sources sources) {
        getMvpView().showTiledImage();
    }

}
