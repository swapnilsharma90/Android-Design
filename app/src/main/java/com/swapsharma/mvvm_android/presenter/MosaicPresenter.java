package com.swapsharma.mvvm_android.presenter;

import com.swapsharma.mvvm_android.di.ConfigPersistent;
import com.swapsharma.mvvm_android.manager.DataManager;
import com.swapsharma.mvvm_android.ui.activity.MainMvpView;

import javax.inject.Inject;

import rx.Subscription;


@ConfigPersistent
public class MosaicPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MosaicPresenter(DataManager dataManager) {
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


}
