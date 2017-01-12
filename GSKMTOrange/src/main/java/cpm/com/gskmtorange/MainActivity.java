package cpm.com.gskmtorange;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GeoTag.GeoTagStoreList;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.dailyentry.AdditionalVisibility;
import cpm.com.gskmtorange.dailyentry.T2PComplianceActivity;
import cpm.com.gskmtorange.dailyentry.StoreListActivity;
import cpm.com.gskmtorange.download.DownloadActivity;
import cpm.com.gskmtorange.gsk_dailyentry.CategoryListActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        imageView = (ImageView) findViewById(R.id.img_main);

        webView = (WebView) findViewById(R.id.webview);

        String url = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK, "");
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        //user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);

        db = new GSKOrangeDB(MainActivity.this);
        db.open();

        coverageList = db.getCoverageData(date);

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

            Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
            startActivity(in);

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

                        Snackbar.make(webView, R.string.no_data_for_upload, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    } else {
                        //if (isStoreCheckedIn()&& isValid()) {

                        Intent i = new Intent(getBaseContext(), UploadActivity.class);
                        startActivity(i);

                        //finish();

                        /*} else {

                            Snackbar.make(webView, error_msg, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }*/

                    }




					/*
                    intent = new Intent(getBaseContext(),
							UploadOptionActivity.class);
					startActivity(intent);

					MainMenuActivity.this.finish();*/

                }

            } else {

                Snackbar.make(webView, "No Network Available", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                //  Toast.makeText(getApplicationContext(), "No Network Available", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_geotag) {

            db.open();
            storelist = db.getStoreData(date);


            if (storelist.size() > 0) {
                Intent startDownload = new Intent(this, GeoTagStoreList.class);
                startActivity(startDownload);

                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            } else {
                Snackbar.make(headerView, R.string.title_store_list_download_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


        } else if (id == R.id.nav_exit) {


            Intent startDownload = 	new Intent(this,AdditionalVisibility.class);
            startActivity(startDownload);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);



        } else if (id == R.id.nav_services) {

            /*Intent startDownload = 	new Intent(this,T2PComplianceActivity.class);
            startActivity(startDownload);
*/
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        } else if (id == R.id.nav_setting) {

            //startActivity(new Intent(MainActivity.this, CategoryListActivity.class));

        } /*else if (id == R.id.nav_export) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Are you sure you want to take the backup of your data")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @SuppressWarnings("resource")
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                *//*File file = new File(Environment
                                        .getExternalStorageDirectory(),
                                        "capital_backup");
                                if (!file.isDirectory()) {
                                    file.mkdir();
                                }*//*

                                File sd = Environment.getExternalStorageDirectory();
                                File data = Environment.getDataDirectory();

                                if (sd.canWrite()) {
                                    long date = System.currentTimeMillis();

                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                    String dateString = sdf.format(date);

                                    String currentDBPath = "//data//cpm.com.gskmtorange//databases//" + GSKOrangeDB.DATABASE_NAME;
                                    String backupDBPath = "GSKMT_ORANGE_Database_backup" + dateString.replace('/', '-');

                                    String path = Environment.getExternalStorageDirectory().getPath();

                                    File currentDB = new File(data, currentDBPath);
                                    File backupDB = new File(path, backupDBPath);

                                    //Snackbar.make(rec_store_data, "Database Exported Successfully", Snackbar.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Database Exported Successfully", Toast.LENGTH_SHORT).show();

                                    if (currentDB.exists()) {
                                        @SuppressWarnings("resource")
                                        FileChannel src = new FileInputStream(currentDB).getChannel();
                                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                        dst.transferFrom(src, 0, src.size());
                                        src.close();
                                        dst.close();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }*/

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

            if (!storestatus.equalsIgnoreCase(CommonString.KEY_U)) {
                if ((storestatus.equalsIgnoreCase(
                        CommonString.KEY_C)
                        || storestatus.equalsIgnoreCase(CommonString.KEY_P) ||
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
