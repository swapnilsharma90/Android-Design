package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;

import com.swapsharma.mvvm_android.network.Tile;
import com.swapsharma.mvvm_android.ui.activity.base.MvpView;

import java.util.List;


public interface MainMvpView extends MvpView {

    void showTiles();

    void showTilesWithColor(List<Tile> tilesList);

    void showTilesWithColor();

    void showError();

    void showTile(Bitmap tile);

}
