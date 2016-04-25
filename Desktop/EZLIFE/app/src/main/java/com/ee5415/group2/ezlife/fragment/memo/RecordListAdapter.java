package com.ee5415.group2.ezlife.fragment.memo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ee5415.group2.ezlife.R;

import java.io.File;
import java.util.ArrayList;


public class RecordListAdapter extends BaseAdapter {
    Context context;
    String path;
    LayoutInflater layoutInflater;
    public Boolean mode = false;

    public RecordListAdapter(Context context, String path) {
        this.context = context;
        this.path = path;
        this.layoutInflater = LayoutInflater.from(context);
        scanFiles(path);
    }

    public ArrayList<File> list = new ArrayList<File>();

    public void scanFiles(String path) {
        list.clear();
        File dir = new File(path);
        File[] subFiles = dir.listFiles();
        for (File f : subFiles)
            list.add(f);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView name;
        ImageView img;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_record, null);
            name = (TextView) convertView.findViewById(R.id.fileName);
            img = (ImageView) convertView.findViewById(R.id.imageAdded);
        } else {
            name = (TextView) convertView.findViewById(R.id.fileName);
            img = (ImageView) convertView.findViewById(R.id.imageAdded);
        }


        File f = list.get(position);
        String fileName = f.getName().substring(0, f.getName().lastIndexOf(".aac"));
        ;
        name.setText(fileName);
        name.setTextColor(Color.BLACK);

        File image = new File(path + "-img/" + fileName + ".jpg");
        if (image.exists()) {
            Bitmap bit = BitmapFactory.decodeFile(path + "-img/" + fileName + ".jpg");
            img.setImageBitmap(bit);
            img.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
