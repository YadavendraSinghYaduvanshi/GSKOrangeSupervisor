package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.dailyentry.AdditionalVisibility;
import cpm.com.gskmtorange.dailyentry.CategoryPicture;
import cpm.com.gskmtorange.dailyentry.T2PComplianceActivity;
import cpm.com.gskmtorange.xmlGetterSetter.DailyDataMenuGetterSetter;

public class DailyDataMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DailyDataMenuGetterSetter> categoryList;
    DailyDataMenuAdapter adapter;
    TextView txt_categoryName;

    GSKOrangeDB db;
    String categoryName = "", categoryId;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id,camera_allow;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_daily_data_menu);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            //txt_categoryName = (TextView) findViewById(R.id.txt_categoryName);

            db = new GSKOrangeDB(this);
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
            camera_allow = preferences.getString(CommonString.KEY_CAMERA_ALLOW, "");
            //Intent data
            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            //txt_categoryName.setText(getResources().getString(R.string.title_activity_daily_main_menu) + " - " + categoryName);
            toolbar.setTitle(getResources().getString(R.string.title_activity_daily_main_menu) + " - " + categoryName);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        try {
            categoryList = new ArrayList<>();

            DailyDataMenuGetterSetter data = new DailyDataMenuGetterSetter();
            //data.setCategory_name("MSL Availability");
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_msl_availability));
            if (db.isMappingStockDataMSL_Availability(categoryId, keyAccount_id, storeType_id, class_id)) {
                if (db.checkMsl_AvailabilityData(store_id, categoryId)) {
                    data.setCategory_img(R.mipmap.msl_availability_done);
                } else {
                    data.setCategory_img(R.mipmap.msl_availability);
                }
            } else {
                data.setCategory_img(R.mipmap.msl_availability_grey);
            }
            categoryList.add(data);

            data = new DailyDataMenuGetterSetter();
            //data.setCategory_name("Stock & Facing");
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_stock_facing));
            if (db.isMappingStockDataStockFacing(categoryId, keyAccount_id, storeType_id, class_id)) {
                if (db.checkStockAndFacingData(store_id, categoryId)) {
                    data.setCategory_img(R.mipmap.stock_facing_done);
                } else {
                    data.setCategory_img(R.mipmap.stock_facing);
                }
            } else {
                data.setCategory_img(R.mipmap.stockandfacing_grey);
            }
            categoryList.add(data);

            //T2p
            data = new DailyDataMenuGetterSetter();
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_t2p));

            if (db.isMappingT2PData(store_id, categoryId)) {
                if (db.isFilledT2P(store_id, categoryId)) {
                    data.setCategory_img(R.mipmap.t2p_compliance_done);
                } else {
                    data.setCategory_img(R.mipmap.t2p_compliance);
                }
            } else {
                data.setCategory_img(R.mipmap.t2pcompliance_grey);
            }

            categoryList.add(data);
            //T2p added

            data = new DailyDataMenuGetterSetter();
            //data.setCategory_name("Additional Visibility");
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_additional_visibility));
            if (db.additionalVisibilitydata(store_id, categoryId)) {
                data.setCategory_img(R.mipmap.additional_visibility_done);
            } else {
                data.setCategory_img(R.mipmap.additional_visibility);
            }

            categoryList.add(data);

            data = new DailyDataMenuGetterSetter();
            //data.setCategory_name("Promo Compliance");
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_promo_compliance));

            if (db.isMappingPromotionData(store_id, categoryId) || db.isMappingAdditionalPromotionData(store_id, categoryId)) {
                if (db.checkPromoComplianceData(store_id, categoryId) ||
                        db.checkAdditionalPromoComplianceData(store_id, categoryId)) {
                    data.setCategory_img(R.mipmap.promo_compliance_done);
                } else {
                    data.setCategory_img(R.mipmap.promo_compliance);
                }
            } else {
                data.setCategory_img(R.mipmap.promocompliance_grey);
            }
            categoryList.add(data);


            //Category Pictures

            data = new DailyDataMenuGetterSetter();
            //data.setCategory_name("Additional Visibility");
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_category_picture));

            if(camera_allow.equalsIgnoreCase("1")){

            if (db.isCategoryPictureData(store_id, categoryId)) {
                data.setCategory_img(R.mipmap.picturecatogory_done);
            } else {
                data.setCategory_img(R.mipmap.picturecatogory);
            }
            }
            else{
                data.setCategory_img(R.mipmap.picturecatogory_grey);
            }

            categoryList.add(data);


            /*data = new DailyDataMenuGetterSetter();
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_competition_tracking));
            //data.setCategory_name("Competition Tracking");
            data.setCategory_img(R.drawable.category);
            categoryList.add(data);

            data = new DailyDataMenuGetterSetter();
            data.setCategory_name(getResources().getString(R.string.daily_data_menu_additional_promotions));
            //data.setCategory_name("Competition Promo");
            data.setCategory_img(R.drawable.category);
            categoryList.add(data);*/

            adapter = new DailyDataMenuAdapter(DailyDataMenuActivity.this, categoryList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DailyDataMenuAdapter extends RecyclerView.Adapter<DailyDataMenuAdapter.MyViewHolder> {
        List<DailyDataMenuGetterSetter> list = Collections.emptyList();
        Context context;
        private LayoutInflater inflator;

        public DailyDataMenuAdapter(Context context, List<DailyDataMenuGetterSetter> list) {
            inflator = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
        }

        @Override
        public DailyDataMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.daily_main_menu_row, parent, false);
            DailyDataMenuAdapter.MyViewHolder holder = new DailyDataMenuAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final DailyDataMenuAdapter.MyViewHolder holder, int position) {
            final DailyDataMenuGetterSetter dailyData = list.get(position);

            holder.categoryName.setText(dailyData.getCategory_name());
            holder.categoryIcon.setImageResource(dailyData.getCategory_img());

            if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_msl_availability))) {
                if (db.isMappingStockDataMSL_Availability(categoryId, keyAccount_id, storeType_id, class_id)) {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.grey_background));
                }
            } else if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_stock_facing))) {
                if (db.isMappingStockDataStockFacing(categoryId, keyAccount_id, storeType_id, class_id)) {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.grey_background));
                }
            } else if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_promo_compliance))) {
                if (db.isMappingPromotionData(store_id, categoryId) || db.isMappingAdditionalPromotionData(store_id, categoryId)) {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.grey_background));
                }
            } else if (dailyData.getCategory_name().equalsIgnoreCase((getResources().getString(R.string.daily_data_menu_t2p)))) {
                if (db.isMappingT2PData(store_id, categoryId)) {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.grey_background));
                }

            }

            else if (dailyData.getCategory_name().equalsIgnoreCase((getResources().getString(R.string.daily_data_menu_category_picture)))) {
                if(camera_allow.equalsIgnoreCase("1")){
                    holder.categoryName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    holder.categoryName.setTextColor(getResources().getColor(R.color.grey_background));
                }

            }


            holder.lay_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_msl_availability))) {
                        if (db.isMappingStockDataMSL_Availability(categoryId, keyAccount_id, storeType_id, class_id)) {
                            Intent intent = new Intent(DailyDataMenuActivity.this, MSL_AvailabilityActivity.class);
                            intent.putExtra("categoryName", dailyData.getCategory_name());
                            intent.putExtra("categoryId", categoryId);
                            startActivity(intent);
                        }
                    } else if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_stock_facing))) {
                        if (db.isMappingStockDataStockFacing(categoryId, keyAccount_id, storeType_id, class_id)) {
                            Intent intent = new Intent(DailyDataMenuActivity.this, Stock_FacingActivity.class);
                            intent.putExtra("categoryName", dailyData.getCategory_name());
                            intent.putExtra("categoryId", categoryId);
                            startActivity(intent);
                        }
                    } else if (dailyData.getCategory_name().equalsIgnoreCase(getResources().getString(R.string.daily_data_menu_promo_compliance))) {

                        if (db.isMappingPromotionData(store_id, categoryId) || db.isMappingAdditionalPromotionData(store_id, categoryId)) {
                            Intent intent = new Intent(DailyDataMenuActivity.this, PromoComplianceActivity.class);
                            intent.putExtra("categoryName", dailyData.getCategory_name());
                            intent.putExtra("categoryId", categoryId);
                            startActivity(intent);
                        }
                    } else if (dailyData.getCategory_name().equalsIgnoreCase((getResources().getString(R.string.daily_data_menu_t2p)))) {
                        if (db.isMappingT2PData(store_id, categoryId)) {
                            Intent intent = new Intent(DailyDataMenuActivity.this, T2PComplianceActivity.class);
                            intent.putExtra("categoryName", dailyData.getCategory_name());
                            intent.putExtra("categoryId", categoryId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    } else if (dailyData.getCategory_name().equalsIgnoreCase((getResources().getString(R.string.title_activity_Additional_visibility)))) {
                        Intent intent = new Intent(DailyDataMenuActivity.this, AdditionalVisibility.class);
                        intent.putExtra("categoryName", dailyData.getCategory_name());
                        intent.putExtra("categoryId", categoryId);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }
                    else if (dailyData.getCategory_name().equalsIgnoreCase((getResources().getString(R.string.daily_data_menu_category_picture)))) {

                        if(camera_allow.equalsIgnoreCase("1")){

                            Intent intent = new Intent(DailyDataMenuActivity.this, CategoryPicture.class);
                            intent.putExtra("categoryName", dailyData.getCategory_name());
                            intent.putExtra("categoryId", categoryId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                        }
                       else{

                        }


                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryIcon;
            LinearLayout lay_menu;

            public MyViewHolder(View itemView) {
                super(itemView);
                categoryName = (TextView) itemView.findViewById(R.id.categoryName);
                categoryIcon = (ImageView) itemView.findViewById(R.id.categoryIcon);
                lay_menu = (LinearLayout) itemView.findViewById(R.id.lay_menu);
            }
        }
    }

    private static boolean updateResources(Context context, String language) {

        String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("UAE")) {
            lang = "AR";
        } else {
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

