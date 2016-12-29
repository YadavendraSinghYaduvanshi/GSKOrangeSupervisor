package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;

public class MSL_AvailabilityActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    TextView txt_mslAvailabilityName;

    ArrayList<MSL_AvailabilityGetterSetter> headerDataList;
    ArrayList<MSL_AvailabilityGetterSetter> childDataList;
    List<MSL_AvailabilityGetterSetter> hashMapListHeaderData;
    HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> hashMapListChildData;

    ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msl__availability);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        txt_mslAvailabilityName = (TextView) findViewById(R.id.txt_mslAvailabilityName);

        prepareList();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }

                expandableListView.invalidateViews();
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

    }

    private void prepareList() {
        headerDataList = new ArrayList<>();

        MSL_AvailabilityGetterSetter msl = new MSL_AvailabilityGetterSetter();
        msl.setBrandName("Parodontax header 1");
        msl.setMbq("6");
        msl.setAvailable("No");
        headerDataList.add(msl);

        msl = new MSL_AvailabilityGetterSetter();
        msl.setBrandName("Parodontax header 2");
        msl.setMbq("7");
        msl.setAvailable("Yes");
        headerDataList.add(msl);

        msl = new MSL_AvailabilityGetterSetter();
        msl.setBrandName("Parodontax header 3");
        msl.setMbq("8");
        msl.setAvailable("Yes");
        headerDataList.add(msl);


        hashMapListHeaderData = new ArrayList<>();
        hashMapListChildData = new HashMap<>();

        if (headerDataList.size() > 0) {

            for (int i = 0; i < headerDataList.size(); i++) {
                hashMapListHeaderData.add(headerDataList.get(i));

                childDataList = new ArrayList<>();

                MSL_AvailabilityGetterSetter msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 1");
                msl.setMbq("1");
                msl.setAvailable("No");
                childDataList.add(msl1);

                msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 2");
                msl.setMbq("2");
                msl.setAvailable("No");
                childDataList.add(msl1);

                hashMapListChildData.put(hashMapListHeaderData.get(i), childDataList);
            }

        }

        adapter = new ExpandableListAdapter(this, hashMapListHeaderData, hashMapListChildData);
        expandableListView.setAdapter(adapter);
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
            txt_categoryHeader.setText(headerTitle.getBrandName());

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
            MSL_AvailabilityGetterSetter childData = (MSL_AvailabilityGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_child, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.lin_category = (LinearLayout) convertView.findViewById(R.id.lin_category);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.ed_mbq = (EditText) convertView.findViewById(R.id.ed_mbq);
                holder.toggle_available = (ToggleButton) convertView.findViewById(R.id.toggle_available);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getBrandName());
            holder.ed_mbq.setText(childData.getMbq());

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
        EditText ed_mbq;
        CardView cardView;
        TextView txt_skuName;
        ToggleButton toggle_available;
        LinearLayout lin_category;
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
