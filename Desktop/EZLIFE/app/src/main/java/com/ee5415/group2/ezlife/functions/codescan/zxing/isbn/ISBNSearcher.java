package com.ee5415.group2.ezlife.functions.codescan.zxing.isbn;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.functions.codescan.zxing.ScanActivity;

import org.json.JSONObject;

public class ISBNSearcher extends Activity {
    private Button bt;
    private JSONObject json;
    private Intent intent;
    private TextView title,author,publisher,date,isbn,summary;
    private ImageView cover;

    private  BookInfo book;
    private Handler hd;
    private String barcodeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isbnsearcher);

        Bundle bundle = getIntent().getExtras();
        barcodeNum = bundle.getString("result");

        title = (TextView)findViewById(R.id.bookview_title);
        author = (TextView)findViewById(R.id.bookview_author);
        publisher = (TextView)findViewById(R.id.bookview_publisher);
        date = (TextView)findViewById(R.id.bookview_publisherdate);
        isbn = (TextView)findViewById(R.id.bookview_isbn);
        summary = (TextView)findViewById(R.id.bookview_summary);
        cover = (ImageView)findViewById(R.id.bookview_cover);
        String urlstr = "https://api.douban.com/v2/book/isbn/:" + barcodeNum;
        System.out.println(urlstr);
        Log.i("OUTPUT",urlstr);
        new DownloadThread(urlstr).start();

        hd = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                book = (BookInfo) msg.obj;
                title.setText(book.getTitle());
                author.setText(book.getAuthor());
                publisher.setText(book.getPublisher());
                date.setText(book.getPublishDate());
                isbn.setText(book.getISBN());
                summary.setText(book.getSummary());
                cover.setImageBitmap(book.getBitmap());
            }
        };

    }

    class DownloadThread extends Thread {
        String url = null;
        public DownloadThread(String urlstr) {
            url = urlstr;
        }
        public void run() {
            String result = Util.Download(url);
            Log.i("OUTPUT", "download over");
            BookInfo book = new Util().parseBookInfo(result);
            Log.i("OUTPUT", "parse over");
            Message msg = Message.obtain();
            msg.obj = book;
            hd.sendMessage(msg);
            Log.i("OUTPUT","send over");
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ScanActivity.class);
        this.finish();
        startActivity(intent);

        return;
    }
}
