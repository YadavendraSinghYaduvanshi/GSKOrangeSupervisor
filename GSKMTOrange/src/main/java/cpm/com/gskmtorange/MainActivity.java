package cpm.com.gskmtorange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import cpm.com.gskmtorange.GeoTag.GeoTagStoreList;
import cpm.com.gskmtorange.constant.CommonString;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    ImageView imageView;

    private SharedPreferences preferences = null;

    String user_name, user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        imageView = (ImageView) findViewById(R.id.img_main);

        webView = (WebView) findViewById(R.id.webview);

        String url = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK,"");
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        //user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);

        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        if(!url.equals("")){

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

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);

        TextView tv_username = (TextView)  headerView.findViewById(R.id.nav_user_name);
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

    @Override
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_route_plan) {
            // Handle the camera action
        } else if (id == R.id.nav_download) {

        } else if (id == R.id.nav_upload) {

        }
        else if (id == R.id.nav_geotag) {


            Intent startDownload = 	new Intent(this,GeoTagStoreList.class);
            startActivity(startDownload);

            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);




        }

        else if (id == R.id.nav_export) {




        }


        else if (id == R.id.nav_exit) {

        } else if (id == R.id.nav_services) {

        }else if (id == R.id.nav_setting) {

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
}
