package cpm.com.gskmtorange.dailyentry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.adapter.ListAdapter;
import cpm.com.gskmtorange.adapter.MyAdapter;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.listener.Listener;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DynamycIdsGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NoCameraDataGetterSetter;

public class NoCameraActivity extends AppCompatActivity implements Listener {

    GSKOrangeDB db;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    String categoryName, categoryId;
    int number_of_rows;
    private SharedPreferences preferences;
    LinearLayout linear_bottom_parent;
    ArrayList<DynamycIdsGetterSetter> ids = new ArrayList<>();

    ArrayList<NoCameraDataGetterSetter> top_list = new ArrayList<>();

    RecyclerView rvTop;
    MSL_AvailabilityStockFacingGetterSetter sub_category_data;

    FloatingActionButton fab;

    ScrollView scroll_no_camera;

    int mScrollDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        rvTop = (RecyclerView) findViewById(R.id.rvTop);
        linear_bottom_parent = (LinearLayout) findViewById(R.id.lin_bottom_parent);
        scroll_no_camera = (ScrollView) findViewById(R.id.scroll_no_camera);

        //SCROLLVIEW
        scroll_no_camera.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mScrollDistance = scroll_no_camera.getScrollY(); // For ScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
            }
        });



        //preference data
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);

        //Intent data
        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");
        number_of_rows = getIntent().getIntExtra(CommonString.KEY_NUMBER_OF_ROWS, 0);
        sub_category_data = (MSL_AvailabilityStockFacingGetterSetter) getIntent().getSerializableExtra(CommonString.KEY_SUB_CATEGORY);

        db = new GSKOrangeDB(NoCameraActivity.this);
        db.open();

        top_list = prepareTopList(categoryId);

        rvTop.setLayoutManager(new GridLayoutManager(this, 3));
        ListAdapter topListAdapter = new ListAdapter(top_list, this);
        rvTop.setAdapter(topListAdapter);
        //tvEmptyListTop.setOnDragListener(topListAdapter.getDragInstance());
        rvTop.setOnDragListener(topListAdapter.getDragInstance());

        for (int i = 1; i <= number_of_rows; i++) {

            DynamycIdsGetterSetter dynamic_ids = new DynamycIdsGetterSetter();

            // first Button
            RelativeLayout rLayout = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams lprams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(getApplicationContext());
            tv.setHeight(60);
            tv.setTextSize(18);
            tv.setBackgroundColor(getResources().getColor(R.color.grey_textview));
            tv.setText("Drop SKU Groups Here ");
            tv.setPadding(5, 5, 0, 0);
            tv.setLayoutParams(lprams);
            int tv_id = View.generateViewId();
            tv.setId(tv_id);

            //set dynamic textview id to object
            dynamic_ids.setTextview_id(tv_id);

            RecyclerView rec_bottom = new RecyclerView(getApplicationContext());
            int rec_id = View.generateViewId();

            //set dynamic recycler id to object
            dynamic_ids.setRecycler_id(rec_id);

            rec_bottom.setLayoutParams(lprams);
            rec_bottom.setPadding(0, 0, 0, 2);

            rec_bottom.setVisibility(View.GONE);

            rec_bottom.setId(rec_id);
            rec_bottom.setBackgroundColor(getResources().getColor(R.color.grey_background));

            RelativeLayout.LayoutParams lprams2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lprams2.height = 2;
            View view = new View(getApplicationContext());
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setLayoutParams(lprams2);

            rLayout.addView(tv);
            rLayout.addView(rec_bottom);
            rLayout.addView(view);
            rLayout.setBackgroundColor(getResources().getColor(R.color.white));

            linear_bottom_parent.addView(rLayout);

            ids.add(dynamic_ids);

            rec_bottom.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));

            List<BrandMasterGetterSetter> bottomList = new ArrayList<>();

        /* bottomList.add("C");
        bottomList.add("D");*/

        /*setEmptyListBottom(true);
        ListAdapter bottomListAdapter = new ListAdapter(bottomList, this);
        rvBottom.setAdapter(bottomListAdapter);*/

            List<NoCameraDataGetterSetter> brands = new ArrayList<>();

            MyAdapter adapter = new MyAdapter(getApplicationContext(), brands, this);
            rec_bottom.setAdapter(adapter);

            tv.setOnDragListener(adapter.getDragInstance());
            rec_bottom.setOnDragListener(adapter.getDragInstance());
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag_all_rows_filled = true;

                HashMap<Integer, List<NoCameraDataGetterSetter>> hashMapRowData = new HashMap<>();

                for(int i=0; i < number_of_rows;i++){

                    int rec_id = ids.get(i).getRecycler_id();

                    RecyclerView target = (RecyclerView) findViewById(rec_id);
                    MyAdapter adapterMYTarget = (MyAdapter) target.getAdapter();
                    List<NoCameraDataGetterSetter> customListMyTarget = adapterMYTarget.getList();
                    if(customListMyTarget.size()==0){
                        flag_all_rows_filled = false;
                        break;
                    }
                    else{
                        hashMapRowData.put(i+1, customListMyTarget);
                    }

                }

                if(flag_all_rows_filled){
                    db.open();
                    db.InsertNoCameraAddedData(store_id, categoryId, sub_category_data.getSub_category_id(), number_of_rows, hashMapRowData);
                    finish();
                }
                else {
                    Snackbar.make(linear_bottom_parent,R.string.please_add_subgroup_facing,Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<NoCameraDataGetterSetter> prepareTopList(String category_id) {

        return db.getSkuGroupMasterData(category_id, sub_category_data.getSub_category_id());
    }

    @Override
    public void setEmptyListBottom(boolean visibility, int tv, RecyclerView rv) {
        ((TextView) findViewById(tv)).setVisibility(visibility ? View.VISIBLE : View.GONE);
        rv.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void addNUpdateRow(View v, int view_id, NoCameraDataGetterSetter parent_item) {

        int rv_id = 0;
        if (v instanceof TextView) {

            for (int i = 0; i < ids.size(); i++) {

                if (ids.get(i).getTextview_id() == view_id) {
                    rv_id = ids.get(i).getRecycler_id();
                }
            }

            RecyclerView target = (RecyclerView) v.getRootView().findViewById(rv_id);
            ((TextView) findViewById(view_id)).setVisibility(false ? View.VISIBLE : View.GONE);
            target.setVisibility(false ? View.GONE : View.VISIBLE);

        } else {
            rv_id = view_id;
        }

        RecyclerView target;
        target = (RecyclerView) v.getRootView().findViewById(rv_id);

        MyAdapter adapterMYTarget = (MyAdapter) target.getAdapter();
        List<NoCameraDataGetterSetter> customListMyTarget = adapterMYTarget.getList();


        showAddFacingDialog(adapterMYTarget, customListMyTarget, parent_item);

    }

    @Override
    public void smoothScrollToRow(View v, DragEvent event) {

        //no action necessary
        int y = Math.round(v.getY())+Math.round(event.getY());
        int translatedY = y - mScrollDistance;
        //Log.i("translated",""+translatedY+" "+ mScrollDistance+" "+y);
        int threshold =50 ;
        // make a scrolling up due the y has passed the threshold
       /* if (translatedY < 200) {
            // make a scroll up by 30 px
            scroll_no_camera.smoothScrollBy(0, -15);
        }
        // make a autoscrolling down due y has passed the 500 px border
        if (translatedY + threshold > 500) {
            // make a scroll down by 30 px
            scroll_no_camera.smoothScrollBy(0, 15);
        }*/
    }

    public void showAddFacingDialog(final MyAdapter adapterMYTarget, final List<NoCameraDataGetterSetter> customListMyTarget, final NoCameraDataGetterSetter parent_item) {

        // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)

        final Dialog dialog = new Dialog(NoCameraActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_facing_custom_dialog);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        final EditText et_facing = (EditText) dialog.findViewById(R.id.et_facing);

        final Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String facing = et_facing.getText().toString();

                if (facing.equals("")) {
                    Snackbar.make(btn_ok, R.string.please_facing, Snackbar.LENGTH_SHORT).show();
                } else {
                    int facing_int = Integer.parseInt(facing);
                    parent_item.setFacing(facing_int);
                    NoCameraDataGetterSetter item = new NoCameraDataGetterSetter();
                    item.setSKUGROUP_ID(parent_item.getSKUGROUP_ID());
                    item.setSKUGROUP_NAME(parent_item.getSKUGROUP_NAME());
                    item.setFacing(facing_int);
                    customListMyTarget.add(item);

                    adapterMYTarget.updateList(customListMyTarget);
                    adapterMYTarget.notifyDataSetChanged();
                    dialog.cancel();
                }

            }
        });

        //dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NoCameraActivity.this);
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
            alert.show();

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NoCameraActivity.this);
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
        alert.show();
    }
}
