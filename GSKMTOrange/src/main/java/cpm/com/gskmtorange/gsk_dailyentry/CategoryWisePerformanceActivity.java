package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryWisePerformaceGetterSetter;

public class CategoryWisePerformanceActivity extends AppCompatActivity {
    TextView txt_categoryName;
    RecyclerView recyclerView;

    String categoryName = "", categoryId;

    ArrayList<CategoryWisePerformaceGetterSetter> categoryWisePerformanceList;
    CategoryWisePerformaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_performance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txt_categoryName = (TextView) findViewById(R.id.txt_categoryName);

        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");

        //txt_categoryName.setText("CategoryWise Performance " + categoryName);
        txt_categoryName.setText(getResources().getString(R.string.title_activity_category_wise_performance) + " " + categoryName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryWisePerformanceActivity.this, DailyDataMenuActivity.class);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("categoryId", categoryId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        categoryWisePerformanceList = new ArrayList<>();
        CategoryWisePerformaceGetterSetter data = new CategoryWisePerformaceGetterSetter();

        data.setPeriod("Period");
        data.setSos("SOS");
        data.setT2p("T2P");
        data.setPromo("Promo");
        data.setMsl_availability("MSL Availability");
        data.setOss("OSS");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("LTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("MTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("RTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("LTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("MTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        data = new CategoryWisePerformaceGetterSetter();
        data.setPeriod("RTM");
        data.setSos("75");
        data.setT2p("0");
        data.setPromo("0");
        data.setMsl_availability("25");
        data.setOss("55");
        categoryWisePerformanceList.add(data);

        adapter = new CategoryWisePerformaceAdapter(CategoryWisePerformanceActivity.this, categoryWisePerformanceList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public class CategoryWisePerformaceAdapter extends RecyclerView.Adapter<CategoryWisePerformaceAdapter.MyViewHolder> {
        Context context;
        private LayoutInflater inflator;
        List<CategoryWisePerformaceGetterSetter> list = Collections.emptyList();

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

            holder.txt_period.setText(categoryData.getPeriod());
            holder.txt_sos.setText(categoryData.getSos());
            holder.txt_t2p.setText(categoryData.getT2p());
            holder.txt_promo.setText(categoryData.getPromo());
            holder.txt_msl_availability.setText(categoryData.getMsl_availability());
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
}
