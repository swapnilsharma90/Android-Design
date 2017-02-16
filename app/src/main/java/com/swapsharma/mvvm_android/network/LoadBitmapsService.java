package com.swapsharma.mvvm_android.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.swapsharma.mvvm_android.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadBitmapsService implements Runnable {

    Handler handler;
    public static final String TAG = "LoadBitmapsService";
    List<String> mhexCodes;
    Context mContext;

    public LoadBitmapsService() {
    }

    public LoadBitmapsService(List<String> hexCodes, Handler handler, Context context) {
        mhexCodes = hexCodes;
        this.handler = handler;
        mContext = context;
    }

    @Override
    public void run() {
        try {
            sendMessage(1, fetchBitmaps(mhexCodes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(int what, ArrayList<Bitmap> loadedChunksList) {
        Message message = handler.obtainMessage(what, loadedChunksList);
        message.sendToTarget();
    }

    private ArrayList<Bitmap> fetchBitmaps(List<String> hexCodes) throws IOException {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        Bitmap bitmap;
        for (int i = 0; i < hexCodes.size(); ++i) {
            try {
                System.out.println("....." + i + "...." + hexCodes.get(i));
                InputStream input = new java.net.URL(hexCodes.get(i)).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                bitmaps.add(bitmap);
            } catch (IOException e) {
                System.out.println("EXCEPTION<<>>>>>>>" + e);
                //adding white color in case color not found on server
                bitmaps.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gray_holder));
            }
        }
        return bitmaps;
    }
}
