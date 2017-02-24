package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.AddittionalGetterSetter;
import cpm.com.gskmtorange.GetterSetter.BrandAvabilityGetterSetter;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;

public class T2pBrand_Avaibility extends AppCompatActivity {
    GSKOrangeDB db;
    private SharedPreferences preferences;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    Spinner spinner_brand;
    Button add;
    ArrayList<BrandAvabilityGetterSetter> brand_list;
    String categoryName, categoryId, brand_name = "", brand_id = "";
    RecyclerView recyclerView;
    ArrayList<BrandAvabilityGetterSetter> brand_new_list = new ArrayList<BrandAvabilityGetterSetter>();
    T2pBrand_Avaibility.ValueAdapter adapteravabiblity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2p_brand__avaibility);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner_brand = (Spinner) findViewById(R.id.spinner);
        add = (Button) findViewById(R.id.button2);
        recyclerView = (RecyclerView) findViewById(R.id.layout_recycle);


        db = new GSKOrangeDB(T2pBrand_Avaibility.this);
        db.open();

        //preference data
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
        keyAccount_id = preferences.getString(CommonString.KEY_KEYACCOUNT_ID, "");
        class_id = preferences.getString(CommonString.KEY_CLASS_ID, "");
        storeType_id = preferences.getString(CommonString.KEY_STORETYPE_ID, "");
        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");
/////
        brand_list = db.getBrandAvailbilitydata(store_id, categoryId, keyAccount_id, class_id, storeType_id);
        BrandAvabilityGetterSetter brand = new BrandAvabilityGetterSetter();
        String str = getResources().getString(R.string.select);
        brand.setBRAND(str);
        brand_list.add(0, brand);
        T2pBrand_Avaibility.CustomAdapter adapter = new T2pBrand_Avaibility.CustomAdapter(T2pBrand_Avaibility.this, R.layout.custom_spinner_item, brand_list);
        spinner_brand.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brand_new_list.size() > 0) {
                    BrandAvabilityGetterSetter br = new BrandAvabilityGetterSetter();

                    br.setCategoryId(categoryId);
                    br.setClass_id(class_id);
                    br.setKeyAccount_id(keyAccount_id);
                    br.setStoreType_id(storeType_id);
                    br.setStore_id(store_id);
                    db.InsertBrandAvabilitydata(br, brand_new_list);

                    brand_new_list.clear();
                    finish();


                } else {
                    Snackbar.make(view, "Please add data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!brand_name.equalsIgnoreCase("") && brand_name != null) {

                    BrandAvabilityGetterSetter brand = new BrandAvabilityGetterSetter();

                    brand.setBRAND(brand_name);
                    brand.setBRAND_ID(brand_id);

                    brand_new_list.add(brand);

                    adapteravabiblity = new T2pBrand_Avaibility.ValueAdapter(T2pBrand_Avaibility.this, brand_new_list);
                    recyclerView.setAdapter(adapteravabiblity);
                    recyclerView.setLayoutManager(new LinearLayoutManager(T2pBrand_Avaibility.this));

                    spinner_brand.setSelection(0);
                    brand_name = "";
                    brand_id = "";

                } else {
                    Snackbar.make(view, "Please select dropdown", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    brand_name = brand_list.get(position).getBRAND();
                    brand_id = brand_list.get(position).getBRAND_ID();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        BrandAvabilityGetterSetter tempValues = null;
        LayoutInflater inflater;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(
                T2pBrand_Avaibility activitySpinner,
                int textViewResourceId,
                ArrayList objects

        ) {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            data = objects;
            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.custom_spinner_item, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (BrandAvabilityGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getResources().getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getBRAND());
            }

            return row;
        }
    }

    private static boolean updateResources(Context context, String language) {

        /*String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("ARABIC-KSA")) {
            lang = "AR";
        } else {
            lang = "TR";
        }*/

        String lang;

        if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ENGLISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ENGLISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ARABIC_KSA;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_TURKISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_TURKISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_OMAN)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_OMAN;
        } else {
            lang = CommonString.KEY_RETURN_LANGUAGE_DEFAULT;
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    public class ValueAdapter extends RecyclerView.Adapter<T2pBrand_Avaibility.ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;

        List<BrandAvabilityGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<BrandAvabilityGetterSetter> data) {

            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public T2pBrand_Avaibility.ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

            View view = inflator.inflate(R.layout.brandavabilityadpterlayout, parent, false);

            T2pBrand_Avaibility.ValueAdapter.MyViewHolder holder = new T2pBrand_Avaibility.ValueAdapter.MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final T2pBrand_Avaibility.ValueAdapter.MyViewHolder viewHolder, final int position) {

            final BrandAvabilityGetterSetter current = data.get(position);

            viewHolder.txt.setText(current.getBRAND());

        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt, address;


            public MyViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.brand_name);


            }
        }

    }

}
