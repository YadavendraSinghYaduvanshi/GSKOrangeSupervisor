package cpm.com.gskmtorange.GeoTag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.download.DownloadActivity;

/**
 * Created by ashishc on 27-12-2016.
 */

public class GeoTagStoreList extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    ArrayList<StoreBean> storelist = new ArrayList<StoreBean>();
    String date, visit_status;
    GSKOrangeDB db;
    // ListView list;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences.Editor editor = null;
    LinearLayout parent_linear, nodata_linear;
    LinearLayout linearlay;
    FloatingActionButton fab;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geotagstorelistfab);
        //list = (ListView) findViewById(R.id.list_id);

        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        linearlay = (LinearLayout) findViewById(R.id.no_data_lay);
        // nodata_linear = (LinearLayout) findViewById(R.id.no_data_lay);
        //parent_linear = (LinearLayout) findViewById(R.id.parent_linear);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        date = preferences.getString(CommonString.KEY_DATE, null);
        visit_status = preferences.getString(CommonString.KEY_STOREVISITED_STATUS, "");

        db = new GSKOrangeDB(GeoTagStoreList.this);
        db.open();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                startActivity(in);

                finish();
            }
        });

        storelist = db.getStoreData(date);

        if (storelist.size() > 0) {
            adapter = new ValueAdapter(getApplicationContext(), storelist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            linearlay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
       /* Intent intent = new Intent(GeoTagStoreList.this, MainActivity.class);

        startActivity(intent);*/
        GeoTagStoreList.this.finish();
    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;

        List<StoreBean> data = Collections.emptyList();

        public ValueAdapter(Context context, List<StoreBean> data) {

            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.geotagstorelist, parent, false);

            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final StoreBean current = data.get(position);
            //viewHolder.txt.setText(current.txt);

            viewHolder.txt.setText(current.getSTORE_NAME());
            viewHolder.txt_storeAddress.setText(current.getADDRESS());

            if (current.getGEO_TAG().equalsIgnoreCase("Y")) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.geopin);
            } else if (current.getGEO_TAG().equalsIgnoreCase("D")) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.exclamation);
            } else if (current.getGEO_TAG().equalsIgnoreCase("U")) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick);
            } else if (current.getGEO_TAG().equalsIgnoreCase("P")) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.exclamation);
            } else {
                viewHolder.imageview.setVisibility(View.INVISIBLE);
            }

            viewHolder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current.getGEO_TAG().equalsIgnoreCase("Y")) {
                        Snackbar.make(v, R.string.title_geo_tag_activity_geo_already_done, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getGEO_TAG().equalsIgnoreCase("D")) {
                        Snackbar.make(v, R.string.title_geo_tag_activity_geo_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else if (current.getGEO_TAG().equalsIgnoreCase("U")) {
                        Snackbar.make(v, R.string.title_geo_tag_activity_upload_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else if (current.getGEO_TAG().equalsIgnoreCase("P")) {
                        Snackbar.make(v, R.string.title_geo_tag_activity_geo_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else {
                        // PUT IN PREFERENCES
                        editor = preferences.edit();
                        editor.putString(CommonString.KEY_STORE_ID, current.getSTORE_ID());
                        editor.putString(CommonString.KEY_STORE_NAME, current.getSTORE_NAME());
                        editor.putString(CommonString.KEY_VISIT_DATE, current.getVISIT_DATE());
                        editor.commit();

                        Intent in = new Intent(GeoTagStoreList.this, GeoTagActivity.class);
                        startActivity(in);
                        finish();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt, txt_storeAddress;
            ImageView icon;
            RelativeLayout relativelayout;
            ImageView imageview;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.geolistviewxml_storename);
                relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
                imageview = (ImageView) itemView.findViewById(R.id.imageView1);
                txt_storeAddress = (TextView) itemView.findViewById(R.id.txt_storeAddress);
            }
        }
    }

    public List<StoreBean> getdata() {
        List<StoreBean> data = new ArrayList<>();

        storelist = db.getStoreData(date);

        StoreBean storelistdata = new StoreBean();

        for (int i = 0; i < storelist.size(); i++) {
            storelistdata.setSTORE_NAME(storelist.get(0).getSTORE_NAME());

            data.add(storelistdata);

        }

        return data;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(getString(R.string.title_activity_store_geotag));
    }


}
