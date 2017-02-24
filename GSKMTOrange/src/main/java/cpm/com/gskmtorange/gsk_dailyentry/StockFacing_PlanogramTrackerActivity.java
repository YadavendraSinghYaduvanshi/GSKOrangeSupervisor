package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.StockFacing_PlanogramTrackerDataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;

public class StockFacing_PlanogramTrackerActivity extends AppCompatActivity {
    Button btn_addShelf, btn_addSKU;
    ExpandableListView expandableListView;
    String brand, brand_id, company_id, sub_category, sub_category_id;
    String addShelfPosition = "";
    ArrayAdapter<String> shelfPositionAdapter, shelfAdapter;
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> shelfList;
    GSKOrangeDB db;
    PlanogramExpandableListAdapter adapter;
    String categoryName, categoryId, Error_Message = "";
    boolean checkflag = true;
    List<Integer> checkHeaderArray = new ArrayList<>();

    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> shelfHeaderDataList = new ArrayList<>();
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> childDataList, childDataList1;
    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> hashMapListChildData = new HashMap<>();
    private SharedPreferences preferences;

    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;

    ExpandableListAdapter adapter1;
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> addSkuHeaderList;
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> addSkuChildList;
    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> addSkuHashMapChildData;

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
        }else{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_facing__planogram_tracker);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(getResources().getString(R.string.stock_planogram_planogram_tracker));
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            db = new GSKOrangeDB(this);
            db.open();

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

            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            btn_addShelf = (Button) findViewById(R.id.btn_addShelf);
            expandableListView = (ExpandableListView) findViewById(R.id.exp_PlanogramListView);

            Intent intent = getIntent();
            /*brand = intent.getStringExtra("brand");
            brand_id = intent.getStringExtra("brand_id");
            company_id = intent.getStringExtra("company_id");
            sub_category = intent.getStringExtra("sub_category");
            sub_category_id = intent.getStringExtra("sub_category_id");*/


            //Shelf Position Adapter
            shelfPositionAdapter = new ArrayAdapter<String>(StockFacing_PlanogramTrackerActivity.this, android.R.layout.simple_list_item_1);
            shelfPositionAdapter.add(getResources().getString(R.string.select));    //Select
            for (int i = 7; i >= 1; i--) {
                shelfPositionAdapter.add(String.valueOf(i));
            }

            //Shelf List
            shelfList = new ArrayList<>();
            shelfList = db.getSHELF_MASTERData();

            shelfAdapter = new ArrayAdapter<String>(StockFacing_PlanogramTrackerActivity.this, android.R.layout.simple_list_item_1);
            for (int j = 0; j < shelfList.size(); j++) {
                shelfAdapter.add(shelfList.get(j).getShelf());
            }

            //DefaultList
            prepareDefaultList();


            btn_addShelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view1) {
                    final Dialog dialog = new Dialog(StockFacing_PlanogramTrackerActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_stock_facing_planogram_tracker);

                    Button addShelf = (Button) dialog.findViewById(R.id.dialog_btn_addShelf);
                    Button cancel = (Button) dialog.findViewById(R.id.dialog_btn_cancel);
                    Spinner sp_addShelf = (Spinner) dialog.findViewById(R.id.sp_addShelf);
                    Spinner sp_shelfPosition = (Spinner) dialog.findViewById(R.id.sp_shelfPosition);

                    sp_addShelf.setAdapter(shelfAdapter);
                    sp_shelfPosition.setAdapter(shelfPositionAdapter);

                    final StockFacing_PlanogramTrackerDataGetterSetter data = new StockFacing_PlanogramTrackerDataGetterSetter();

                    sp_addShelf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            for (int i1 = 0; i1 < shelfList.size(); i1++) {
                                if (position == i1) {
                                    //Selected spinner position value
                                    data.setSp_addShelf_id(shelfList.get(i1).getShelf_id());
                                    data.setSp_addShelf(shelfList.get(i1).getShelf());
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    sp_shelfPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                            data.setSp_shelfPosition(parent.getItemAtPosition(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    addShelf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!data.getSp_addShelf_id().equals("0") && !data.getSp_shelfPosition().equals("Select")) {

                                //Check here the shelf and position is already exists or not
                                //if (shelfHeaderDataList.size() > 0) {

                                //Checking shelf is exists or not, if not add the shelf
                                boolean isContain = false;
                                for (int i = 0; i < shelfHeaderDataList.size(); i++) {
                                    if (data.getSp_addShelf_id().equals(shelfHeaderDataList.get(i).getSp_addShelf_id())
                                            && data.getSp_shelfPosition().equals(shelfHeaderDataList.get(i).getSp_shelfPosition())) {

                                        isContain = true;
                                        break;
                                    }
                                }

                                if (isContain) {
                                    Snackbar.make(view, data.getSp_addShelf() +
                                                    getString(R.string.stock_planogram_shelf_position) + data.getSp_shelfPosition()
                                                    + getString(R.string.stock_planogram_already_exists)
                                            , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                } else {
                                    shelfHeaderDataList.add(data);
                                    dialog.dismiss();
                                    prepareHeaderList(shelfHeaderDataList);
                                }
                            } else {
                                Toast.makeText(StockFacing_PlanogramTrackerActivity.this, getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    dialog.show();
                }
            });


            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_stockFacing_PlanogramTracker);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (validateData(shelfHeaderDataList, hashMapListChildData)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockFacing_PlanogramTrackerActivity.this);
                        builder.setMessage(getResources().getString(R.string.check_save_message))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        db.InsertStock_Facing_PlanogramTracker(store_id, categoryId, company_id, brand_id,
                                                sub_category_id, shelfHeaderDataList, hashMapListChildData);

                                        finish();
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockFacing_PlanogramTrackerActivity.this);
                        builder.setMessage(Error_Message)
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });

            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;

                    if (firstVisibleItem == 0) {
                        fab.setVisibility(View.VISIBLE);
                    } else if (lastItem == totalItemCount) {
                        fab.setVisibility(View.INVISIBLE);
                    } /*else {
                        fab.setVisibility(View.VISIBLE);
                    }*/
                }

                @Override
                public void onScrollStateChanged(AbsListView arg0, int arg1) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        getCurrentFocus().clearFocus();
                    }

                    //expandableListView.invalidateViews();
                }
            });

            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareDefaultList() {

        //After save shelf header data
        shelfHeaderDataList = db.getPlanogramAddShelfHeaderAfterSaveData(store_id, categoryId);

        if (shelfHeaderDataList.size() > 0) {

            //Sort Shelf Position wise
            Collections.sort(shelfHeaderDataList, StockFacing_PlanogramTrackerDataGetterSetter.shelfPositionComparator);
            //Sort Shelf wise
            Collections.sort(shelfHeaderDataList, StockFacing_PlanogramTrackerDataGetterSetter.shelfComparator);


            for (int i = 0; i < shelfHeaderDataList.size(); i++) {
                childDataList = db.getStockAndFacingPlanogramAfterSKUData(shelfHeaderDataList.get(i).getKey_id());

                //After save sku child data
                if (childDataList.size() > 0) {
                    hashMapListChildData.put(shelfHeaderDataList.get(i), childDataList);
                }
            }
        }

        adapter = new PlanogramExpandableListAdapter(this, shelfHeaderDataList, hashMapListChildData);
        expandableListView.setAdapter(adapter);

        if (childDataList != null && childDataList.size() > 0) {
            for (int j = 0; j < shelfHeaderDataList.size(); j++) {
                if (hashMapListChildData.get(shelfHeaderDataList.get(j)).size() > 0) {
                    expandableListView.expandGroup(j);
                }
            }
        }
    }

    //New Shelf Add
    private void prepareHeaderList(ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> shelfHeaderDataList1) {
        try {
            //Header Data
            if (shelfHeaderDataList1.size() > 0) {

                //Sort Shelf Position wise
/*                Collections.sort(shelfHeaderDataList1, StockFacing_PlanogramTrackerDataGetterSetter.shelfPositionComparator);
                //Sort Shelf wise
                Collections.sort(shelfHeaderDataList1, StockFacing_PlanogramTrackerDataGetterSetter.shelfComparator);*/

                adapter.notifyDataSetChanged();

                if (childDataList1 != null && childDataList1.size() > 0) {
                    for (int j = 0; j < shelfHeaderDataList.size(); j++) {
                        if (hashMapListChildData.get(shelfHeaderDataList.get(j)).size() > 0) {
                            expandableListView.expandGroup(j);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Sku Add in Particular Shelf with position
    private void prepareSkuList(StockFacing_PlanogramTrackerDataGetterSetter object,
                                ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> tempAddSkuHeaderList,
                                HashMap<StockFacing_PlanogramTrackerDataGetterSetter,
                                        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> tempAddSkuHashMapChildData) {
        try {
            childDataList1 = new ArrayList<>();

            for (int i1 = 0; i1 < tempAddSkuHeaderList.size(); i1++) {
                ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> tempList =
                        tempAddSkuHashMapChildData.get(tempAddSkuHeaderList.get(i1));

                for (int j = 0; j < tempList.size(); j++) {
                    StockFacing_PlanogramTrackerDataGetterSetter data = tempList.get(j);

                    data.setSp_addShelf_id(object.getSp_addShelf_id());

                    if (data.getCheckbox_sku().equals("1")) {
                        childDataList1.add(data);
                    }
                }
            }

            if (shelfHeaderDataList.size() > 0) {
                //Child Sku Data
                hashMapListChildData.put(object, childDataList1);
                adapter.notifyDataSetChanged();

                if (childDataList1 != null && childDataList1.size() > 0) {
                    for (int j = 0; j < shelfHeaderDataList.size(); j++) {
                        if (hashMapListChildData.get(shelfHeaderDataList.get(j)).size() > 0) {
                            expandableListView.expandGroup(j);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PlanogramExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockFacing_PlanogramTrackerDataGetterSetter> _listDataHeader;
        private HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> _listDataChild;

        public PlanogramExpandableListAdapter(Context context, List<StockFacing_PlanogramTrackerDataGetterSetter> listDataHeader,
                                              HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final StockFacing_PlanogramTrackerDataGetterSetter headerTitle =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getGroup(groupPosition);

            if (headerTitle != null) {
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.item_stock_facing_planogram_header, null, false);
                }

                TextView txt_shelfHeader = (TextView) convertView.findViewById(R.id.txt_shelfHeader);
                Button btn_addSku = (Button) convertView.findViewById(R.id.btn_addSku);

                txt_shelfHeader.setText(headerTitle.getSp_addShelf() + " (Position : " + headerTitle.getSp_shelfPosition() + ")");

                btn_addSku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Stock Facing Planogram SKU Data
                        final Dialog dialog1 = new Dialog(StockFacing_PlanogramTrackerActivity.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog1.setContentView(R.layout.dialog_stock_facing_planogram_tracker_add_sku);

                        //LinearLayout lin_addSku = (LinearLayout) dialog1.findViewById(R.id.lin_addSku);
                        Button addSku_Shelf = (Button) dialog1.findViewById(R.id.dialog_btn_addSku_Shelf);
                        Button cancel = (Button) dialog1.findViewById(R.id.dialog_btn_cancel_addSku);
                        ExpandableListView exp_addSkuListView = (ExpandableListView) dialog1.findViewById(R.id.exp_addSkuListView);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        //Add Sku Dailog List
                        dialogAddSkuList(exp_addSkuListView, headerTitle);

                        addSku_Shelf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean flag = false;

                                for (int i1 = 0; i1 < addSkuHeaderList.size(); i1++) {
                                    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> tempList =
                                            addSkuHashMapChildData.get(addSkuHeaderList.get(i1));

                                    for (int j = 0; j < tempList.size(); j++) {
                                        StockFacing_PlanogramTrackerDataGetterSetter data = tempList.get(j);

                                        if (data.getCheckbox_sku().equals("1")) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }

                                if (flag) {
                                    prepareSkuList(headerTitle, addSkuHeaderList, addSkuHashMapChildData);
                                    dialog1.dismiss();
                                } else {
                                    Snackbar.make(view, getString(R.string.stock_planogram_data_select_one_sku), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }
                        });
                        dialog1.show();
                    }
                });

                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_shelfHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_shelfHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }

            }
            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            StockFacing_PlanogramTrackerDataGetterSetter childData =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (childData != null) {
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.item_stock_facing_planogram_child, null, false);

                    holder = new ViewHolder();
                    holder.txt_skuChild = (TextView) convertView.findViewById(R.id.txt_skuChild);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.chk_sku);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.checkBox.setVisibility(View.GONE);
                holder.checkBox.setEnabled(false);

                holder.txt_skuChild.setText(childData.getSku());

                if (childData.getCheckbox_sku().equals("1")) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
        TextView txt_skuChild;
        CheckBox checkBox;
    }

    boolean validateData(List<StockFacing_PlanogramTrackerDataGetterSetter> listDataHeader,
                         HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> listDataChild) {
        boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader.size(); i++) {

            if (listDataChild.get(listDataHeader.get(i)) == null) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }

                checkflag = false;
                Error_Message = getString(R.string.stock_planogram_data_add_shelf);
                break;
            } else {
                if (listDataChild.get(listDataHeader.get(i)).size() <= 0) {
                    if (!checkHeaderArray.contains(i)) {
                        checkHeaderArray.add(i);
                    }

                    flag = false;
                    Error_Message = getString(R.string.stock_planogram_data_add_shelf);
                    break;
                }
            }

            if (flag == false) {
                checkflag = false;
                break;
            } else {
                checkflag = true;
            }
        }

        //expListView.invalidate();
        adapter.notifyDataSetChanged();

        return checkflag;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StockFacing_PlanogramTrackerActivity.this);
        builder.setTitle(getResources().getString(R.string.dialog_title));
        builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StockFacing_PlanogramTrackerActivity.this);
            builder.setTitle(getResources().getString(R.string.dialog_title));
            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //Add Sku Dailog Display List
    private void dialogAddSkuList(ExpandableListView exp_addSkuListView,
                                  StockFacing_PlanogramTrackerDataGetterSetter headerObject) {
        try {
            //By this headerObject we need to get the sku list
            ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> tempSkuList = new ArrayList<>();
            tempSkuList = db.getStockAndFacingPlanogramAfterSKU_PerShelfData(headerObject.getSp_addShelf_id(),
                    headerObject.getSp_shelfPosition());


            addSkuHeaderList = new ArrayList<>();
            addSkuChildList = new ArrayList<>();
            addSkuHashMapChildData = new HashMap<>();

            //Header Data
            addSkuHeaderList = db.getPlanogramAddSkuHeaderData(categoryId, keyAccount_id, storeType_id, class_id);

            if (addSkuHeaderList.size() > 0) {

                for (int i = 0; i < addSkuHeaderList.size(); i++) {
                    //Child Data
                    if (addSkuHeaderList.get(i).getCompany_id().equals("1")) {

                        //Default Case
                        if (!(tempSkuList.size() > 0)) {
                            addSkuChildList = db.getPlanogramAddSkuChildData(categoryId, addSkuHeaderList.get(i).getBrand_id(),
                                    keyAccount_id, storeType_id, class_id);

                            addSkuHashMapChildData.put(addSkuHeaderList.get(i), addSkuChildList);

                        } else {    //After Save Sku Data
                            addSkuChildList = db.getPlanogramAddSkuChildData(categoryId, addSkuHeaderList.get(i).getBrand_id(),
                                    keyAccount_id, storeType_id, class_id);

                            for (int i1 = 0; i1 < addSkuChildList.size(); i1++) {

                                for (int j = 0; j < tempSkuList.size(); j++) {
                                    if (addSkuChildList.get(i1).getSku_id().equals(tempSkuList.get(j).getSku_id())) {
                                        addSkuChildList.set(i1, tempSkuList.get(j));
                                    }
                                }
                            }

                            addSkuHashMapChildData.put(addSkuHeaderList.get(i), addSkuChildList);
                        }

                    }
                }
            }

            adapter1 = new ExpandableListAdapter(this, addSkuHeaderList, addSkuHashMapChildData);
            exp_addSkuListView.setAdapter(adapter1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add Sku Dailog Display List Adapter
    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockFacing_PlanogramTrackerDataGetterSetter> _listDataHeader;
        private HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockFacing_PlanogramTrackerDataGetterSetter> listDataHeader,
                                     HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final StockFacing_PlanogramTrackerDataGetterSetter headerTitle =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_stock_facing_add_sku_header, null, false);
            }

            TextView txt_stockFaceupHeader = (TextView) convertView.findViewById(R.id.txt_stockFaceupHeader);

            txt_stockFaceupHeader.setTypeface(null, Typeface.BOLD);
            txt_stockFaceupHeader.setText(headerTitle.getSub_category() + "-" + headerTitle.getBrand());

            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final StockFacing_PlanogramTrackerDataGetterSetter childData =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder1 holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_stock_facing_planogram_child, null, false);

                holder = new ViewHolder1();
                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuChild);
                holder.chk_sku = (CheckBox) convertView.findViewById(R.id.chk_sku);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getSku());

            holder.chk_sku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                    if (isCheck) {
                        childData.setCheckbox_sku("1");
                    } else {
                        childData.setCheckbox_sku("0");
                    }
                }
            });

            if (childData.getCheckbox_sku().equals("0")) {
                holder.chk_sku.setChecked(false);
            } else if (childData.getCheckbox_sku().equals("1")) {
                holder.chk_sku.setChecked(true);
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder1 {
        //CardView cardView;
        TextView txt_skuName;
        CheckBox chk_sku;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }
}
