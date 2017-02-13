package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LongThread implements Runnable {

    int threadNo;
    Handler handler;
    String imageUrl;
    int mchunknumbers;
    Bitmap scaledBitmap;
    public static final String TAG = "LongThread";

    List<String> mhexCodes;
    Handler hexHandler;



    public LongThread() {
    }

    public LongThread(List<String> hexCodes, Handler handler) {
        mhexCodes = hexCodes;
        this.hexHandler = handler;
    }



    public LongThread(Bitmap scaledBitmapx, int chunknumbers, Handler handler) {
        mchunknumbers = chunknumbers;
        scaledBitmap = scaledBitmapx;
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);
        // getBitmap(imageUrl);
        sendMessage(1, splitImage(scaledBitmap, mchunknumbers));
        Log.i(TAG, "Thread Completed " + threadNo);
    }

    public void sendMessage(int what, ArrayList<Bitmap> splittedList) {
        Message message = handler.obtainMessage(what, splittedList);
        message.sendToTarget();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(url).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            // Do extra processing with the bitmap
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private ArrayList<Bitmap> splitImage(Bitmap scaledBitmapxx, int chunkNumbers) {
        //For the number of rows and columns of the grid to be displayed
        int rows, cols;
        //For height and width of the small image smallimage_s
        int smallimage_Height, smallimage_Width;
        //To store all the small image smallimage_s in bitmap format in this list
        ArrayList<Bitmap> smallimages = new ArrayList<Bitmap>(chunkNumbers);
        //Getting the scaled bitmap of the source image
//        BitmapDrawable mydrawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = mydrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(scaledBitmapxx, scaledBitmapxx.getWidth(), scaledBitmapxx.getHeight(), true);
        rows = cols = (int) Math.sqrt(chunkNumbers);
        smallimage_Height = scaledBitmapxx.getHeight() / rows;
        smallimage_Width = scaledBitmapxx.getWidth() / cols;
        //xCo and yCo are the pixel positions of the image smallimage_s
        int yCo = 0;
        for (int x = 0; x < rows; x++) {
            int xCo = 0;
            for (int y = 0; y < cols; y++) {
                smallimages.add(Bitmap.createBitmap(scaledBitmap, xCo, yCo, smallimage_Width, smallimage_Height));
                xCo += smallimage_Width;
            }
            yCo += smallimage_Height;
        }
        return smallimages;
    }
}
