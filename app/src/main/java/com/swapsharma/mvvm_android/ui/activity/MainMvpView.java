package com.swapsharma.mvvm_android.ui.activity;

import com.swapsharma.mvvm_android.ui.activity.base.MvpView;


public interface MainMvpView extends MvpView {
    void showError();

    void showMosaicImage();

    void showTiledImage();
}
