/**
 ************************************** ॐ ***********************************
 ***************************** लोकाः समस्ताः सुखिनो भवन्तु॥**************************
 * <p/>
 * Quoter is a Quotes collection with daily notification and widget
 * Copyright (C) 2016  vishnu
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.islamologique.Athkar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdkarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    private AdView mAdView;
    private static String BANERID ="ca-app-pub-6786585078622584/1967265511";

    AdkarAdapter qa;
    RecyclerView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adkar);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6786585078622584/1967265511");
// TODO: Add adView to your view hierarchy.


        Toolbar toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);





        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.addView();

        lv = (RecyclerView)findViewById(R.id.quotesList);
        lv.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbh = new DBHelper(getApplicationContext());
        Cursor tags = dbh.getTags();

        int i = 0;
        final Menu menu = navigationView.getMenu();
        menu.add(R.id.tags, i, i, "الجميع");

        if(tags.moveToFirst()) {
            do {
                i++;
                menu.add(R.id.tags, tags.getInt(0), i, tags.getString(1));
            }while (tags.moveToNext());
        }

        RecyclerView.Adapter qa = new AdkarAdapter(getApplicationContext(),
                new DBHelper(getApplicationContext()).getQuotes(0, 0), this);
        lv.setAdapter(qa);


        menu.setGroupCheckable(R.id.tags, true, true);
        int count = 0;
        for (i = 0, count = navigationView.getChildCount(); i < count; i++) {
            final View child = navigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }



        new EANotificationManager().setAlarmIfNeeded(getApplicationContext());







    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            saveScrollPositon();
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        saveScrollPositon();
        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
        scrollToLastPosition();

    }

    private void scrollToLastPosition(){
        SharedPreferences pref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int y = pref.getInt("scroll-y", 0);
        if(lv != null ){
            //lv.smoothScrollToPosition(y);
            lv.scrollToPosition(y);
        }
    }

    private void saveScrollPositon(){
        if(lv != null ){
            int y = ((LinearLayoutManager)lv.getLayoutManager()).
                    findFirstCompletelyVisibleItemPosition();
            SharedPreferences pref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt("scroll-y", y);
            edit.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adkar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.random) {
            Intent random = new Intent(this, DailyAdkarActivity.class);
            random.putExtra("from_quote", true);
            startActivity(random);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        lv.setAdapter(null);
        qa = new AdkarAdapter(getApplicationContext(),
                new DBHelper(getApplicationContext()).getQuotes(id, 0), this);
        lv.setAdapter(qa);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(id+1).setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotoWebsite(View v){
        String url = "https://www.facebook.com/Morad.Derhourhi";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




}
