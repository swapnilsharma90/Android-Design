package com.swapsharma.mvvm_android.network;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swapsharma.mvvm_android.util.MyGsonTypeAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface TileService {

    //http://localhost:8765/color/32/32/abc123


//    String ENDPOINT = "http://10.0.2.2:8765/color/32/32/abc123";

    String ENDPOINT = "http://10.0.2.2:8765/color/32/32/";

//    @GET("tile")
//    Observable<Tile> getTile();

    @GET("{hexCode}")
    Observable<Bitmap> getTile(@Path("hexCode") String hexCode);


    /******** Helper class that sets up a new services *******/
    class Creator {

        public static TileService newTilesService() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TileService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(TileService.class);
        }
    }
}
