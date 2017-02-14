package com.swapsharma.mvvm_android.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swapsharma.mvvm_android.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadChunksThread implements Runnable {

    Handler handler;
    public static final String TAG = "LoadChunksThread";
    List<String> mhexCodes;
    Context mContext;

    public LoadChunksThread() {
    }

    public LoadChunksThread(List<String> hexCodes, Handler handler, Context context) {
        mhexCodes = hexCodes;
        this.handler = handler;
        mContext = context;
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : ");
        try {
            sendMessage(1, loadChunksFromServer(mhexCodes));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "EXCEPTION occured ");
        }
        Log.i(TAG, "Thread Completed ");
    }


    public void sendMessage(int what, ArrayList<Bitmap> loadedChunksList) {
        Message message = handler.obtainMessage(what, loadedChunksList);
        message.sendToTarget();
    }

    //add chunksize also here
    private ArrayList<Bitmap> loadChunksFromServer(List<String> hexCodes) throws IOException {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        Bitmap bitmap;
        for (int i = 0; i < hexCodes.size(); ++i) {
            try {
                System.out.println("....." + i + "...." + hexCodes.get(i));
                InputStream input = new java.net.URL(hexCodes.get(i)).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                bitmaps.add(bitmap);
                System.out.println("..sss..." + i + "...." + bitmaps.size());
            } catch (IOException e) {
                System.out.println("EXCEPTION<<>>>>>>>" + e);
                //adding white color in case color not found on server
                bitmaps.add(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gray_holder));
            }
        }
        return bitmaps;
    }
}
