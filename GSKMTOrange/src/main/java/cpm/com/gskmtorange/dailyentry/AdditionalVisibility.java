package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.AdditionalDialogGetterSetter;
import cpm.com.gskmtorange.GetterSetter.AddittionalGetterSetter;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;

/**
 * Created by ashishc on 05-01-2017.
 */

public class AdditionalVisibility extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayList<AdditionalDialogGetterSetter> list = new ArrayList<AdditionalDialogGetterSetter>();
    ArrayList<AddittionalGetterSetter> listdata = new ArrayList<AddittionalGetterSetter>();

    ArrayList<AdditionalDialogGetterSetter> uploadlist = new ArrayList<AdditionalDialogGetterSetter>();
    ArrayList<AdditionalDialogGetterSetter> defdata = new ArrayList<AdditionalDialogGetterSetter>();
    Spinner spinner_brand, spinner_sku;
    Spinner spinner_brand_list, spinner_sku_list;

    public static ArrayList<AddittionalGetterSetter> data = new ArrayList<AddittionalGetterSetter>();
    ToggleButton btntoggle;
    ImageView btnimage, btnedit;
    Button btnsku, btnaddlayout;
    GSKOrangeDB db;
    ArrayList<SkuGetterSetter> sku_list;
    String brand_name = "", brand_id = "", SKU_name = "", SKU_ID = "", brand_list_name = "", brand_list_id = "", sku_list_name = "", sku_list_id = "";
    public ListView listview;
    LinearLayout linearlay;
    ArrayList<BrandMasterGetterSetter> brandList;

    ArrayList<BrandMasterGetterSetter> brand_list;
    ArrayList<SkuMasterGetterSetter> skuMaster_list;


    ArrayList<SkuGetterSetter> empty_list = new ArrayList<>();
    String _pathforcheck, _path, str, msg;
    private SharedPreferences preferences;
    String store_id, date, intime, img_str, togglevalue = "1";
    ImageView img_cam, img_clicked;
    Button btn_add, btn_close;
    EditText Edt_txt;
    MyAdaptorStock adapterData;
    ListView listviewlay;
    String errormsg;
    MyAdaptorAdditionalStock adapteradditional;
    AddittionalGetterSetter adGt;
    LinearLayout brandlayout, diaplylayout, cameralayout;
    //RelativeLayout skulayout;

    ////String brand_id,SKU_ID;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additionalvisibilitylayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);


        store_id = "2";
        date = preferences.getString(CommonString.KEY_DATE, null);
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        db = new GSKOrangeDB(AdditionalVisibility.this);
        db.open();
        spinner_brand_list = (Spinner) findViewById(R.id.spinner_Brand1);
        spinner_sku_list = (Spinner) findViewById(R.id.spinner_SkuMaster);

        btntoggle = (ToggleButton) findViewById(R.id.btntoggle);
        btnimage = (ImageView) findViewById(R.id.btn_image);
        btnedit = (ImageView) findViewById(R.id.btn_edit);
        btnsku = (Button) findViewById(R.id.btn_sku);
        btnaddlayout = (Button) findViewById(R.id.btadd);
        listviewlay = (ListView) findViewById(R.id.listviewlv);
        brandlayout = (LinearLayout) findViewById(R.id.tv_brandlayout);
        diaplylayout = (LinearLayout) findViewById(R.id.tv_displaylayout);
        cameralayout = (LinearLayout) findViewById(R.id.tv_cameralayout);

        //skulayout = (RelativeLayout) findViewById(R.id.tv_skulayout);

        btntoggle.setChecked(true);


        str = CommonString.FILE_PATH;

        ///band List
        brand_list = db.getBrandMasterData(store_id);
        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();
        brand.setBRAND("select");
        brand_list.add(0, brand);
        CustomAdapter adapter = new CustomAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, brand_list);

        spinner_brand_list.setAdapter(adapter);

        ///Display List

        skuMaster_list = db.getSKUMasterData(store_id);

        SkuMasterGetterSetter select = new SkuMasterGetterSetter();
        select.setSKU("Select");
        skuMaster_list.add(0, select);
        CustomSkuMasterAdpter skuadapter = new CustomSkuMasterAdpter(AdditionalVisibility.this, R.layout.custom_spinner_item, skuMaster_list);
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

                    sku_list_name = skuMaster_list.get(position).getSKU().get(0);
                    sku_list_id = skuMaster_list.get(position).getSKU_ID().get(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listdata = db.getAdditionalStock(store_id);

        for (int k = 0; k < listdata.size(); k++) {
            String KeyID = listdata.get(k).getKey_id();

            uploadlist = db.getDialogStock(KeyID);

        }


        if (listdata.size() > 0) {
            adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
            listviewlay.setAdapter(adapteradditional);
            listviewlay.invalidateViews();
        }

        btnaddlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adGt = new AddittionalGetterSetter();
                adGt.setBrand(brand_list_name);
                adGt.setBrand_id(brand_list_id);
                adGt.setImage(img_str);
                adGt.setSku(sku_list_name);
                adGt.setSku_id(sku_list_id);
                adGt.setStore_id(store_id);
                adGt.setBtn_toogle(togglevalue);

                if (validateData(adGt, defdata)) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdditionalVisibility.this);

                    // set title
                    alertDialogBuilder.setTitle("Do You Want To Save");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    db.InsertAdditionalData(adGt, defdata);

                                    spinner_brand_list.setSelection(0);

                                    spinner_sku_list.setSelection(0);

                                    img_str = "";
                                    brand_list_name = "";
                                    brand_list_id = "";
                                    sku_list_name = "";
                                    sku_list_id = "";

                                    defdata.clear();

                                    btntoggle.setChecked(true);

                                    btnimage.setBackgroundResource(R.mipmap.camera);
                                    togglevalue = "1";
                                    brandlayout.setVisibility(View.VISIBLE);
                                    diaplylayout.setVisibility(View.VISIBLE);
                                    cameralayout.setVisibility(View.VISIBLE);
                                    btnsku.setVisibility(View.VISIBLE);

                                    listdata = db.getAdditionalStock(store_id);

                                    if (listdata.size() > 0) {
                                        adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                        listviewlay.setAdapter(adapteradditional);
                                        listviewlay.invalidateViews();
                                    } else {

                                    }

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                    togglevalue = "1";

                    brandlayout.setVisibility(View.VISIBLE);
                    diaplylayout.setVisibility(View.VISIBLE);
                    cameralayout.setVisibility(View.VISIBLE);
                    btnsku.setVisibility(View.VISIBLE);

                } else {
                    togglevalue = "0";

                    brandlayout.setVisibility(View.INVISIBLE);
                    diaplylayout.setVisibility(View.INVISIBLE);
                    cameralayout.setVisibility(View.INVISIBLE);
                    btnsku.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pathforcheck = store_id + "Store"
                        + "Image" + date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();
                startCameraActivity();

            }
        });


        btnsku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkuDialog();

            }
        });

    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

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
                label.setText("Select");
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
                label.setText("Select");
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
        SkuMasterGetterSetter tempValues = null;
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
            tempValues = (SkuMasterGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText("Select");
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getSKU().get(0));
            }

            return row;
        }
    }


    protected void startCameraActivity() {

        try {

            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }

            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
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

                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {

                        btnimage.setBackgroundResource(R.mipmap.camera_done);

                        img_str = _pathforcheck;
                        _pathforcheck = "";
                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showSkuDialog() {
        final ArrayList<BrandMasterGetterSetter> brandList = db.getBrandT2PData("1", "1", "1");
        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();
        brand.setBRAND("select");
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

        //list = db.getDialogStock(store_id);


        if (defdata.size() > 0) {
            linearlay.setVisibility(View.VISIBLE);
            adapterData = new MyAdaptorStock(AdditionalVisibility.this, defdata);
            listview.setAdapter(adapterData);
            listview.invalidateViews();
        } else {

        }


        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    SKU_ID = sku_list.get(position).getSKU_ID();

                    SKU_name = sku_list.get(position).getSKU();

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
                ab.setQuantity(Edt_txt.getText().toString());
                // ab.setCategory_id(category_id);


                if (validateDialogData(ab)) {


                    defdata.add(ab);
                    // db.InsertStockDialog(ab);

                    spinner_brand.setSelection(0);
                    spinner_sku.setSelection(0);
                    Edt_txt.setText("");

                    // list = db.getDialogStock(store_id);
                    linearlay.setVisibility(View.VISIBLE);
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
        select.setSKU("Select");
        empty_list.add(select);
        CustomSkuAdapter skuadapter = new CustomSkuAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, empty_list);
        spinner_sku.setAdapter(skuadapter);

        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    brand_id = brandList.get(position).getBRAND_ID().get(0);
                    brand_name = brandList.get(position).getBRAND().get(0);

                    sku_list = db.getSkuT2PData("1", "1", "1", brand_id);
                    SkuGetterSetter select = new SkuGetterSetter();
                    select.setSKU("Select");
                    sku_list.add(0, select);
                    // Create custom adapter object ( see below CustomSkuAdapter.java )
                    CustomSkuAdapter skuadapter = new CustomSkuAdapter(AdditionalVisibility.this, R.layout.custom_spinner_item, sku_list);
                    // Set adapter to spinner
                    spinner_sku.setAdapter(skuadapter);

                    spinner_sku.setSelection(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_sku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    SKU_ID = sku_list.get(position).getSKU_ID();

                    SKU_name = sku_list.get(position).getSKU();

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

                convertView = mInflater
                        .inflate(R.layout.additionaldialoglayout, null);
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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdditionalVisibility.this);

                    // set title
                    alertDialogBuilder.setTitle("Do You Want To Delete?");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Yes To Delete!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    // db.deletedialogStockEntry(list.get(position1).getKEY_ID());

                                    defdata.remove(position1);

                                    adapterData.notifyDataSetChanged();

                                   /* list = db.getTOTStockEntryDetail(store_id, category_id, process_id,
                                            list.get(position1).getDisplay_id(),list.get(position1).getUnique_id());*/


                                    //list = db.getDialogStock(store_id);

                                    listview.setAdapter(new MyAdaptorStock(AdditionalVisibility.this, defdata));
                                    listview.invalidateViews();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

                convertView = mInflater
                        .inflate(R.layout.additionallistlayout, null);
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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            AdditionalVisibility.this);

                    // set title
                    alertDialogBuilder.setTitle("Do You Want To Delete?");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Yes To Delete!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    db.deleteStockEntry(listdata.get(position1).getKey_id());

                                    adapteradditional.notifyDataSetChanged();

                                    listdata = db.getAdditionalStock(store_id);

                                    adapteradditional = new MyAdaptorAdditionalStock(AdditionalVisibility.this, listdata);
                                    listviewlay.setAdapter(adapteradditional);
                                    listviewlay.invalidateViews();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
            holder.display.setText(list.get(position1).getSku().toString());


            holder.brand.setId(position1);
            holder.display.setId(position1);
            holder.qty_bought.setId(position1);
            holder.delete.setId(position1);

            return convertView;
        }
    }

    boolean validateData(AddittionalGetterSetter data, ArrayList<AdditionalDialogGetterSetter> dialog) {
        boolean flag = true;

        String brandid = data.getBrand_id();
        String skuid = data.getSku_id();

        String imageu = data.getImage();
        String toggleid = data.getBtn_toogle();


        if (toggleid.equalsIgnoreCase("0")) {
            flag = true;
        } else {


            if (brandid.equalsIgnoreCase("") || skuid.equalsIgnoreCase("")) {
                flag = false;

                errormsg = "Please Select dropdown";

            } else if (imageu == null || imageu.equalsIgnoreCase("")) {
                flag = false;

                errormsg = "Please Take a image";

            } else if (dialog.size() == 0) {

                errormsg = "Please fill sku data";
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
        String QTy = data.getQuantity();


        if (brandid.equalsIgnoreCase("") || brandid == null) {
            flag = false;

            msg = "Please Select Dropdown";
        } else if (displayid == null || displayid.equalsIgnoreCase("")) {
            flag = false;
            msg = "Please Select Dropdown";
        } else if (QTy.equalsIgnoreCase("") || QTy == null) {
            flag = false;
            msg = "Please enter Quantity";
        } else {
            flag = true;
        }


        return flag;
    }


}