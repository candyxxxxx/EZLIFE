package com.ee5415.group2.ezlife;

import android.app.Activity;
import android.content.Intent;
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
                Intent intent = new Intent(WelcomePage.this, MainTab.class);
                startActivity(intent);
                finish();
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
}
