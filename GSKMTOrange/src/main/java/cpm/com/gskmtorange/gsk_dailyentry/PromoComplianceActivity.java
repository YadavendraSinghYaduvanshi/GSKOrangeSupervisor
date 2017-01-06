package cpm.com.gskmtorange.gsk_dailyentry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Promo_Compliance_DataGetterSetter;

import static android.R.attr.data;
import static cpm.com.gskmtorange.R.id.toggle_inStock;

public class PromoComplianceActivity extends AppCompatActivity {
    LinearLayout lin_promo_sku, lin_addtional_promo;
    View view_promo_sku, view_additional_promo;
    Spinner sp_promo;
    ToggleButton toggle_add_InStock, toggle_add_promoAnnouncer, toggle_add_runningPos;
    Button btn_add;

    ArrayList<Promo_Compliance_DataGetterSetter> promoSkuListData;
    ArrayList<Promo_Compliance_DataGetterSetter> promoSpinnerListData;
    ArrayList<Promo_Compliance_DataGetterSetter> additionalPromoListData;

    GSKOrangeDB db;
    String categoryName, categoryId;

    private SharedPreferences preferences;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_compliance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lin_promo_sku = (LinearLayout) findViewById(R.id.lin_promo_sku);
        lin_addtional_promo = (LinearLayout) findViewById(R.id.lin_addtional_promo);
        view_promo_sku = findViewById(R.id.view_promo_sku);
        view_additional_promo = findViewById(R.id.view_additional_promo);

        sp_promo = (Spinner) findViewById(R.id.sp_promo);
        toggle_add_InStock = (ToggleButton) findViewById(R.id.toggle_add_InStock);
        toggle_add_promoAnnouncer = (ToggleButton) findViewById(R.id.toggle_add_promoAnnouncer);
        toggle_add_runningPos = (ToggleButton) findViewById(R.id.toggle_add_runningPos);
        btn_add = (Button) findViewById(R.id.btn_add);

        db = new GSKOrangeDB(this);
        db.open();

        //preference data
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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

        prepareList();
        promoSkuListView();

        additionalPromoListData = new ArrayList<>();
        //AdditionalPromoListView();

        final Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();
        cd.setStore_id(store_id);
        cd.setPromo_id("");
        cd.setPromo("");
        cd.setIn_stock("1");
        cd.setPromo_announcer("1");
        cd.setRunning_pos("1");
        cd.setSp_promo("0");

        toggle_add_InStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cd.setIn_stock("1");
                } else {
                    cd.setIn_stock("0");
                }
            }
        });

        if (cd.getIn_stock().equals("1")) {
            toggle_add_InStock.setChecked(true);
        } else {
            toggle_add_InStock.setChecked(false);
        }

        toggle_add_promoAnnouncer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cd.setPromo_announcer("1");
                } else {
                    cd.setPromo_announcer("0");
                }
            }
        });

        if (cd.getPromo_announcer().equals("1")) {
            toggle_add_promoAnnouncer.setChecked(true);
        } else {
            toggle_add_promoAnnouncer.setChecked(false);
        }

        toggle_add_runningPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cd.setRunning_pos("1");
                } else {
                    cd.setRunning_pos("0");
                }
            }
        });

        if (cd.getRunning_pos().equals("1")) {
            toggle_add_runningPos.setChecked(true);
        } else {
            toggle_add_runningPos.setChecked(false);
        }

        sp_promo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                childData.setSp_condition(position);
