package com.swapsharma.mvvm_android.network;

public interface TileService {

    String ENDPOINT = "http://10.0.2.2:8765/color/32/32/";

//    @GET("")
//    Observable<Bitmap> getTile(@Path("hexCode") String hexCode);

    /******** Helper class that sets up a new services *******/
//    class Creator {
//
//        public static TileService newTilesService() {
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setLenient()
//                    .create();
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(TileService.ENDPOINT)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//            return retrofit.create(TileService.class);
//        }
//    }
}
