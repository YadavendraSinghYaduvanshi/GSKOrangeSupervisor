package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.adapter.ListAdapter;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SelectGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;

public class CreateSelfActivity extends AppCompatActivity {

    GSKOrangeDB db;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    String categoryName, categoryId;
    private SharedPreferences preferences;
    MSL_AvailabilityStockFacingGetterSetter brand_selected;
    int number_of_rows=0;
    RecyclerView rec_sub_category;
    static int FROM_DIALOG = 0;
    static int FROM_CLICK = 1;

    ArrayList<MSL_AvailabilityStockFacingGetterSetter> added_sub_category_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_self);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rec_sub_category = (RecyclerView) findViewById(R.id.rec_sub_category);

        //preference data
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);

        //Intent data
        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");

        db = new GSKOrangeDB(CreateSelfActivity.this);
        db.open();

        keyAccount_id = preferences.getString(CommonString.KEY_KEYACCOUNT_ID, "");
        class_id = preferences.getString(CommonString.KEY_CLASS_ID, "");
        storeType_id = preferences.getString(CommonString.KEY_STORETYPE_ID, "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkuDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        number_of_rows=0;
        brand_selected = null;

        db.open();

        added_sub_category_list = new ArrayList<>();

        ArrayList<MSL_AvailabilityStockFacingGetterSetter> sub_category_list = db.getSubCategoryMaster(categoryId);

        for(int i=0; i<sub_category_list.size(); i++){

            if(db.getNoCameraCategoryDataInserted(store_id, categoryId, sub_category_list.get(i).getSub_category_id()).size()>0){

                MSL_AvailabilityStockFacingGetterSetter subCategory = new MSL_AvailabilityStockFacingGetterSetter();
                subCategory.setSub_category(sub_category_list.get(i).getSub_category());
                subCategory.setSub_category_id(sub_category_list.get(i).getSub_category_id());
                added_sub_category_list.add(subCategory);
            }
        }

        if(added_sub_category_list.size()>0){
            rec_sub_category.setLayoutManager(new GridLayoutManager(this, 3));
            SubcategoryAdapter skuAdapter = new SubcategoryAdapter(added_sub_category_list);
            rec_sub_category.setAdapter(skuAdapter);

        }

    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public void showSkuDialog() {

        final ArrayList<MSL_AvailabilityStockFacingGetterSetter> sub_category_list = db.getSubCategoryMaster(categoryId);
        MSL_AvailabilityStockFacingGetterSetter brand = new MSL_AvailabilityStockFacingGetterSetter();
        brand.setSub_category("select");
        sub_category_list.add(0, brand);
        // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)

        final Dialog dialog = new Dialog(CreateSelfActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.create_self_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        final Spinner spinner_sub_category = (Spinner) dialog.findViewById(R.id.spinner_sub_category);
        final Spinner spinner_sku = (Spinner) dialog.findViewById(R.id.spinner_no_rows);
        final Button btn_create = (Button) dialog.findViewById(R.id.btn_create);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(brand_selected==null){
                    Snackbar.make(btn_create,"Please select Sub Category",Snackbar.LENGTH_SHORT).show();
                }
                else  if(number_of_rows==0){
                    Snackbar.make(btn_create,"Please select number of rows",Snackbar.LENGTH_SHORT).show();
                }
                else {

                    boolean sub_category_already_filled = false;
                    if(added_sub_category_list.size()>0){
                        for(int k=0;k<added_sub_category_list.size();k++){
                            if(added_sub_category_list.get(k).getSub_category_id().equals(brand_selected.getSub_category_id())){
                                sub_category_already_filled = true;
                                break;
                            }
                        }
                    }

                    if(sub_category_already_filled){
                        Snackbar.make(btn_create,"Sub Category already added",Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Intent in = new Intent(getApplicationContext(), NoCameraActivity.class);
                        in.putExtra("categoryName", categoryName);
                        in.putExtra("categoryId", categoryId);
                        in.putExtra(CommonString.KEY_NUMBER_OF_ROWS, number_of_rows);
                        in.putExtra(CommonString.KEY_SUB_CATEGORY, brand_selected);
                        in.putExtra(CommonString.KEY_FROM, FROM_DIALOG);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        dialog.cancel();
                    }


                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });

        // Create custom adapter object ( see below CustomAdapter.java )
        CustomAdapter adapter = new CustomAdapter(CreateSelfActivity.this, R.layout.custom_spinner_item, sub_category_list);
        // Set adapter to spinner
        spinner_sub_category.setAdapter(adapter);

        final ArrayList<String> no_of_rows = new ArrayList<>();
        no_of_rows.add(getString(R.string.select));
        for(int i=1; i<9; i++){
            no_of_rows.add(i + "");
        }
        CustomSpinnerAdapter skuadapter = new CustomSpinnerAdapter(CreateSelfActivity.this, R.layout.custom_spinner_item, no_of_rows);
        spinner_sku.setAdapter(skuadapter);


        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    brand_selected = sub_category_list.get(position);
                }
                else{
                    brand_selected = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    number_of_rows = Integer.parseInt(no_of_rows.get(position));
                }
                else {
                    number_of_rows = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    public class CustomSpinnerAdapter extends ArrayAdapter<String> {

        String tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSpinnerAdapter(
                CreateSelfActivity activitySpinner,
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
            tempValues = (String) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues);
            }

            return row;
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        MSL_AvailabilityStockFacingGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(
                CreateSelfActivity activitySpinner,
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
            tempValues = (MSL_AvailabilityStockFacingGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getSub_category());
            }

            return row;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

           /* android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateSelfActivity.this);
            builder.setTitle("Parinaam");
            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();*/

            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Adapte sub category

    public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

        private ArrayList<MSL_AvailabilityStockFacingGetterSetter> list;

        public SubcategoryAdapter(ArrayList<MSL_AvailabilityStockFacingGetterSetter> skuList) {
            list = skuList;
        }

        @Override
        public SubcategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_category_item_list, parent, false);
            return new SubcategoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SubcategoryAdapter.ViewHolder holder, final int position) {

            holder.tv_sub_category.setText(list.get(position).getSub_category());

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getApplicationContext(), NoCameraActivity.class);
                    in.putExtra("categoryName", categoryName);
                    in.putExtra("categoryId", categoryId);
                    in.putExtra(CommonString.KEY_NUMBER_OF_ROWS, 0);
                    in.putExtra(CommonString.KEY_SUB_CATEGORY, list.get(position));
                    in.putExtra(CommonString.KEY_FROM, FROM_CLICK);
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_sub_category;

            public ViewHolder(View view) {
                super(view);

                mView = view;

                tv_sub_category = (TextView) mView.findViewById(R.id.tv_sub_category);
                parentLayout = (LinearLayout) mView.findViewById(R.id.linear_parent);

            }

        }
    }
}
