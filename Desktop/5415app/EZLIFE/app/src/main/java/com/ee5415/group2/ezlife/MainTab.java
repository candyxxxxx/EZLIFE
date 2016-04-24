package com.ee5415.group2.ezlife;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.LinearLayout;

import com.ee5415.group2.ezlife.fragment.FragmentFunction;
import com.ee5415.group2.ezlife.fragment.FragmentHome;
import com.ee5415.group2.ezlife.fragment.FragmentMemo;
import com.ee5415.group2.ezlife.fragment.FragmentMy;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainTab extends FragmentActivity {

    public FragmentTabHost mTabHost;
    private String[] TabTag = {"tab1", "tab2", "tab3", "tab4"};

    private Integer[] ImgTab = {R.layout.tab_main_home,
            R.layout.tab_main_function, R.layout.tab_main_memo, R.layout.tab_main_my};

    private Class[] ClassTab = {FragmentHome.class, FragmentFunction.class,
            FragmentMemo.class, FragmentMy.class};

    private Integer[] StyleTab = {R.color.grey_background, R.color.grey_background, R.color.grey_background,
            R.color.grey_background};

    private Drawer result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintabs);


        displayButtons();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName(R.string.drawer_item_settings);

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                //.withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();

        result.setSelection(item1,true);
        result.setSelection(item2);


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);

        for (int i = 0; i < TabTag.length; i++) {
            View v = getLayoutInflater().inflate(ImgTab[i], null);
            LinearLayout tv_lay = (LinearLayout) v.findViewById(R.id.layout_back);
            tv_lay.setBackgroundResource(StyleTab[i]);
            mTabHost.addTab(
                    mTabHost.newTabSpec(TabTag[i]).setIndicator(v),
                    ClassTab[i], null);

        }
        mTabHost.getTabWidget().setDividerDrawable(R.color.white);

    }

    //---------------FloatView
//    @Override
//    protected void onStart() {
//        // TODO Auto-generated method stub
//        Intent intent = new Intent(this, FloatViewService.class);
//        //启动FloatViewService
//        startService(intent);
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        // 销毁悬浮窗
//        Intent intent = new Intent(this, FloatViewService.class);
//        //终止FloatViewService
//        stopService(intent);
//        super.onStop();
//    }


    public void customize(View view) {
        Intent i = new Intent(this, UserSettingsActivity.class);
        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                displayButtons();
                break;
        }
    }

    public void displayButtons(){
       /* Button currency = (Button) findViewById(R.id.home_currency);
        Button weather = (Button) findViewById(R.id.home_weather);
        Button translation = (Button) findViewById(R.id.home_translation);
        Button isbn = (Button) findViewById(R.id.home_isbn);

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        if(sharedPrefs.getBoolean("Currency", false))
            currency.setVisibility(View.GONE);
        else
            currency.setVisibility(View.VISIBLE);

        if(sharedPrefs.getBoolean("Weather", false))
            weather.setVisibility(View.GONE);
        else
            weather.setVisibility(View.VISIBLE);

        if(sharedPrefs.getBoolean("Translation", false))
            translation.setVisibility(View.GONE);
        else
            translation.setVisibility(View.VISIBLE);

        if(sharedPrefs.getBoolean("ISBN", false))
            isbn.setVisibility(View.GONE);
        else
            isbn.setVisibility(View.VISIBLE);
    */
    }
}

