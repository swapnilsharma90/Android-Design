package com.swapsharma.mvvm_android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class LongThread implements Runnable {

    int threadNo;
    Handler handler;
    String imageUrl;
    int mchunknumbers;
    ImageView mImageView;


    public static final String TAG = "LongThread";

    public LongThread() {
    }

//    public LongThread(int threadNo, String imageUrl, Handler handler) {
//        this.threadNo = threadNo;
//        this.handler = handler;
//        this.imageUrl = imageUrl;
//    }



    public LongThread( ImageView imageView,int chunknumbers,Handler handler) {
        mchunknumbers=chunknumbers;
        mImageView = imageView;
        this.handler = handler;

    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);
       // getBitmap(imageUrl);
        sendMessage(1,splitImage( mImageView,mchunknumbers));
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


    private ArrayList<Bitmap> splitImage(ImageView image, int chunkNumbers) {
        //For the number of rows and columns of the grid to be displayed
        int rows, cols;
        //For height and width of the small image smallimage_s
        int smallimage_Height, smallimage_Width;
        //To store all the small image smallimage_s in bitmap format in this list
       ArrayList<Bitmap> smallimages = new ArrayList<Bitmap>(chunkNumbers);
//        smallimages = new ArrayList<Bitmap>(chunkNumbers);

        //Getting the scaled bitmap of the source image
        BitmapDrawable mydrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = mydrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = cols = (int) Math.sqrt(chunkNumbers);
        smallimage_Height = bitmap.getHeight() / rows;
        smallimage_Width = bitmap.getWidth() / cols;
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
