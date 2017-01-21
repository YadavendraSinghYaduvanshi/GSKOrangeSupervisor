package cpm.com.gskmtorange.gsk_dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.dailyentry.T2PComplianceActivity;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;

import static android.R.attr.angle;

public class Stock_FacingActivity extends AppCompatActivity {
    static int child_position = -1;
    ExpandableListView expandableListView;
    TextView txt_stockFacingName;
    ArrayList<Stock_FacingGetterSetter> headerDataList;
    ArrayList<Stock_FacingGetterSetter> childDataList;
    List<Stock_FacingGetterSetter> hashMapListHeaderData;
    HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> hashMapListChildData;
    List<Integer> checkHeaderArray = new ArrayList<>();
    ExpandableListAdapter adapter;
    GSKOrangeDB db;
    String categoryName, categoryId, Error_Message = "";
    String path = "", str = "", _pathforcheck = "", img1 = "", img2 = "";
    boolean isDialogOpen = true;
    boolean checkflag = true;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    Uri outputFileUri = null;
    String gallery_package = "";
    private SharedPreferences preferences;
    boolean isExpand = true;

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
            setContentView(R.layout.activity_stock_facing);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            db = new GSKOrangeDB(this);
            db.open();

            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            //txt_stockFacingName = (TextView) findViewById(R.id.txt_stockFacingName);


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

            categoryName = getIntent().getStringExtra("categoryName");
            categoryId = getIntent().getStringExtra("categoryId");

            //txt_stockFacingName.setText(getResources().getString(R.string.title_activity_stock_facing));
            toolbar.setTitle(getResources().getString(R.string.title_activity_stock_facing));
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            prepareList();

            str = CommonString.FILE_PATH + _pathforcheck;

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/

                    if (validateData(hashMapListHeaderData, hashMapListChildData)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Stock_FacingActivity.this);
                        builder.setMessage(getResources().getString(R.string.check_save_message))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();