//                childData.setConditionName(item);

                for (int i = 0; i < promoSpinnerListData.size(); i++) {
                    if (position == i) {
                        cd.setSp_promo(promoSpinnerListData.get(i).getPromo_id());
                        cd.setPromo(promoSpinnerListData.get(i).getPromo());
                        cd.setPromo_id(promoSpinnerListData.get(i).getPromo_id());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0; i < promoSpinnerListData.size(); i++) {
            if (cd.getSp_promo() == promoSpinnerListData.get(i).getPromo_id()) {
                sp_promo.setSelection(i);
            }
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.InsertAdditionalPromoData(cd);
                AdditionalPromoListView();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*ViewTreeObserver.OnScrollChangedListener onScrollChangedListener
                = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

            }
        };*/
    }

    private void prepareList() {
        //Promo SKU List
        promoSkuListData = db.getPromoComplianceSkuData(store_id);

        //Promo Spinner List
        promoSpinnerListData = db.getPromoSpinnerData(store_id);

        ArrayAdapter<String> sp_promo_adapter = new ArrayAdapter<>(PromoComplianceActivity.this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < promoSpinnerListData.size(); i++) {
            sp_promo_adapter.add(promoSpinnerListData.get(i).getPromo());
        }
        sp_promo.setAdapter(sp_promo_adapter);
    }

    private void promoSkuListView() {
        View view;

        for (int i = 0; i < promoSkuListData.size(); i++) {
            view = getLayoutInflater().inflate(R.layout.item_promo_sku_list, null, false);

            final Promo_Compliance_DataGetterSetter data = promoSkuListData.get(i);

            TextView txt_promoSkuName = (TextView) view.findViewById(R.id.txt_promoSkuName);
            ToggleButton toggle_inStock = (ToggleButton) view.findViewById(R.id.toggle_inStock);
            ToggleButton toggle_promoAnnouncer = (ToggleButton) view.findViewById(R.id.toggle_promoAnnouncer);
            ToggleButton toggle_runningPos = (ToggleButton) view.findViewById(R.id.toggle_runningPos);

            txt_promoSkuName.setText(data.getPromo());

            //In Stock
            toggle_inStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        data.setIn_stock("1");
                    } else {
                        data.setIn_stock("0");
                    }
                }
            });

            if (data.getIn_stock().equals("1")) {
                toggle_inStock.setChecked(true);
            } else {
                toggle_inStock.setChecked(false);
            }

            //Promo Announcer
            toggle_promoAnnouncer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        data.setPromo_announcer("1");
                    } else {
                        data.setPromo_announcer("0");
                    }
                }
            });

            if (data.getPromo_announcer().equals("1")) {
                toggle_promoAnnouncer.setChecked(true);
            } else {
                toggle_promoAnnouncer.setChecked(false);
            }

            //Running on POS
            toggle_runningPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        data.setRunning_pos("1");
                    } else {
                        data.setRunning_pos("0");
                    }
                }
            });

            if (data.getRunning_pos().equals("1")) {
                toggle_runningPos.setChecked(true);
            } else {
                toggle_runningPos.setChecked(false);
            }

            lin_promo_sku.addView(view);
        }
    }

    private void AdditionalPromoListView() {
        additionalPromoListData.clear();

        //Additional Promo List
        additionalPromoListData = db.getAdditionalPromoData();

        View view;

        for (int i = 0; i < additionalPromoListData.size(); i++) {
            view = getLayoutInflater().inflate(R.layout.item_additional_promo_list, null, false);

            final Promo_Compliance_DataGetterSetter data = additionalPromoListData.get(i);

            TextView txt_promoSkuName = (TextView) view.findViewById(R.id.txt_promoSkuName);
            TextView txt_inStock = (TextView) view.findViewById(R.id.txt_inStock);
            TextView txt_promoAnnouncer = (TextView) view.findViewById(R.id.txt_promoAnnouncer);
            TextView txt_runningPos = (TextView) view.findViewById(R.id.txt_runningPos);

            txt_promoSkuName.setText(data.getPromo());

            //In Stock
            if (data.getIn_stock().equals("1")) {
                txt_inStock.setText("Yes");
            } else {
                txt_inStock.setText("No");
            }

            //Promo Announcer
            if (data.getPromo_announcer().equals("1")) {
                txt_promoAnnouncer.setText("Yes");
            } else {
                txt_promoAnnouncer.setText("No");
            }

            //Running on POS
            if (data.getRunning_pos().equals("1")) {
                txt_runningPos.setText("Yes");
            } else {
                txt_runningPos.setText("No");
            }

            lin_addtional_promo.addView(view);
        }
    }
}
