package cpm.com.gskmtorange.dailyentry;

import android.app.Activity;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import cpm.com.gskmtorange.GetterSetter.BrandAvabilityGetterSetter;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.GapsChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.T2PGetterSetter;

public class T2PComplianceActivity extends AppCompatActivity {

    String gallery_package = "";
    Uri outputFileUri;

    GSKOrangeDB db;
    ArrayList<T2PGetterSetter> t2PGetterSetters;
    T2PAdapter t2PAdapter;
    RecyclerView rec_t2p;
    MyAdaptorStock adapterData;
    String categoryName, categoryId;
    String store_id, visit_date, username, intime, date, keyAccount_id, class_id, storeType_id, camera_allow;
    String str = CommonString.FILE_PATH,
            path = "", path1 = "", path2 = "",
            _pathforcheck = "", _pathforcheck1 = "", _pathforcheck2 = "",
            img = "", img1 = "", img2 = "";
    int child_position = -1, child_position1 = -1, child_position2 = -1;
    String error_msg;
    private SharedPreferences preferences;
    Spinner spinner_brand;
    Button btn_add, btn_close;
    ListView listview;
    LinearLayout linearlay;
    //CardView cardlay;
    String brand_name = "", brand_id = "";
    ArrayList<BrandAvabilityGetterSetter> brand_new_list = new ArrayList<BrandAvabilityGetterSetter>();
    ArrayList<BrandAvabilityGetterSetter> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2_pcompliance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Intent data
        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getStringExtra("categoryId");
        // toolbar.setTitle(R.string.title_activity_t2_pcompliance + " - " + categoryName);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new GSKOrangeDB(T2PComplianceActivity.this);
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

        rec_t2p = (RecyclerView) findViewById(R.id.rec_t2p);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            T2PComplianceActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(getResources().getString(R.string.title_activity_Want_save))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.InsertT2PData(t2PGetterSetters, store_id, categoryId);
                                    //Snackbar.make(view, "Data Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                    Snackbar.make(view, error_msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        t2PGetterSetters = db.getT2pComplianceData(store_id, categoryId);

        if (t2PGetterSetters.size() == 0) {
            t2PGetterSetters = db.getT2PDefaultData(store_id);
        } else {
            for (int i = 0; i < t2PGetterSetters.size(); i++) {

                ArrayList<GapsChecklistGetterSetter> gapsList = db.getGapsData(t2PGetterSetters.get(i).getKey_id());
                ArrayList<SkuGetterSetter> skuList = db.getT2PSKUData(t2PGetterSetters.get(i).getKey_id());
                ArrayList<BrandAvabilityGetterSetter> brandList = db.getT2BrandData(t2PGetterSetters.get(i).getKey_id());

                t2PGetterSetters.get(i).setGapsChecklist(gapsList);
                t2PGetterSetters.get(i).setSkulist(skuList);
                t2PGetterSetters.get(i).setBrandlist(brandList);
            }

        }

        if (t2PGetterSetters.size() > 0) {

            rec_t2p.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            t2PAdapter = new T2PAdapter(t2PGetterSetters);
            rec_t2p.setAdapter(t2PAdapter);

        }

