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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;

public class MSL_AvailabilityActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    TextView txt_mslAvailabilityName;

    ArrayList<MSL_AvailabilityGetterSetter> headerDataList;
    ArrayList<MSL_AvailabilityGetterSetter> childDataList;
    List<MSL_AvailabilityGetterSetter> hashMapListHeaderData;
    HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> hashMapListChildData;

    ExpandableListAdapter adapter;

    GSKOrangeDB db;

    String categoryName, categoryId, storeId;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_msl__availability);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            txt_mslAvailabilityName = (TextView) findViewById(R.id.txt_mslAvailabilityName);

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

                    //if (validateData(listDataHeader, listDataChild)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MSL_AvailabilityActivity.this);
                    builder.setMessage(getResources().getString(R.string.check_save_message))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.open();

                                    if (db.checkMsl_AvailabilityData(store_id, categoryId)) {
                                        db.updateMSL_Availability(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                        Snackbar.make(view, getResources().getString(R.string.update_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    } else {
                                        db.InsertMSL_Availability(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                        Snackbar.make(view, getResources().getString(R.string.save_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }

                                    //Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_LONG).show();
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

                    /*} else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_AvailabilityActivity.this);
                        builder.setMessage("Fill the value or fill 0 ")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }*/

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
        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }

    private void prepareList() {
        try {
            hashMapListHeaderData = new ArrayList<>();
            hashMapListChildData = new HashMap<>();

            //Header
            headerDataList = db.getMSL_AvailabilityHeaderData(categoryId);

            if (headerDataList.size() > 0) {
                for (int i = 0; i < headerDataList.size(); i++) {
                    hashMapListHeaderData.add(headerDataList.get(i));

                    //childDataList = new ArrayList<>();
                    childDataList = db.getMSL_AvailabilitySKU_AfterSaveData(categoryId, headerDataList.get(i).getBrand_id());
                    if (!(childDataList.size() > 0)) {
                        childDataList = db.getMSL_AvailabilitySKUData(categoryId, headerDataList.get(i).getBrand_id());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MSL_AvailabilityActivity.this);
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MSL_AvailabilityActivity.this);
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

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<MSL_AvailabilityGetterSetter> _listDataHeader;
        private HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<MSL_AvailabilityGetterSetter> listDataHeader,
                                     HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> listChildData) {
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
            MSL_AvailabilityGetterSetter headerTitle = (MSL_AvailabilityGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_header, null, false);
            }

            TextView txt_categoryHeader = (TextView) convertView.findViewById(R.id.txt_categoryHeader);
            RelativeLayout rel_header = (RelativeLayout) convertView.findViewById(R.id.rel_categoryHeader);
            ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);

            txt_categoryHeader.setTypeface(null, Typeface.BOLD);
            txt_categoryHeader.setTextColor(getResources().getColor(R.color.colorPrimary));
            txt_categoryHeader.setText(headerTitle.getSub_category() + "-" + headerTitle.getBrand());

            /*img_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = new Date().toLocaleString().toString();
                    String TempDate = new Date().toLocaleString().toString().replace(' ', '_').replace(',', '_').replace(':', '-');

                    _pathforcheck = "Stock" + headerTitle.getBrand_cd() + "_" + store_cd + "_" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                    child_position = groupPosition;
                    path = str + _pathforcheck;

                    startCameraActivity(groupPosition);
                }
            });

            if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == child_position) {
                    headerTitle.setImg_cam(img1);
                    img1 = "";
                }
            }

            if (headerTitle.getImg_cam().equals("")) {
                img_camera.setBackgroundResource(R.drawable.cam);
            } else {
                img_camera.setBackgroundResource(R.drawable.camtick);
            }

            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    txt_header.setTextColor(getResources().getColor(R.color.red));
                } else {
                    txt_header.setTextColor(getResources().getColor(R.color.grey_dark));
                }
            }*/

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
            final MSL_AvailabilityGetterSetter childData = (MSL_AvailabilityGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_child, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.lin_category = (LinearLayout) convertView.findViewById(R.id.lin_category);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.txt_mbq = (TextView) convertView.findViewById(R.id.txt_mbq);
                holder.toggle_available = (ToggleButton) convertView.findViewById(R.id.toggle_available);

                /*holder.toggle_available.setTextOff("No");
                holder.toggle_available.setTextOn("Yes");*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setTextColor(getResources().getColor(R.color.colorPrimary));
            holder.txt_skuName.setText(childData.getSku());
            holder.txt_mbq.setText(childData.getMbq());

            /*if (childData.getToggleValue().equals("1")) {
                holder.toggle_available.setText("Yes");
            } else {
                holder.toggle_available.setText("No");
            }*/

            holder.toggle_available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childData.setToggleValue("1");
                    } else {
                        childData.setToggleValue("0");
                    }

                    expandableListView.invalidateViews();
                }
            });

            if (childData.getToggleValue().equals("1")) {
                holder.toggle_available.setChecked(true);
            } else {
                holder.toggle_available.setChecked(false);
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
