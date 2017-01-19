package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryWisePerformaceGetterSetter;

public class CategoryWisePerformanceActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;

    String categoryName = "", categoryId;

    ArrayList<CategoryWisePerformaceGetterSetter> categoryWisePerformanceList;
    CategoryWisePerformaceAdapter adapter;

    GSKOrangeDB db;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_category_wise_performance);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


            db = new GSKOrangeDB(this);
            db.open();

            //preference data
            preferences = PreferenceManager.getDefaultSharedPreferences(this);            
            updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
            store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
            visit_date = preferences.getString(CommonString.KEY_DATE, null);
            date = preferences.getString(CommonString.KEY_DATE, null);
            username = preferences.getString(CommonString.KEY_USERNAME, null);
            intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");
            keyAccount_id = preferences.getString(CommonString.KEY_KEYACCOUNT_ID, "");
            class_id = preferences.getString(CommonString.KEY_CLASS_ID, "");
            storeType_id = preferences.getString(CommonString.KEY_STORETYPE_ID, "");

            //Intent data
            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            //txt_categoryName.setText(getResources().getString(R.string.title_activity_category_wise_performance) + " " + categoryName);
            toolbar.setTitle(getResources().getString(R.string.title_activity_category_wise_performance) + " " + categoryName);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CategoryWisePerformanceActivity.this, DailyDataMenuActivity.class);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("categoryId", categoryId);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
            categoryWisePerformanceList = db.getCategoryWisePerformance(store_id, categoryId);

            adapter = new CategoryWisePerformaceAdapter(CategoryWisePerformanceActivity.this, categoryWisePerformanceList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
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
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CategoryWisePerformaceAdapter extends RecyclerView.Adapter<CategoryWisePerformaceAdapter.MyViewHolder> {
        Context context;
        List<CategoryWisePerformaceGetterSetter> list = Collections.emptyList();
        private LayoutInflater inflator;

        public CategoryWisePerformaceAdapter(Context context, List<CategoryWisePerformaceGetterSetter> list) {
            inflator = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
        }

        @Override
        public CategoryWisePerformaceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.item_category_wise_performance, parent, false);
            CategoryWisePerformaceAdapter.MyViewHolder holder = new CategoryWisePerformaceAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CategoryWisePerformaceAdapter.MyViewHolder holder, int position) {
            final CategoryWisePerformaceGetterSetter categoryData = list.get(position);

            if (categoryData.getPeriod().equalsIgnoreCase("LTM")) {
                holder.txt_period.setText(getResources().getString(R.string.category_performance_ltm));
            } else if (categoryData.getPeriod().equalsIgnoreCase("MTD")) {
                holder.txt_period.setText(getResources().getString(R.string.category_performance_mtd));
            } else if (categoryData.getPeriod().equalsIgnoreCase("LSV")) {
                holder.txt_period.setText(getResources().getString(R.string.category_performance_lsv));
            }
            //holder.txt_period.setText(categoryData.getPeriod());
            holder.txt_msl_availability.setText(categoryData.getMsl_availability());
            holder.txt_sos.setText(categoryData.getSos());
            holder.txt_t2p.setText(categoryData.getT2p());
            holder.txt_promo.setText(categoryData.getPromo());
            holder.txt_oss.setText(categoryData.getOss());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_period, txt_sos, txt_t2p, txt_promo, txt_msl_availability, txt_oss;
            LinearLayout lay_menu;

            public MyViewHolder(View itemView) {
                super(itemView);
                lay_menu = (LinearLayout) itemView.findViewById(R.id.lay_menu);
                txt_period = (TextView) itemView.findViewById(R.id.txt_period);
                txt_sos = (TextView) itemView.findViewById(R.id.txt_sos);
                txt_t2p = (TextView) itemView.findViewById(R.id.txt_t2p);
                txt_promo = (TextView) itemView.findViewById(R.id.txt_promo);
                txt_msl_availability = (TextView) itemView.findViewById(R.id.txt_msl_availability);
                txt_oss = (TextView) itemView.findViewById(R.id.txt_oss);
            }
        }
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
