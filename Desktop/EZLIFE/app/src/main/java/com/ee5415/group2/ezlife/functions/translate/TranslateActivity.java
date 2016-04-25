package com.ee5415.group2.ezlife.functions.translate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ee5415.group2.ezlife.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Locale;

import library.src.main.java.cn.refactor.library.ExpandableLayout;

public class TranslateActivity extends Activity implements TextToSpeech.OnInitListener {

    private ExpandableLayout mExpandableLayout;
    private ImageView querenBt;
    private ImageView speakBt;
    private EditText bianjiEt;
    private TextView xianshiTv;
    private Handler myHandler;
    private String value;
    private String result;
    private String result1;
    private TextToSpeech speak = null;

    private ImageButton add_function;
    private Button add;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        querenBt= (ImageView) findViewById(R.id.switcher);
        speakBt = (ImageView)findViewById(R.id.speak);
        bianjiEt = (EditText)findViewById(R.id.et1);
        xianshiTv = (TextView)findViewById(R.id.tv1);

        mExpandableLayout = (ExpandableLayout) findViewById(R.id.expandableLayout);
        mExpandableLayout.setSwitcher(speakBt);
        mExpandableLayout.setExpandInterpolator(new BounceInterpolator());
        mExpandableLayout.setCollapseInterpolator(new AccelerateDecelerateInterpolator());
        mExpandableLayout.setExpandDuration(800);
        mExpandableLayout.setCollapseDuration(400);
        mExpandableLayout.setOnStateChangedListener(new ExpandableLayout.OnStateChangedListener() {
            @Override
            public void onPreExpand() {
                Log.d("ExpandableLayout", "onPreExpand");
            }

            @Override
            public void onPreCollapse() {
                Log.d("ExpandableLayout", "onPreCollapse");
            }

            @Override
            public void onExpanded() {
                Log.d("ExpandableLayout", "onExpanded");
            }

            @Override
            public void onCollapsed() {
                Log.d("ExpandableLayout", "onCollapsed");
            }
        });


        myHandler = new MyHandler();
        speak = new TextToSpeech(this,  this);
        speakBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speak.speak(result1, TextToSpeech.QUEUE_ADD, null);
            }
        });
        querenBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                result = bianjiEt.getText().toString();
                MyThread M = new MyThread();
                new Thread(M).start();
                mExpandableLayout.toggle();
            }
        });

    }

    class MyThread implements Runnable{
        String danci;
        String yinbiao;
        String shiyi;
        String jieguo;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 命名空间
            String nameSpace = "http://WebXml.com.cn/";
            // 调用的方法名称
            String methodName = "TranslatorString";
            // EndPoint
            String endPoint = "http://fy.webxml.com.cn/webservices/EnglishChinese.asmx";
            // SOAP Action
            String soapAction = "http://WebXml.com.cn/TranslatorString";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);

            // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
            rpc.addProperty("wordKey", result);

            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取返回的数据

            try {
                if (envelope.getResponse() != null) {
                    SoapObject object = (SoapObject)envelope.getResponse();
                    danci = object.getProperty(0).toString();
                    yinbiao = object.getProperty(1).toString();
                    shiyi = object.getProperty(3).toString();
                    result1 = danci;
                    jieguo =danci + "\n" + "/" + yinbiao + "/" + "\n" +shiyi;
                }
            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString(value,jieguo);
            msg.setData(b);
            TranslateActivity.this.myHandler.sendMessage(msg);
        }
    }
    class MyHandler extends Handler{
        public MyHandler(){
        }
        public MyHandler(Looper L){
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Bundle b = msg.getData();
            result = b.getString(value);
            xianshiTv.setText(result);
        }
    }
    @Override
    public void onInit(int arg0) {
        // TODO Auto-generated method stub
        speak.setLanguage(Locale.US);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (speak != null)
            speak.shutdown();//关闭tts引擎
    }
}
