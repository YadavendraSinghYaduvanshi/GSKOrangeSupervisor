package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;

public class Stock_FacingActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    TextView txt_stockFacingName;

    ArrayList<Stock_FacingGetterSetter> headerDataList;
    ArrayList<Stock_FacingGetterSetter> childDataList;
    List<Stock_FacingGetterSetter> hashMapListHeaderData;
    HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> hashMapListChildData;

    ExpandableListAdapter adapter;

    String title;

    String path = "", str = "", _pathforcheck = "", img1 = "";
    static int child_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_facing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        txt_stockFacingName = (TextView) findViewById(R.id.txt_stockFacingName);

        title = getIntent().getStringExtra("categoryName");
        txt_stockFacingName.setText(title);

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

        Stock_FacingGetterSetter msl = new Stock_FacingGetterSetter();
        msl.setBrandName("Parodontax header 1");
        msl.setMbq("1");
        msl.setAvailable("No");
        headerDataList.add(msl);

        msl = new Stock_FacingGetterSetter();
        msl.setBrandName("Parodontax header 2");
        msl.setMbq("2");
        msl.setAvailable("Yes");
        headerDataList.add(msl);

        msl = new Stock_FacingGetterSetter();
        msl.setBrandName("Parodontax header 3");
        msl.setMbq("3");
        msl.setAvailable("Yes");
        headerDataList.add(msl);

        msl = new Stock_FacingGetterSetter();
        msl.setBrandName("Parodontax header 4");
        msl.setMbq("4");
        msl.setAvailable("No");
        headerDataList.add(msl);


        hashMapListHeaderData = new ArrayList<>();
        hashMapListChildData = new HashMap<>();

        if (headerDataList.size() > 0) {

            for (int i = 0; i < headerDataList.size(); i++) {
                hashMapListHeaderData.add(headerDataList.get(i));

                childDataList = new ArrayList<>();

                Stock_FacingGetterSetter msl1 = new Stock_FacingGetterSetter();
                msl.setBrandName("Parodontax 1");
                msl.setMbq("1");
                msl.setAvailable("No");
                childDataList.add(msl1);

                msl1 = new Stock_FacingGetterSetter();
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
        private List<Stock_FacingGetterSetter> _listDataHeader;
        private HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<Stock_FacingGetterSetter> listDataHeader,
                                     HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> listChildData) {
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
            final Stock_FacingGetterSetter headerTitle = (Stock_FacingGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_stock_facing_header, null, false);
            }

            TextView txt_stockFaceupHeader = (TextView) convertView.findViewById(R.id.txt_stockFaceupHeader);
            LinearLayout lin_stockFaceupHeader = (LinearLayout) convertView.findViewById(R.id.lin_stockFaceupHeader);
            ImageView img_reference = (ImageView) convertView.findViewById(R.id.img_reference);
            ImageView img_camera1 = (ImageView) convertView.findViewById(R.id.img_camera1);
            ImageView img_camera2 = (ImageView) convertView.findViewById(R.id.img_camera2);
            ImageView img_edit = (ImageView) convertView.findViewById(R.id.img_edit);

            txt_stockFaceupHeader.setTypeface(null, Typeface.BOLD);
            txt_stockFaceupHeader.setText(headerTitle.getBrandName());

            img_camera1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = new Date().toLocaleString().toString();
                    String tempDate = new Date().toLocaleString().toString().replace(' ', '_').replace(',', '_').replace(':', '-');

                    _pathforcheck = "Stock Camera1_" + tempDate + ".jpg";
                    child_position = groupPosition;
                    path = str + _pathforcheck;

                    startCameraActivity(groupPosition);
                }
            });

            if (!img1.equalsIgnoreCase("")) {
                if (groupPosition == child_position) {
                    //headerTitle.setImg_cam(img1);
                    img1 = "";
                }
            }

            /*if (headerTitle.getImg_cam().equals("")) {
                img_camera1.setBackgroundResource(R.drawable.ic_menu_camera);
            } else {
                //img_camera1.setBackgroundResource(R.drawable.camtick);
            }*/

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
            Stock_FacingGetterSetter childData = (Stock_FacingGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_stock_facing_child, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.lin_category = (LinearLayout) convertView.findViewById(R.id.lin_category);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.ed_stock = (EditText) convertView.findViewById(R.id.ed_stock);
                holder.ed_facing = (EditText) convertView.findViewById(R.id.ed_facing);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getBrandName());
            holder.ed_stock.setText(childData.getMbq());

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
        EditText ed_stock, ed_facing;
        CardView cardView;
        TextView txt_skuName;
        LinearLayout lin_category;
    }

    private void startCameraActivity(int position) {
        try {
            Log.e("Stock & Facing ", "startCameraActivity()");
            File file = new File(path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(intent, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Stock & Facing", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.e("Stock & Facing", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        img1 = _pathforcheck;
                        //adapter.notifyDataSetChanged();
                        _pathforcheck = "";
                    }
                }
                break;
        }
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

        return cdate;
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
