package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.AdditionalDialogGetterSetter;
import cpm.com.gskmtorange.GetterSetter.AddittionalGetterSetter;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.ADDITIONAL_DISPLAY_MASTERGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;

/**
 * Created by ashishc on 05-01-2017.
 */

public class AdditionalVisibility extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayList<AdditionalDialogGetterSetter> list = new ArrayList<AdditionalDialogGetterSetter>();
    ArrayList<AddittionalGetterSetter> listdata = new ArrayList<>();
    ArrayList<AddittionalGetterSetter> listMain = new ArrayList<AddittionalGetterSetter>();
    ArrayList<AdditionalDialogGetterSetter> additionalVisibilitySkuList;
    ArrayList<AdditionalDialogGetterSetter> additionalVisibilityinsertSkuList;
    ArrayList<AdditionalDialogGetterSetter> uploadlist = new ArrayList<AdditionalDialogGetterSetter>();
    ArrayList<AdditionalDialogGetterSetter> defdata = new ArrayList<AdditionalDialogGetterSetter>();
    Spinner spinner_brand, spinner_sku;
    Spinner spinner_brand_list, spinner_sku_list;

    AdditionalDialogGetterSetter additionalgeetersetter;
    public static ArrayList<AddittionalGetterSetter> data = new ArrayList<AddittionalGetterSetter>();
    ToggleButton btntoggle;
    ImageView btnimage, btnedit, btnimage1, btnimage2;
    Button btnsku, btnaddlayout;
    GSKOrangeDB db;
    ArrayList<SkuGetterSetter> sku_list;
    String brand_name = "", brand_id = "", SKU_name = "", SKU_ID = "", brand_list_name = "", brand_list_id = "", sku_list_name = "", sku_list_id = "";
    public ListView listview;
    LinearLayout linearlay;
    CardView cardlay;
    ArrayList<BrandMasterGetterSetter> brandList;

    ArrayList<BrandMasterGetterSetter> brand_list;
    ArrayList<ADDITIONAL_DISPLAY_MASTERGetterSetter> DisplayMaster_list;
    //ArrayList<ADDITIONAL_DISPLAY_MASTERGetterSetter> DisplayMaster_list;


    ArrayList<SkuGetterSetter> empty_list = new ArrayList<>();
    String _pathforcheck1, _pathforcheck2, _pathforcheck3, _path, str, msg;
    private SharedPreferences preferences;
    String store_id, date, intime, img_str1, img_str2, img_str3, togglevalue = "1", CATEGORY_ID, camera_allow, store_type_id, class_id, key_account_id;
    ImageView img_cam, img_clicked;
    Button btn_add, btn_close;
    EditText Edt_txt;
    MyAdaptorStock adapterData;
    ListView listviewlay;
    String errormsg, categoryName, categoryId;
    MyAdaptorAdditionalStock adapteradditional;
    AddittionalGetterSetter adGt, newadd;
    LinearLayout brandlayout, diaplylayout, cameralayout;
    FloatingActionButton fab;
    //RelativeLayout skulayout;
    CardView cardvew, maincard;
    String gallery_package = "";
    Uri outputFileUri;

    Toolbar toolbar;

    ////String brand_id,SKU_ID;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.additionalvisibilitylayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
        camera_allow = preferences.getString(CommonString.KEY_CAMERA_ALLOW, "");
        store_type_id = preferences.getString(CommonString.KEY_STORETYPE_ID, "");
        class_id = preferences.getString(CommonString.KEY_CLASS_ID, "");
        key_account_id = preferences.getString(CommonString.KEY_KEYACCOUNT_ID, "");
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");

        //store_id = "2";
        date = preferences.getString(CommonString.KEY_DATE, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        db = new GSKOrangeDB(AdditionalVisibility.this);
        db.open();
        spinner_brand_list = (Spinner) findViewById(R.id.spinner_Brand1);
        spinner_sku_list = (Spinner) findViewById(R.id.spinner_SkuMaster);
        cardvew = (CardView) findViewById(R.id.cardviewid);
        btntoggle = (ToggleButton) findViewById(R.id.btntoggle);
        btnimage = (ImageView) findViewById(R.id.btn_image);
        btnimage1 = (ImageView) findViewById(R.id.btn_image2);
        btnimage2 = (ImageView) findViewById(R.id.btn_image3);

        btnedit = (ImageView) findViewById(R.id.btn_edit);
        btnsku = (Button) findViewById(R.id.btn_sku);
        btnaddlayout = (Button) findViewById(R.id.btadd);
        listviewlay = (ListView) findViewById(R.id.listviewlv);
        brandlayout = (LinearLayout) findViewById(R.id.tv_brandlayout);
        diaplylayout = (LinearLayout) findViewById(R.id.tv_displaylayout);
        cameralayout = (LinearLayout) findViewById(R.id.tv_cameralayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        maincard = (CardView) findViewById(R.id.cardviewid);

        btntoggle.setChecked(true);

        str = CommonString.FILE_PATH;

        ///band List

        brand_list = db.getBrandMasterData(store_id, categoryId);
        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();
        String str = getResources().getString(R.string.select);
        brand.setBRAND(str);
        brand_list.add(0, brand);
        CustomAdapter adapter = new CustomAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, brand_list);

        spinner_brand_list.setAdapter(adapter);

        ///Display List

        DisplayMaster_list = db.getADDITIONAL_DISPLAYData(store_id);

        ADDITIONAL_DISPLAY_MASTERGetterSetter select = new ADDITIONAL_DISPLAY_MASTERGetterSetter();
        select.setDISPLAY_ID(str);
        DisplayMaster_list.add(0, select);
        CustomSkuMasterAdpter skuadapter = new CustomSkuMasterAdpter(AdditionalVisibility.this, R.layout.custom_spinner_item, DisplayMaster_list);
        spinner_sku_list.setAdapter(skuadapter);

        spinner_brand_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    brand_list_name = brand_list.get(position).getBRAND().get(0);
                    brand_list_id = brand_list.get(position).getBRAND_ID().get(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sku_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    sku_list_name = DisplayMaster_list.get(position).getDISPLAY().get(0);
                    sku_list_id = DisplayMaster_list.get(position).getDISPLAY_ID().get(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/// maintable

        listdata = db.getAdditionalMainStock(store_id, categoryId);

        for (int k = 0; k < listdata.size(); k++) {
            listdata.get(k).setSkuDialogList(db.getDialogStock(listdata.get(k).getKey_id()));

            String tooglevalue = listdata.get(k).getBtn_toogle();

            /*if (tooglevalue.equalsIgnoreCase("0")) {
                btnaddlayout.setVisibility(View.INVISIBLE);
                cardvew.setVisibility(View.INVISIBLE);
                listviewlay.setVisibility(View.INVISIBLE);

                maincard.setVisibility(View.INVISIBLE);
                btntoggle.setChecked(false);
                brandlayout.setVisibility(View.GONE);
                diaplylayout.setVisibility(View.INVISIBLE);
                cameralayout.setVisibility(View.INVISIBLE);
                btnsku.setVisibility(View.INVISIBLE);
            }*/

            if (tooglevalue.equalsIgnoreCase("0")) {
                listviewlay.setVisibility(View.INVISIBLE);
                maincard.setVisibility(View.INVISIBLE);
                cardvew.setVisibility(View.INVISIBLE);
                btnaddlayout.setVisibility(View.INVISIBLE);

                btntoggle.setChecked(false);
                brandlayout.setVisibility(View.GONE);
                diaplylayout.setVisibility(View.INVISIBLE);
                cameralayout.setVisibility(View.INVISIBLE);
                btnsku.setVisibility(View.INVISIBLE);
            } else {
                adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                listviewlay.setAdapter(adapteradditional);
                listviewlay.invalidateViews();
                btnaddlayout.setVisibility(View.VISIBLE);
                cardvew.setVisibility(View.VISIBLE);
                listviewlay.setVisibility(View.VISIBLE);
                maincard.setVisibility(View.VISIBLE);
            }

           /* String KeyID = listdata.get(k).getKey_id();

            uploadlist = db.getDialogStock(KeyID);*/

        }

       /* listdata = db.getAdditionalStock(store_id, categoryId);

        for (int k = 0; k < listdata.size(); k++) {

            String tooglevalue = listdata.get(k).getBtn_toogle();

            if (tooglevalue.equalsIgnoreCase("0")) {
                btnaddlayout.setVisibility(View.INVISIBLE);
                cardvew.setVisibility(View.INVISIBLE);
                listviewlay.setVisibility(View.INVISIBLE);

                maincard.setVisibility(View.INVISIBLE);
                btntoggle.setChecked(false);
                brandlayout.setVisibility(View.GONE);
                diaplylayout.setVisibility(View.INVISIBLE);
                cameralayout.setVisibility(View.INVISIBLE);
                btnsku.setVisibility(View.INVISIBLE);
            }

        }*/


        /*if (listdata.size() > 0) {
            for (int i = 0; i < listdata.size(); i++) {
                if (listdata.get(i).getBtn_toogle().equalsIgnoreCase("0")) {
                    listviewlay.setVisibility(View.INVISIBLE);
                    maincard.setVisibility(View.INVISIBLE);
                    cardvew.setVisibility(View.INVISIBLE);
                    btnaddlayout.setVisibility(View.INVISIBLE);
                } else {
                    adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                    listviewlay.setAdapter(adapteradditional);
                    listviewlay.invalidateViews();
                    btnaddlayout.setVisibility(View.VISIBLE);
                    cardvew.setVisibility(View.VISIBLE);
                    listviewlay.setVisibility(View.VISIBLE);
                    maincard.setVisibility(View.VISIBLE);
                }
            }

        }*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (togglevalue.equals("1")) {

                    //listdata = db.getAdditionalStock(store_id, categoryId);

                    if (listdata.size() > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                AdditionalVisibility.this);
                        // set title
                        alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getResources().getString(R.string.title_activity_Want_save))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        db.deleteStockEntryMainTable(store_id, categoryId);

                                        /*for (int J = 0; J < listdata.size(); J++) {
                                            newadd = new AddittionalGetterSetter();
                                            newadd.setBrand(listdata.get(J).getBrand_id());
                                            newadd.setBrand_id(listdata.get(J).getBrand_id());
                                            newadd.setImage(listdata.get(J).getImage());
                                            newadd.setImage2(listdata.get(J).getImage2());
                                            newadd.setImage3(listdata.get(J).getImage3());
                                            newadd.setSku(listdata.get(J).getSku());
                                            newadd.setSku_id(listdata.get(J).getSku_id());
                                            newadd.setStore_id(listdata.get(J).getStore_id());
                                            newadd.setBtn_toogle(listdata.get(J).getBtn_toogle());
                                            newadd.setCategoryId(listdata.get(J).getCategoryId());
                                            String KeyID = listdata.get(J).getKey_id();

                                            //additionalVisibilitySkuList = db.getDialogStock(KeyID);

                                            db.InsertMainListAdditionalData(listdata, additionalVisibilitySkuList, categoryId);

                                            KeyID = "";
                                            additionalVisibilitySkuList.clear();
                                        }*/
                                        db.InsertMainListAdditionalData(listdata, categoryId);

                                        finish();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    } else {
                        Snackbar.make(view, getResources().getString(R.string.title_activity_Want_add), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdditionalVisibility.this);
                    // set title
                    alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getResources().getString(R.string.title_activity_Want_save))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    db.deleteStockEntryMainTable(store_id, categoryId);

                                    listdata.clear();

                                    newadd = new AddittionalGetterSetter();
                                    newadd.setBrand("");
                                    newadd.setBrand_id("");
                                    newadd.setImage("");
                                    newadd.setImage2("");
                                    newadd.setImage3("");
                                    newadd.setSku("");
                                    newadd.setSku_id("");
                                    newadd.setStore_id(store_id);
                                    newadd.setBtn_toogle(togglevalue);
                                    newadd.setCategoryId(categoryId);

                                    listdata.add(newadd);

                                    db.InsertMainListAdditionalData(listdata, categoryId);
                                    finish();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }


            }
        });


        btnaddlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adGt = new AddittionalGetterSetter();
                adGt.setBrand(brand_list_name);
                adGt.setBrand_id(brand_list_id);
                adGt.setImage(img_str1);
                adGt.setImage2(img_str2);
                adGt.setImage3(img_str3);

                adGt.setSku(sku_list_name);
                adGt.setSku_id(sku_list_id);
                adGt.setStore_id(store_id);
                adGt.setBtn_toogle(togglevalue);
                adGt.setCategoryId(categoryId);

                ArrayList<AdditionalDialogGetterSetter> listdataTemp = new ArrayList<>();
                listdataTemp.addAll(defdata);
                adGt.setSkuDialogList(listdataTemp);

                if (validateData(adGt)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdditionalVisibility.this);
                    // set title
                    alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getResources().getString(R.string.title_activity_Want_to_add))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //  db.InsertAdditionalData(adGt, defdata, categoryId);

                                    //Adding data to existing parent list
                                    listdata.add(adGt);

                                    spinner_brand_list.setSelection(0);
                                    spinner_sku_list.setSelection(0);
                                    img_str1 = "";
                                    img_str2 = "";
                                    img_str3 = "";
                                    brand_list_name = "";
                                    brand_list_id = "";
                                    sku_list_name = "";
                                    sku_list_id = "";
                                    defdata.clear();

                                    btnsku.setBackgroundResource(R.color.colorPrimary);

                                    if (camera_allow.equals("1")) {
                                        btnimage.setBackgroundResource(R.mipmap.camera_orange);

                                        btnimage1.setBackgroundResource(R.mipmap.camera_orange);
                                        btnimage2.setBackgroundResource(R.mipmap.camera_orange);

                                    } else {
                                        btnimage.setBackgroundResource(R.mipmap.camera_grey);

                                        btnimage1.setBackgroundResource(R.mipmap.camera_grey);
                                        btnimage2.setBackgroundResource(R.mipmap.camera_grey);
                                    }


                                    //listdata = db.getAdditionalStock(store_id, categoryId);
                                    if (listdata.size() > 0) {
                                        for (int i = 0; i < listdata.size(); i++) {
                                            if (listdata.get(i).getBtn_toogle().equalsIgnoreCase("0")) {
                                                listviewlay.setVisibility(View.INVISIBLE);
                                                maincard.setVisibility(View.INVISIBLE);
                                                cardvew.setVisibility(View.INVISIBLE);
                                                btnaddlayout.setVisibility(View.INVISIBLE);
                                            } else {
                                                adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                                listviewlay.setAdapter(adapteradditional);
                                                listviewlay.invalidateViews();
                                                btnaddlayout.setVisibility(View.VISIBLE);
                                                cardvew.setVisibility(View.VISIBLE);
                                                listviewlay.setVisibility(View.VISIBLE);
                                                maincard.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {
                    Snackbar.make(view, errormsg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });

        btntoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btntoggle.isChecked()) {
                    //listdata = db.getAdditionalStock(store_id, categoryId);

                    /*if (listdata.size() > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                AdditionalVisibility.this);
                        // set title
                        alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getResources().getString(R.string.data_will_be_lost))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        togglevalue = "1";
                                        btntoggle.setChecked(true);
                                        db.deleteStockEntryall(store_id, categoryId);
                                        brandlayout.setVisibility(View.GONE);
                                        diaplylayout.setVisibility(View.VISIBLE);
                                        cameralayout.setVisibility(View.VISIBLE);
                                        btnsku.setVisibility(View.VISIBLE);
                                        btnaddlayout.setVisibility(View.VISIBLE);
                                        cardvew.setVisibility(View.VISIBLE);
                                        listviewlay.setVisibility(View.VISIBLE);
                                        maincard.setVisibility(View.INVISIBLE);

                                        listdata = db.getAdditionalStock(store_id, categoryId);

                                        adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                        listviewlay.setAdapter(adapteradditional);
                                        listviewlay.invalidateViews();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                        btntoggle.setChecked(false);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();

                    } else {

                        listMain = db.getAdditionalMainStock(store_id, categoryId);

                        if (listMain.size() > 0) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    AdditionalVisibility.this);
                            // set title
                            alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(getResources().getString(R.string.data_will_be_lost))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.deleteStockEntryall(store_id, categoryId);
                                            togglevalue = "1";
                                            btntoggle.setChecked(true);
                                            brandlayout.setVisibility(View.GONE);
                                            diaplylayout.setVisibility(View.VISIBLE);
                                            cameralayout.setVisibility(View.VISIBLE);
                                            btnsku.setVisibility(View.VISIBLE);
                                            btnaddlayout.setVisibility(View.VISIBLE);
                                            cardvew.setVisibility(View.VISIBLE);
                                            listviewlay.setVisibility(View.INVISIBLE);
                                            maincard.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            dialog.cancel();
                                            btntoggle.setChecked(false);
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();

                        } else {
                            togglevalue = "1";
                            btntoggle.setChecked(true);
                            brandlayout.setVisibility(View.GONE);
                            diaplylayout.setVisibility(View.VISIBLE);
                            cameralayout.setVisibility(View.VISIBLE);
                            btnsku.setVisibility(View.VISIBLE);
                            btnaddlayout.setVisibility(View.VISIBLE);
                            cardvew.setVisibility(View.VISIBLE);
                            listviewlay.setVisibility(View.INVISIBLE);
                            maincard.setVisibility(View.INVISIBLE);

                        }


                    }*/

                    listdata.clear();

                    togglevalue = "1";
                    btntoggle.setChecked(true);
                    brandlayout.setVisibility(View.GONE);
                    diaplylayout.setVisibility(View.VISIBLE);
                    cameralayout.setVisibility(View.VISIBLE);
                    btnsku.setVisibility(View.VISIBLE);
                    btnaddlayout.setVisibility(View.VISIBLE);
                    cardvew.setVisibility(View.VISIBLE);
                    listviewlay.setVisibility(View.INVISIBLE);
                    maincard.setVisibility(View.INVISIBLE);

                } else {

                    //listdata = db.getAdditionalStock(store_id, categoryId);

                    if (listdata.size() > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                AdditionalVisibility.this);
                        // set title
                        alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getResources().getString(R.string.data_will_be_lost))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //db.deleteStockEntryall(store_id, categoryId);
                                   /*     togglevalue = "0";
                                        btntoggle.setChecked(false);
*/
                                        //clear both parent and child data lists
                                        //defdata.clear();
                                        listdata.clear();

                                     /*   btnaddlayout.setVisibility(View.INVISIBLE);
                                        brandlayout.setVisibility(View.GONE);
                                        diaplylayout.setVisibility(View.INVISIBLE);
                                        cameralayout.setVisibility(View.INVISIBLE);
                                        btnsku.setVisibility(View.INVISIBLE);
                                        maincard.setVisibility(View.INVISIBLE);*/

                                        // listdata = db.getAdditionalStock(store_id, categoryId);

                                       /* adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                        listviewlay.setAdapter(adapteradditional);
                                        listviewlay.invalidateViews();*/

                                        adapteradditional.notifyDataSetChanged();

                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        //btntoggle.setChecked(true);
                                        dialog.cancel();

                                        togglevalue = "1";
                                        btntoggle.setChecked(true);
                                        brandlayout.setVisibility(View.GONE);
                                        diaplylayout.setVisibility(View.VISIBLE);
                                        cameralayout.setVisibility(View.VISIBLE);
                                        btnsku.setVisibility(View.VISIBLE);
                                        btnaddlayout.setVisibility(View.VISIBLE);
                                        cardvew.setVisibility(View.VISIBLE);
                                        listviewlay.setVisibility(View.INVISIBLE);
                                        maincard.setVisibility(View.INVISIBLE);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();


                    }

                    togglevalue = "0";
                    btntoggle.setChecked(false);
                    defdata.clear();
                    btnaddlayout.setVisibility(View.INVISIBLE);
                    brandlayout.setVisibility(View.GONE);
                    diaplylayout.setVisibility(View.INVISIBLE);
                    cameralayout.setVisibility(View.INVISIBLE);
                    btnsku.setVisibility(View.INVISIBLE);
                    maincard.setVisibility(View.INVISIBLE);

                }
            }
        });


        if (camera_allow.equals("1")) {

            btnimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck1 = store_id + categoryId + "AdditionalImage1" + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                    _path = CommonString.FILE_PATH + _pathforcheck1;
                    intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                    startCameraActivity();

                }
            });

            btnimage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck2 = store_id + categoryId + "AdditionalImage2" + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                    _path = CommonString.FILE_PATH + _pathforcheck2;
                    intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                    startCameraActivity();

                }
            });

            btnimage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck3 = store_id + categoryId + "AdditionalImage3" + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                    _path = CommonString.FILE_PATH + _pathforcheck3;
                    intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                    startCameraActivity();

                }
            });


        } else {
            btnimage.setBackgroundResource(R.mipmap.camera_grey);
            btnimage1.setBackgroundResource(R.mipmap.camera_grey);
            btnimage2.setBackgroundResource(R.mipmap.camera_grey);
        }


        btnsku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkuDialog();
            }
        });

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

    public String getCurrentTimeNotUsed() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

        if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            cdate = arabicToenglish(cdate);
        }else if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_UAE)) {
            cdate = arabicToenglish(cdate);
        }

        return cdate;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        BrandMasterGetterSetter tempValues = null;
        LayoutInflater inflater;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(
                AdditionalVisibility activitySpinner,
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
            tempValues = (BrandMasterGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getResources().getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getBRAND().get(0));
            }

            return row;
        }
    }

    public class CustomSkuAdapter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        SkuGetterSetter tempValues = null;
        LayoutInflater inflater;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSkuAdapter(
                AdditionalVisibility activitySpinner,
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
            tempValues = (SkuGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getResources().getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getSKU());
            }

            return row;
        }
    }

    public class CustomSkuMasterAdpter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        ADDITIONAL_DISPLAY_MASTERGetterSetter tempValues = null;
        LayoutInflater inflater;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSkuMasterAdpter(
                AdditionalVisibility activitySpinner,
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
            tempValues = (ADDITIONAL_DISPLAY_MASTERGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getResources().getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getDISPLAY().get(0));
            }

            return row;
        }
    }

    protected void startCameraActivity() {
        try {

            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);

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

            //com.android.gallery3d

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:

                if (_pathforcheck1 != null && !_pathforcheck1.equals("")) {
                    if (new File(str + _pathforcheck1).exists()) {

                        btnimage.setBackgroundResource(R.mipmap.camera_green);

                        img_str1 = _pathforcheck1;
                        _pathforcheck1 = "";
                    }
                }
                if (_pathforcheck2 != null && !_pathforcheck2.equals("")) {
                    if (new File(str + _pathforcheck2).exists()) {

                        btnimage1.setBackgroundResource(R.mipmap.camera_green);

                        img_str2 = _pathforcheck2;
                        _pathforcheck2 = "";
                    }
                }

                if (_pathforcheck3 != null && !_pathforcheck3.equals("")) {
                    if (new File(str + _pathforcheck3).exists()) {

                        btnimage2.setBackgroundResource(R.mipmap.camera_green);

                        img_str3 = _pathforcheck3;
                        _pathforcheck3 = "";
                    }
                }


                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showSkuDialog() {

        final ArrayList<BrandMasterGetterSetter> brandList = db.getBrandT2PData(store_type_id, class_id, key_account_id, categoryId);

        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();
        brand.setBRAND(getResources().getString(R.string.select));
        brandList.add(0, brand);

        // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)
        final Dialog dialog = new Dialog(AdditionalVisibility.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.additionalvisibilitydialoglayout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        spinner_brand = (Spinner) dialog.findViewById(R.id.spinner_brand);
        spinner_sku = (Spinner) dialog.findViewById(R.id.spinner_sku);
        btn_add = (Button) dialog.findViewById(R.id.btn_add);
        btn_close = (Button) dialog.findViewById(R.id.btn_cancel);

        Edt_txt = (EditText) dialog.findViewById(R.id.et_stock);
        listview = (ListView) dialog.findViewById(R.id.lv);
        linearlay = (LinearLayout) dialog.findViewById(R.id.list_layout);
        cardlay = (CardView) dialog.findViewById(R.id.cardId);

        //list = db.getDialogStock(store_id);

        if (defdata.size() > 0) {
            linearlay.setVisibility(View.VISIBLE);
            cardlay.setVisibility(View.VISIBLE);
            adapterData = new MyAdaptorStock(AdditionalVisibility.this, defdata);
            listview.setAdapter(adapterData);
            listview.invalidateViews();

        } else {
            linearlay.setVisibility(View.INVISIBLE);
            cardlay.setVisibility(View.INVISIBLE);
        }

        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    SKU_ID = sku_list.get(position).getSKU_ID();

                    SKU_name = sku_list.get(position).getSKU();

                    listview.invalidateViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (defdata.size() > 0) {
                    // btn_add.setBackgroundResource(Color);
                    btnsku.setBackgroundResource(R.color.green);

                } else {
                    btnsku.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdditionalDialogGetterSetter ab = new AdditionalDialogGetterSetter();

                ab.setBrand(brand_name);
                ab.setBrand_id(brand_id);
                //ab.setDisplay_id(data.get(position).getDisplay_id());
                ab.setStore_id(store_id);
                // ab.setUnique_id(data.get(position).getUnique_id());
                ab.setSku_id(SKU_ID);
                ab.setSku_name(SKU_name);
                // ab.setProcess_id(process_id);
                ab.setQuantity(Edt_txt.getText().toString().replaceFirst("^0+(?!$)", ""));
                // ab.setCategory_id(category_id);

                if (validateDialogData(ab)) {
                    defdata.add(ab);
                    // db.InsertStockDialog(ab);
                    // spinner_brand.setSelection(0);
                    spinner_sku.setSelection(0);
                    Edt_txt.setText("");
                    SKU_ID = "";
                    SKU_name = "";
                    // list = db.getDialogStock(store_id);
                    linearlay.setVisibility(View.VISIBLE);
                    cardlay.setVisibility(View.VISIBLE);

                    adapterData = new MyAdaptorStock(AdditionalVisibility.this, defdata);
                    listview.setAdapter(adapterData);
                    listview.invalidateViews();

                } else {
                    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                // dialog.cancel();
            }
        });


        // Create custom adapter object ( see below CustomAdapter.java )
        CustomAdapter adapter = new CustomAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, brandList);
        // Set adapter to spinner
        spinner_brand.setAdapter(adapter);

        ArrayList<SkuGetterSetter> empty_list = new ArrayList<>();
        SkuGetterSetter select = new SkuGetterSetter();
        select.setSKU(getResources().getString(R.string.select));
        empty_list.add(select);
        CustomSkuAdapter skuadapter = new CustomSkuAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, empty_list);
        spinner_sku.setAdapter(skuadapter);

        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    brand_id = brandList.get(position).getBRAND_ID().get(0);
                    brand_name = brandList.get(position).getBRAND().get(0);

                    sku_list = db.getSkuT2PData(store_type_id, class_id, key_account_id, brand_id);
                    SkuGetterSetter select = new SkuGetterSetter();
                    select.setSKU(getResources().getString(R.string.select));
                    sku_list.add(0, select);
                    // Create custom adapter object ( see below CustomSkuAdapter.java )
                    CustomSkuAdapter skuadapter = new CustomSkuAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, sku_list);
                    // Set adapter to spinner
                    spinner_sku.setAdapter(skuadapter);

                    spinner_sku.setSelection(0);
                    SKU_ID = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (defdata.size() > 0) {
            // btn_add.setBackgroundResource(Color);
            btnsku.setBackgroundResource(R.color.green);

        } else {
            btnsku.setBackgroundResource(R.color.colorPrimary);
        }


        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    SKU_ID = sku_list.get(position).getSKU_ID();

                    SKU_name = sku_list.get(position).getSKU();

                } else {
                    SKU_ID = "";
                    SKU_name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog.show();

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            // NavUtils.navigateUpFromSameTask(this);
            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdaptorStock extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mcontext;
        private ArrayList<AdditionalDialogGetterSetter> list;

        public MyAdaptorStock(Activity activity, ArrayList<AdditionalDialogGetterSetter> list1) {
            mInflater = LayoutInflater.from(getBaseContext());
            mcontext = activity;
            list = list1;
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position1) {

            return position1;
        }

        @Override
        public long getItemId(int position1) {

            return position1;
        }

        class ViewHolder {
            TextView brand, qty_bought, display;
            Button save, delete;

        }

        @Override
        public View getView(final int position1, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.additionaldialoglayout, null);
                holder = new ViewHolder();

                holder.brand = (TextView) convertView.findViewById(R.id.brand_name);
                holder.display = (TextView) convertView.findViewById(R.id.display_name);
                holder.qty_bought = (TextView) convertView.findViewById(R.id.qty_bought);

                holder.delete = (Button) convertView.findViewById(R.id.delete_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdditionalVisibility.this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));

                    // set dialog message
                    alertDialogBuilder.setMessage(getResources().getString(R.string.data_will_be_lost))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // db.deletedialogStockEntry(list.get(position1).getKEY_ID());

                                    defdata.remove(position1);
                                    adapterData.notifyDataSetChanged();

                                   /* list = db.getTOTStockEntryDetail(store_id, category_id, process_id,
                                            list.get(position1).getDisplay_id(),list.get(position1).getUnique_id());*/
                                    //list = db.getDialogStock(store_id);

                                    //listview.setAdapter(new MyAdaptorStock(AdditionalVisibility.this, defdata));
                                    listview.invalidateViews();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });


            holder.brand.setText(list.get(position1).getBrand().toString());
            holder.display.setText(list.get(position1).getSku_name().toString());

            holder.qty_bought.setText(list.get(position1).getQuantity());


            holder.brand.setId(position1);
            holder.display.setId(position1);
            holder.qty_bought.setId(position1);
            holder.delete.setId(position1);

            return convertView;
        }
    }

    public class MyAdaptorAdditionalStock extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mcontext;
        private ArrayList<AddittionalGetterSetter> list;

        public MyAdaptorAdditionalStock(Activity activity, ArrayList<AddittionalGetterSetter> list1) {

            mInflater = LayoutInflater.from(getBaseContext());
            mcontext = activity;
            list = list1;
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position1) {

            return position1;
        }

        @Override
        public long getItemId(int position1) {

            return position1;
        }

        class ViewHolder {
            TextView brand, qty_bought, display;
            Button save, delete;

        }

        @Override
        public View getView(final int position1, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.additionallistlayout, null);
                holder = new ViewHolder();

                holder.brand = (TextView) convertView.findViewById(R.id.brand_name);
                holder.display = (TextView) convertView.findViewById(R.id.display_name);
                holder.qty_bought = (TextView) convertView.findViewById(R.id.qty_bought);
                holder.delete = (Button) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdditionalVisibility.this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getResources().getString(R.string.data_will_be_lost))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //db.deleteStockEntry(listdata.get(position1).getKey_id());

                                    listdata.remove(position1);
                                    adapteradditional.notifyDataSetChanged();
                                    listviewlay.invalidateViews();

                                    /*listdata = db.getAdditionalStock(store_id, categoryId);


                                    adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                    listviewlay.setAdapter(adapteradditional);

                                    listviewlay.invalidateViews();*/


                                    if (listdata.size() > 0) {

                                        for (int i = 0; i < listdata.size(); i++) {
                                            if (listdata.get(i).getBtn_toogle().equalsIgnoreCase("0")) {
                                                listviewlay.setVisibility(View.INVISIBLE);
                                                cardvew.setVisibility(View.INVISIBLE);
                                                btnaddlayout.setVisibility(View.INVISIBLE);
                                                maincard.setVisibility(View.INVISIBLE);
                                            } else {
                                                adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                                listviewlay.setAdapter(adapteradditional);
                                                listviewlay.invalidateViews();
                                                btnaddlayout.setVisibility(View.VISIBLE);
                                                cardvew.setVisibility(View.VISIBLE);
                                                listviewlay.setVisibility(View.VISIBLE);
                                                maincard.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }


                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            holder.brand.setText(list.get(position1).getBrand().toString());
            holder.display.setText(list.get(position1).getSku().toString());
            holder.brand.setId(position1);
            holder.display.setId(position1);
            holder.qty_bought.setId(position1);
            holder.delete.setId(position1);

            return convertView;
        }
    }

    boolean validateData(AddittionalGetterSetter data) {
        boolean flag = true;

        String brandid = data.getBrand_id();
        String skuid = data.getSku_id();

        String imageu = data.getImage();
        String toggleid = data.getBtn_toogle();
        ArrayList<AdditionalDialogGetterSetter> skuList = data.getSkuDialogList();

        if (toggleid.equalsIgnoreCase("0")) {
            flag = true;
        } else {

            if (/*brandid.equalsIgnoreCase("") ||*/ skuid.equalsIgnoreCase("")) {
                flag = false;

                errormsg = getResources().getString(R.string.title_activity_select_dropdown);

            } else if (camera_allow.equals("1")) {
                if (imageu == null || imageu.equalsIgnoreCase("")) {
                    flag = false;

                    errormsg = getResources().getString(R.string.title_activity_take_image);

                } else if (skuList.size() == 0) {
                    errormsg = getResources().getString(R.string.title_activity_fill_sku);
                    flag = false;
                }

            } else if (skuList.size() == 0) {

                errormsg = getResources().getString(R.string.title_activity_fill_sku);
                flag = false;


            } else {
                flag = true;
            }


        }


        return flag;
    }

    boolean validateDialogData(AdditionalDialogGetterSetter data) {
        boolean flag = true;

        String brandid = data.getBrand_id();
        String displayid = data.getSku_id();
        String skuname = data.getSku_name();
        String QTy = data.getQuantity();


        if (brandid.equalsIgnoreCase("") || brandid == null) {
            flag = false;

            msg = getResources().getString(R.string.title_activity_select_dropdown);
        } else if (displayid == null || displayid.equalsIgnoreCase("") || skuname.equalsIgnoreCase("select")) {
            flag = false;
            msg = getResources().getString(R.string.title_activity_select_dropdown);
        } else if (QTy.equalsIgnoreCase("") || QTy == null) {
            flag = false;
            msg = getResources().getString(R.string.title_activity_enter_quantity);
        } else {
            flag = true;
        }


        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(getResources().getString(R.string.title_activity_Additional_visibility));

    }


}