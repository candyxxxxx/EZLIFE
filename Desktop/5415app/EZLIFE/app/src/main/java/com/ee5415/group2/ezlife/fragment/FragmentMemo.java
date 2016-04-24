package com.ee5415.group2.ezlife.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.fragment.memo.OnRecordClickListener;
import com.ee5415.group2.ezlife.fragment.memo.RecordListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;


public class FragmentMemo extends Fragment {

    private MediaRecorder mRecorder;
    private File file, dir;
    private String path;
    private Context context;
    private Button backToDatePicker;
    private ListView lv;
    private DatePicker goToDate;
    private RecordListAdapter adapter;
    private LinearLayout ll;
    private TextView listDate;
    String goToPath;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_memo, null);
            listDate = (TextView) v.findViewById(R.id.listDate);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            goToDate = (DatePicker) v.findViewById(R.id.goToDate);
            lv = (ListView) v.findViewById(R.id.lv);
            backToDatePicker = (Button) v.findViewById(R.id.backToDatePicker);
            backToDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToDatePicker.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    listDate.setVisibility(View.GONE);
                }
            });

            context = v.getContext();
            path = context.getFilesDir().getAbsolutePath() + "/audio_records";


            Button click = (Button) v.findViewById(R.id.recordButton);
            click.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Toast.makeText(context, "recording", Toast.LENGTH_SHORT).show();

                            dir = new File(path);
                            file = new File(dir, "cache.aac");

                            if (!dir.exists()) {
                                dir.mkdir();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    fos.write("".getBytes());
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            mRecorder = new MediaRecorder();
                            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                            mRecorder.setOutputFile(file.getAbsolutePath());
                            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                            mRecorder.setAudioSamplingRate(44100);
                            mRecorder.setAudioEncodingBitRate(16);
                            try {
                                mRecorder.prepare();
                                mRecorder.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            break;

                        case MotionEvent.ACTION_UP:
                            Toast.makeText(context, "finish recording", Toast.LENGTH_SHORT).show();
                            try {
                                mRecorder.stop();
                                mRecorder.release();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mRecorder = null;


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View view = View.inflate(context, R.layout.save_record_dialog, null);
                            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.setDate);
                            final EditText editText = (EditText) view.findViewById(R.id.description);
                            builder.setView(view);
                            Calendar calendar = Calendar.getInstance();
                            datePicker.init(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH), null);

                            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String year = Integer.toString(datePicker.getYear());
                                    String month = Integer.toString(datePicker.getMonth() + 1);
                                    String day = Integer.toString(datePicker.getDayOfMonth());
                                    String description = editText.getText().toString();

                                    String dirName = path + "/" + year + "-" + month + "-" + day;
                                    String fileName = description + ".aac";

                                    File subDir = new File(dirName);
                                    if (!subDir.exists())
                                        subDir.mkdir();
                                    if (file.renameTo(new File(subDir, fileName))) {
                                        Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show();

                                        if (adapter != null)
                                            if (goToPath.equals(dirName)) {
                                                adapter.list.clear();
                                                File dir = new File(dirName);
                                                File[] subFiles = dir.listFiles();
                                                for (File f : subFiles)
                                                    adapter.list.add(f);
                                                adapter.notifyDataSetChanged();
                                            }


                                    } else {
                                        Toast.makeText(context, "Save unsuccessfully", Toast.LENGTH_SHORT).show();
                                        file.delete();
                                    }
                                }
                            });

                            Dialog dialog = builder.create();

                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
//                                delete the cache.aac
                                    file.delete();
                                }
                            });


                            dialog.show();
                            break;

                        default:
                            break;
                    }
                    return true;
                }
            });

            Calendar calendar = Calendar.getInstance();
            goToDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String date = Integer.toString(goToDate.getYear()) + "-"
                                    + Integer.toString(goToDate.getMonth() + 1) + "-"
                                    + Integer.toString(goToDate.getDayOfMonth());

                            goToPath = path + "/"
                                    + date;

                            File goToDir = new File(goToPath);
                            File[] files = goToDir.listFiles();

                            if ((!goToDir.exists()) || files.length == 0) {
                                Toast.makeText(context, "No records", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                ll.setVisibility(View.GONE);
                                backToDatePicker.setVisibility(View.VISIBLE);
                                listDate.setVisibility(View.VISIBLE);
                                listDate.setText(date);
                                lv.setVisibility(View.VISIBLE);
                                adapter = new RecordListAdapter(context, goToPath);
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new OnRecordClickListener(context));



                                /*
                                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                                lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                                    @Override
                                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                        MenuInflater inflater = mode.getMenuInflater();
                                        inflater.inflate(R.menu.cab, menu);
                                        adapter.mode = true;
                                        adapter.notifyDataSetChanged();
                                        return true;
                                    }

                                    @Override
                                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                        adapter.notifyDataSetChanged();
                                        return false;
                                    }

                                    @Override
                                    public void onDestroyActionMode(ActionMode mode) {
                                        adapter.mode = false;
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                                */
                            }
                        }
                    });


        }



        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null)
            parent.removeView(v);

        return v;
    }
}