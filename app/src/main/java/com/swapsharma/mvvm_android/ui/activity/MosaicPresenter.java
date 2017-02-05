package com.swapsharma.mvvm_android.ui.activity;

import com.swapsharma.mvvm_android.di.ConfigPersistent;
import com.swapsharma.mvvm_android.manager.DataManager;
import com.swapsharma.mvvm_android.ui.activity.base.BasePresenter;

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

//
//    public void loadTiles(String hexCode) {
//        checkViewAttached();
//        RxUtil.unsubscribe(mSubscription);
//        mSubscription = mDataManager.getTiles(hexCode)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Bitmap>() {
//                    @Override
//                    public void onCompleted() {
//
//                        System.out.println("1111...d");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("11111....f...."+e.toString());
//
//
//                        getMvpView().showError();
//                    }
//
//                    @Override
//                    public void onNext(Bitmap tile) {
//                        System.out.println("11111....g");
//
//                        if (null != tile) {
//                            getMvpView().showTile(tile);
//                        } else {
//                            getMvpView().showTilesWithColor();
//                        }
//                    }
//                });
//    }
}
