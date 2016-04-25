package com.ee5415.group2.ezlife.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.functions.codescan.zxing.ScanActivity;
import com.ee5415.group2.ezlife.functions.currency_converter.HKDConverter;
import com.ee5415.group2.ezlife.functions.map.Map;
import com.ee5415.group2.ezlife.functions.translate.TranslateActivity;
import com.ee5415.group2.ezlife.functions.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentFunction extends Fragment {
    private View v;
    LayoutInflater inflater;

    private ViewPager viewPager;
    private ImageView imageView;
    private TextView textView1, textView2, textView3;
    private ImageView imageView1, imageView2, imageView3;
    private List<View> views;
    private int offset, one;
    private int currIndex;
    private int bmpW;
    private View view1, view2, view3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (imageView != null) {
            Animation animation = new TranslateAnimation(one * currIndex, one * currIndex, 0, 0);
            animation.setFillAfter(true);
            animation.setDuration(1);
            imageView.startAnimation(animation);
        }

        if (v == null) {
            offset = 0;
            currIndex = 0;
            this.inflater = inflater;
            v = inflater.inflate(R.layout.fragment_function, null);
            initImageView();
            initTextView();
            initViewPager();
        }

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;

    }

    private void initImageView() {
        imageView = (ImageView) v.findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.slide_line).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / 3 - bmpW) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);
        one = offset * 2 + bmpW;
    }

    private void initTextView() {
        textView1 = (TextView) v.findViewById(R.id.subFunction_Finance);
        textView2 = (TextView) v.findViewById(R.id.subFunction_LifeAround);
        textView3 = (TextView) v.findViewById(R.id.subFunction_Management);
        imageView1 = (ImageView) v.findViewById(R.id.im1);
        imageView2 = (ImageView) v.findViewById(R.id.im2);
        imageView3 = (ImageView) v.findViewById(R.id.im3);
        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
        textView3.setOnClickListener(new MyOnClickListener(2));
        imageView1.setOnClickListener(new MyOnClickListener(0));
        imageView2.setOnClickListener(new MyOnClickListener(1));
        imageView3.setOnClickListener(new MyOnClickListener(2));
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        views = new ArrayList<View>();
        view1 = inflater.inflate(R.layout.function_finace, null);
        view2 = inflater.inflate(R.layout.function_lifearound, null);
        view3 = inflater.inflate(R.layout.function_management, null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        view1.findViewById(R.id.startCurrency).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HKDConverter.class));
            }
        });
        view2.findViewById(R.id.startMap).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Map.class));
            }
        });
        view2.findViewById(R.id.startWeather).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeatherActivity.class));
            }
        });
        view3.findViewById(R.id.startTranslation).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TranslateActivity.class));
            }
        });
        view3.findViewById(R.id.startIsbn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanActivity.class));
            }
        });
    }

        public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            imageView.startAnimation(animation);
        }
    }
}