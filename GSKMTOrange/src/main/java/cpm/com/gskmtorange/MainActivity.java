package cpm.com.gskmtorange;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GeoTag.GeoTagStoreList;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.dailyentry.FutureJCPActivity;
import cpm.com.gskmtorange.dailyentry.PlanogramPDFActivity;
import cpm.com.gskmtorange.dailyentry.ServiceActivity;
import cpm.com.gskmtorange.dailyentry.SettingsActivity;
import cpm.com.gskmtorange.dailyentry.StoreListActivity;
import cpm.com.gskmtorange.download.DownloadActivity;
import cpm.com.gskmtorange.upload.PreviousDataUploadActivity;
import cpm.com.gskmtorange.upload.UploadActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    ImageView imageView;
    String date, visit_status;
    private SharedPreferences preferences = null;
    GSKOrangeDB db;
    String user_name, user_type;
    ArrayList<StoreBean> storelist = new ArrayList<StoreBean>();
    View headerView;

    ArrayList<CoverageBean> coverageList;

    String error_msg;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        date = preferences.getString(CommonString.KEY_DATE, null);
        imageView = (ImageView) findViewById(R.id.img_main);

        webView = (WebView) findViewById(R.id.webview);

        String url = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK, "");
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        //user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);

        db = new GSKOrangeDB(MainActivity.this);
        db.open();

        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        if (!url.equals("")) {

            webView.loadUrl(url);

        }

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);

        TextView tv_username = (TextView) headerView.findViewById(R.id.nav_user_name);
        //tv_usertype = (TextView) headerView.findViewById(R.id.nav_user_type);

        tv_username.setText(user_name);
        //tv_usertype.setText(user_type);
        navigationView.addHeaderView(headerView);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.open();
        coverageList = db.getCoverageData(date);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(getString(R.string.main_menu_activity_name));

        db.open();

        coverageList = db.getCoverageData(date);

        storelist = db.getStoreData(date);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_route_plan) {

            Intent startDownload = new Intent(this, StoreListActivity.class);
            startActivity(startDownload);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


            // Handle the camera action
        } else if (id == R.id.nav_download) {
            if (checkNetIsAvailable()) {

                if (db.isCoverageDataFilled(date)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Parinaam");
                    builder.setMessage(getResources().getString(R.string.previous_data_upload)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent in = new Intent(getApplicationContext(), PreviousDataUploadActivity.class);
                                    startActivity(in);
                                    //finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                    startActivity(in);
                }
            } else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else if (id == R.id.nav_upload) {

            db.open();

            if (checkNetIsAvailable()) {

                storelist = db.getStoreData(date);

                if (storelist.size() == 0) {

                    Snackbar.make(webView, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    //  Toast.makeText(getBaseContext(), "Please Download Data First", Toast.LENGTH_LONG).show();
                } else {

                    if (coverageList.size() == 0) {
                        Snackbar.make(webView, R.string.no_data_for_upload, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    } else {
                        if (isStoreCheckedIn() && isValid()) {

                            Intent i = new Intent(getBaseContext(), UploadActivity.class);
                            startActivity(i);

                            //finish();
                        } else {
                            Snackbar.make(webView, error_msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }

                    }

                }

            } else {

                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                //  Toast.makeText(getApplicationContext(), "No Network Available", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_geotag) {




            if (storelist.size() > 0) {
                Intent startDownload = new Intent(this, GeoTagStoreList.class);
                startActivity(startDownload);

                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            } else {
                Snackbar.make(headerView, R.string.title_store_list_download_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


        } else if (id == R.id.nav_exit) {

          //  finish();
            ActivityCompat.finishAffinity(this);

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        } else if (id == R.id.nav_setting) {

            Intent startDownload = new Intent(this, SettingsActivity.class);
            startActivity(startDownload);

            finish();

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            //startActivity(new Intent(MainActivity.this, CategoryListActivity.class));

        } else if (id == R.id.nav_services) {

            Intent startservice = new Intent(this, ServiceActivity.class);
            startActivity(startservice);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        }else if(id == R.id.nav_future_jcp){
            Intent startDownload = new Intent(this, FutureJCPActivity.class);
            startActivity(startDownload);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }else if(id == R.id.nav_planogram){

            if(checkNetIsAvailable()){
                Intent planogram_pdf = new Intent(this, PlanogramPDFActivity.class);
                startActivity(planogram_pdf);

                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }else {

                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
           /* progress.setVisibility(View.GONE);
            WebViewActivity.this.progress.setProgress(100);*/
            imageView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           /* progress.setVisibility(View.VISIBLE);
            WebViewActivity.this.progress.setProgress(0);*/
            super.onPageStarted(view, url, favicon);
        }

    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean isStoreCheckedIn() {

        boolean result_flag = true;
        for (int i = 0; i < coverageList.size(); i++) {

            String status = coverageList.get(i).getStatus();
            if (status.equals(CommonString.KEY_INVALID) || status.equals(CommonString.KEY_VALID)) {
                result_flag = false;
                error_msg = getResources().getString(R.string.title_store_list_checkout_current);
                break;
            }
        }

        return result_flag;
    }

    public boolean isValid() {
        boolean flag = false;
        String storestatus;
        for (int i = 0; i < coverageList.size(); i++) {

            storestatus = db.getSpecificStoreData(date, coverageList.get(i).getStoreId()).getUPLOAD_STATUS();
            String coverage_status = db.getSpecificStoreData(date, coverageList.get(i).getStoreId()).getCHECKOUT_STATUS();

            if (!storestatus.equalsIgnoreCase(CommonString.KEY_U)) {
                if ((coverage_status.equalsIgnoreCase(CommonString.KEY_Y) || storestatus.equalsIgnoreCase(CommonString.KEY_P) ||
                        storestatus.equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE))) {
                    flag = true;
                    break;

                }
            }
        }

        if (!flag)
            error_msg = getResources().getString(R.string.no_data_for_upload);

        return flag;
    }

}
