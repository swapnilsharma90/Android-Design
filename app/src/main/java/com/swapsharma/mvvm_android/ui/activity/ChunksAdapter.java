package com.swapsharma.mvvm_android.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.swapsharma.mvvm_android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by swapsharma on 2/1/17.
 */

public class ChunksAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mhexCodes;
    private ArrayList<Bitmap> imageChunks;


    private int imageWidth, imageHeight;

    @Inject
    public ChunksAdapter(Context context, ArrayList<Bitmap> images) {
        mContext = context;
        imageChunks = images;
        imageWidth = images.get(0).getWidth();
        imageHeight = images.get(0).getHeight();
    }

    @Override
    public int getCount() {
        return imageChunks.size();
    }

    @Override
    public Object getItem(int position) {
        return imageChunks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        View grid;
        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.grid_single_item, parent, false);
        } else {
            grid = (View) convertView;
        }
        ImageView imageView = (ImageView) grid.findViewById(R.id.image);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth + 2, imageHeight - 2));
        imageView.setImageBitmap(imageChunks.get(position));
        return grid;
    }
}
