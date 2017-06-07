package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.StreamHandler;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.dailyentry.T2PComplianceActivity;
import cpm.com.gskmtorange.xmlGetterSetter.Promo_Compliance_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SelectGetterSetter;

public class PromoComplianceActivity extends AppCompatActivity {
    LinearLayout lin_promo_sku, lin_addtional_promo;
    View view_promo_sku, view_additional_promo;
    Spinner sp_promo;
    Spinner toggle_add_InStock, toggle_add_promoAnnouncer;
    ToggleButton toggle_add_runningPos;
    Button btn_add;
    ImageView img_addPromotion;

    ArrayList<Promo_Compliance_DataGetterSetter> promoSkuListData;
    ArrayList<Promo_Compliance_DataGetterSetter> promoSpinnerListData;
    ArrayList<Promo_Compliance_DataGetterSetter> additionalPromoListData;

    GSKOrangeDB db;
    String categoryName, categoryId;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    private SharedPreferences preferences;

    String str = CommonString.FILE_PATH,
            path = "", _pathforcheck = "", img = "";
    int child_position = -1;
    Uri outputFileUri;
    String gallery_package = "";
    String error_msg;
    Promo_Compliance_DataGetterSetter cd;

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

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_UAE)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_UAE_ARABIC;
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
            toggle_add_InStock = (Spinner) findViewById(R.id.toggle_add_InStock);
            toggle_add_promoAnnouncer = (Spinner) findViewById(R.id.toggle_add_promoAnnouncer);
            toggle_add_runningPos = (ToggleButton) findViewById(R.id.toggle_add_runningPos);
            btn_add = (Button) findViewById(R.id.btn_add);
            img_addPromotion = (ImageView) findViewById(R.id.img_addPromotion);

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
            camera_allow = preferences.getString(CommonString.KEY_CAMERA_ALLOW, "");

            //Intent data
            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            prepareList();
            promoSkuListView();
            adiitionalAnswerList();

            additionalPromoListData = new ArrayList<>();
            AdditionalPromoListView();

            cd = new Promo_Compliance_DataGetterSetter();

            cd.setStore_id(store_id);
            cd.setPromo_id("");
            cd.setPromo("");
            cd.setSku_id("");
            cd.setSku("");
            cd.setIn_stock("-1");
            cd.setPromo_announcer("-1");
            cd.setRunning_pos("0");
            cd.setSp_promo("0");
            cd.setImage_promotion("");

            img_addPromotion.setBackgroundResource(R.mipmap.camera_grey);

            toggle_add_InStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 1) {
                        cd.setIn_stock("1");
                        img_addPromotion.setBackgroundResource(R.mipmap.camera_orange);

                        if (camera_allow.equals("1")) {
                            img_addPromotion.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    _pathforcheck = "AddPromo_Image_" + store_id + categoryId + "_"
                                            + visit_date.replace("/", "") + "_" +
                                            getCurrentTime().replace(":", "") + ".jpg";
                                    path = str + _pathforcheck;

                                    startCameraActivity(2);
                                }
                            });
                        }
                    } else if (i == 2) {
                        cd.setIn_stock("0");
                        img_addPromotion.setClickable(false);
                        img_addPromotion.setBackgroundResource(R.mipmap.camera_grey);

                        if(!cd.getImage_promotion().equalsIgnoreCase(""))
                        {
                            if (new File(str + cd.getImage_promotion()).exists()) {
                                //img = _pathforcheck;
                                new File(str + cd.getImage_promotion()).delete();
                                cd.setImage_promotion("");
                            }
                        }


                    } else {
                        cd.setIn_stock("-1");
                        img_addPromotion.setClickable(false);
                        img_addPromotion.setBackgroundResource(R.mipmap.camera_grey);
                        if(!cd.getImage_promotion().equalsIgnoreCase(""))
                        {
                            if (new File(str + cd.getImage_promotion()).exists()) {
                                //img = _pathforcheck;
                                new File(str + cd.getImage_promotion()).delete();
                                cd.setImage_promotion("");
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (cd.getIn_stock().equals("1")) {
                toggle_add_InStock.setSelection(1);
            } else if (cd.getIn_stock().equals("0")) {
                toggle_add_InStock.setSelection(2);
            } else {
                toggle_add_InStock.setSelection(0);
            }

            toggle_add_promoAnnouncer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 1) {
                        cd.setPromo_announcer("1");
                    } else if (i == 2) {
                        cd.setPromo_announcer("0");
                    } else {
                        cd.setPromo_announcer("-1");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (cd.getPromo_announcer().equals("1")) {
                toggle_add_promoAnnouncer.setSelection(1);
            } else if (cd.getPromo_announcer().equals("0")) {
                toggle_add_promoAnnouncer.setSelection(2);
            } else {
                toggle_add_promoAnnouncer.setSelection(0);
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
                        boolean flag = true;

                        if (cd.getIn_stock().equalsIgnoreCase("-1") || cd.getPromo_announcer().equalsIgnoreCase("-1")) {
                            flag = false;
                        }

                        if (flag) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PromoComplianceActivity.this);
                            builder.setMessage(getResources().getString(R.string.want_add))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            db.InsertAdditionalPromoData(cd, categoryId);
                                            AdditionalPromoListView();

                                            sp_promo.setSelection(0);
                                            toggle_add_InStock.setSelection(0);
                                            toggle_add_promoAnnouncer.setSelection(0);
                                            toggle_add_runningPos.setChecked(false);
                                            img_addPromotion.setBackgroundResource(R.mipmap.camera_grey);

                                            Snackbar.make(v, getResources().getString(R.string.promo_add), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            //Toast.makeText(getApplicationContext(), "promo is add", Toast.LENGTH_LONG).show();
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
                            Snackbar.make(v, getResources().getString(R.string.please_select_answer), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    } else {
                        Snackbar.make(v, getResources().getString(R.string.select_promo_value), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    //if (isValid()) {
                    boolean flag = true;
                    if (promoSkuListData.size() <= 0) {
                        if (additionalPromoListData.size() <= 0) {
                            flag = false;
                            Snackbar.make(view, getResources().getString(R.string.fill_data), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    } else {
                        for (int i = 0; i < promoSkuListData.size(); i++) {
                            if (promoSkuListData.get(i).getIn_stock().equalsIgnoreCase("-1") || promoSkuListData.get(i).getPromo_announcer().equalsIgnoreCase("-1")) {
                                flag = false;
                            }
                        }
                        if (!flag) {
                            Snackbar.make(view, getResources().getString(R.string.please_select_answer), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }

                    if (flag) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PromoComplianceActivity.this);
                        builder.setMessage(getResources().getString(R.string.want_add))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (db.checkPromoComplianceData(store_id, categoryId)) {
                                            db.updatePromoComplianceSKU(promoSkuListData, categoryId, store_id);
                                            Snackbar.make(view, getResources().getString(R.string.update_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        } else {
                                            db.InsertPromoSkuData(promoSkuListData, categoryId);
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
                    }
                    /*} else {
                        Snackbar.make(view, error_msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }*/
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

    public boolean isValid() {
        boolean flag = true;

        for (int i = 0; i < promoSkuListData.size(); i++) {

            if (promoSkuListData.get(i).getIn_stock().equals("1")) {
                if (promoSkuListData.get(i).getImage_promotion().equals("")) {
                    flag = false;
                    error_msg = getResources().getString(R.string.click_image);
                    break;
                }
            }

        }

        return flag;
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
                promoSkuListData = db.getPromoComplianceSkuData(store_id, categoryId);
            }

            //Promo Spinner List
            promoSpinnerListData = db.getPromoSpinnerData(store_id, categoryId);

            ArrayAdapter<String> sp_promo_adapter = new ArrayAdapter<>(PromoComplianceActivity.this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < promoSpinnerListData.size(); i++) {
                sp_promo_adapter.add(promoSpinnerListData.get(i).getPromo());

            }
            sp_promo_adapter.setDropDownViewResource(R.layout.spinner_text_view);
            sp_promo.setAdapter(sp_promo_adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void promoSkuListView() {
        try {
            View view = null;

            if (lin_promo_sku != null) {
                lin_promo_sku.removeAllViews();
            }

            for (int i = 0; i < promoSkuListData.size(); i++) {

                final boolean[] userSelect = {false};
                final boolean[] userSelect2 = {false};

                view = getLayoutInflater().inflate(R.layout.item_promo_sku_list, null, false);

                final Promo_Compliance_DataGetterSetter data = promoSkuListData.get(i);

                TextView txt_promoSkuName = (TextView) view.findViewById(R.id.txt_promoSkuName);
                Spinner spinner_inStock = (Spinner) view.findViewById(R.id.spinner_inStock);
                Spinner spinner_promoAnnouncer = (Spinner) view.findViewById(R.id.spinner_promoAnnouncer);
                ToggleButton toggle_inStock = (ToggleButton) view.findViewById(R.id.toggle_inStock);
                ToggleButton toggle_promoAnnouncer = (ToggleButton) view.findViewById(R.id.toggle_promoAnnouncer);
                ToggleButton toggle_runningPos = (ToggleButton) view.findViewById(R.id.toggle_runningPos);
                final ImageView img_promotion = (ImageView) view.findViewById(R.id.img_promotion);

                txt_promoSkuName.setText(data.getPromo());

                ArrayList<SelectGetterSetter> ans_list = new ArrayList<>();
                SelectGetterSetter select = new SelectGetterSetter();
                select.setAns(getString(R.string.select));
                select.setAns_id(0);
                ans_list.clear();
                ans_list.add(select);

                select = new SelectGetterSetter();
                select.setAns(getString(R.string.yes));
                select.setAns_id(1);
                ans_list.add(select);

                select = new SelectGetterSetter();
                select.setAns(getString(R.string.no));
                select.setAns_id(2);
                ans_list.add(select);

                CustomSpinnerAdapter skuadapter = new CustomSpinnerAdapter(PromoComplianceActivity.this, R.layout.custom_t2p_spinner_item, ans_list);
                spinner_inStock.setAdapter(skuadapter);
                spinner_inStock.setSelection(0);

                ArrayList<SelectGetterSetter> ans_list2 = new ArrayList<>();
                SelectGetterSetter select2 = new SelectGetterSetter();
                select2.setAns(getString(R.string.select));
                select2.setAns_id(0);
                ans_list2.clear();
                ans_list2.add(select2);

                select2 = new SelectGetterSetter();
                select2.setAns(getString(R.string.yes));
                select2.setAns_id(1);
                ans_list2.add(select2);

                select2 = new SelectGetterSetter();
                select2.setAns(getString(R.string.no));
                select2.setAns_id(2);
                ans_list2.add(select2);

                CustomSpinnerAdapter skuadapter2 = new CustomSpinnerAdapter(PromoComplianceActivity.this, R.layout.custom_t2p_spinner_item, ans_list2);
                spinner_promoAnnouncer.setAdapter(skuadapter2);
                spinner_promoAnnouncer.setSelection(0);

                spinner_inStock.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        userSelect[0] = true;
                        return false;
                    }
                });

                //In Stock
                final int finalI = i;

                spinner_inStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        if (userSelect[0]) {
                            userSelect[0] = false;

                            switch (position) {

                                case 0:
                                    data.setIn_stock("-1");
                                    img_promotion.setClickable(false);
                                    img_promotion.setBackgroundResource(R.mipmap.camera_grey);

                                    //Camera
                                    if (!data.getImage_promotion().equals("")) {
                                        new File(str + data.getImage_promotion()).delete();
                                        data.setImage_promotion("");
                                    }


                                    break;
                                case 1:
                                    data.setIn_stock("1");
                                    img_promotion.setClickable(true);
                                    img_promotion.setBackgroundResource(R.mipmap.camera_orange);
                                    if (camera_allow.equals("1")) {
                                        img_promotion.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                _pathforcheck = "Promo_Image_" + store_id + categoryId + "_" + data.getSku_id() +
                                                        data.getPromo_id() + visit_date.replace("/", "") + "_" +
                                                        getCurrentTime().replace(":", "") + ".jpg";
                                                //child_position = position;
                                                child_position = finalI;
                                                path = str + _pathforcheck;

                                                startCameraActivity(1);
                                            }
                                        });
                                    }
                                    break;
                                case 2:

                                    data.setIn_stock("0");
                                    img_promotion.setClickable(false);
                                    img_promotion.setBackgroundResource(R.mipmap.camera_grey);

                                    //Camera
                                    if (!data.getImage_promotion().equals("")) {
                                        new File(str + data.getImage_promotion()).delete();
                                        data.setImage_promotion("");
                                    }

                                    break;
                            }

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


               /* toggle_inStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            data.setIn_stock("1");
                            img_promotion.setBackgroundResource(R.mipmap.camera_orange);

                            if (camera_allow.equals("1")) {
                                img_promotion.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        _pathforcheck = "Promo_Image_" + store_id + categoryId + "_" + data.getSku_id() +
                                                data.getPromo_id() + visit_date.replace("/", "") + "_" +
                                                getCurrentTime().replace(":", "") + ".jpg";
                                        //child_position = position;
                                        child_position = finalI;
                                        path = str + _pathforcheck;

                                        startCameraActivity(1);
                                    }
                                });
                            }
                        } else {
                            data.setIn_stock("0");
                            img_promotion.setClickable(false);
                            img_promotion.setBackgroundResource(R.mipmap.camera_grey);

                            //Camera
                            if (!data.getImage_promotion().equals("")) {
                                new File(str + data.getImage_promotion()).delete();
                                data.setImage_promotion("");
                            }
                        }
                    }
                });*/

             /*   if (data.getIn_stock().equals("1")) {
                    toggle_inStock.setChecked(true);
                } else {
                    toggle_inStock.setChecked(false);
                }*/

                switch (data.getIn_stock()) {
                    case "-1":
                        spinner_inStock.setSelection(0);
                        break;
                    case "0":
                        spinner_inStock.setSelection(2);
                        break;
                    case "1":
                        spinner_inStock.setSelection(1);
                        break;
                }


                spinner_promoAnnouncer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        userSelect2[0] = true;
                        return false;
                    }
                });


                spinner_promoAnnouncer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        if (userSelect2[0]) {
                            userSelect2[0] = false;

                            switch (position) {

                                case 0:
                                    data.setPromo_announcer("-1");

                                    break;
                                case 1:
                                    data.setPromo_announcer("1");
                                    break;
                                case 2:

                                    data.setPromo_announcer("0");

                                    break;
                            }

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


              /*  //Promo Announcer
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
                }*/

                //Running on POS
              /*  toggle_runningPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                }*/

                switch (data.getPromo_announcer()) {
                    case "-1":
                        spinner_promoAnnouncer.setSelection(0);
                        break;
                    case "0":
                        spinner_promoAnnouncer.setSelection(2);
                        break;
                    case "1":
                        spinner_promoAnnouncer.setSelection(1);
                        break;
                }


                if (!img.equalsIgnoreCase("")) {
                    if (i == child_position) {
                        data.setImage_promotion(img);
                        img = "";
                    }
                }

                if (camera_allow.equals("1")) {
                    //Camera
                    if (data.getIn_stock().equals("1")) {
                        if (data.getImage_promotion().equals("")) {
                            img_promotion.setBackgroundResource(R.mipmap.camera_orange);
                        } else {
                            img_promotion.setBackgroundResource(R.mipmap.camera_green);
                        }
                    } else {
                        img_promotion.setBackgroundResource(R.mipmap.camera_grey);
                    }
                } else {
                    img_promotion.setBackgroundResource(R.mipmap.camera_grey);
                }

                lin_promo_sku.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void adiitionalAnswerList() {

        ArrayList<SelectGetterSetter> ans_list = new ArrayList<>();
        SelectGetterSetter select = new SelectGetterSetter();
        select.setAns(getString(R.string.select));
        select.setAns_id(0);
        ans_list.clear();
        ans_list.add(select);

        select = new SelectGetterSetter();
        select.setAns(getString(R.string.yes));
        select.setAns_id(1);
        ans_list.add(select);

        select = new SelectGetterSetter();
        select.setAns(getString(R.string.no));
        select.setAns_id(2);
        ans_list.add(select);

        CustomSpinnerAdapter skuadapter = new CustomSpinnerAdapter(PromoComplianceActivity.this, R.layout.custom_t2p_spinner_item, ans_list);
        toggle_add_InStock.setAdapter(skuadapter);
        toggle_add_InStock.setSelection(0);

        ArrayList<SelectGetterSetter> ans_list2 = new ArrayList<>();
        SelectGetterSetter select2 = new SelectGetterSetter();
        select2.setAns(getString(R.string.select));
        select2.setAns_id(0);
        ans_list2.clear();
        ans_list2.add(select2);

        select2 = new SelectGetterSetter();
        select2.setAns(getString(R.string.yes));
        select2.setAns_id(1);
        ans_list2.add(select2);

        select2 = new SelectGetterSetter();
        select2.setAns(getString(R.string.no));
        select2.setAns_id(2);
        ans_list2.add(select2);

        CustomSpinnerAdapter skuadapter2 = new CustomSpinnerAdapter(PromoComplianceActivity.this, R.layout.custom_t2p_spinner_item, ans_list2);
        toggle_add_promoAnnouncer.setAdapter(skuadapter2);
        toggle_add_promoAnnouncer.setSelection(0);
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
                ImageView img_add_promotion_view = (ImageView) view.findViewById(R.id.img_add_promotion_view);

                txt_promoName.setText(data.getPromo());

                //In Stock
                if (data.getIn_stock().equals("1")) {
                    txt_inStock.setText(getResources().getString(R.string.yes));
                } else {
                    txt_inStock.setText(getResources().getString(R.string.no));
                }

                //Promo Announcer
                if (data.getPromo_announcer().equals("1")) {
                    txt_promoAnnouncer.setText(getResources().getString(R.string.yes));
                } else {
                    txt_promoAnnouncer.setText(getResources().getString(R.string.no));
                }

                //Running on POS
                if (data.getRunning_pos().equals("1")) {
                    txt_runningPos.setText(getResources().getString(R.string.yes));
                } else {
                    txt_runningPos.setText(getResources().getString(R.string.no));
                }

                //Camera Image
                if (!data.getImage_promotion().equals("")) {
                    img_add_promotion_view.setBackgroundResource(R.mipmap.camera_green);
                } else {
                    img_add_promotion_view.setBackgroundResource(R.mipmap.camera_orange);
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

    private void startCameraActivity(int pos) {
        try {

            Log.i("Stock & Facing ", "startCameraActivity()");
            File file = new File(path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //temp value in case camera is gallery app above jellybean
                    String packag = list.get(n).loadLabel(packageManager).toString();
                    if (packag.equalsIgnoreCase("Gallery") || packag.equalsIgnoreCase("Galeri") || packag.equalsIgnoreCase("الاستوديو")) {
                        gallery_package = list.get(n).packageName;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("الكاميرا")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {

                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("الكاميرا")) {

                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, pos);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Stock & Facing", "resultCode: " + resultCode + " requestCode: " + requestCode);

        switch (requestCode) {
            case 1:
                if (resultCode == 0) {
                    Log.e("Stock & Facing", "User cancelled");
                } else if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            img = _pathforcheck;
                            promoSkuListView();
                            //t2PAdapter.notifyDataSetChanged();
                            _pathforcheck = "";
                        }
                    }
                }
                break;

            case 2:
                if (resultCode == 0) {
                    Log.e("Stock & Facing", "User cancelled");
                } else if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            //img = _pathforcheck;
                            img_addPromotion.setBackgroundResource(R.mipmap.camera_green);
                            cd.setImage_promotion(_pathforcheck);
                            _pathforcheck = "";
                        }
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private static String arabicToenglish(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

        if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            cdate = arabicToenglish(cdate);
        } else if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_UAE)) {
            cdate = arabicToenglish(cdate);
        }

        return cdate;
    }

    public class CustomSpinnerAdapter extends ArrayAdapter<String> {

        SelectGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSpinnerAdapter(
                PromoComplianceActivity activitySpinner,
                int textViewResourceId,
                ArrayList objects

        ) {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            data = objects;
            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.custom_spinner_item, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (SelectGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getAns());
            }

            return row;
        }
    }
}