                                        if (db.checkStockAndFacingData(store_id, categoryId)) {
                                            db.updateStockAndFacing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                            Snackbar.make(view, getResources().getString(R.string.update_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        } else {
                                            db.InsertStock_Facing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                            Snackbar.make(view, getResources().getString(R.string.save_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            //Toast.makeText(getApplicationContext(), "Data has been saved", Toast.LENGTH_LONG).show();
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
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Stock_FacingActivity.this);
                        //builder.setMessage(getResources().getString(R.string.empty_field))
                        builder.setMessage(Error_Message)
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });

            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;

                    if (isExpand) {
                        if (firstVisibleItem == 0) {
                            fab.setVisibility(View.VISIBLE);
                        } else if (lastItem == totalItemCount) {
                            fab.setVisibility(View.INVISIBLE);
                        } else {
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
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

                    if (groupPosition == 0) {
                        isExpand = false;
                    } else {
                        isExpand = true;
                    }

                    fab.setVisibility(View.INVISIBLE);
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

                    if (groupPosition == 0) {
                        isExpand = false;
                    } else {
                        isExpand = true;
                    }
                    fab.setVisibility(View.VISIBLE);
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void prepareList() {
        try {
            hashMapListHeaderData = new ArrayList<>();
            hashMapListChildData = new HashMap<>();

            //Header Data
            headerDataList = db.getStockAndFacingHeader_AfterSaveData(categoryId, store_id);
            if (!(headerDataList.size() > 0)) {
                headerDataList = db.getStockAndFacingHeaderData(categoryId, keyAccount_id, storeType_id, class_id);
            }

            if (headerDataList.size() > 0) {

                for (int i = 0; i < headerDataList.size(); i++) {
                    hashMapListHeaderData.add(headerDataList.get(i));

                    //Child Data
                    childDataList = db.getStockAndFacingSKU_AfterSaveData(categoryId, headerDataList.get(i).getBrand_id(), store_id);
                    if (!(childDataList.size() > 0)) {
                        childDataList = db.getStockAndFacingSKUData(categoryId, headerDataList.get(i).getBrand_id(), keyAccount_id, storeType_id, class_id);
                    }

                    hashMapListChildData.put(hashMapListHeaderData.get(i), childDataList);
                }
            }

            adapter = new ExpandableListAdapter(this, hashMapListHeaderData, hashMapListChildData);
            expandableListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean validateData(List<Stock_FacingGetterSetter> listDataHeader,
                         HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> listDataChild) {
        boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader.size(); i++) {
            String imagePath = listDataHeader.get(i).getImage1();
            String imagePath1 = listDataHeader.get(i).getImage2();

            for (int j = 0; j < listDataChild.get(listDataHeader.get(i)).size(); j++) {
                String stock = listDataChild.get(listDataHeader.get(i)).get(j).getStock();
                String faceup = listDataChild.get(listDataHeader.get(i)).get(j).getFacing();

                //Company_id
                if (listDataChild.get(listDataHeader.get(i)).get(j).getCompany_id().equals("1")) {
                    //Camera allow enable
                    if (camera_allow.equalsIgnoreCase("1")) {

                        //if (!imagePath.equals("") || !imagePath1.equals("")) {
                        if (!stock.equals("0")) {
                            if (!imagePath.equals("") || !imagePath1.equals("")) {
                                if (stock.equals("") || faceup.equals("")) {
                                    if (!checkHeaderArray.contains(i)) {
                                        checkHeaderArray.add(i);
                                    }

                                    flag = false;
                                    Error_Message = getResources().getString(R.string.fill_data);
                                    break;
                                }
                            } else {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }

                                flag = false;
                                Error_Message = getResources().getString(R.string.click_image);
                                break;
                            }
                        } else {
                            if (stock.equals("")) {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }

                                flag = false;
                                Error_Message = getResources().getString(R.string.fill_data);
                                break;
                            }
                        }
                        /*} else {
                            if (!checkHeaderArray.contains(i)) {
                                checkHeaderArray.add(i);
                            }

                            flag = false;
                            Error_Message = getResources().getString(R.string.click_image);
                            break;
                        }*/

                    } else {
                        //Camera allow disable
                        if (!stock.equals("0")) {
                            if (stock.equals("") || faceup.equals("")) {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }

                                flag = false;
                                Error_Message = getResources().getString(R.string.fill_data);
                                break;
                            }
                        } else {
                            if (stock.equals("")) {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }

                                flag = false;
                                Error_Message = getResources().getString(R.string.fill_data);
                                break;
                            }
                        }
                    }
                } else {
                    if (faceup.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        flag = false;
                        Error_Message = getResources().getString(R.string.fill_data);
                        break;
                    }
                }
            }

            if (flag == false) {
                checkflag = false;
                break;
            } else {
                checkflag = true;
            }
        }
        //expListView.invalidate();
        adapter.notifyDataSetChanged();

        return checkflag;
    }

    private void startCameraActivity1(int position) {
        /*try {
            Log.e("Stock & Facing ", "startCameraActivity()");
            File file = new File(path);
            Uri outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    *//*Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);*//*

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

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 1);
            //startActivityForResult(intent, position);

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            Log.e("MakeMachine", "startCameraActivity()");
            File file = new File(path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    //Log.e("TAG", "package name  : " + list.get(n).packageName);

                    //temp value in case camera is gallery app above jellybean
                    if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
                        gallery_package = list.get(n).packageName;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCameraActivity2(int position) {
        /*try {
            Log.i("Stock & Facing ", "startCameraActivity()");
            File file = new File(path);
            Uri outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    *//*Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);*//*

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

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            Log.e("MakeMachine", "startCameraActivity()");
            File file = new File(path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    //Log.e("TAG", "package name  : " + list.get(n).packageName);

                    //temp value in case camera is gallery app above jellybean
                    if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
                        gallery_package = list.get(n).packageName;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Stock & Facing", "resultCode: " + resultCode);
        /*switch (resultCode) {
            case 0:
                Log.e("Stock & Facing", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        img1 = _pathforcheck;
                        adapter.notifyDataSetChanged();
                        _pathforcheck = "";
                    }
                }
                break;
        }*/

        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            img1 = _pathforcheck;
                            adapter.notifyDataSetChanged();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.e("Stock & Facing", "User cancelled");
                }
                break;
            case 2:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            img2 = _pathforcheck;
                            adapter.notifyDataSetChanged();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.e("Stock & Facing", "User cancelled");
                }
                break;
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.planogram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Stock_FacingActivity.this);
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

        //Planogram Dialog
        if (id == R.id.action_planogram) {
            expandableListView.clearFocus();

            //final Dialog dialog = new Dialog(Stock_FacingActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            final Dialog dialog = new Dialog(Stock_FacingActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.planogram_dialog_layout);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            ArrayList<MAPPING_PLANOGRAM_DataGetterSetter> mappingPlanogramList = db.getMappingPlanogramData(categoryId);

            //ImageView img_planogram = (ImageView) dialog.findViewById(R.id.img_planogram);
            WebView webView = (WebView) dialog.findViewById(R.id.webview);
            webView.setWebViewClient(new MyWebViewClient());

            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);

            String planogram_image = "";
            if (mappingPlanogramList.size() > 0) {
                planogram_image = mappingPlanogramList.get(0).getPLANOGRAM_IMAGE();
            }
            if (!planogram_image.equals("")) {
                if (new File(str + planogram_image).exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(str + planogram_image);
                    // img_planogram.setRotation(90);
                    //img_planogram.setImageBitmap(bmp);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    String imagePath = "file://" + CommonString.FILE_PATH + "/" + planogram_image;
                    String html = "<html><head></head><body><img src=\"" + imagePath + "\"></body></html>";
                    webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

                    dialog.show();
                } /*else {
                //webView.loadUrl(String.valueOf(R.drawable.sad_cloud));

                //img_planogram.setBackgroundResource(R.drawable.sad_cloud);
            }*/
            }


            ImageView cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    dialog.dismiss();
                }
            });

            //dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Stock_FacingActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
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
            TextView txt_sosHeader = (TextView) convertView.findViewById(R.id.txt_sosHeader);
            ImageView img_camera1 = (ImageView) convertView.findViewById(R.id.img_camera1);
            ImageView img_camera2 = (ImageView) convertView.findViewById(R.id.img_camera2);
            LinearLayout lin_stockFaceupHeader = (LinearLayout) convertView.findViewById(R.id.lin_stockFaceupHeader);

            txt_stockFaceupHeader.setTypeface(null, Typeface.BOLD);
            txt_stockFaceupHeader.setText(headerTitle.getSub_category() + "-" + headerTitle.getBrand());


            if (headerTitle.getCompany_id().equals("1")) {
                txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                if (!headerTitle.getSos_target().equals("") && !headerTitle.getSos_target().equals("-")) {
                    headerTitle.setSos_target(headerTitle.getSos_target());
                } else if (headerTitle.getSos_target().equals("-")) {
                    headerTitle.setSos_target("-");
                }
                txt_sosHeader.setText(headerTitle.getSos_target().toString());

                txt_sosHeader.setVisibility(View.VISIBLE);
                img_camera1.setVisibility(View.VISIBLE);
                img_camera2.setVisibility(View.VISIBLE);
            } else {
                txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.black));

                txt_sosHeader.setVisibility(View.GONE);
                img_camera1.setVisibility(View.GONE);
                img_camera2.setVisibility(View.GONE);
            }

            //Camera allow enable
            if (camera_allow.equalsIgnoreCase("1")) {

                img_camera1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String date = new Date().toLocaleString().toString();
                        //String tempDate = new Date().toLocaleString().toString().replace(' ', '_').replace(',', '_').replace(':', '-');

                        _pathforcheck = "Stock_Cam1_" + store_id + "_" + headerTitle.getBrand_id()
                                + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                        child_position = groupPosition;
                        path = str + _pathforcheck;

                        startCameraActivity1(groupPosition);
                    }
                });

                if (!img1.equalsIgnoreCase("")) {
                    if (groupPosition == child_position) {
                        headerTitle.setImage1(img1);
                        img1 = "";
                    }
                }

                if (headerTitle.getImage1().equals("")) {
                    img_camera1.setBackgroundResource(R.mipmap.camera_orange);
                } else {
                    img_camera1.setBackgroundResource(R.mipmap.camera_green);
                }


                img_camera2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String date = new Date().toLocaleString().toString();
                        //String tempDate = new Date().toLocaleString().toString().replace(' ', '_').replace(',', '_').replace(':', '-');

                        _pathforcheck = "Stock_Cam2_" + store_id + "_" + headerTitle.getBrand_id()
                                + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                        child_position = groupPosition;
                        path = str + _pathforcheck;

                        startCameraActivity2(groupPosition);
                    }
                });

                if (!img2.equalsIgnoreCase("")) {
                    if (groupPosition == child_position) {
                        headerTitle.setImage2(img2);
                        img2 = "";
                    }
                }

                if (headerTitle.getImage2().equals("")) {
                    img_camera2.setBackgroundResource(R.mipmap.camera_orange);
                } else {
                    img_camera2.setBackgroundResource(R.mipmap.camera_green);
                }
            } else {
                //Camera allow disable
                img_camera1.setBackgroundResource(R.mipmap.camera_grey);
                img_camera2.setBackgroundResource(R.mipmap.camera_grey);
            }

            if (headerTitle.getCompany_id().equals("1")) {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_stockFaceupHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        /*if (headerTitle.getCompany_id().equals("1")) {
                        } else {
                            txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.black));
                        }*/
                    }
                }
            } else {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_stockFaceupHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        /*if (headerTitle.getCompany_id().equals("1")) {
                            txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        } else {*/
                        txt_stockFaceupHeader.setTextColor(getResources().getColor(R.color.black));
                        //}
                    }
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
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            final Stock_FacingGetterSetter childData = (Stock_FacingGetterSetter) getChild(groupPosition, childPosition);
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

            holder.txt_skuName.setText(childData.getSku());

            if (childData.getCompany_id().equals("1")) {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                holder.ed_stock.setVisibility(View.VISIBLE);
            } else {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.black));
                holder.ed_stock.setVisibility(View.GONE);
            }

            if (childData.getStock().equals("0")) {
                if (childData.getCompany_id().equals("1")) {
                    holder.ed_facing.setEnabled(false);
                } else {
                    holder.ed_facing.setEnabled(true);
                }
            } else {
                holder.ed_facing.setEnabled(true);
            }

            final ViewHolder finalHolder = holder;
            holder.ed_stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    final EditText caption = (EditText) v;
                    String edStock = caption.getText().toString();

                    if (!edStock.equals("")) {
                        String stock = edStock.replaceFirst("^0+(?!$)", "");
                        childData.setStock(stock);

                        if (stock.equals("0")) {
                            childData.setFacing("0");
                            finalHolder.ed_facing.setText("0");

                            finalHolder.ed_facing.setEnabled(false);
                        } else {
                            childData.setFacing(childData.getFacing());
                            finalHolder.ed_facing.setEnabled(true);
                        }
                    } else {
                        childData.setStock("");
                        childData.setFacing("");
                        finalHolder.ed_facing.setEnabled(true);
                    }
                }
            });

           /* holder.ed_stock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //final EditText caption = (EditText) v;
                    String edStock = s.toString();

                    if (!edStock.equals("")) {
                        String stock = edStock.replaceFirst("^0+(?!$)", "");
                        childData.setStock(stock);

                        if (edStock.equals("0")) {
                            if (childData.getCompany_id().equals("1")) {
                                childData.setFacing("0");
                            }
                            finalHolder.ed_facing.setEnabled(false);
                        } else {
                            childData.setFacing(childData.getFacing());
                            finalHolder.ed_facing.setEnabled(true);
                        }
                    } else {
                        childData.setStock("");
                        childData.setFacing("");
                        finalHolder.ed_facing.setEnabled(true);
                    }
                    //expandableListView.invalidateViews();
                }
            });*/

            holder.ed_stock.setText(childData.getStock());

            holder.ed_facing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    final String edFaceup = caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    if (childData.getCompany_id().equals("1")) {
                        if (!childData.getStock().equals("")) {
                            if (!edFaceup.equals("")) {
                                if (Integer.parseInt(edFaceup) <= Integer.parseInt(childData.getStock())) {
                                    childData.setFacing(edFaceup);
                                } else {
                                    if (isDialogOpen) {
                                        isDialogOpen = !isDialogOpen;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Stock_FacingActivity.this);
                                        builder.setMessage(getString(R.string.check_faceup))
                                                .setCancelable(false)
                                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                                childData.setFacing("");
                            }
                        } else {
                            if (isDialogOpen) {
                                isDialogOpen = !isDialogOpen;
                                AlertDialog.Builder builder = new AlertDialog.Builder(Stock_FacingActivity.this);
                                builder.setMessage(getString(R.string.fill_stock_value))
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
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
                        childData.setFacing(edFaceup);
                        childData.setStock("0");
                    }
                }
            });

            holder.ed_facing.setText(childData.getFacing());

            if (!checkflag) {
                boolean tempflag = false;

                if (childData.getCompany_id().equals("1")) {
                    if (holder.ed_stock.getText().toString().equals("")) {
                        holder.ed_stock.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.ed_stock.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        holder.ed_stock.setHint(getString(R.string.empty));
                        tempflag = true;
                    }

                    if (holder.ed_facing.getText().toString().equals("")) {
                        holder.ed_facing.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.ed_facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        holder.ed_facing.setHint(getString(R.string.empty));
                        tempflag = true;
                    }

                    if (tempflag) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                } else {
                    if (holder.ed_facing.getText().toString().equals("")) {
                        holder.ed_facing.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.ed_facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        holder.ed_facing.setHint(getString(R.string.empty));
                        tempflag = true;
                    }

                    if (tempflag) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
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