        rec_t2p.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }


    public class T2PAdapter extends RecyclerView.Adapter<T2PAdapter.ViewHolder> {

        private ArrayList<T2PGetterSetter> list;

        public T2PAdapter(ArrayList<T2PGetterSetter> t2PList) {
            list = t2PList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.t2p_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final T2PGetterSetter mItem = list.get(position);
            holder.tv_brand.setText(mItem.getBrand());
            holder.tv_display.setText(mItem.getDisplay().trim());

            //holder.tv_display.setTypeface(FontManager.getTypeface(getApplicationContext(),FontManager.FONTAWESOME));

           /* Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);
*/


            holder.btn_gaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGapsDialog(mItem);
                }
            });

            holder.toggle_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (((ToggleButton) v).getText().toString().equalsIgnoreCase(getResources().getString(R.string.yes))) {
                        mItem.setPresent(true);
                    } else {
                      /*  mItem.setPresent(false);
                        mItem.getGapsChecklist().clear();
                        mItem.getSkulist().clear();

                        //Camera
                        if (!mItem.getImage().equals("")) {
                            new File(str + mItem.getImage()).delete();
                            mItem.setImage("");
                        }

                        //Camera 1
                        if (!mItem.getImage1().equals("")) {
                            new File(str + mItem.getImage1()).delete();
                            mItem.setImage1("");
                        }

                        //Camera 2
                        if (!mItem.getImage2().equals("")) {
                            new File(str + mItem.getImage2()).delete();
                            mItem.setImage2("");
                        }
*/

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(T2PComplianceActivity.this);
                        alertDialogBuilder.setTitle(getResources().getString(R.string.dialog_title));

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(getResources().getString(R.string.data_will_be_lost))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mItem.setPresent(false);
                                        mItem.getGapsChecklist().clear();
                                        mItem.getSkulist().clear();

                                        //Camera
                                        if (!mItem.getImage().equals("")) {
                                            new File(str + mItem.getImage()).delete();
                                            mItem.setImage("");
                                        }

                                        //Camera 1
                                        if (!mItem.getImage1().equals("")) {
                                            new File(str + mItem.getImage1()).delete();
                                            mItem.setImage1("");
                                        }

                                        //Camera 2
                                        if (!mItem.getImage2().equals("")) {
                                            new File(str + mItem.getImage2()).delete();
                                            mItem.setImage2("");
                                        }

                                        t2PAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                        mItem.setPresent(true);
                                        t2PAdapter.notifyDataSetChanged();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    t2PAdapter.notifyDataSetChanged();
                }
            });


            holder.btn_sku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSkuDialog(mItem.getSkulist());
                }
            });

            //Camera
            if (!img.equalsIgnoreCase("")) {
                if (position == child_position) {
                    mItem.setImage(img);
                    img = "";
                }
            }

            //Camera 1
            if (!img1.equalsIgnoreCase("")) {
                if (position == child_position1) {
                    mItem.setImage1(img1);
                    img1 = "";
                }
            }

            //Camera 2
            if (!img2.equalsIgnoreCase("")) {
                if (position == child_position2) {
                    mItem.setImage2(img2);
                    img2 = "";
                }
            }

            holder.btn_ref_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPlanogram(mItem.getRef_image_url());
                }
            });


            if (camera_allow.equals("1")) {

                //Camera
                holder.img_cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _pathforcheck = "T2P_Image_" + store_id + categoryId + "_" + mItem.getBrand_id() +
                                mItem.getDisplay_id() + visit_date.replace("/", "") + "_" +
                                getCurrentTime().replace(":", "") + ".jpg";
                        child_position = position;
                        path = str + _pathforcheck;

                        startCameraActivity(1);
                    }
                });

                if (mItem.getImage().equals("")) {


                    if (mItem.isPresent()) {

                        holder.img_cam.setBackgroundResource(R.mipmap.camera_orange);
                    } else {
                        //if not present camera disabled
                        holder.img_cam.setBackgroundResource(R.mipmap.camera_grey);
                    }

                } else {
                    holder.img_cam.setBackgroundResource(R.mipmap.camera_green);
                }

                //Camera 1
                holder.img_cam1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _pathforcheck1 = "T2P_Image1_" + store_id + categoryId + "_" + mItem.getBrand_id() +
                                mItem.getDisplay_id() + visit_date.replace("/", "") + "_" +
                                getCurrentTime().replace(":", "") + ".jpg";
                        child_position1 = position;
                        path = str + _pathforcheck1;

                        startCameraActivity(2);
                    }
                });

                if (mItem.getImage1().equals("")) {
                    if (mItem.isPresent()) {

                        holder.img_cam1.setVisibility(View.VISIBLE);
                        holder.img_cam1.setBackgroundResource(R.mipmap.camera_orange);
                    } else {
                        //if not present camera disabled
                        holder.img_cam1.setBackgroundResource(R.mipmap.camera_grey);
                    }

                } else {
                    holder.img_cam1.setBackgroundResource(R.mipmap.camera_green);
                }

                //Camera 2
                holder.img_cam2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _pathforcheck2 = "T2P_Image2_" + store_id + categoryId + "_" + mItem.getBrand_id() +
                                mItem.getDisplay_id() + visit_date.replace("/", "") + "_" +
                                getCurrentTime().replace(":", "") + ".jpg";
                        child_position2 = position;
                        path = str + _pathforcheck2;

                        startCameraActivity(3);
                    }
                });

                if (mItem.getImage2().equals("")) {
                    if (mItem.isPresent()) {

                        holder.img_cam2.setVisibility(View.VISIBLE);
                        holder.img_cam2.setBackgroundResource(R.mipmap.camera_orange);
                    } else {
                        //if not present camera disabled
                        holder.img_cam2.setBackgroundResource(R.mipmap.camera_grey);
                    }

                } else {
                    holder.img_cam2.setBackgroundResource(R.mipmap.camera_green);
                }
            } else {
                // holder.img_cam.setBackgroundResource(R.mipmap.camera_grey);
                // holder.img_cam1.setBackgroundResource(R.mipmap.camera_grey);
                // holder.img_cam2.setBackgroundResource(R.mipmap.camera_grey);
                holder.img_cam1.setVisibility(View.INVISIBLE);
                holder.img_cam2.setVisibility(View.INVISIBLE);

                holder.img_cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   /* Intent in =new Intent(T2PComplianceActivity.this,T2pBrand_Avaibility.class);
                        in.putExtra("categoryName", categoryName);
                        in.putExtra("categoryId", categoryId);
                        startActivity(in);*/

                        showBrandAvabilitydialog(mItem.getBrandlist());
                    }
                });

                if (mItem.getBrandlist().size() > 0) {
                    holder.img_cam.setBackgroundResource(R.mipmap.new_no_camera_done_edit);
                } else {

                    if (mItem.isPresent()) {
                        holder.img_cam.setBackgroundResource(R.mipmap.new_no_camera_edit);
                    } else {
                        //if not present camera disabled
                        holder.img_cam.setBackgroundResource(R.mipmap.no_camera);
                    }
                }
            }

            boolean is_enabled = mItem.isPresent();

            holder.toggle_btn.setChecked(is_enabled);
            holder.img_cam.setEnabled(is_enabled);
            holder.img_cam1.setEnabled(is_enabled);
            holder.img_cam2.setEnabled(is_enabled);
            holder.btn_gaps.setEnabled(is_enabled);
            holder.btn_sku.setEnabled(is_enabled);


            if (mItem.getGapsChecklist().size() > 0) {
                holder.btn_gaps.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                if (mItem.isPresent()) {
                    holder.btn_gaps.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.btn_gaps.setBackgroundColor(getResources().getColor(R.color.grey_background));
                }
            }

            if (mItem.getSkulist().size() > 0) {
                holder.btn_sku.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                if (mItem.isPresent()) {
                    holder.btn_sku.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.btn_sku.setBackgroundColor(getResources().getColor(R.color.grey_background));
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_brand, tv_display;
            public final ImageView img_cam;
            public ImageView img_cam1, img_cam2;
            //public ImageView img_remark;
            public final Button btn_gaps, btn_sku, btn_ref_img;
            public final ToggleButton toggle_btn;

            public ViewHolder(View view) {
                super(view);

                mView = view;
                tv_brand = (TextView) mView.findViewById(R.id.tv_brand);
                tv_display = (TextView) mView.findViewById(R.id.tv_display);
                img_cam = (ImageView) mView.findViewById(R.id.img_cam);
                img_cam1 = (ImageView) mView.findViewById(R.id.img_cam1);
                img_cam2 = (ImageView) mView.findViewById(R.id.img_cam2);
                // img_remark = (ImageView) mView.findViewById(R.id.img_remark);
                btn_gaps = (Button) mView.findViewById(R.id.btn_gaps);
                btn_sku = (Button) mView.findViewById(R.id.btn_sku);
                btn_ref_img = (Button) mView.findViewById(R.id.btn_ref_image);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);
                toggle_btn = (ToggleButton) mView.findViewById(R.id.toggle_btn);
            }
        }
    }


    public void showGapsDialog(final T2PGetterSetter t2p) {

        final ArrayList<GapsChecklistGetterSetter> gapsChecklist;

        if (t2p.getGapsChecklist().size() > 0) {

            gapsChecklist = t2p.getGapsChecklist();
        } else {
            gapsChecklist = db.getGapsDefaultData(t2p.getDisplay_id());
        }

        final Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.gaps_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        RecyclerView rec_gap_checklist = (RecyclerView) dialog.findViewById(R.id.rec_gap_checklist);
        rec_gap_checklist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        GapsAdapter gapAdapter = new GapsAdapter(gapsChecklist);
        rec_gap_checklist.setAdapter(gapAdapter);

        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t2p.setGapsChecklist(gapsChecklist);
                dialog.cancel();
                t2PAdapter.notifyDataSetChanged();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    public void showSkuDialog(final ArrayList<SkuGetterSetter> skuAddedList) {

        final SkuGetterSetter[] sku_selected = new SkuGetterSetter[1];
        final BrandMasterGetterSetter[] brand_selected = new BrandMasterGetterSetter[1];

        final ArrayList<BrandMasterGetterSetter> brandList = db.getBrandT2PData(storeType_id, class_id, keyAccount_id, categoryId);
        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();
        brand.setBRAND("select");
        brandList.add(0, brand);
        // ArrayList<SkuMasterGetterSetter> skuMasterGetterSetterArrayList = db.getSkuT2PData("1", "1", "1",)

        final Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.t2p_sku_dialog_layout);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        final Spinner spinner_brand = (Spinner) dialog.findViewById(R.id.spinner_brand);
        final Spinner spinner_sku = (Spinner) dialog.findViewById(R.id.spinner_sku);
        Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final EditText et_stock = (EditText) dialog.findViewById(R.id.et_stock);
        final RecyclerView rec_sku = (RecyclerView) dialog.findViewById(R.id.rec_sku);

        final ArrayList<SkuGetterSetter> sku_list = new ArrayList<>();

        if (skuAddedList.size() > 0) {

            rec_sku.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            SkuAddedAdapter skuAdapter = new SkuAddedAdapter(skuAddedList);
            rec_sku.setAdapter(skuAdapter);

        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (brand_selected[0] == null || sku_selected[0] == null || et_stock.getText().toString().equals("")) {

                    Snackbar.make(v, getResources().getString(R.string.enter_the_values), Snackbar.LENGTH_SHORT).show();
                } else {
                    SkuGetterSetter sku = new SkuGetterSetter();
                    sku.setBRAND_ID(brand_selected[0].getBRAND_ID().get(0));
                    sku.setBRAND(brand_selected[0].getBRAND().get(0));
                    sku.setSKU(sku_selected[0].getSKU());
                    sku.setSKU_ID(sku_selected[0].getSKU_ID());
                    sku.setSTOCK(et_stock.getText().toString().replaceFirst("^0+(?!$)", ""));

                    skuAddedList.add(sku);

                    rec_sku.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    SkuAddedAdapter skuAdapter = new SkuAddedAdapter(skuAddedList);
                    rec_sku.setAdapter(skuAdapter);

                    et_stock.setText("");
                    spinner_brand.setSelection(0);

                    SkuGetterSetter select = new SkuGetterSetter();
                    select.setSKU(getString(R.string.select));
                    sku_list.clear();
                    sku_list.add(select);
                    CustomSkuAdapter skuadapter = new CustomSkuAdapter(T2PComplianceActivity.this, R.layout.custom_spinner_item, sku_list);
                    spinner_sku.setAdapter(skuadapter);

                    spinner_sku.setSelection(0);

                    brand_selected[0] = null;
                    sku_selected[0] = null;
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
                t2PAdapter.notifyDataSetChanged();
            }
        });

        // Create custom adapter object ( see below CustomAdapter.java )
        CustomAdapter adapter = new CustomAdapter(T2PComplianceActivity.this, R.layout.custom_spinner_item, brandList);
        // Set adapter to spinner
        spinner_brand.setAdapter(adapter);


        SkuGetterSetter select = new SkuGetterSetter();
        select.setSKU(getString(R.string.select));
        sku_list.add(select);
        CustomSkuAdapter skuadapter = new CustomSkuAdapter(T2PComplianceActivity.this, R.layout.custom_spinner_item, sku_list);
        spinner_sku.setAdapter(skuadapter);


        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    sku_list.clear();

                    brand_selected[0] = brandList.get(position);

                    String brand_id = brandList.get(position).getBRAND_ID().get(0);

                    ArrayList<SkuGetterSetter> temp_list = db.getSkuT2PData(storeType_id, class_id, keyAccount_id, brand_id);

                    for (int k = 0; k < temp_list.size(); k++) {
                        sku_list.add(temp_list.get(k));
                    }

                    SkuGetterSetter select = new SkuGetterSetter();
                    select.setSKU(getString(R.string.select));
                    sku_list.add(0, select);
                    // Create custom adapter object ( see below CustomSkuAdapter.java )
                    CustomSkuAdapter skuadapter = new CustomSkuAdapter(T2PComplianceActivity.this, R.layout.custom_spinner_item, sku_list);
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

                    sku_selected[0] = sku_list.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(T2PComplianceActivity.this);
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
        }

        return cdate;
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
                  /*  Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);*/

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
            //startActivityForResult(intent, 1);
            startActivityForResult(intent, pos);
            //startActivityForResult(intent, position);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            //startActivityForResult(intent, 0);
            startActivityForResult(intent, pos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Stock & Facing", "resultCode: " + resultCode + " requestCode: " + requestCode);
        /*switch (resultCode) {
            case 0:
                Log.e("Stock & Facing", "User cancelled");

                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        img = _pathforcheck;
                        t2PAdapter.notifyDataSetChanged();
                        _pathforcheck = "";
                    }
                }
                break;
        }*/

        switch (requestCode) {
            case 1:
                if (resultCode == 0) {
                    Log.e("Stock & Facing", "User cancelled");
                } else if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(str + _pathforcheck).exists()) {
                            img = _pathforcheck;
                            t2PAdapter.notifyDataSetChanged();
                            _pathforcheck = "";
                        }
                    }
                }
                break;
            case 2:
                if (resultCode == 0) {
                    Log.e("Stock & Facing", "User cancelled");
                } else if (resultCode == -1) {
                    if (_pathforcheck1 != null && !_pathforcheck1.equals("")) {
                        if (new File(str + _pathforcheck1).exists()) {
                            img1 = _pathforcheck1;
                            t2PAdapter.notifyDataSetChanged();
                            _pathforcheck1 = "";
                        }
                    }
                }
                break;

            case 3:
                if (resultCode == 0) {
                    Log.e("Stock & Facing", "User cancelled");
                } else if (resultCode == -1) {
                    if (_pathforcheck2 != null && !_pathforcheck2.equals("")) {
                        if (new File(str + _pathforcheck2).exists()) {
                            img2 = _pathforcheck2;
                            t2PAdapter.notifyDataSetChanged();
                            _pathforcheck2 = "";
                        }
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isValid() {
        boolean flag = true;

        for (int i = 0; i < t2PGetterSetters.size(); i++) {

            if (t2PGetterSetters.get(i).isPresent()) {
                //if (camera_allow.equals("1") && t2PGetterSetters.get(i).getImage().equals("")) {
                if (t2PGetterSetters.get(i).getGapsChecklist().size() == 0) {
                    flag = false;
                    error_msg = getResources().getString(R.string.fill_gaps_data);
                    break;
                } else if (t2PGetterSetters.get(i).getSkulist().size() == 0) {
                    flag = false;
                    error_msg = getResources().getString(R.string.title_activity_fill_sku);
                    break;
                } else if (camera_allow.equals("1") && (t2PGetterSetters.get(i).getImage().equals("") &&
                        t2PGetterSetters.get(i).getImage1().equals("") &&
                        t2PGetterSetters.get(i).getImage2().equals(""))) {
                    flag = false;
                    error_msg = getResources().getString(R.string.click_image);
                    break;
                } else if (camera_allow.equals("0") && t2PGetterSetters.get(i).getBrandlist().size() == 0) {
                    flag = false;
                    error_msg = getResources().getString(R.string.title_activity_fill_brand);
                    break;
                }

            }

        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(T2PComplianceActivity.this);
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


    public class GapsAdapter extends RecyclerView.Adapter<GapsAdapter.ViewHolder> {

        private ArrayList<GapsChecklistGetterSetter> list;

        public GapsAdapter(ArrayList<GapsChecklistGetterSetter> gapsPList) {
            list = gapsPList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gap_checklist_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final GapsChecklistGetterSetter mItem = list.get(position);
            holder.tv_checklist.setText(mItem.getChecklist());

            holder.tb_present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mItem.setPresent(((ToggleButton) v).getText().toString().equalsIgnoreCase("Yes"));

                }
            });

            holder.tb_present.setChecked(mItem.isPresent());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_checklist;
            public final ToggleButton tb_present;


            public ViewHolder(View view) {
                super(view);

                mView = view;

                tv_checklist = (TextView) mView.findViewById(R.id.tv_checklist);
                tb_present = (ToggleButton) mView.findViewById(R.id.btn_is_present);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);

            }

        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        BrandMasterGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomAdapter(
                T2PComplianceActivity activitySpinner,
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
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getBRAND().get(0));
            }

            return row;
        }
    }

    public class CustomSkuAdapter extends ArrayAdapter<String> {

        SkuGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomSkuAdapter(
                T2PComplianceActivity activitySpinner,
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
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getSKU());
            }

            return row;
        }
    }

    public class SkuAddedAdapter extends RecyclerView.Adapter<SkuAddedAdapter.ViewHolder> {

        private ArrayList<SkuGetterSetter> list;

        public SkuAddedAdapter(ArrayList<SkuGetterSetter> skuList) {
            list = skuList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sku_added_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final SkuGetterSetter mItem = list.get(position);
            holder.tv_brand.setText(mItem.getBRAND());
            holder.tv_sku.setText(mItem.getSKU().trim());
            holder.tv_stock.setText(mItem.getSTOCK());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final LinearLayout parentLayout;
            public final TextView tv_brand, tv_sku, tv_stock;

            public ViewHolder(View view) {
                super(view);

                mView = view;

                tv_brand = (TextView) mView.findViewById(R.id.tv_brand);
                tv_sku = (TextView) mView.findViewById(R.id.tv_sku);
                tv_stock = (TextView) mView.findViewById(R.id.tv_stock);
                parentLayout = (LinearLayout) mView.findViewById(R.id.parent_layout);

            }

        }
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

    public void showPlanogram(String planogram_image) {

        final Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.planogram_dialog_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        //ArrayList<MAPPING_PLANOGRAM_DataGetterSetter> mp = db.getMappingPlanogramData("");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        WebView webView = (WebView) dialog.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        //String planogram_image = mp.get(0).getPLANOGRAM_IMAGE();
        if (new File(str + planogram_image).exists()) {

            String imagePath = "file://" + CommonString.FILE_PATH + "/" + planogram_image;
            //String imagePath = "file://" + CommonString.FILE_PATH + "/" + "image_ref.png";
            String html = "<html><head></head><body><img src=\"" + imagePath + "\"></body></html>";
            webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

            dialog.show();
        }

        ImageView cancel = (ImageView) dialog.findViewById(R.id.img_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dialog.dismiss();
            }
        });
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


    //// new <code></code>


    public void showBrandAvabilitydialog(final ArrayList<BrandAvabilityGetterSetter> brandGetdata) {

        final ArrayList<BrandAvabilityGetterSetter> brandList = db.getBrandAvailbilitydata(store_id, categoryId, keyAccount_id, class_id, storeType_id);

        BrandAvabilityGetterSetter brand = new BrandAvabilityGetterSetter();
        brand.setBRAND(getResources().getString(R.string.select));
        brandList.add(0, brand);

        final Dialog dialog = new Dialog(T2PComplianceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.t2pbranddialoglayout);
        dialog.setCancelable(false);
        //pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        //dialog.setCancelable(false);
        spinner_brand = (Spinner) dialog.findViewById(R.id.spinner_brand);

        btn_add = (Button) dialog.findViewById(R.id.btn_add);
        btn_close = (Button) dialog.findViewById(R.id.btn_cancel);

        listview = (ListView) dialog.findViewById(R.id.lv);
        linearlay = (LinearLayout) dialog.findViewById(R.id.list_layout);
        // cardlay = (CardView) dialog.findViewById(R.id.cardId);

        // Create custom adapter object ( see below CustomAdapter.java )
        T2PComplianceActivity.CustomBRANDAdapter adapter = new T2PComplianceActivity.CustomBRANDAdapter(T2PComplianceActivity.this, R.layout.custom_spinner_item, brandList);

        spinner_brand.setAdapter(adapter);

        if (brandGetdata.size() > 0) {
            linearlay.setVisibility(View.VISIBLE);
            // cardlay.setVisibility(View.VISIBLE);
            adapterData = new T2PComplianceActivity.MyAdaptorStock(T2PComplianceActivity.this, brandGetdata);
            listview.setAdapter(adapterData);
            listview.invalidateViews();
        } else {
            linearlay.setVisibility(View.GONE);
            // cardlay.setVisibility(View.GONE);
        }

        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    brand_name = brandList.get(position).getBRAND();
                    brand_id = brandList.get(position).getBRAND_ID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //brandGetdata.add(brand_new_list);
                dialog.cancel();
                t2PAdapter.notifyDataSetChanged();

            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandAvabilityGetterSetter ab = new BrandAvabilityGetterSetter();

                if (!brand_name.equalsIgnoreCase("") && brand_name != null) {

                    BrandAvabilityGetterSetter brand = new BrandAvabilityGetterSetter();

                    brand.setBRAND(brand_name);
                    brand.setBRAND_ID(brand_id);

                    brandGetdata.add(brand);

                    adapterData = new T2PComplianceActivity.MyAdaptorStock(T2PComplianceActivity.this, brandGetdata);
                    listview.setAdapter(adapterData);
                    listview.invalidateViews();

                    // cardlay.setVisibility(View.VISIBLE);
                    linearlay.setVisibility(View.VISIBLE);
                    spinner_brand.setSelection(0);
                    brand_name = "";
                    brand_id = "";

                } else {
                    Snackbar.make(v, "Please select dropdown", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        dialog.show();

    }


    public class MyAdaptorStock extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mcontext;
        private ArrayList<BrandAvabilityGetterSetter> list;

        public MyAdaptorStock(Activity activity, ArrayList<BrandAvabilityGetterSetter> list1) {

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
            TextView brand;


        }

        @Override
        public View getView(final int position1, View convertView, ViewGroup parent) {

            final T2PComplianceActivity.MyAdaptorStock.ViewHolder holder;

            if (convertView == null) {

                convertView = mInflater
                        .inflate(R.layout.brandavabilityadpterlayout, null);
                holder = new T2PComplianceActivity.MyAdaptorStock.ViewHolder();

                holder.brand = (TextView) convertView.findViewById(R.id.brand_name);

                convertView.setTag(holder);
            } else {
                holder = (T2PComplianceActivity.MyAdaptorStock.ViewHolder) convertView.getTag();
            }

            holder.brand.setText(list.get(position1).getBRAND());

            holder.brand.setId(position1);


            return convertView;
        }
    }

    public class CustomBRANDAdapter extends ArrayAdapter<String> {

        BrandAvabilityGetterSetter tempValues = null;
        LayoutInflater inflater;
        private Activity activity;
        private ArrayList data;

        /*************
         * CustomAdapter Constructor
         *****************/
        public CustomBRANDAdapter(
                T2PComplianceActivity activitySpinner,
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
            tempValues = (BrandAvabilityGetterSetter) data.get(position);

            TextView label = (TextView) row.findViewById(R.id.tv_text);

            if (position == 0) {

                // Default selected Spinner item
                label.setText(getString(R.string.select));
                //sub.setText("");
            } else {
                // Set values for spinner each row
                label.setText(tempValues.getBRAND());
            }

            return row;
        }
    }


}
