package cpm.com.gskmtorange.gsk_dailyentry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.StockFacing_PlanogramTrackerDataGetterSetter;

public class MSL_Availability_StockFacingActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    TextView txt_mslAvailabilityName;

    ArrayList<MSL_AvailabilityStockFacingGetterSetter> headerDataList;
    ArrayList<MSL_AvailabilityStockFacingGetterSetter> childDataList;
    List<MSL_AvailabilityStockFacingGetterSetter> hashMapListHeaderData;
    HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> hashMapListChildData;

    List<Integer> checkHeaderArray = new ArrayList<>();
    boolean checkflag = true;

    ExpandableListAdapter adapter;
    ImageView camera1, camera2, camera3, camera4;
    LinearLayout lin_camera1, lin_camera2, lin_camera3, lin_camera4;

    GSKOrangeDB db;

    String categoryName, categoryId, storeId, Error_Message = "";
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    boolean isDialogOpen = true;
    private SharedPreferences preferences;

    String str = "", _pathforcheck = "";

    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> planogramShelfHeaderDataList = new ArrayList<>();
    ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> planogramSkuChildDataList;
    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> planogramHashMapListChildData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_msl__availability_stock_facing);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            txt_mslAvailabilityName = (TextView) findViewById(R.id.txt_mslAvailabilityName);

            camera1 = (ImageView) findViewById(R.id.img_camera1);
            camera2 = (ImageView) findViewById(R.id.img_camera2);
            camera3 = (ImageView) findViewById(R.id.img_camera3);
            camera4 = (ImageView) findViewById(R.id.img_camera4);

            lin_camera1 = (LinearLayout) findViewById(R.id.lin_camera1);
            lin_camera2 = (LinearLayout) findViewById(R.id.lin_camera2);
            lin_camera3 = (LinearLayout) findViewById(R.id.lin_camera3);
            lin_camera4 = (LinearLayout) findViewById(R.id.lin_camera4);

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

            //txt_mslAvailabilityName.setText(getResources().getString(R.string.title_activity_msl__availability));
            toolbar.setTitle(getResources().getString(R.string.title_activity_msl__availability));
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            str = CommonString.FILE_PATH ;

            prepareList();

            //Camera
            prepareDefaultList();

            cameraMethod();

            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    expandableListView.clearFocus();

                    if (validateData(hashMapListHeaderData, hashMapListChildData)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                        builder.setMessage(getResources().getString(R.string.check_save_message))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.open();

                                        if (db.checkMsl_Availability_StockFacingData(store_id, categoryId)) {
                                            db.updateMSL_Availability_StockFacing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
                                            Snackbar.make(view, getResources().getString(R.string.update_message), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        } else {
                                            db.InsertMSL_Availability_StockFacing(store_id, categoryId, hashMapListHeaderData, hashMapListChildData);
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
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                        builder.setMessage(Error_Message)
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
            });

            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;

                    if (firstVisibleItem == 0) {
                        fab.setVisibility(View.VISIBLE);
                    } else if (lastItem == totalItemCount) {
                        fab.setVisibility(View.INVISIBLE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
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

                    //expandableListView.invalidateViews();
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.planogram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
            builder.setTitle(getResources().getString(R.string.dialog_title));
            builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (!validateData(hashMapListHeaderData, hashMapListChildData)) {
                                if (!camera_allow.equals("1")) {
                                    db.deletePlanogramListStoreAndCategorywise(store_id, categoryId,
                                            planogramShelfHeaderDataList, planogramHashMapListChildData);
                                }
                            }
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            //finish();
        }

        //Planogram Dialog
        if (id == R.id.action_planogram) {
            expandableListView.clearFocus();

            //final Dialog dialog = new Dialog(Stock_FacingActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            final Dialog dialog = new Dialog(MSL_Availability_StockFacingActivity.this);
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

    private void cameraMethod() {
        /*cameraData = new Store_wise_camera_DataGetterSetter();

        if (db.isStorewiseCameraSave(store_id, categoryId)) {
            cameraData = db.getStore_wise_camera(store_id, categoryId);
        } else {
            cameraData.setStore_id(store_id);
            cameraData.setCategory_id(categoryId);
            cameraData.setCamera1("");
            cameraData.setCamera2("");
            cameraData.setCamera3("");
            cameraData.setCamera4("");
            cameraData.setCheckSaveStatus("0");
        }


        if (camera_allow.equals("1")) {

            findViewById(R.id.view_camera2).setVisibility(View.VISIBLE);
            findViewById(R.id.view_camera3).setVisibility(View.VISIBLE);

            if (cameraData.getCamera1().equals("")) {
                camera1.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                camera1.setBackgroundResource(R.mipmap.camera_green);
            }

            if (cameraData.getCamera2().equals("")) {
                camera2.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                camera2.setBackgroundResource(R.mipmap.camera_green);
            }

            if (cameraData.getCamera3().equals("")) {
                camera3.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                camera3.setBackgroundResource(R.mipmap.camera_green);
            }

            if (cameraData.getCamera4().equals("")) {
                camera4.setBackgroundResource(R.mipmap.camera_orange);
            } else {
                camera4.setBackgroundResource(R.mipmap.camera_green);
            }


            lin_camera1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck = "Stock_Camera1_" + store_id + "_" + categoryId
                            + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    path = str + _pathforcheck;

                    startCameraActivity(3);
                }
            });

            lin_camera2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck = "Stock_Camera2_" + store_id + "_" + categoryId
                            + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    path = str + _pathforcheck;

                    startCameraActivity(4);
                }
            });

            lin_camera3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck = "Stock_Camera3_" + store_id + "_" + categoryId
                            + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    path = str + _pathforcheck;

                    startCameraActivity(5);
                }
            });

            lin_camera4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _pathforcheck = "Stock_Camera4_" + store_id + "_" + categoryId
                            + "_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    path = str + _pathforcheck;

                    startCameraActivity(6);
                }
            });

        } else {*/

        if (!camera_allow.equals("1")) {
            findViewById(R.id.lin_camera).setVisibility(View.VISIBLE);

            findViewById(R.id.view_camera2).setVisibility(View.GONE);
            findViewById(R.id.view_camera3).setVisibility(View.GONE);

            lin_camera2.setVisibility(View.GONE);
            lin_camera3.setVisibility(View.GONE);
            lin_camera4.setVisibility(View.GONE);

            if (db.isPlanogramAddShelfSaveData(store_id, categoryId)) {
                camera1.setBackgroundResource(R.mipmap.new_no_camera_done_edit);
            } else {
                camera1.setBackgroundResource(R.mipmap.new_no_camera);
            }

            lin_camera1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MSL_Availability_StockFacingActivity.this,
                            StockFacing_PlanogramTrackerActivity.class);

                    intent.putExtra("storeId", store_id);
                    intent.putExtra("keyAccount_id", keyAccount_id);
                    intent.putExtra("class_id", class_id);
                    intent.putExtra("storeType_id", storeType_id);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("categoryName", categoryName);

                    startActivityForResult(intent, 100);
                }

            });
        } else {
            findViewById(R.id.lin_camera).setVisibility(View.GONE);
        }
    }

    //Planogram List for check and delete on backPress
    private void prepareDefaultList() {
        // Planogram After save shelf header data
        planogramShelfHeaderDataList = db.getPlanogramAddShelfHeaderAfterSaveData(store_id, categoryId);

        if (planogramShelfHeaderDataList.size() > 0) {

            for (int i = 0; i < planogramShelfHeaderDataList.size(); i++) {
                planogramSkuChildDataList = db.getStockAndFacingPlanogramAfterSKUData(planogramShelfHeaderDataList.get(i).getKey_id());

                //After save sku child data
                if (planogramSkuChildDataList.size() > 0) {
                    planogramHashMapListChildData.put(planogramShelfHeaderDataList.get(i), planogramSkuChildDataList);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }

    private void prepareList() {
        try {
            hashMapListHeaderData = new ArrayList<>();
            hashMapListChildData = new HashMap<>();

            //Header
            headerDataList = db.getMSL_Availability_StockFacingHeaderData(categoryId, keyAccount_id, storeType_id, class_id);

            if (headerDataList.size() > 0) {
                for (int i = 0; i < headerDataList.size(); i++) {
                    hashMapListHeaderData.add(headerDataList.get(i));

                    //childDataList = new ArrayList<>();
                    childDataList = db.getMSL_Availability_StockFacingSKU_AfterSaveData(categoryId, headerDataList.get(i).getBrand_id(), store_id);
                    if (!(childDataList.size() > 0)) {
                        childDataList = db.getMSL_Availability_StockFacingSKUData(categoryId, headerDataList.get(i).getBrand_id(), keyAccount_id, storeType_id, class_id);
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

    boolean validateData(List<MSL_AvailabilityStockFacingGetterSetter> listDataHeader,
                         HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> listDataChild) {
        boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < listDataHeader.size(); i++) {

            for (int j = 0; j < listDataChild.get(listDataHeader.get(i)).size(); j++) {
                MSL_AvailabilityStockFacingGetterSetter data = listDataChild.get(listDataHeader.get(i)).get(j);

                String stock = data.getStock();
                String faceup = data.getFacing();

                //Company_id
                if (listDataChild.get(listDataHeader.get(i)).get(j).getCompany_id().equals("1")) {

                    if (!camera_allow.equalsIgnoreCase("1")) {
                        if (!(planogramShelfHeaderDataList.size() > 0)) {
                            flag = false;
                            Error_Message = getResources().getString(R.string.stock_planogram_data_noCamera_data);
                            break;
                        }
                    }

                    if (faceup.equals("")) {
                        if (!checkHeaderArray.contains(i)) {
                            checkHeaderArray.add(i);
                        }

                        flag = false;
                        Error_Message = getResources().getString(R.string.faceup_value);
                        break;
                    } else {
                        if (data.getToggleValue().equals("0")) {

                            if (stock.equals("")) {
                                if (!checkHeaderArray.contains(i)) {
                                    checkHeaderArray.add(i);
                                }
                                flag = false;
                                Error_Message = getResources().getString(R.string.stock_value);
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
                        Error_Message = getResources().getString(R.string.faceup_value);
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

        adapter.notifyDataSetChanged();

        return checkflag;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
        builder.setTitle(getResources().getString(R.string.dialog_title));
        builder.setMessage(getResources().getString(R.string.data_will_be_lost)).setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!validateData(hashMapListHeaderData, hashMapListChildData)) {

                            if (!camera_allow.equals("1")) {
                                db.deletePlanogramListStoreAndCategorywise(store_id, categoryId,
                                        planogramShelfHeaderDataList, planogramHashMapListChildData);
                            }
                        }
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<MSL_AvailabilityStockFacingGetterSetter> _listDataHeader;
        private HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<MSL_AvailabilityStockFacingGetterSetter> listDataHeader,
                                     HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> listChildData) {
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
            MSL_AvailabilityStockFacingGetterSetter headerTitle = (MSL_AvailabilityStockFacingGetterSetter) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_stock_facing_header, null, false);
            }

            TextView txt_categoryHeader = (TextView) convertView.findViewById(R.id.txt_categoryHeader);
            RelativeLayout rel_header = (RelativeLayout) convertView.findViewById(R.id.rel_categoryHeader);
            ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);

            txt_categoryHeader.setTypeface(null, Typeface.BOLD);

            if (headerTitle.getCompany_id().equals("1")) {
                txt_categoryHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                txt_categoryHeader.setTextColor(getResources().getColor(R.color.black));
            }
            txt_categoryHeader.setText(headerTitle.getSub_category() + "-" + headerTitle.getBrand());


            //empty check color change
            if (headerTitle.getCompany_id().equals("1")) {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_categoryHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_categoryHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            } else {
                if (!checkflag) {
                    if (checkHeaderArray.contains(groupPosition)) {
                        txt_categoryHeader.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        txt_categoryHeader.setTextColor(getResources().getColor(R.color.black));
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

            final MSL_AvailabilityStockFacingGetterSetter childData =
                    (MSL_AvailabilityStockFacingGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_msl_availability_stock_facing_child, null, false);

                holder = new ViewHolder();
                holder.cardView = (CardView) convertView.findViewById(R.id.card_view);
                holder.lin_category = (LinearLayout) convertView.findViewById(R.id.lin_category);

                holder.txt_skuName = (TextView) convertView.findViewById(R.id.txt_skuName);
                holder.txt_mbq = (TextView) convertView.findViewById(R.id.txt_mbq);
                holder.toggle_available = (ToggleButton) convertView.findViewById(R.id.toggle_available);

                holder.facing = (EditText) convertView.findViewById(R.id.ed_facing);
                holder.stock = (EditText) convertView.findViewById(R.id.ed_stock);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt_skuName.setText(childData.getSku());
            holder.txt_mbq.setText(childData.getMbq());

            if (childData.getCompany_id().equals("1")) {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.txt_mbq.setVisibility(View.VISIBLE);
                holder.toggle_available.setVisibility(View.VISIBLE);
                holder.facing.setVisibility(View.VISIBLE);

                if (childData.getToggleValue().equals("1")) {
                    holder.stock.setVisibility(View.GONE);
                } else {
                    holder.stock.setVisibility(View.VISIBLE);
                }
            } else {
                holder.txt_skuName.setTextColor(getResources().getColor(R.color.black));
                holder.txt_mbq.setVisibility(View.GONE);
                holder.toggle_available.setVisibility(View.GONE);
                holder.facing.setVisibility(View.VISIBLE);
                holder.stock.setVisibility(View.GONE);
            }


            final ViewHolder finalHolder = holder;
            holder.toggle_available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        childData.setToggleValue("1");
                        finalHolder.stock.setVisibility(View.GONE);
                        /*childData.setStock("");
                        childData.setFacing("");*/
                    } else {
                        /*childData.setStock("");
                        childData.setFacing("");*/
                        childData.setToggleValue("0");
                        finalHolder.stock.setVisibility(View.VISIBLE);

                        //If MBQ Value is 0
                        if (Integer.parseInt(childData.getMbq()) == 0) {
                            childData.setStock("0");
                            childData.setFacing("0");
                        }/* else {
                            if (finalHolder.facing.getText().toString() != null && !finalHolder.facing.getText().toString().equals("")) {
                                if (Integer.parseInt(finalHolder.facing.getText().toString()) >= 0
                                        && Integer.parseInt(finalHolder.facing.getText().toString()) >= Integer.parseInt(childData.getMbq())) {
                                    childData.setFacing("");
                                    finalHolder.facing.setText("");
                                }
                            }
                        }*/
                    }

                    expandableListView.clearFocus();
                    expandableListView.invalidateViews();
                }
            });

            if (childData.getToggleValue().equals("1")) {
                holder.toggle_available.setChecked(true);
                finalHolder.stock.setVisibility(View.GONE);
                childData.setStock("");
            } else {
                holder.toggle_available.setChecked(false);
                finalHolder.stock.setVisibility(View.VISIBLE);
            }


            holder.stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    String edStock = caption.getText().toString();

                    /*if (!childData.getFacing().equals("") && Integer.parseInt(stock) >= 0 && Integer.parseInt(childData.getFacing()) < Integer.parseInt(stock)) {

                    }*/

                    if (!edStock.equals("")) {
                        String stock = edStock.replaceFirst("^0+(?!$)", "");

                        if (Integer.parseInt(stock) >= 0 && Integer.parseInt(stock) < Integer.parseInt(childData.getMbq())) {

                            if (!childData.getFacing().equals("")) {
                                if (Integer.parseInt(stock) >= 0 && Integer.parseInt(childData.getFacing()) <= Integer.parseInt(stock)) {

                                    childData.setStock(stock);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                                    builder.setMessage(getString(R.string.check_faceup))
                                            .setCancelable(false)
                                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finalHolder.stock.setText("");
                                                    dialog.dismiss();

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } else {
                                childData.setStock(stock);
                            }
                        } else {
                            if (isDialogOpen) {
                                isDialogOpen = !isDialogOpen;
                                AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                                builder.setMessage(getString(R.string.check_stock))
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finalHolder.stock.setText("");
                                                dialog.dismiss();
                                                isDialogOpen = !isDialogOpen;
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    } else {
                        childData.setStock("");
                    }
                }
            });
            holder.stock.setText(childData.getStock());


            final ViewHolder finalHolder1 = holder;
            holder.facing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText caption = (EditText) v;
                    final String edFaceup = caption.getText().toString().replaceFirst("^0+(?!$)", "");

                    boolean isFill = false;

                    //Toggle is no selected
                    if (childData.getToggleValue().equals("0")) {

                        //if stock is emplty
                        /*if (childData.getStock().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                            builder.setMessage(getString(R.string.msl_availability_new_stock_value))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {*/
                        if (edFaceup != null && !edFaceup.equals("")) {

                            if (Integer.parseInt(edFaceup) >= 0 && Integer.parseInt(edFaceup) < Integer.parseInt(childData.getMbq())) {
                                if (!childData.getStock().equals("")) {

                                    //if (edFaceup != null && !edFaceup.equals("")) {

                                    String faceup = edFaceup.replaceFirst("^0+(?!$)", "");


                                    if (Integer.parseInt(faceup) <= Integer.parseInt(childData.getStock())) {

                                        isFill = true;

                          /*          if (!edFaceup.equals("")) {
                                        childData.setFacing(faceup);
                                    } else {
                                        childData.setFacing("");
                                    }*/
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                                        builder.setMessage(getString(R.string.check_faceup))
                                                .setCancelable(false)
                                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finalHolder1.facing.setText("");
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    //  }
                                } else {
                                    isFill = true;
                                }
                            } else {
                                if (isDialogOpen) {
                                    isDialogOpen = !isDialogOpen;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MSL_Availability_StockFacingActivity.this);
                                    builder.setMessage(getString(R.string.check_faceing))
                                            .setCancelable(false)
                                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finalHolder.facing.setText("");
                                                    dialog.dismiss();
                                                    isDialogOpen = !isDialogOpen;
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }
                        //}
                    } else {
                        isFill = true;

                        /*if (!edFaceup.equals("")) {
                            String faceup = edFaceup.replaceFirst("^0+(?!$)", "");
                            childData.setFacing(faceup);
                        } else {
                            childData.setFacing("");
                        }*/
                    }

                    if (isFill) {
                        if (!edFaceup.equals("")) {
                            String faceup = edFaceup.replaceFirst("^0+(?!$)", "");
                            childData.setFacing(faceup);
                        } else {
                            childData.setFacing("");
                        }
                    }
                }
            });

            holder.facing.setText(childData.getFacing());


            //empty check color change
            if (!checkflag) {
                boolean tempflag = false;

                if (childData.getCompany_id().equals("1")) {
                    if (childData.getToggleValue().equals("0")) {
                        if (holder.stock.getText().toString().equals("")) {
                            holder.stock.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.stock.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.stock.setHint(getString(R.string.empty));
                            tempflag = true;
                        }

                        if (holder.facing.getText().toString().equals("")) {
                            holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.facing.setHint(getString(R.string.empty));
                            tempflag = true;
                        }
                    } else {
                        if (holder.facing.getText().toString().equals("")) {
                            holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            holder.facing.setHint(getString(R.string.empty));
                            tempflag = true;
                        }
                    }

                    if (tempflag) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                } else {
                    if (holder.facing.getText().toString().equals("")) {
                        holder.facing.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.facing.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        holder.facing.setHint(getString(R.string.empty));
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
        CardView cardView;
        TextView txt_skuName, txt_mbq;
        ToggleButton toggle_available;
        LinearLayout lin_category;
        EditText facing, stock;
    }

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

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("MSL_AVailability_StockFacing", "resultCode: " + resultCode);

        switch (requestCode) {
            case 100:
                //Planogram List for check and delete on backPress
                prepareDefaultList();
                if (db.isPlanogramAddShelfSaveData(store_id, categoryId)) {
                    camera1.setBackgroundResource(R.mipmap.new_no_camera_done_edit);
                } else {
                    camera1.setBackgroundResource(R.mipmap.new_no_camera);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
