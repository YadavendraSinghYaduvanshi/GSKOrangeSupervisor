package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.Promo_Compliance_DataGetterSetter;

public class PromoComplianceActivity extends AppCompatActivity {
    LinearLayout lin_promo_sku, lin_addtional_promo;
    View view_promo_sku, view_additional_promo;
    Spinner sp_promo;
    ToggleButton toggle_add_InStock, toggle_add_promoAnnouncer, toggle_add_runningPos;
    Button btn_add;

    ArrayList<Promo_Compliance_DataGetterSetter> promoSkuListData, promoSkuListAfterData;
    ArrayList<Promo_Compliance_DataGetterSetter> promoSpinnerListData;
    ArrayList<Promo_Compliance_DataGetterSetter> additionalPromoListData;

    GSKOrangeDB db;
    String categoryName, categoryId;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id;
    private SharedPreferences preferences;

    private static boolean updateResources(Context context, String language) {

        String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("UAE")) {
            lang = "AR";
        } else {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_promo_compliance);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(getResources().getString(R.string.title_activity_promo_compliance));
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

            prepareList();
            promoSkuListView();

            additionalPromoListData = new ArrayList<>();
            AdditionalPromoListView();

            final Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();
            cd.setStore_id(store_id);
            cd.setPromo_id("");
            cd.setPromo("");
            cd.setSku_id("");
            cd.setSku("");
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
                            cd.setSku_id(promoSpinnerListData.get(i).getSku_id());
                            cd.setSku(promoSpinnerListData.get(i).getSku());
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
                public void onClick(final View v) {
                    if (!cd.getSp_promo().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PromoComplianceActivity.this);
                        builder.setMessage("Are you sure you want to add")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.InsertAdditionalPromoData(cd, categoryId);
                                        AdditionalPromoListView();

                                        sp_promo.setSelection(0);
                                        toggle_add_InStock.setChecked(true);
                                        toggle_add_promoAnnouncer.setChecked(true);
                                        toggle_add_runningPos.setChecked(true);

                                        Snackbar.make(v, "promo is add", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        //Toast.makeText(getApplicationContext(), "promo is add", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(v, "Select the promo value", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PromoComplianceActivity.this);
                    builder.setMessage("Are you sure you want to save")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (db.checkPromoComplianceData(store_id, categoryId)) {
                                        db.updatePromoComplianceSKU(promoSkuListData, categoryId, store_id);
                                        Snackbar.make(view, "Data has been updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    } else {
                                        db.InsertPromoSkuData(promoSkuListData, categoryId);
                                        Snackbar.make(view, "Data has been saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }

                                    finish();
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });


        /*ViewTreeObserver.OnScrollChangedListener onScrollChangedListener
                = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

            }
        };*/
        } catch (Exception e) {
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
            //Promo SKU List
            promoSkuListData = db.getPromoComplianceSkuAfterData(store_id, categoryId);
            if (!(promoSkuListData.size() > 0)) {
                promoSkuListData = db.getPromoComplianceSkuData(store_id);
            }

            //Promo Spinner List
            promoSpinnerListData = db.getPromoSpinnerData(store_id);

            ArrayAdapter<String> sp_promo_adapter = new ArrayAdapter<>(PromoComplianceActivity.this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < promoSpinnerListData.size(); i++) {
                sp_promo_adapter.add(promoSpinnerListData.get(i).getPromo());
            }
            sp_promo.setAdapter(sp_promo_adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void promoSkuListView() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AdditionalPromoListView() {
        try {
            additionalPromoListData.clear();
            lin_addtional_promo.removeAllViews();

            //Additional Promo List
            additionalPromoListData = db.getAdditionalPromoData(store_id, categoryId);

            View view;

            for (int i = 0; i < additionalPromoListData.size(); i++) {
                view = getLayoutInflater().inflate(R.layout.item_additional_promo_list, null, false);

                final Promo_Compliance_DataGetterSetter data = additionalPromoListData.get(i);

                TextView txt_promoName = (TextView) view.findViewById(R.id.txt_promoName);
                TextView txt_inStock = (TextView) view.findViewById(R.id.txt_inStock);
                TextView txt_promoAnnouncer = (TextView) view.findViewById(R.id.txt_promoAnnouncer);
                TextView txt_runningPos = (TextView) view.findViewById(R.id.txt_runningPos);

                txt_promoName.setText(data.getPromo());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PromoComplianceActivity.this);
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PromoComplianceActivity.this);
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
}
