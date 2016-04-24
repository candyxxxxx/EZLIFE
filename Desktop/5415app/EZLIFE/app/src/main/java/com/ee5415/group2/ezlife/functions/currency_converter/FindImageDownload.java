package com.ee5415.group2.ezlife.functions.currency_converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Harry on 2016/3/3.
 */
public class FindImageDownload {
    protected Bitmap findImageDownload(String from, String to) {
        String url = "http://ichart.finance.yahoo.com/3m?" + from + to + "=x";
        System.out.println(url);
        URL myFileURL;
        Bitmap bitmap = null;
        try{
            myFileURL = new URL(url);
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}

