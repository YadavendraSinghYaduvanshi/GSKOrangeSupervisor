package cpm.com.gskmtorange.gsk_dailyentry;

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
import cpm.com.gskmtorange.xmlGetterSetter.CategoryGetterSetter;

public class CategoryListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txt_categoryName;

    ArrayList<CategoryGetterSetter> categoryList;
    CategoryListAdapter adapter;

    GSKOrangeDB db;

    private SharedPreferences preferences;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        db = new GSKOrangeDB(this);
        db.open();

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


        /*txt_categoryName = (TextView) findViewById(R.id.txt_categoryName);
        txt_categoryName.setText(getResources().getString(R.string.title_activity_category_list));*/
        toolbar.setTitle(getResources().getString(R.string.title_activity_category_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        categoryList = new ArrayList<>();

        categoryList = db.getCategoryListData(keyAccount_id, storeType_id, class_id);

        adapter = new CategoryListAdapter(CategoryListActivity.this, categoryList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<CategoryGetterSetter> list = Collections.emptyList();
        Context context;

        public CategoryListAdapter(CategoryListActivity context, List<CategoryGetterSetter> list) {
            inflator = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
        }

        @Override
        public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.category_menu_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final CategoryGetterSetter categoryData = list.get(position);

            holder.categoryName.setText(categoryData.getCategory());
            //holder.categoryIcon.setImageResource(R.drawable.category);

            if (categoryData.getCategory().equalsIgnoreCase("Oral Health")) {
                if (db.checkMsl_AvailabilityData(store_id, categoryData.getCategory_id())
                        && db.checkStockAndFacingData(store_id, categoryData.getCategory_id())
                        && db.checkPromoComplianceData(store_id, categoryData.getCategory_id())) {

                    holder.categoryIcon.setImageResource(R.drawable.ohc_done);
                } else {
                    holder.categoryIcon.setImageResource(R.drawable.ohc);
                }
            } else if (categoryData.getCategory().equalsIgnoreCase("Wellness")) {
                if (db.checkMsl_AvailabilityData(store_id, categoryData.getCategory_id())
                        && db.checkStockAndFacingData(store_id, categoryData.getCategory_id())
                        && db.checkPromoComplianceData(store_id, categoryData.getCategory_id())) {

                    holder.categoryIcon.setImageResource(R.drawable.pdr_done);
                } else {

                    holder.categoryIcon.setImageResource(R.drawable.pdr);
                }
            } else if (categoryData.getCategory().equalsIgnoreCase("Nutritionals")) {

                if (db.checkMsl_AvailabilityData(store_id, categoryData.getCategory_id())
                        && db.checkStockAndFacingData(store_id, categoryData.getCategory_id())
                        && db.checkPromoComplianceData(store_id, categoryData.getCategory_id())) {

                    holder.categoryIcon.setImageResource(R.drawable.hfd_done);
                } else {
                    holder.categoryIcon.setImageResource(R.drawable.hfd);
                }
            }


            holder.lay_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CategoryListActivity.this, CategoryWisePerformanceActivity.class);
                    intent.putExtra("categoryName", categoryData.getCategory());
                    intent.putExtra("categoryId", categoryData.getCategory_id());
                    startActivity(intent);
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

        if (id == android.R.id.home) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
