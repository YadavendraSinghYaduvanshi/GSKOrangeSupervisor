package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
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

    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> headerDataList = new ArrayList<>();
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> childDataList, tempChildDataList;
    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, List<StockFacing_PlanogramTrackerDataGetterSetter>> hashMapListChildData = new HashMap<>();

    private SharedPreferences preferences;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_facing__planogram_tracker);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            db = new GSKOrangeDB(this);
            db.open();

            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            //updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

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
            brand = intent.getStringExtra("brand");
            brand_id = intent.getStringExtra("brand_id");
            company_id = intent.getStringExtra("company_id");
            sub_category = intent.getStringExtra("sub_category");
            sub_category_id = intent.getStringExtra("sub_category_id");


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

            //Add Shelf Header Data
            //prepareHeaderList(headerDataList);

            /*StockFacing_PlanogramTrackerDataGetterSetter sb = new StockFacing_PlanogramTrackerDataGetterSetter();
            sb.setSp_addShelf_id("1");
            sb.setSp_addShelf("Shelf1");
            sb.setSp_shelfPosition("1");

            headerDataList.add(sb);
            prepareHeaderList(headerDataList);*/

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

                                headerDataList.add(data);
                                dialog.dismiss();
                                prepareHeaderList(headerDataList);
                            } else {
                                /*Snackbar.make(view1, getResources().getString(R.string.empty_field), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
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
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }
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

            /*expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;

                    *//*if (firstVisibleItem == 0) {
                        fab.setVisibility(View.VISIBLE);
                    } else if (lastItem == totalItemCount) {
                        fab.setVisibility(View.INVISIBLE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }*//*
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

            // Listview Group click listener
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return false;
                }
            });

            // Listview Group expanded listener
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getWindow().getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        getCurrentFocus().clearFocus();
                    }
                }
            });

            // Listview Group collasped listener
            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getWindow().getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        getCurrentFocus().clearFocus();
                    }
                }
            });

            // Listview on child click listener
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    return false;
                }
            });*/
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareHeaderList(ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> headerDataList) {
        try {
            //Header Data
            if (headerDataList.size() > 0) {

                /*for (int i = 0; i < headerDataList.size(); i++) {
                    hashMapListChildData.put(headerDataList.get(i), childDataList);

                    *//*if (childDataList != null && childDataList.size() > 0) {
                        if (headerDataList.get(i).getSp_addShelf_id().equals(childDataList.get(i).getSp_addShelf_id())) {
                            hashMapListChildData.put(headerDataList.get(i), childDataList);
                        } else {
                            hashMapListChildData.put(headerDataList.get(i), tempChildDataList);
                        }
                    } else {
                        hashMapListChildData.put(headerDataList.get(i), tempChildDataList);
                    }*//*
                }*/

                adapter = new PlanogramExpandableListAdapter(this, headerDataList, hashMapListChildData);
                expandableListView.setAdapter(adapter);

                if (childDataList != null && childDataList.size() > 0) {
                    for (int j = 0; j < headerDataList.size(); j++) {
                        if (hashMapListChildData.get(headerDataList.get(j)).size() > 0) {
                            expandableListView.expandGroup(j);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareSkuList(ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> childDataList1,
                                StockFacing_PlanogramTrackerDataGetterSetter object) {
        try {
            if (headerDataList.size() > 0) {

                hashMapListChildData.put(object, childDataList1);
                /*//Child Sku Data
                for (int i = 0; i < headerDataList.size(); i++) {
                    if (object.getSp_addShelf_id().equals(headerDataList.get(i).getSp_addShelf_id())) {
                        hashMapListChildData.put(object, childDataList1);
                    } else {
                        hashMapListChildData.put(headerDataList.get(i), childDataList);
                    }
                }*/

/*                adapter = new PlanogramExpandableListAdapter(this, headerDataList, hashMapListChildData);
                expandableListView.setAdapter(adapter);*/
                expandableListView.invalidate();


                if (childDataList != null && childDataList.size() > 0) {
                    for (int j = 0; j < headerDataList.size(); j++) {
                        if (hashMapListChildData.get(headerDataList.get(j)).size() > 0) {
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
        private HashMap<StockFacing_PlanogramTrackerDataGetterSetter, List<StockFacing_PlanogramTrackerDataGetterSetter>> _listDataChild;

        public PlanogramExpandableListAdapter(Context context, List<StockFacing_PlanogramTrackerDataGetterSetter> listDataHeader,
                                              HashMap<StockFacing_PlanogramTrackerDataGetterSetter, List<StockFacing_PlanogramTrackerDataGetterSetter>> listChildData) {
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

                    TextView txt_shelfHeader = (TextView) convertView.findViewById(R.id.txt_shelfHeader);
                    Button btn_addSku = (Button) convertView.findViewById(R.id.btn_addSku);

                    txt_shelfHeader.setText(headerTitle.getSp_addShelf() + " (Position : " + headerTitle.getSp_shelfPosition() + ")");

                    btn_addSku.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Stock Facing Planogram SKU Data
                            tempChildDataList = db.getStockAndFacingPlanogramDefaultSKUData(categoryId, brand_id,
                                    keyAccount_id, storeType_id, class_id);

                            final Dialog dialog1 = new Dialog(StockFacing_PlanogramTrackerActivity.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog1.setContentView(R.layout.dialog_stock_facing_planogram_tracker_add_sku);

                            LinearLayout lin_addSku = (LinearLayout) dialog1.findViewById(R.id.lin_addSku);
                            Button addSKU = (Button) dialog1.findViewById(R.id.dialog_btn_addSku_Shelf);
                            Button cancel = (Button) dialog1.findViewById(R.id.dialog_btn_cancel_addSku);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                }
                            });

                            childDataList = new ArrayList<>();

                            for (int i = 0; i < tempChildDataList.size(); i++) {
                                View view1 = getLayoutInflater().inflate(R.layout.item_stock_facing_planogram_child, null);

                                TextView txt_skuChild = (TextView) view1.findViewById(R.id.txt_skuChild);
                                CheckBox chk_sku = (CheckBox) view1.findViewById(R.id.chk_sku);

                                final StockFacing_PlanogramTrackerDataGetterSetter childData = tempChildDataList.get(i);
                                txt_skuChild.setText(childData.getSku());

                                childData.setSp_addShelf_id(headerTitle.getSp_addShelf_id());

                                if (childData.getCheckbox_sku().equals("0")) {
                                    chk_sku.setChecked(false);
                                } else if (childData.getCheckbox_sku().equals("1")) {
                                    chk_sku.setChecked(true);
                                }

                                chk_sku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                                        if (isCheck) {
                                            childData.setCheckbox_sku("1");
                                        } else {
                                            childData.setCheckbox_sku("0");
                                        }
                                    }
                                });

                                childDataList.add(childData);
                                lin_addSku.addView(view1);
                            }

                            addSKU.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    /*headerTitle.getShelf_id();
                                    childDataList.size();*/

                                    prepareSkuList(childDataList, headerTitle);
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                        }
                    });
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
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            StockFacing_PlanogramTrackerDataGetterSetter childData =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getChild(groupPosition, childPosition);
            //ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_stock_facing_planogram_child, null, false);

                //holder = new ViewHolder();
                TextView txt_skuChild = (TextView) convertView.findViewById(R.id.txt_skuChild);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chk_sku);
                //checkBox.setVisibility(View.GONE);
                checkBox.setEnabled(false);

                txt_skuChild.setText(childData.getSku());

                if (childData.getCheckbox_sku().equals("1")) {
                    checkBox.setChecked(true);
                }


                //convertView.setTag(holder);
            } /*else {
                holder = (ViewHolder) convertView.getTag();
            }*/


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
}
