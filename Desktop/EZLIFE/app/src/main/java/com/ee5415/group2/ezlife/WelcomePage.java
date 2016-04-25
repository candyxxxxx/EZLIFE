package com.ee5415.group2.ezlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shushu on 4/8/16.
 */
public class WelcomePage extends Activity {

    private ImageView imageView;
    private Animation alphaAnimation;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        imageView = (ImageView) findViewById(R.id.welcome_image_view);
        textView = (TextView) findViewById(R.id.welcome_text_view);

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
        alphaAnimation.setFillEnabled(true); //启动Fill保持
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面

        imageView.setAnimation(alphaAnimation);
        textView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时结束欢迎界面并转到软件的主界面
                if (checkNetworkAvailable(WelcomePage.this) && isGpsEnabled(WelcomePage.this)){
                    Intent intent = new Intent(WelcomePage.this, MainTab.class);
                    startActivity(intent);

                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomePage.this);
                    builder.setMessage("Making sure the GPS has opened or connected to the internet.");
                    builder.setTitle("Warning");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.create().show();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });


//    //计时器
//    Timer timer = new Timer();
//        final Intent intent = new Intent(WelcomePage.this, MainTab.class);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(intent);
//                finish();
//            }
//        };
//
//        timer.schedule(timerTask, 3000);
//    }

    }



    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
