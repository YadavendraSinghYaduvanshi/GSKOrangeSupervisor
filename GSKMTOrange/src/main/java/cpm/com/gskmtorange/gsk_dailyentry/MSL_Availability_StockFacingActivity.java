package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;

public class MSL_Availability_StockFacingActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    TextView txt_mslAvailabilityName;

    ArrayList<MSL_AvailabilityStockFacingGetterSetter> headerDataList;
    ArrayList<MSL_AvailabilityStockFacingGetterSetter> childDataList;
    List<MSL_AvailabilityStockFacingGetterSetter> hashMapListHeaderData;
    HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> hashMapListChildData;

    List<Integer> checkHeaderArray = new ArrayList<>();
    boolean checkflag = true;

    ExpandableListAdapter adapter;

    GSKOrangeDB db;

    String categoryName, categoryId, storeId, Error_Message = "";
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;
    boolean isDialogOpen = true;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_msl__availability_stock_facing);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            txt_mslAvailabilityName = (TextView) findViewById(R.id.txt_mslAvailabilityName);

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

            //Intent data
            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            //txt_mslAvailabilityName.setText(getResources().getString(R.string.title_activity_msl__availability));
            toolbar.setTitle(getResources().getString(R.string.title_activity_msl__availability));
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            prepareList();

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if (validateData(hashMapListHeaderData, hashMapListChildData)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                        builder.setMessage(getResources().getString(R.string.check_save_message))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();

                                        if (db.checkMsl_Availability_StockFacingData(store_id, categoryId)) {
                                            db.updateMSL_Availability_StockFacing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                            Snackbar.make(view, getResources().getString(R.string.update_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        } else {
                                            db.InsertMSL_Availability_StockFacing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                            Snackbar.make(view, getResources().getString(R.string.save_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                        builder.setMessage("Fill the value or fill 0 ")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }

    private void prepareList() {
        try {
            hashMapListHeaderData = new ArrayList<>();
            hashMapListChildData = new HashMap<>();

            //Header
            headerDataList = db.getMSL_Availability_StockFacingHeaderData(categoryId, keyAccount_id, storeType_id, class_id);

            if (headerDataList.size() > 0) {
                for (int i = 0; i < headerDataList.size(); i++) {
                    hashMapListHeaderData.add(headerDataList.get(i));

                    //childDataList = new ArrayList<>();
                    childDataList = db.getMSL_Availability_StockFacingSKU_AfterSaveData(categoryId, headerDataList.get(i).getBrand_id(), store_id);
                    if (!(childDataList.size() > 0)) {
                        childDataList = db.getMSL_Availability_StockFacingSKUData(categoryId, headerDataList.get(i).getBrand_id(), keyAccount_id, storeType_id, class_id);
                    }

                    hashMapListChildData.put(hashMapListHeaderData.get(i), childDataList);
                }
            }

            adapter = new ExpandableListAdapter(this, hashMapListHeaderData, hashMapListChildData);
            expandableListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean validateData(List<MSL_AvailabilityStockFacingGetterSetter> listDataHeader,
                         HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> listDataChild) {
        boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader.size(); i++) {

            for (int j = 0; j < listDataChild.get(listDataHeader.get(i)).size(); j++) {
                MSL_AvailabilityStockFacingGetterSetter data = listDataChild.get(listDataHeader.get(i)).get(j);

                String stock = data.getStock();
                String faceup = data.getFacing();

                //Company_id
                if (listDataChild.get(listDataHeader.get(i)).get(j).getCompany_id().equals("1")) {
                    if (faceup.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        flag = false;
                        Error_Message = getResources().getString(R.string.faceup_value);
                        break;
                    } else {
                        if (data.getToggleValue().equals("0")) {

                            if (stock.equals("")) {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }
                                flag = false;
                                Error_Message = getResources().getString(R.string.stock_value);
                                break;
                            }
                        }
                    }
                } else {
                    if (faceup.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }
                        flag = false;
                        Error_Message = getResources().getString(R.string.faceup_value);
                        break;
                    }
                }
            }

            if (flag == false) {
                checkflag = false;
                break;
            } else {
                checkflag = true;
            }
        }

        adapter.notifyDataSetChanged();

        return checkflag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
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
            AlertDialog alert = builder.create();
            alert.show();
            //finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
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
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<MSL_AvailabilityStockFacingGetterSetter> _listDataHeader;
        private HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<MSL_AvailabilityStockFacingGetterSetter> listDataHeader,
                                     HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> listChildData) {
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
            MSL_AvailabilityStockFacingGetterSetter headerTitle = (MSL_AvailabilityStockFacingGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_stock_facing_header, null, false);
            }

            TextView txt_categoryHeader = (TextView) convertView.findViewById(R.id.txt_categoryHeader);
            RelativeLayout rel_header = (RelativeLayout) convertView.findViewById(R.id.rel_categoryHeader);
            ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);

            txt_categoryHeader.setTypeface(null, Typeface.BOLD);

            if (headerTitle.getCompany_id().equals("1")) {
                txt_categoryHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                txt_categoryHeader.setTextColor(getResources().getColor(R.color.black));
            }
            txt_categoryHeader.setText(headerTitle.getSub_category() + "-" + headerTitle.getBrand());


            //empty check color change
            if (headerTitle.getCompany_id().equals("1")) {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_categoryHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_categoryHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            } else {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_categoryHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_categoryHeader.setTextColor(getResources().getColor(R.color.black));
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
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final MSL_AvailabilityStockFacingGetterSetter childData =
                    (MSL_AvailabilityStockFacingGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_stock_facing_child, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.lin_category = (LinearLayout) convertView.findViewById(R.id.lin_category);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.txt_mbq = (TextView) convertView.findViewById(R.id.txt_mbq);
                holder.toggle_available = (ToggleButton) convertView.findViewById(R.id.toggle_available);

                holder.facing = (EditText) convertView.findViewById(R.id.ed_facing);
                holder.stock = (EditText) convertView.findViewById(R.id.ed_stock);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getSku());
            holder.txt_mbq.setText(childData.getMbq());

            if (childData.getCompany_id().equals("1")) {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.txt_mbq.setVisibility(View.VISIBLE);
                holder.toggle_available.setVisibility(View.VISIBLE);
                holder.facing.setVisibility(View.VISIBLE);

                if (childData.getToggleValue().equals("1")) {
                    holder.stock.setVisibility(View.GONE);
                } else {
                    holder.stock.setVisibility(View.VISIBLE);
                }
            } else {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.black));
                holder.txt_mbq.setVisibility(View.GONE);
                holder.toggle_available.setVisibility(View.GONE);
                holder.facing.setVisibility(View.VISIBLE);
                holder.stock.setVisibility(View.GONE);
            }

            final ViewHolder finalHolder = holder;
            holder.toggle_available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childData.setToggleValue("1");
                        finalHolder.stock.setVisibility(View.GONE);
                        childData.setStock("");
                    } else {
                        childData.setToggleValue("0");
                        finalHolder.stock.setVisibility(View.VISIBLE);
                    }

                    expandableListView.invalidateViews();
                }
            });

            if (childData.getToggleValue().equals("1")) {
                holder.toggle_available.setChecked(true);
                finalHolder.stock.setVisibility(View.GONE);
            } else {
                holder.toggle_available.setChecked(false);
                finalHolder.stock.setVisibility(View.VISIBLE);
            }

            holder.facing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    final String edFaceup = caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    if (!edFaceup.equals("")) {
                        String faceup = edFaceup.replaceFirst("^0+(?!$)", "");
                        childData.setFacing(faceup);
                    } else {
                        childData.setFacing("");
                    }
                }
            });

            holder.facing.setText(childData.getFacing());


            holder.stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    String edStock = caption.getText().toString();

                    if (!edStock.equals("")) {
                        String stock = edStock.replaceFirst("^0+(?!$)", "");

                        if (Integer.parseInt(stock) >= 0 && Integer.parseInt(stock) < Integer.parseInt(childData.getMbq())) {
                            childData.setStock(stock);
                        } else {
                            if (isDialogOpen) {
                                isDialogOpen = !isDialogOpen;
                                AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                                builder.setMessage(getString(R.string.check_stock))
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                isDialogOpen = !isDialogOpen;
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    } else {
                        childData.setStock("");
                    }
                }
            });

            holder.stock.setText(childData.getStock());


            //empty check color change
            if (!checkflag) {
                boolean tempflag = false;

                if (childData.getCompany_id().equals("1")) {
                    if (childData.getToggleValue().equals("0")) {
                        if (holder.stock.getText().toString().equals("")) {
                            holder.stock.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.stock.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.stock.setHint(getString(R.string.empty));
                            tempflag = true;
                        }

                        if (holder.facing.getText().toString().equals("")) {
                            holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.facing.setHint(getString(R.string.empty));
                            tempflag = true;
                        }
                    } else {
                        if (holder.facing.getText().toString().equals("")) {
                            holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.facing.setHint(getString(R.string.empty));
                            tempflag = true;
                        }
                    }

                    if (tempflag) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                } else {
                    if (holder.facing.getText().toString().equals("")) {
                        holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        holder.facing.setHint(getString(R.string.empty));
                        tempflag = true;
                    }

                    if (tempflag) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
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
        CardView cardView;
        TextView txt_skuName, txt_mbq;
        ToggleButton toggle_available;
        LinearLayout lin_category;
        EditText facing, stock;
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

}
