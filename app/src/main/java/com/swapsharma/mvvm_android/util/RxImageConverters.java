package com.swapsharma.mvvm_android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxImageConverters {


    public static Observable<Bitmap> uriToBitmap(final Context context, final Uri uri) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Log.e(RxImageConverters.class.getSimpleName(), "Error converting uri", e);
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
