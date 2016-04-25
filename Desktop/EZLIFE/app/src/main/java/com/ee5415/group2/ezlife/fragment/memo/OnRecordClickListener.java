package com.ee5415.group2.ezlife.fragment.memo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.ee5415.group2.ezlife.R;

import java.io.File;

public class OnRecordClickListener implements AdapterView.OnItemClickListener {
    Context context;
    String[] s = {"Play", "Delete"};
    File f;
    AsyncPlayer player;

    public OnRecordClickListener(Context context) {
        this.context = context;
        player = new AsyncPlayer(context.getPackageName());
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
        final RecordListAdapter adapter = (RecordListAdapter) parent.getAdapter();
        f = adapter.list.get(position);
        final String img = f.getParent() + "-img/" + f.getName().substring(0, f.getName().lastIndexOf(".aac")) + ".jpg";
        final File image = new File(img);


        String[] s = {"Play", "Delete"};
        new AlertDialog.Builder(context)
                .setTitle("Operations")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Uri uri = Uri.fromFile(f);
                            player.play(context, uri, false, AudioManager.STREAM_MUSIC);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View view = View.inflate(context, R.layout.play_record_dialog, null);
                            ImageView imageView = (ImageView) view.findViewById(R.id.playDialogImage);
                            if (!image.exists()) {
                                imageView.setImageResource(R.drawable.play);
                                imageView.setMaxHeight(50);
                            } else {
                                Bitmap bit = BitmapFactory.decodeFile(img);
                                imageView.setImageBitmap(bit);
                            }
                            builder.setTitle(f.getName().substring(0, f.getName().lastIndexOf(".aac")));
                            builder.setView(view);
                            builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    player.stop();
                                }
                            });
                            Dialog dialog1 = builder.create();

                            dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    player.stop();
                                }
                            });


                            dialog1.show();

                        } else if (which == 1) {
                            adapter.list.clear();
                            File dir = new File(f.getParent());
                            f.delete();
                            File[] subFiles = dir.listFiles();
                            for (File f : subFiles)
                                adapter.list.add(f);
                            adapter.notifyDataSetChanged();
                            if (adapter.list.size() == 0)
                                dir.delete();

                            if (image.exists()) {
                                File dir1 = new File(image.getParent());
                                image.delete();
                                File[] subFiles1 = dir1.listFiles();
                                if (subFiles1.length == 0)
                                    dir1.delete();
                            }
                        }
                    }
                })
                .show();
    }
}
