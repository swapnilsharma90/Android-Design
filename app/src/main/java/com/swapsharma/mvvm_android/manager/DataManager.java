package com.swapsharma.mvvm_android.manager;

import android.graphics.Bitmap;

import com.swapsharma.mvvm_android.network.TileService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;


@Singleton
public class DataManager {

    private final TileService mTilessService;

    @Inject
    public DataManager(TileService tilesService) {
        mTilessService = tilesService;
    }

    public Observable<Bitmap> getTiles(String hexCode) {
        return mTilessService.getTile(hexCode);
    }

}


