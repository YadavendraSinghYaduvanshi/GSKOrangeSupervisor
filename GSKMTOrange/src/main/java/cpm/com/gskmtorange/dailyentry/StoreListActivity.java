package cpm.com.gskmtorange.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.gettersetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;

/**
 * Created by ashishc on 29-12-2016.
 */

public class StoreListActivity  extends AppCompatActivity {

    ArrayList<StoreBean> storelist = new ArrayList<StoreBean>();


    ListView list;
    private SharedPreferences preferences;

    String date,visit_status;
    GSKOrangeDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        visit_status = preferences.getString(CommonString.KEY_STOREVISITED_STATUS, "");

        db = new GSKOrangeDB(StoreListActivity.this);
        db.open();


        list = (ListView)findViewById(R .id.list_id);


        storelist = db.getStoreData(date);


        if (storelist.size()>0) {
            list.setAdapter(new MyAdaptor());
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_LONG).show();



            }
        });

    }


    private class MyAdaptor extends BaseAdapter {

        @Override
        public int getCount() {

            return storelist.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.storeviewlist, null);

                holder.storename = (TextView) convertView
                        .findViewById(R.id.storelistviewxml_storename);
                holder.storeaddress = (TextView) convertView
                        .findViewById(R.id.storelistviewxml_storeaddress);

                holder.imgtick = (ImageView) convertView
                        .findViewById(R.id.storelistviewxml_storeico);

                holder.checkout = (Button) convertView
                        .findViewById(R.id.chkout);

                holder.l1 = (RelativeLayout) convertView
                        .findViewById(R.id.storenamelistview_layout);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.storename.setText(storelist.get(position).getSTORE_NAME());
            holder.storeaddress.setText(storelist.get(position).getCITY());





            holder.checkout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            StoreListActivity.this);
                    builder.setMessage("Are you sure you want to checkout")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {


                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });



            return convertView;
        }

    }


    private class ViewHolder {
        TextView storename, storeaddress;
        ImageView imgtick;
        Button checkout;

        RelativeLayout l1;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home){

            // NavUtils.navigateUpFromSameTask(this);
            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }


}
