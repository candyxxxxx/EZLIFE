package com.ee5415.group2.ezlife.fragment.memo;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;

public class OnRecordClickListener implements AdapterView.OnItemClickListener {
    Context context;
    String[] s = {"Play", "Delete"};
    File f;
    AsyncPlayer player;
    boolean isPlay;

    public OnRecordClickListener(Context context) {
        this.context = context;
        player = new AsyncPlayer(context.getPackageName());
//        isPlay = false;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
        final RecordListAdapter adapter = (RecordListAdapter) parent.getAdapter();
        f = adapter.list.get(position);

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
                        }
                    }
                })
                .show();

//        if (isPlay) {
//            player.stop();
//            isPlay = false;
//        } else {
//            Uri uri = Uri.fromFile(f);
//            player.play(context, uri, false, AudioManager.STREAM_MUSIC);
//            isPlay = true;
    }
}
