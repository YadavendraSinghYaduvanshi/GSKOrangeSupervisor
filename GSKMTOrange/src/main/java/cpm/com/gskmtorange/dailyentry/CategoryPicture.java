package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CategoryPictureGetterSetter;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.gsk_dailyentry.MSL_Availability_StockFacingActivity;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryImagesAllowed;

public class CategoryPicture extends AppCompatActivity {
    String _pathforcheck1, _pathforcheck2, _pathforcheck3, _pathforcheck4, Camerapath1, Camerapath2, _path, CaMpath, str, msg, categoryName, categoryId;
    ImageView im1, im2, im3, im4;
    ListView listview;
    String store_id, date, intime, img_str1="", img_str2="", img_str3="", img_str4="", togglevalue = "1", CATEGORY_ID, camera_allow, store_type_id, class_id, key_account_id;
    private SharedPreferences preferences;
    Uri outputFileUri;
    String gallery_package = "";
    GSKOrangeDB db;
    ArrayList<CategoryPictureGetterSetter> adddata = new ArrayList<CategoryPictureGetterSetter>();
    int Adapterposition;
    ArrayList<CategoryPictureGetterSetter> listdat = new ArrayList<CategoryPictureGetterSetter>();
    CategoryAdapter adapteradditional;
    Toolbar toolbar;

    ArrayList<CategoryImagesAllowed> categoryImagesAllowed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        str = CommonString.FILE_PATH;
        db = new GSKOrangeDB(CategoryPicture.this);
        db.open();

