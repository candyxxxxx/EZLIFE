package com.ee5415.group2.ezlife.functions.currency_converter;

import android.graphics.Bitmap;

/**
 * Created by Harry on 2016/3/3.
 */
public class ImageDownloadInfo {
    private Bitmap bitmap;

    public ImageDownloadInfo(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap(){
        return this.bitmap;
    }
}

