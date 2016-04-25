package com.ee5415.group2.ezlife.functions.codescan.zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.functions.codescan.zxing.isbn.ISBNSearcher;

public class ScanActivity extends Activity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static int REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent intent = new Intent();
        intent.setClass(ScanActivity.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if (bundle.getString("result").contains("http")){
                        dialog(data);
                    } else {
                        Intent intent = new Intent(this, ISBNSearcher.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
    protected void dialog(Intent data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setMessage("Sure to go to the external links？");
        builder.setTitle("Tips");
        final Bundle bundle = data.getExtras();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(bundle.getString("result")));
                startActivity(intent);
                Intent intent2 = new Intent();
                intent2.setClass(ScanActivity.this, MipcaActivityCapture.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent2, SCANNIN_GREQUEST_CODE);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(ScanActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        builder.create().show();
    }
    @Override
    public void onBackPressed() {
        this.finish();
        return;
    }
}
