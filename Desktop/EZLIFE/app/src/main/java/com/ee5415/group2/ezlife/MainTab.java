package com.ee5415.group2.ezlife;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.LinearLayout;

import com.ee5415.group2.ezlife.fragment.FragmentFunction;
import com.ee5415.group2.ezlife.fragment.FragmentHome;
import com.ee5415.group2.ezlife.fragment.FragmentMemo;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainTab extends FragmentActivity {

    public FragmentTabHost mTabHost;
    private String[] TabTag = {"tab1", "tab2", "tab3"};

    private Integer[] ImgTab = {R.layout.tab_main_home,
            R.layout.tab_main_function, R.layout.tab_main_memo};

    private Class[] ClassTab = {FragmentHome.class, FragmentFunction.class,
            FragmentMemo.class};

    private Integer[] StyleTab = {R.color.grey_background, R.color.grey_background, R.color.grey_background};

    private Drawer result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintabs);

        Iconify
                .with(new FontAwesomeModule());

        displayButtons();

        //--------Drawer
        AccountHeader headerResult = null;
        result = null;
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
              /* .addProfiles(
                        profile,
                        profile2
              )*/
                .withSavedInstance(savedInstanceState)
                .build();


        //----------create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Calculator").withIdentifier(1).withSelectable(false).withIcon(R.mipmap.icon_calculator),
                        new PrimaryDrawerItem().withName("Take Photo").withIdentifier(2).withSelectable(false).withIcon(R.mipmap.icon_photo),
                        new PrimaryDrawerItem().withName("Search").withIdentifier(3).withSelectable(false).withIcon(R.mipmap.icon_ok),
                        new ExpandableDrawerItem().withName("Share").withIdentifier(4).withSelectable(false).withIcon(R.mipmap.icon_share).withSubItems(
                                new SecondaryDrawerItem().withName("Wechat").withLevel(2).withIdentifier(111).withSelectable(false).withIcon(R.mipmap.icon_wechat),
                                new SecondaryDrawerItem().withName("Weibo").withLevel(2).withIdentifier(112).withSelectable(false).withIcon(R.mipmap.icon_weibo)
                        ),
                        new PrimaryDrawerItem().withName("About").withIdentifier(6).withSelectable(false).withIcon(R.mipmap.icon_about)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                                   @Override
                                                   public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                   //check if the drawerItem is set.
                   //there are different reasons for the drawerItem to be null
                   //--> click on the header
                   //--> click on the footer
                   //those items don't contain a drawerItem
                   if (drawerItem != null) {
                       Intent intent = null;
                       if (drawerItem.getIdentifier() == 1) {
                           intent = new Intent(MainTab.this,Calculator.class);
                           startActivity(intent);
                       } else if (drawerItem.getIdentifier() == 2) {
                           intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                           startActivity(intent);
                       } else if (drawerItem.getIdentifier() == 3) {
                           intent = new Intent(MainTab.this, Webview.class);
                           startActivity(intent);
                       } else if (drawerItem.getIdentifier() == 111) {
                           String pakName = "";
                           intent = new Intent(Intent.ACTION_SEND);
                           intent.setType("text/plain");
                           pakName = "com.tencent.mm";
                           intent.setPackage(pakName);
                           intent.putExtra(Intent.EXTRA_TEXT, "this is to share");
                           startActivity(Intent.createChooser(intent, ""));
                       }
                       if (drawerItem.getIdentifier() == 112) {
                           String pakName = "";
                           intent = new Intent(Intent.ACTION_SEND);
                           intent.setType("text/plain");
                           pakName = "com.sina.weibo";
                           intent.setPackage(pakName);
                           intent.putExtra(Intent.EXTRA_TEXT, "this is share");
                           startActivity(Intent.createChooser(intent, ""));
                       }
                       if (drawerItem.getIdentifier() == 6) {

                           intent = new Intent(MainTab.this, About.class);
                           startActivity(intent);
                       }
                       if (intent != null) {
                           MainTab.this.startActivity(intent);
                       }
                   }
                   return false;
               }
           }

                )
                .

                        withSavedInstance(savedInstanceState)

                .

                        withShowDrawerOnFirstLaunch(true)

                .

                        build();


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
    public void onBackPressed() {
        if (result.isDrawerOpen())
            result.closeDrawer();
        else
            super.onBackPressed();
    }
}

