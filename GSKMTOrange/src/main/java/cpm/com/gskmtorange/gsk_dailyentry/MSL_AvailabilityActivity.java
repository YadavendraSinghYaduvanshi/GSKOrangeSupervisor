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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;

public class MSL_AvailabilityActivity extends AppCompatActivity {
    ExpandableListView expandableListView;

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

        prepareList();
        adapter = new ExpandableListAdapter(this, hashMapListHeaderData, hashMapListChildData);
        expandableListView.setAdapter(adapter);


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
        msl.setMbq("6");
        msl.setAvailable("No");
        headerDataList.add(msl);


        hashMapListHeaderData = new ArrayList<>();
        hashMapListChildData = new HashMap<>();

        if (headerDataList.size() > 0) {

            for (int i = 0; i < headerDataList.size(); i++) {
                hashMapListHeaderData.add(headerDataList.get(i));

                childDataList = new ArrayList<>();

                MSL_AvailabilityGetterSetter msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 1");
                msl.setMbq("6");
                msl.setAvailable("No");
                childDataList.add(msl1);

                msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 2");
                msl.setMbq("6");
                msl.setAvailable("No");
                childDataList.add(msl1);

                /*msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 3");
                msl.setMbq("6");
                msl.setAvailable("No");
                childDataList.add(msl1);

                msl1 = new MSL_AvailabilityGetterSetter();
                msl.setBrandName("Parodontax 4");
                msl.setMbq("6");
                msl.setAvailable("No");
                childDataList.add(msl1);*/

                hashMapListChildData.put(hashMapListHeaderData.get(i), childDataList);
            }

        }

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
            final MSL_AvailabilityGetterSetter headerTitle = (MSL_AvailabilityGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_opening, null, false);
            }

            //final int position = convertView.getId();
            TextView txt_header = (TextView) convertView.findViewById(R.id.txt_Header);
            ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);
            RelativeLayout rel_header = (RelativeLayout) convertView.findViewById(R.id.rel_header);

            txt_header.setTypeface(null, Typeface.BOLD);
            txt_header.setText(headerTitle.getBrandName());

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
                convertView = infalInflater.inflate(R.layout.list_item_openingstk, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.ed_stock = (EditText) convertView.findViewById(R.id.ed_stock);
                holder.ed_faceup = (EditText) convertView.findViewById(R.id.ed_faceup);
                holder.lin_item = (LinearLayout) convertView.findViewById(R.id.lin_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getBrandName());

            /*if (childData.getFocus().equals("1")) {
                holder.lin_item.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (childData.getInno().equals("1")) {
                holder.lin_item.setBackgroundColor(getResources().getColor(R.color.yellow));
            }

            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setSku_cd(childData.getSku_cd());
            holder.txt_skuName.setText(childData.getSku());

            //MFD Visibility
            if (childData.getMfd().equals("1")) {
                holder.lin_oldest_MFD.setVisibility(View.VISIBLE);
                holder.lin_latest_MFD.setVisibility(View.VISIBLE);
            } else {
                holder.lin_oldest_MFD.setVisibility(View.GONE);
                holder.lin_latest_MFD.setVisibility(View.GONE);
            }

            if (childData.getStock().equals("0")) {
                holder.ed_faceup.setEnabled(false);
                holder.btn_old_Date.setEnabled(false);
                holder.btn_new_Date.setEnabled(false);
            } else {
                holder.ed_faceup.setEnabled(true);
                holder.btn_old_Date.setEnabled(true);
                holder.btn_new_Date.setEnabled(true);
            }

            final ViewHolder finalHolder = holder;
            holder.ed_stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    String edStock = caption.getText().toString();

                    if (!edStock.equals("")) {
                        String stock = edStock.replaceFirst("^0+(?!$)", "");
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock(stock);

                        if (edStock.equals("0")) {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setFaceup("0");
                            childData.setOldDate("");
                            childData.setNewDate("");

                            finalHolder.ed_faceup.setEnabled(false);
                            finalHolder.btn_old_Date.setEnabled(false);
                            finalHolder.btn_new_Date.setEnabled(false);
                        } else {
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setFaceup(childData.getFaceup());
                            finalHolder.ed_faceup.setEnabled(true);
                            finalHolder.btn_old_Date.setEnabled(true);
                            finalHolder.btn_new_Date.setEnabled(true);
                        }
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock("");
                        finalHolder.ed_faceup.setEnabled(true);
                        finalHolder.btn_old_Date.setEnabled(true);
                        finalHolder.btn_new_Date.setEnabled(true);
                    }
                }
            });

            holder.ed_stock.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getStock());

            holder.ed_faceup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    final String edFaceup = caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    if (!childData.getStock().equals("")) {
                        if (!edFaceup.equals("")) {
                            if (Integer.parseInt(edFaceup) <= Integer.parseInt(childData.getStock())) {
                                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setFaceup(edFaceup);
                            } else {
                                if (isDialogOpen) {
                                    isDialogOpen = !isDialogOpen;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                    builder.setMessage("Faceup can not be greater than stock value")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                            _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setFaceup("");
                        }
                    } else {
                        if (isDialogOpen) {
                            isDialogOpen = !isDialogOpen;
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                            builder.setMessage("First fill the stock value")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            isDialogOpen = !isDialogOpen;
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }
            });

            holder.ed_faceup.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getFaceup());


            holder.btn_new_Date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(OrderEntryActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    try {
                                        if (childData.getOldDate().equals("")) {
                                            String sDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                                            Date date1 = sdf.parse(sDate);
                                            Date visitDate = sdf.parse(visit_date);

                                            if (date1.compareTo(visitDate) != 1) {
                                                String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setNewDate(date);

                                                expListView.invalidateViews();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                                builder.setMessage("Latest Date can not be greater than the current date ")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        } else {
                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                            Date oldDate = sdf.parse(childData.getOldDate());

                                            String sDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                            Date newDate = sdf.parse(sDate);
                                            Date visitDate = sdf.parse(visit_date);

                                            if (newDate.compareTo(oldDate) == -1) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                                builder.setMessage("Latest Date can not be less than the oldest date ")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            } else {
                                                if (newDate.compareTo(visitDate) != 1) {
                                                    String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setNewDate(date);

                                                    expListView.invalidateViews();
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                                    builder.setMessage("Latest Date can not be greater than the current date ")
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
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            , mYear, mMonth, mDay);
                    dpd.show();
                }
            });

            if (childData.getNewDate().equals("")) {
                holder.txt_showNewDate.setText("");
            } else {
                holder.txt_showNewDate.setText(childData.getNewDate());
            }

            holder.btn_old_Date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(OrderEntryActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    try {
                                        if (!childData.getNewDate().equals("")) {

                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                            String sDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                                            Date date1 = sdf.parse(sDate);//pick date
                                            Date date2 = sdf.parse(childData.getNewDate()); //latest date selected
                                            Date visitDate = sdf.parse(visit_date);//current date

                                            if (date1.compareTo(visitDate) != 1) { //Compare Oldest Date and Current Date

                                                if (date1.compareTo(date2) != 1) {  //Compare Oldest Date and Latest Date
                                                    String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setOldDate(date);

                                                    expListView.invalidateViews();
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                                    builder.setMessage("Oldest Date can not be greater than the latest date ")
                                                            .setCancelable(false)
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                }
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                                builder.setMessage("Oldest Date can not be greater than the current date ")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderEntryActivity.this);
                                            builder.setMessage("First Select the Latest Date ")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });

            if (childData.getOldDate().equals("")) {
                holder.txt_showOldDate.setText("");
            } else {
                holder.txt_showOldDate.setText(childData.getOldDate());
            }


            if (!checkflag) {
                boolean tempflag = false;

                if (holder.ed_stock.getText().toString().equals("")) {
                    holder.ed_stock.setBackgroundColor(getResources().getColor(R.color.grey_background));
                    holder.ed_stock.setHintTextColor(getResources().getColor(R.color.red));
                    holder.ed_stock.setHint("Empty");
                    tempflag = true;
                }

                if (holder.ed_faceup.getText().toString().equals("")) {
                    holder.ed_faceup.setBackgroundColor(getResources().getColor(R.color.grey_background));
                    holder.ed_faceup.setHintTextColor(getResources().getColor(R.color.red));
                    holder.ed_faceup.setHint("Empty");
                    tempflag = true;
                }

                if (!holder.ed_stock.getText().toString().equals("0")) {
                    if (holder.txt_showOldDate.getText().toString().equals("")) {
                        holder.txt_showOldDate.setHintTextColor(getResources().getColor(R.color.red));
                        holder.txt_showOldDate.setHint("Select Oldest MFD ");
                    }
                } else if (holder.ed_stock.getText().toString().equals("0")) {
                    holder.txt_showOldDate.setHint("");
                }

                if (!holder.ed_stock.getText().toString().equals("0")) {
                    if (holder.txt_showNewDate.getText().toString().equals("")) {
                        holder.txt_showNewDate.setHintTextColor(getResources().getColor(R.color.red));
                        holder.txt_showNewDate.setHint("Select Latest MFD ");
                    }
                } else if (holder.ed_stock.getText().toString().equals("0")) {
                    holder.txt_showNewDate.setHint("");
                }

                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
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
        EditText ed_stock, ed_faceup;
        CardView cardView;
        TextView txt_skuName, txt_showOldDate, txt_showNewDate;
        LinearLayout lin_item, lin_latest_MFD, lin_oldest_MFD;
        Button btn_old_Date, btn_new_Date;
        //public MutableWatcher mWatcher;
        //public int mYear, mMonth, mDay, mHour, mMinute;
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
