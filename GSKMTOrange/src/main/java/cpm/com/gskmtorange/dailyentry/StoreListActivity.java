package cpm.com.gskmtorange.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GeoTag.GeoTagActivity;

import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.download.DownloadActivity;

/**
 * Created by ashishc on 29-12-2016.
 */

public class StoreListActivity extends AppCompatActivity {
    ArrayList<CoverageBean> coverage;
    ArrayList<StoreBean> storelist = new ArrayList<StoreBean>();
    //ListView list;
    private SharedPreferences preferences;
    String date, visit_status;
    GSKOrangeDB db;
    StoreListActivity.ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences.Editor editor = null;
    LinearLayout linearlay;
    String store_id;
    private Dialog dialog;
    boolean result_flag = false, leaveflag = false;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelistfablayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

        date = preferences.getString(CommonString.KEY_DATE, null);
        visit_status = preferences.getString(CommonString.KEY_STOREVISITED_STATUS, "");
        db = new GSKOrangeDB(StoreListActivity.this);
        db.open();

        linearlay = (LinearLayout) findViewById(R.id.no_data_lay);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                startActivity(in);

                finish();
            }
        });

    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

        storelist = db.getStoreData(date);
        coverage=db.getCoverageData(date);

        if (storelist.size() > 0) {
            //list.setAdapter(new MyAdaptor());
            adapter = new StoreListActivity.ValueAdapter(getApplicationContext(), storelist);
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

            // NavUtils.navigateUpFromSameTask(this);
            finish();


            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }


    public class ValueAdapter extends RecyclerView.Adapter<StoreListActivity.ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;

        List<StoreBean> data = Collections.emptyList();

        public ValueAdapter(Context context, List<StoreBean> data) {

            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public StoreListActivity.ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

            View view = inflator.inflate(R.layout.storeviewlist, parent, false);

            StoreListActivity.ValueAdapter.MyViewHolder holder = new StoreListActivity.ValueAdapter.MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final StoreListActivity.ValueAdapter.MyViewHolder viewHolder, final int position) {

            final StoreBean current = data.get(position);


            String storeid = current.getSTORE_ID();
            //viewHolder.txt.setText(current.txt);

            viewHolder.txt.setText(current.getSTORE_NAME());
            viewHolder.address.setText(current.getADDRESS());

            if (current.getCHECKOUT_STATUS().equalsIgnoreCase(CommonString.KEY_VALID)) {
                viewHolder.chkbtn.setVisibility(View.VISIBLE);
                viewHolder.imageview.setVisibility(View.INVISIBLE);
            } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_U)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick_u);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_D)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick_d);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            } else if (current.getCHECKOUT_STATUS().equalsIgnoreCase(CommonString.KEY_C)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick_c);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_P)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick_p);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_L)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tickl);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            }
            else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.leave_tick);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            }




            else if (current.getCHECKOUT_STATUS().equalsIgnoreCase(CommonString.KEY_INVALID)) {


                if (coverage.size() > 0) {

                    int i;

                    for (i = 0; i < coverage.size(); i++) {


                        if (coverage.get(i).getInTime() != null) {

                            if (coverage.get(i).getOutTime() == null) {

                                if (storeid.equals(coverage.get(i).getStoreId())) {
                                    viewHolder.imageview.setVisibility(View.VISIBLE);
                                    viewHolder.imageview.setBackgroundResource(R.mipmap.checkin);
                                    viewHolder.chkbtn.setVisibility(View.INVISIBLE);

                                }
                                break;
                            }

                        }

                    }
                }
            } else {
                viewHolder.imageview.setVisibility(View.INVISIBLE);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            }


            viewHolder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    store_id = current.getSTORE_ID();


                    if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_U)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_already_done, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_D)) {

                        Snackbar.make(v, R.string.title_store_list_activity_store_data_uploaded, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getCHECKOUT_STATUS().equalsIgnoreCase(CommonString.KEY_C)) {

                        Snackbar.make(v, R.string.title_store_list_activity_store_already_checkout, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_P)) {

                        Snackbar.make(v, R.string.title_store_list_activity_store_again_uploaded, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.KEY_L)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_closed, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                    else if (current.getUPLOAD_STATUS().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                        Snackbar.make(v, R.string.title_store_list_activity_already_store_closed, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                    else {

                        // PUT IN PREFERENCES
                        editor = preferences.edit();
                        editor.putString(CommonString.KEY_STORE_ID, current.getSTORE_ID());
                        editor.putString(CommonString.KEY_STORE_NAME, current.getSTORE_NAME());
                        editor.putString(CommonString.KEY_VISIT_DATE, current.getVISIT_DATE());
                        editor.putString(CommonString.KEY_CAMERA_ALLOW, current.getCAMERA_ALLOW());
                        editor.putString(CommonString.KEY_CHECKOUT_STATUS, current.getCHECKOUT_STATUS());
                        editor.putString(CommonString.KEY_CLASS_ID, current.getCLASS_ID());
                        editor.putString(CommonString.KEY_EMP_ID, current.getEMP_ID());
                        editor.putString(CommonString.KEY_GEO_TAG, current.getGEO_TAG());
                        editor.putString(CommonString.KEY_KEYACCOUNT_ID, current.getKEYACCOUNT_ID());
                        editor.putString(CommonString.KEY_STORETYPE_ID, current.getSTORETYPE_ID());
                        editor.putString(CommonString.KEY_UPLOAD_STATUS, current.getUPLOAD_STATUS());
                        editor.commit();

                        // showMyDialog(store_id, current.getSTORE_NAME(), "Yes", current.getVISIT_DATE(), current.getCHECKOUT_STATUS());

                        if (!setcheckedmenthod(store_id)) {
                            boolean enteryflag = true;
                            if (coverage.size() > 0) {
                                int i;
                                for (i = 0; i < coverage.size(); i++) {

                                    if (coverage.get(i).getInTime() != null) {

                                        if (coverage.get(i).getOutTime() == null) {
                                            if (!store_id.equals(coverage.get(i).getStoreId())) {
                                                Snackbar.make(v, R.string.title_store_list_checkout_current, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                                enteryflag = false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            if (enteryflag) {
                                showMyDialog(store_id, current.getSTORE_NAME(), "Yes", current.getVISIT_DATE(), current.getCHECKOUT_STATUS());
                            }
                        } else {
                            Snackbar.make(v, R.string.title_store_list_checkout_Already_filled, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt, address;
            ImageView icon;
            RelativeLayout relativelayout;
            ImageView imageview;
            Button chkbtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.storelistviewxml_storename);
                address = (TextView) itemView.findViewById(R.id.storelistviewxml_storeaddress);

                relativelayout = (RelativeLayout) itemView.findViewById(R.id.storenamelistview_layout);
                imageview = (ImageView) itemView.findViewById(R.id.imageView2);

                chkbtn = (Button) itemView.findViewById(R.id.chkout);

            }
        }

    }


    void showMyDialog(final String storeCd, final String storeName, final String status, final String visitDate, final String checkout_status) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogrpvisit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.yes) {

                    Intent in = new Intent(StoreListActivity.this, StoreimageActivity.class);
                    startActivity(in);

                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    dialog.cancel();

                } else if (checkedId == R.id.no) {

                    dialog.cancel();

                    if (checkout_status.equals(CommonString.KEY_INVALID) || checkout_status.equals(CommonString.KEY_VALID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreListActivity.this);
                        builder.setMessage(CommonString.DATA_DELETE_ALERT_MESSAGE)
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {



                                                Intent in  = new Intent(StoreListActivity.this, NonWorkingReason.class);
                                                startActivity(in);

                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {


                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();

                        alert.show();
                    } else {




                        Intent in  = new Intent(StoreListActivity.this, NonWorkingReason.class);
                        startActivity(in);
                    }

                }
            }

        });


        dialog.show();
    }


    public boolean setcheckedmenthod(String store_cd) {


        for (int i = 0; i < coverage.size(); i++) {
            if (store_cd.equals(coverage.get(i).getStoreId())) {
                if (coverage.get(i).getOutTime() != null) {
                    result_flag = true;
                    break;
                }
            } else {

                result_flag = false;

            }
        }

        return result_flag;
    }

    private static boolean updateResources(Context context, String language) {

        String lang ;

        if(language.equalsIgnoreCase("English")){
            lang = "EN";
        }
        else if(language.equalsIgnoreCase("UAE")) {
            lang = "AR";
        }
        else {
            lang = "TR";
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }
}
