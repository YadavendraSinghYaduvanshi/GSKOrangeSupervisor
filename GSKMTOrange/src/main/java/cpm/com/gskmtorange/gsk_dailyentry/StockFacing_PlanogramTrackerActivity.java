package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
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
    ExpandableListAdapter adapter;

    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> headerDataList = new ArrayList<>();
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> childDataList;
    List<StockFacing_PlanogramTrackerDataGetterSetter> hashMapListHeaderData;
    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, List<StockFacing_PlanogramTrackerDataGetterSetter>> hashMapListChildData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_facing__planogram_tracker);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            db = new GSKOrangeDB(this);
            db.open();

            btn_addShelf = (Button) findViewById(R.id.btn_addShelf);
            expandableListView = (ExpandableListView) findViewById(R.id.exp_stockFacing_PlanogramTrackerListView);

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
            prepareHeaderList(headerDataList);

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


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void prepareHeaderList(ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> headerDataList) {
        try {
            hashMapListHeaderData = new ArrayList<>();
            hashMapListChildData = new HashMap<>();

            //Header Data
            if (headerDataList.size() > 0) {
                hashMapListHeaderData.addAll(headerDataList);
            //    adapter.notifyDataSetChanged();
            }

            adapter = new ExpandableListAdapter(this, hashMapListHeaderData, hashMapListChildData);
            expandableListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockFacing_PlanogramTrackerDataGetterSetter> _listDataHeader;
        private HashMap<StockFacing_PlanogramTrackerDataGetterSetter, List<StockFacing_PlanogramTrackerDataGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockFacing_PlanogramTrackerDataGetterSetter> listDataHeader,
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final StockFacing_PlanogramTrackerDataGetterSetter headerTitle =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getGroup(groupPosition);

            if (headerTitle != null) {
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.item_stock_facing_planogram_header, null, false);

                    TextView txt_shelfHeader = (TextView) convertView.findViewById(R.id.txt_shelfHeader);
                    Button btn_addSku = (Button) convertView.findViewById(R.id.btn_addSku);

                    txt_shelfHeader.setText(headerTitle.getSp_addShelf() + " (Position : " + headerTitle.getSp_shelfPosition() + ")");

                    btn_addSku.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            
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
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final StockFacing_PlanogramTrackerDataGetterSetter childData =
                    (StockFacing_PlanogramTrackerDataGetterSetter) getChild(groupPosition, childPosition);
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
}
