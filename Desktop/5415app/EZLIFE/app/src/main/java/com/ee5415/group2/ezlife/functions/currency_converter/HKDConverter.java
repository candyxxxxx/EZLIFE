package com.ee5415.group2.ezlife.functions.currency_converter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ee5415.group2.ezlife.R;

import java.text.DecimalFormat;

public class HKDConverter extends BaseActivity {
    private EditText hkdET;
    private EditText rmbET;
    private EditText usdET;
    private EditText gbpET;
    private EditText eurET;
    private EditText jpyET;
    private ListView lv;
    private ImageView img;

    private DecimalFormat df = new DecimalFormat("#.##");
    private double temp = 0;
    private ConvertHandler ch = new ConvertHandler();
    private ImageDownloadHandler idh = new ImageDownloadHandler();
    private Resources resource;

    private String[] bufferString = {"HKD", "CNY", "USD", "GBP", "EUR", "JPY"};
    private String[] name;
    public static String[] mTitleArray;
    public static String[] mLanguageArray;


    private static final int LOAD_SUCCESS = 1;
    private static final int LOAD_ERROR = -1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCCESS:
                    img.setImageBitmap(adaptive(idh));
                    break;
                case LOAD_ERROR:
                    break;
            }
        }
    };
    private static final int LOAD_SUCCESS_2 = 1;
    private static final int LOAD_ERROR_2 = -1;
    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SUCCESS_2:
                    hkdET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (hkdET.getText().toString().length() >= 1) {
                                Convert(hkdET.getText().toString());
                                temp = Double.parseDouble(hkdET.getText().toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    hkdET.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_DEL)) {
                                temp = 0;
                                clearEditText();
                                return true;
                            }
                            return false;
                        }
                    });
                    break;
                case LOAD_ERROR_2:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleArray = getResources().getStringArray(R.array.currency_name);

        setContentView(R.layout.activity_hkdconverter);
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.alert, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        initialized();
        MyThread2 M = new MyThread2();
        new Thread(M).start();
    }


    public void onConfigurationChanged(Configuration newConfig) {
        switchLanguage(PreferenceUtil.getString("language", "en"));
        super.onConfigurationChanged(newConfig);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(hkdET.getWindowToken(), 0);
        setContentView(R.layout.activity_hkdconverter);
        initialized();
        hkdET.setText(String.valueOf(temp));
        Convert(String.valueOf(temp));
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lv.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.titles_item, name));
            lv.setOnItemClickListener(new ListView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    idh.setFrom(bufferString[0]);
                    idh.setTo(bufferString[position + 1]);
                    MyThread M = new MyThread();
                    new Thread(M).start();
                }
            });
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            hkdET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (hkdET.getText().toString().length() >= 1) {
                        Convert(hkdET.getText().toString());
                        temp = Double.parseDouble(hkdET.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            hkdET.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_DEL)) {
                        temp = 0;
                        clearEditText();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu1:
                mLanguageArray = getResources().getStringArray(R.array.language_item);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.menu1);
                builder.setItems(mLanguageArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Configuration config = resource.getConfiguration();
                        if (item == 0)
                            switchLanguage("zh");
                        else if (item == 1)
                            switchLanguage("en");
                        finish();
                        Intent intent = new Intent(HKDConverter.this, HKDConverter.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();
                return true;
            case R.id.menu2:
                openOptionsDialog();
                return true;
            case R.id.menu3:
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://finance.yahoo.com/"));
                startActivity(intent);
                return true;
            case R.id.menu4:
                finish();
                return true;
        }
        return false;
    }

    public void openOptionsDialog() {
        new AlertDialog.Builder(HKDConverter.this)
                .setTitle(R.string.about)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

    public void clearEditText() {
        hkdET.setText("");
        rmbET.setText("");
        usdET.setText("");
        gbpET.setText("");
        eurET.setText("");
        jpyET.setText("");
    }

    private void Convert(String st) {
        double originalNum = Double.parseDouble(st);
        rmbET.setText(String.valueOf(df.format(ch.getCi().getResult(0) * originalNum)));
        usdET.setText(String.valueOf(df.format(ch.getCi().getResult(1) * originalNum)));
        gbpET.setText(String.valueOf(df.format(ch.getCi().getResult(2) * originalNum)));
        eurET.setText(String.valueOf(df.format(ch.getCi().getResult(3) * originalNum)));
        jpyET.setText(String.valueOf(df.format(ch.getCi().getResult(4) * originalNum)));
        for (int i = 0; i < 5; i++)
            name[i] = name[i] + "\n" + ch.getCi().getResult(i);
    }

    private void initialized() {
        name = getResources().getStringArray(R.array.currency_name);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, name));
        hkdET = (EditText) findViewById(R.id.hkdET);
        rmbET = (EditText) findViewById(R.id.rmbET);
        rmbET.setEnabled(false);
        usdET = (EditText) findViewById(R.id.usdET);
        usdET.setEnabled(false);
        gbpET = (EditText) findViewById(R.id.gbpET);
        gbpET.setEnabled(false);
        eurET = (EditText) findViewById(R.id.eurET);
        eurET.setEnabled(false);
        jpyET = (EditText) findViewById(R.id.jpyET);
        jpyET.setEnabled(false);
        img = (ImageView) findViewById(R.id.resultImageView);
        resource =getResources();
    }

    private Bitmap adaptive(ImageDownloadHandler idh) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels + 1;
        int height = dm.heightPixels + 1;
        int t_width;
        int t_height;
        if (idh.getidi().getBitmap().getWidth() > width || idh.getidi().getBitmap().getHeight() > height) {
            t_width = width;
            t_height = idh.getidi().getBitmap().getHeight() * width / idh.getidi().getBitmap().getWidth();
            if (t_height > height) {
                t_width = t_width * height / t_height;
                t_height = height;
            }
        } else if (idh.getidi().getBitmap().getWidth() < width && idh.getidi().getBitmap().getHeight() < height) {
            t_width = width;
            t_height = idh.getidi().getBitmap().getHeight() * width / idh.getidi().getBitmap().getWidth();
            if (t_height > height) {
                t_width = t_width * height / t_height;
                t_height = height;
            }
        } else {
            t_width = idh.getidi().getBitmap().getWidth();
            t_height = idh.getidi().getBitmap().getHeight();
        }
        Bitmap bt = Bitmap.createScaledBitmap(idh.getidi().getBitmap(), t_width, t_height, true);
        return bt;
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                idh.imageDownloadHandler();
                handler.sendEmptyMessage(LOAD_SUCCESS);
            } catch (Exception e) {
                handler.sendEmptyMessage(LOAD_ERROR);
                e.printStackTrace();
            }
        }
    }

    class MyThread2 implements Runnable {
        @Override
        public void run() {
            try {
                ch.convertHandler();
                handler2.sendEmptyMessage(LOAD_SUCCESS_2);
            } catch (Exception e) {
                handler.sendEmptyMessage(LOAD_ERROR_2);
                e.printStackTrace();
            }
        }
    }
}