        im1 = (ImageView) findViewById(R.id.image1);
        im2 = (ImageView) findViewById(R.id.image2);
        im3 = (ImageView) findViewById(R.id.image3);
        im4 = (ImageView) findViewById(R.id.image4);
        listview = (ListView) findViewById(R.id.listview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        adddata = db.getCategoryPictureData(store_id, categoryId);
        categoryImagesAllowed = db.getCategoryPictureAllowedData(categoryId);

        if(categoryImagesAllowed.size()>0){
            setCamAllowImage(categoryImagesAllowed.get(0).isImg_cam1(), im1);
            setCamAllowImage(categoryImagesAllowed.get(0).isImg_cam2(), im2);
            setCamAllowImage(categoryImagesAllowed.get(0).isImg_cam2(), im3);
            setCamAllowImage(categoryImagesAllowed.get(0).isImg_cam3(), im4);
        }

        if (adddata.size() != 0) {

                String key_id = adddata.get(0).getKEY_ID();

                listdat = db.getCategoryPictureListData(store_id, categoryId, key_id);

                String image1 = adddata.get(0).getCategoryImage1();
                String image2 = adddata.get(0).getCategoryImage2();
                String image3 = adddata.get(0).getCategoryImage3();
                String image4 = adddata.get(0).getCategoryImage4();

                if (image1 != null && !image1.equals("")) {
                    im1.setBackgroundResource(R.mipmap.camera_green);
                    img_str1 = image1;
                }

                if (image2 != null && !image2.equals("")) {
                    im2.setBackgroundResource(R.mipmap.camera_green);
                    img_str2 = image2;
                }

                if (image3 != null && !image3.equals("")) {
                    im3.setBackgroundResource(R.mipmap.camera_green);
                    img_str3 = image3;
                }

                if (image4 != null && !image4.equals("")) {
                    im4.setBackgroundResource(R.mipmap.camera_green);
                    img_str4 = image4;
                }

        } else {

            listdat = db.getCategoryPicturedata(categoryId, key_account_id, store_type_id, class_id);

        }

        adapteradditional = new CategoryPicture.CategoryAdapter(CategoryPicture.this, listdat);
        listview.setAdapter(adapteradditional);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final CategoryPictureGetterSetter CP = new CategoryPictureGetterSetter();

                CP.setCategoryImage1(img_str1);
                CP.setCategoryImage2(img_str2);
                CP.setCategoryImage3(img_str3);
                CP.setCategoryImage4(img_str4);
                CP.setStore_ID(store_id);
                CP.setCamera_allow(camera_allow);

                if (validateData(CP, listdat)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryPicture.this);
                    builder.setMessage(getResources().getString(R.string.check_save_message))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.open();

                                    db.InsertCategoryPictureData(CP, listdat, categoryId);
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


                } else {
                    Snackbar.make(view, R.string.title_activity_take_image, Snackbar.LENGTH_LONG).setAction("Action", null).show();


                }


            }
        });

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                _pathforcheck1 = store_id + "CategoryPicture1" + categoryId + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck1;
                intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                startCameraActivity();


            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _pathforcheck2 = store_id + "CategoryPicture2" + categoryId + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck2;
                intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                startCameraActivity();


            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _pathforcheck3 = store_id + "CategoryPicture3" + categoryId + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck3;
                intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                startCameraActivity();


            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _pathforcheck4 = store_id + "CategoryPicture4" + categoryId + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck4;
                intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                startCameraActivity();


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

                        im1.setBackgroundResource(R.mipmap.camera_green);

                        img_str1 = _pathforcheck1;
                        _pathforcheck1 = "";
                    }
                }
                if (_pathforcheck2 != null && !_pathforcheck2.equals("")) {
                    if (new File(str + _pathforcheck2).exists()) {

                        im2.setBackgroundResource(R.mipmap.camera_green);

                        img_str2 = _pathforcheck2;
                        _pathforcheck2 = "";
                    }
                }

                if (_pathforcheck3 != null && !_pathforcheck3.equals("")) {
                    if (new File(str + _pathforcheck3).exists()) {

                        im3.setBackgroundResource(R.mipmap.camera_green);

                        img_str3 = _pathforcheck3;
                        _pathforcheck3 = "";
                    }
                }

                if (_pathforcheck4 != null && !_pathforcheck4.equals("")) {
                    if (new File(str + _pathforcheck4).exists()) {

                        im4.setBackgroundResource(R.mipmap.camera_green);

                        img_str4 = _pathforcheck4;
                        _pathforcheck4 = "";
                    }
                }
                if (Camerapath1 != null && !Camerapath1.equals("")) {
                    if (new File(str + Camerapath1).exists()) {


                        listdat.get(Adapterposition).setSubCategoryCamera1(Camerapath1);

                        Camerapath1 = "";
                        listview.invalidateViews();
                    }
                }

                if (Camerapath2 != null && !Camerapath2.equals("")) {
                    if (new File(str + Camerapath2).exists()) {

                        listdat.get(Adapterposition).setSubCategoryCamera2(Camerapath2);

                        Camerapath2 = "";
                        listview.invalidateViews();
                    }
                }


                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public class CategoryAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mcontext;
        private ArrayList<CategoryPictureGetterSetter> list;

        public CategoryAdapter(Activity activity, ArrayList<CategoryPictureGetterSetter> list1) {

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
            Button camera1, camera2, delete;

        }

        @Override
        public View getView(final int position1, View convertView, ViewGroup parent) {

            final CategoryPicture.CategoryAdapter.ViewHolder holder;

            if (convertView == null) {

                convertView = mInflater
                        .inflate(R.layout.contentcatgoryadpterlayout, null);
                holder = new CategoryPicture.CategoryAdapter.ViewHolder();

                holder.brand = (TextView) convertView.findViewById(R.id.textviewname);

                holder.camera1 = (Button) convertView.findViewById(R.id.button3);
                holder.camera2 = (Button) convertView.findViewById(R.id.cameranew);
                convertView.setTag(holder);
            } else {
                holder = (CategoryPicture.CategoryAdapter.ViewHolder) convertView.getTag();
            }

            holder.camera1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Camerapath1 = store_id + "CategoryPicture" + list.get(position1).getSUB_CATEGORY_ID().toString() + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";
                    Adapterposition = position1;

                    _path = CommonString.FILE_PATH + Camerapath1;
                    intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                    startCameraActivity();

                    listview.invalidateViews();


                }

            });

            holder.camera2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Camerapath2 = store_id + "CategoryPicture" + list.get(position1).getSUB_CATEGORY_ID().toString() + date.replace("/", "") + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";
                    Adapterposition = position1;
                    _path = CommonString.FILE_PATH + Camerapath2;
                    intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());
                    startCameraActivity();
                    listview.invalidateViews();
                }

            });

            holder.brand.setText(list.get(position1).getSUB_CATEGORY().toString());


            if (!listdat.get(position1).getSubCategoryCamera1().equalsIgnoreCase("")) {

                holder.camera1.setBackgroundResource(R.mipmap.camera_green);


            } else if (listdat.get(position1).getImage_allow().equals("1")) {
                holder.camera1.setBackgroundResource(R.drawable.camera_orange_star_green);
            } else {
                holder.camera1.setBackgroundResource(R.mipmap.camera_orange);
            }

            if (!listdat.get(position1).getSubCategoryCamera2().equalsIgnoreCase("")) {


                holder.camera2.setBackgroundResource(R.mipmap.camera_green);

            } else {
                holder.camera2.setBackgroundResource(R.mipmap.camera_orange);
            }


            holder.brand.setId(position1);

            return convertView;
        }
    }


    boolean validateData(CategoryPictureGetterSetter data, ArrayList<CategoryPictureGetterSetter> list) {
        boolean flag = true;

            if(categoryImagesAllowed.get(0).isImg_cam1()){
               if(data.getCategoryImage1().equals("")){
                   flag = false;
               }
            }

            if(flag){
                if(categoryImagesAllowed.get(0).isImg_cam2()){
                    if(data.getCategoryImage2().equals("")){
                        flag = false;
                    }
                }
            }

        if(flag){
            if(categoryImagesAllowed.get(0).isImg_cam3()){
                if(data.getCategoryImage3().equals("")){
                    flag = false;
                }
            }
        }

        if(flag){
            if(categoryImagesAllowed.get(0).isImg_cam4()){
                if(data.getCategoryImage4().equals("")){
                    flag = false;
                }
            }
        }


        if (flag) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).getImage_allow().equals("1")) {

                    String imageu = list.get(i).getSubCategoryCamera1();

                    if (imageu.equalsIgnoreCase("")) {

                        flag = false;
                        break;
                    }
                }

            }
        }

        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CategoryPicture.this);
            builder.setTitle("Parinaam");
            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            android.app.AlertDialog alert = builder.create();
            alert.show();
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CategoryPicture.this);
        builder.setTitle("Parinaam");
        builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(getResources().getString(R.string.title_activity_category_picture));
    }

    public void setCamAllowImage(boolean isAllowed, ImageView img_cam){

        if(isAllowed){
            img_cam.setBackgroundResource(R.drawable.camera_orange_star_green);
        }
        else{
            img_cam.setBackgroundResource(R.mipmap.camera_orange);
        }
    }
}
