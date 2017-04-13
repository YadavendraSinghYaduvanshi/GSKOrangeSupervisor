package cpm.com.gskmtorange.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.GetterSetter.AdditionalDialogGetterSetter;
import cpm.com.gskmtorange.GetterSetter.AddittionalGetterSetter;
import cpm.com.gskmtorange.GetterSetter.BrandAvabilityGetterSetter;
import cpm.com.gskmtorange.GetterSetter.CategoryPictureGetterSetter;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.GeotaggingBeans;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.ADDITIONAL_DISPLAY_MASTERGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryWisePerformaceGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayChecklistMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.GapsChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_SOS_TARGET_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityStockFacingGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingDisplayChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPromotionGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingSubCategoryImageAllowGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NonWorkingReasonGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Promo_Compliance_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.STORE_PERFORMANCE_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.ShelfMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.StockFacing_PlanogramTrackerDataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.StoreWisePerformaceGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Store_wise_camera_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SubCategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.T2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;

/**
 * Created by ashishc on 29-12-2016.
 */

public class GSKOrangeDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GSK_ORANGE_DB3";
    public static final int DATABASE_VERSION = 13;
    TableBean tableBean;
    private SQLiteDatabase db;
    Context context;
    ArrayList<T2PGetterSetter> t2PGetterSetters;

    public GSKOrangeDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(TableBean.getJourneyPlan());

            //Gagan Start
            db.execSQL(TableBean.getBrandMaster());
            db.execSQL(TableBean.getSkuMaster());
            db.execSQL(TableBean.getCategoryMaster());
            db.execSQL(TableBean.getSubCategoryMaster());
            db.execSQL(TableBean.getDisplayMaster());
            db.execSQL(TableBean.getMappingStock());
            db.execSQL(TableBean.getMappingT2p());
            db.execSQL(TableBean.getNonWorkingReason());
            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
            db.execSQL(TableBean.getDisplayChecklistMaster());
            db.execSQL(TableBean.getMappingDisplayChecklist());
            db.execSQL(TableBean.getMappingAdditionalPromotion());
            db.execSQL(TableBean.getMappingPromotion());

            db.execSQL(CommonString.CREATE_TABLE_INSERT_MSL_AVAILABILITY);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_ADDITIONAL_VISIBILITY);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_HEADER);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_CHILD);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_DIALOG);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_ADDITIONAL_STOCK_DATA);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_PROMO_SKU);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_ADDITIONAL_VISIBILITY_MAIN);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_DIALOG_MAIN);

            db.execSQL(TableBean.getStorePerformance());

            //Gagan End

            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);

            db.execSQL(TableBean.getDisplayChecklistMaster());
            db.execSQL(TableBean.getMappingDisplayChecklist());

            db.execSQL(TableBean.getNonWorkingReason());

            db.execSQL(CommonString.CREATE_TABLE_INSERT_T2P_COMPLIANCE);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_T2P_GAPS);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_T2P_SKU);

            db.execSQL(TableBean.getMappingPlanogram());
            db.execSQL(TableBean.getAdditionalDisplay());

            db.execSQL(TableBean.getMappingSosTarget());

            db.execSQL(CommonString.CREATE_TABLE_INSERT_BRAND_AVAIBILITY_DATA);

            db.execSQL(TableBean.getShelfMaster());

            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_HEADER);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_CHILD);

            db.execSQL(CommonString.CREATE_TABLE_INSERT_STORE_CAMERA);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_CATEGORY_PICTURE_LIST);

            db.execSQL(CommonString.CREATE_TABLE_INSERT_CATEGORY_PICTURE);

            db.execSQL(TableBean.getMappingSubCategoryImageAllow());

            //15-03-2017
            db.execSQL(CommonString.CREATE_TABLE_INSERT_MSL_AVAILABILITY_STOCK_FACING);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error -" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableBean.getJourneyPlan());
    }

    public void deleteTableWithStoreID(String storeid) {

        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);

        //Gagan start code
        db.delete(CommonString.TABLE_INSERT_MSL_AVAILABILITY, "Store_Id='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_HEADER, "Store_Id='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, "Store_Id='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE, "STORE_ID='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_PROMO_SKU, "STORE_ID='" + storeid + "'", null);
        //Gagan end code

        t2PGetterSetters = getT2pCompliancedaletedata(storeid);

        for (int q = 0; q < t2PGetterSetters.size(); q++) {

            db.delete(CommonString.TABLE_INSERT_T2P_GAPS, "COMMON_ID='" + t2PGetterSetters.get(q).getKey_id() + "'", null);
            db.delete(CommonString.TABLE_INSERT_T2P_SKU, "COMMON_ID='" + t2PGetterSetters.get(q).getKey_id() + "'", null);
            db.delete(CommonString.TABLE_INSERT_BRAND_AVAIBILITY_DATA, "COMMON_ID='" + t2PGetterSetters.get(q).getKey_id() + "'", null);

        }

        db.delete(CommonString.TABLE_INSERT_T2P_COMPLIANCE, "STORE_ID='" + storeid + "'", null);

        db.delete(CommonString.TABLE_INSERT_CATEGORY_PICTURE, "Store_Id='" + storeid + "'", null);

        db.delete(CommonString.TABLE_INSERT_CATEGORY_PICTURE_LIST, "Store_Id='" + storeid + "'", null);

        db.delete(CommonString.TABLE_INSERT_MSL_AVAILABILITY_STOCK_FACING, "Store_Id='" + storeid + "'", null);
    }

    public void deleteAllTables() {

        db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);

        db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_DATA, null, null);

        //Gagan start code
        db.delete(CommonString.TABLE_INSERT_MSL_AVAILABILITY, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_HEADER, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, null, null);
        db.delete(CommonString.TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE, null, null);
        db.delete(CommonString.TABLE_INSERT_PROMO_SKU, null, null);
        //Gagan end code

        db.delete(CommonString.TABLE_INSERT_T2P_COMPLIANCE, null, null);

        db.delete(CommonString.TABLE_INSERT_T2P_GAPS, null, null);
        db.delete(CommonString.TABLE_INSERT_T2P_SKU, null, null);
        db.delete(CommonString.TABLE_INSERT_BRAND_AVAIBILITY_DATA, null, null);

        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_HEADER, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_CHILD, null, null);

        db.delete(CommonString.TABLE_INSERT_CATEGORY_PICTURE, null, null);
        db.delete(CommonString.TABLE_INSERT_CATEGORY_PICTURE_LIST, null, null);
        db.delete(CommonString.TABLE_INSERT_MSL_AVAILABILITY_STOCK_FACING, null, null);
    }

    public void InsertJCP(JourneyPlanGetterSetter data) {
        db.delete("JOURNEY_PLAN", null, null);

        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("EMP_ID", data.getEMP_ID().get(i));

                values.put("STORE_NAME", data.getSTORE_NAME().get(i));

                values.put("CITY", data.getCITY().get(i));

                values.put("VISIT_DATE", data.getVISIT_DATE().get(i));

                values.put("ADDRESS", data.getADDRESS().get(i));

                values.put("UPLOAD_STATUS", data.getUPLOAD_STATUS().get(i));

                values.put("STORETYPE", data.getSTORETYPE().get(i));

                values.put("KEYACCOUNT_ID", data.getKEYACCOUNT_ID().get(i));


                values.put("STORETYPE_ID", data.getSTORETYPE_ID().get(i));

                values.put("CHECKOUT_STATUS", data.getCHECKOUT_STATUS().get(i));

                values.put("CLASSIFICATION", data.getCLASSIFICATION().get(i));

                values.put("KEYACCOUNT", data.getKEYACCOUNT().get(i));
                values.put("CLASS_ID", data.getCLASS_ID().get(i));

                values.put("CAMERA_ALLOW", data.getCAMERA_ALLOW().get(i));
                values.put("GEO_TAG", data.getGEO_TAG().get(i));


                db.insert("JOURNEY_PLAN", null, values);

            }

        } catch (Exception ex) {
            Log.d("Exception in JCP", ex.toString());
        }
    }

    public ArrayList<T2PGetterSetter> getT2PDefaultData(String store_id) {

        ArrayList<T2PGetterSetter> t2PList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select  BM.BRAND As BRAND, BM.BRAND_ID As BRAND_ID, DM.DISPLAY As DISPLAY, DM.DISPLAY_ID As DISPLAY_ID, DM.IMAGE_URL As IMAGE_URL, DM.IMAGE_PATH As IMAGE_PATH from BRAND_MASTER BM INNER JOIN MAPPING_T2P T ON BM.BRAND_ID = T.BRAND_ID INNER JOIN  DISPLAY_MASTER DM  ON T.DISPLAY_ID= DM.DISPLAY_ID WHERE T.STORE_ID = '" + store_id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    T2PGetterSetter t2p = new T2PGetterSetter();

                    t2p.setBrand_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_ID")));

                    t2p.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    t2p.setDisplay_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DISPLAY_ID")));

                    t2p.setDisplay(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DISPLAY")));

                    t2p.setRef_image_url(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("IMAGE_URL")));

                    t2p.setRef_image_path(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("IMAGE_PATH")));

                    t2p.setImage("");
                    t2p.setImage1("");
                    t2p.setImage2("");

                    t2p.setRemark("");

                    t2p.isPresent();

                    t2PList.add(t2p);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return t2PList;
            }
        } catch (Exception e) {

            Log.d("Exception get T2P", e.toString());
            return t2PList;
        }

        return t2PList;
    }

    //get Gaps data
    public ArrayList<GapsChecklistGetterSetter> getGapsDefaultData(String display_id) {

        ArrayList<GapsChecklistGetterSetter> checkList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select DC.CHECKLIST_ID As CHECKLIST_ID, DC.CHECKLIST As CHECKLIST, C.DISPLAY_ID As DISPLAY_ID from MAPPING_DISPLAY_CHECKLIST C INNER JOIN DISPLAY_CHECKLIST_MASTER DC ON C.CHECKLIST_ID= DC.CHECKLIST_ID WHERE C.DISPLAY_ID = '" + display_id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    GapsChecklistGetterSetter check = new GapsChecklistGetterSetter();

                    check.setChecklist_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKLIST_ID")));

                    check.setChecklist(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKLIST")));

                    check.setDisplay_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DISPLAY_ID")));

                    check.isPresent();

                    checkList.add(check);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return checkList;
            }
        } catch (Exception e) {

            Log.d("Exception get T2P", e.toString());
            return checkList;
        }

        return checkList;
    }

    //get Brand data for T2P
    public ArrayList<BrandMasterGetterSetter> getBrandT2PData(String store_type_id, String class_id, String key_account_id, String category_id) {

        ArrayList<BrandMasterGetterSetter> brandList = new ArrayList<>();
        Cursor dbcursor = null;

        try {


            dbcursor = db.rawQuery("SELECT * FROM(SELECT DISTINCT BR.BRAND_ID, SCM.SUB_CATEGORY||'-'||BR.BRAND AS BRAND FROM MAPPING_STOCK MS INNER JOIN SKU_MASTER SM ON MS.SKU_ID = SM.SKU_ID  INNER JOIN BRAND_MASTER BR ON SM.BRAND_ID=BR.BRAND_ID INNER JOIN SUB_CATEGORY_MASTER SCM ON  BR.SUB_CATEGORY_ID = SCM.SUB_CATEGORY_ID WHERE MS.KEYACCOUNT_ID ='" + key_account_id + "' AND STORETYPE_ID ='" + store_type_id + "' AND CLASS_ID = '" + class_id + "' AND BR.COMPANY_ID ='1' AND SCM.CATEGORY_ID  = '" + category_id + "' ORDER BY  SCM.SUB_CATEGORY_SEQUENCE, BR.BRAND_SEQUENCE ) As Brand", null);
            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    BrandMasterGetterSetter brand = new BrandMasterGetterSetter();

                    brand.setBRAND(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    brand.setBRAND_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_ID")));


                    brandList.add(brand);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return brandList;
            }
        } catch (Exception e) {

            Log.d("Exception get T2P", e.toString());
            return brandList;
        }

        return brandList;
    }

    //get Sku data for T2P
    public ArrayList<SkuGetterSetter> getSkuT2PData(String store_type_id, String class_id, String key_account_id, String brand_id) {

        ArrayList<SkuGetterSetter> skuList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SM.SKU, SM.SKU_ID, BR.BRAND_ID FROM MAPPING_STOCK MS INNER JOIN SKU_MASTER SM ON MS.SKU_ID = SM.SKU_ID  INNER JOIN BRAND_MASTER BR ON SM.BRAND_ID=BR.BRAND_ID INNER JOIN SUB_CATEGORY_MASTER SCM ON  BR.SUB_CATEGORY_ID = SCM.SUB_CATEGORY_ID WHERE MS.KEYACCOUNT_ID ='" + key_account_id + "' AND STORETYPE_ID ='" + store_type_id + "' AND CLASS_ID = '" + class_id + "' AND SM.BRAND_ID='" + brand_id + "'", null);
            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    SkuGetterSetter sku = new SkuGetterSetter();

                    sku.setSKU(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU")));

                    sku.setBRAND_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_ID")));

                    sku.setSKU_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("SKU_ID")));

                    skuList.add(sku);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return skuList;
            }
        } catch (Exception e) {

            Log.d("Exception get T2P", e.toString());
            return skuList;
        }

        return skuList;
    }

    public ArrayList<StoreBean> getStoreData(String date) {
        ArrayList<StoreBean> list = new ArrayList<StoreBean>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from JOURNEY_PLAN  " +
                    "where VISIT_DATE ='" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreBean sb = new StoreBean();

                    sb.setSTORE_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_ID")));

                    sb.setEMP_ID((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("EMP_ID"))));

                    sb.setKEYACCOUNT(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("KEYACCOUNT")));

                    sb.setSTORE_NAME(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_NAME")));

                    sb.setADDRESS((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ADDRESS"))));
                    sb.setCITY(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CITY")));

                    sb.setSTORETYPE(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORETYPE")));

                    sb.setCLASSIFICATION(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CLASSIFICATION")));

                    sb.setKEYACCOUNT_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("KEYACCOUNT_ID")));

                    sb.setSTORETYPE_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORETYPE_ID")));

                    sb.setCLASS_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CLASS_ID")));

                    sb.setVISIT_DATE(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("VISIT_DATE")));

                    sb.setCAMERA_ALLOW(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CAMERA_ALLOW")));

                    sb.setUPLOAD_STATUS(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("UPLOAD_STATUS")));
                    sb.setCHECKOUT_STATUS(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKOUT_STATUS")));

                    sb.setGEO_TAG(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("GEO_TAG")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }


    //insert MAPPING_DISPLAY_CHECKLIST
    public void InsertMappingDisplayChecklist(MappingDisplayChecklistGetterSetter data) {
        db.delete("MAPPING_DISPLAY_CHECKLIST", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getCHECKLIST_ID().size(); i++) {

                values.put("DISPLAY_ID", data.getDISPLAY_ID().get(i));
                values.put("CHECKLIST_ID", data.getCHECKLIST_ID().get(i));

                db.insert("MAPPING_DISPLAY_CHECKLIST", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_STOCK " + ex.toString());
        }
    }


    //insert DISPLAY_CHECKLIST_MASTER
    public void InsertDisplayChecklistMaster(DisplayChecklistMasterGetterSetter data) {
        db.delete("DISPLAY_CHECKLIST_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getCHECKLIST_ID().size(); i++) {

                values.put("CHECKLIST_ID", data.getCHECKLIST_ID().get(i));
                values.put("CHECKLIST", data.getCHECKLIST().get(i));

                db.insert("DISPLAY_CHECKLIST_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in DISPLAY_CHECKLIST_MASTER " + ex.toString());
        }
    }

    public void InsertDisplayMaster(DisplayMasterGetterSetter data) {
        db.delete("DISPLAY_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getDISPLAY_ID().size(); i++) {

                values.put("DISPLAY_ID", data.getDISPLAY_ID().get(i));
                values.put("DISPLAY", data.getDISPLAY().get(i));
                values.put("IMAGE_URL", data.getIMAGE_URL().get(i));
                values.put("IMAGE_PATH", data.getIMAGE_PATH().get(i));

                db.insert("DISPLAY_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in DISPLAY_MASTER " + ex.toString());
        }
    }


    public void InsertSTOREgeotag(String storeid, double lat, double longitude, String path, String status) {

        ContentValues values = new ContentValues();

        try {

            values.put("STORE_ID", storeid);
            values.put("LATITUDE", Double.toString(lat));
            values.put("LONGITUDE", Double.toString(longitude));
            values.put("FRONT_IMAGE", path);
            values.put("GEO_TAG", status);
            values.put("STATUS", status);

            db.insert(CommonString.TABLE_STORE_GEOTAGGING, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
        }

    }

    public void updateStatus(String id, String status, double lat, double longtitude) {

        ContentValues values = new ContentValues();

        try {

            values.put("GEO_TAG", status);

            db.update(CommonString.KEY_JOURNEY_PLAN, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);

        } catch (Exception ex) {

        }

    }


    public void updateCheckoutStatus(String id, String status) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            values.put("CHECKOUT_STATUS", status);
            values1.put(CommonString.KEY_COVERAGE_STATUS, status);

            db.update(CommonString.TABLE_COVERAGE_DATA, values1, CommonString.KEY_STORE_ID + "='" + id + "'", null);
            db.update(CommonString.KEY_JOURNEY_PLAN, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            Log.e("Exception", "checkOut Status" + ex.toString());
        }
    }


    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String status) {


        ArrayList<GeotaggingBeans> geodata = new ArrayList<GeotaggingBeans>();

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_STORE_GEOTAGGING + "  WHERE GEO_TAG = '" + status + "'", null);

            if (dbcursor != null) {
                int numrows = dbcursor.getCount();

                dbcursor.moveToFirst();
                for (int i = 1; i <= numrows; ++i) {

                    GeotaggingBeans data = new GeotaggingBeans();

                    data.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    data.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow("LATITUDE"))));
                    data.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow("LONGITUDE"))));
                    data.setUrl1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));

                    geodata.add(data);
                    dbcursor.moveToNext();
                }

                dbcursor.close();

            }

        } catch (Exception e) {

        } finally {
            if (dbcursor != null && !dbcursor.isClosed()) {
                dbcursor.close();
            }
        }


        return geodata;

    }


    public void updateGeoTagData(String storeid, String status) {

        try {

            ContentValues values = new ContentValues();
            values.put("GEO_TAG", status);

            int l = db.update(CommonString.TABLE_STORE_GEOTAGGING, values,
                    CommonString.KEY_STORE_ID + "=?", new String[]{storeid});
            System.out.println("update : " + l);
        } catch (Exception e) {
            Log.d("Database Data ", e.toString());

        }
    }

    public void updateDataStatus(String id, String status) {

        ContentValues values = new ContentValues();

        try {

            values.put("GEO_TAG", status);

            db.update(CommonString.KEY_JOURNEY_PLAN, values,
                    CommonString.KEY_STORE_ID + "='" + id + "'", null);

        } catch (Exception ex) {

        }

    }


    public void deleteGeoTagData(String storeid) {

        try {
            db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        } catch (Exception e) {

        }
    }

    public ArrayList<CoverageBean> getCoverageData(String visitdate) {

        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where "
                            + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'",
                    null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setReason((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));
                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    if (dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor
                                .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }
                    sb.setCheckOut_Image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }
        return list;
    }

    //Gagan Start Method

    public void InsertCategory(CategoryMasterGetterSetter data) {
        db.delete("CATEGORY_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getCATEGORY_ID().size(); i++) {

                values.put("CATEGORY_ID", data.getCATEGORY_ID().get(i));
                values.put("CATEGORY", data.getCATEGORY().get(i));
                values.put("CATEGORY_SEQUENCE", data.getCATEGORY_SEQUENCE().get(i));

                db.insert("CATEGORY_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in CATEGORY_MASTER " + ex.toString());
        }
    }

    public void InsertSubCategoryMaster(SubCategoryMasterGetterSetter data) {
        db.delete("SUB_CATEGORY_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSUB_CATEGORY_ID().size(); i++) {

                values.put("SUB_CATEGORY_ID", data.getSUB_CATEGORY_ID().get(i));
                values.put("SUB_CATEGORY", data.getSUB_CATEGORY().get(i));
                values.put("CATEGORY_ID", data.getCATEGORY_ID().get(i));
                values.put("SUB_CATEGORY_SEQUENCE", data.getSUB_CATEGORY_SEQUENCE().get(i));

                db.insert("SUB_CATEGORY_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_STOCK " + ex.toString());
        }
    }

    public void InsertBrandMaster(BrandMasterGetterSetter data) {
        db.delete("BRAND_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getBRAND_ID().size(); i++) {

                values.put("BRAND_ID", data.getBRAND_ID().get(i));
                values.put("BRAND", data.getBRAND().get(i));
                values.put("SUB_CATEGORY_ID", data.getSUB_CATEGORY_ID().get(i));
                values.put("COMPANY_ID", data.getCOMPANY_ID().get(i));
                values.put("BRAND_SEQUENCE", data.getBRAND_SEQUENCE().get(i));

                db.insert("BRAND_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in BRAND_MASTER " + ex.toString());
        }
    }

    public void InsertSkuMaster(SkuMasterGetterSetter data) {
        db.delete("SKU_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSKU_ID().size(); i++) {

                values.put("SKU_ID", data.getSKU_ID().get(i));
                values.put("SKU", data.getSKU().get(i));
                values.put("BRAND_ID", data.getBRAND_ID().get(i));
                values.put("MRP", data.getMRP().get(i));
                values.put("SKU_SEQUENCE", data.getSKU_SEQUENCE().get(i));

                db.insert("SKU_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in SKU_MASTER " + ex.toString());
        }
    }

    public void InsertMappingStock(MappingStockGetterSetter data) {
        db.delete("MAPPING_STOCK", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getKEYACCOUNT_ID().size(); i++) {

                values.put("KEYACCOUNT_ID", data.getKEYACCOUNT_ID().get(i));
                values.put("STORETYPE_ID", data.getSTORETYPE_ID().get(i));
                values.put("CLASS_ID", data.getCLASS_ID().get(i));
                values.put("SKU_ID", data.getSKU_ID().get(i));
                values.put("MUST_HAVE", data.getMUST_HAVE().get(i));
                values.put("MBQ", data.getMBQ().get(i));

                db.insert("MAPPING_STOCK", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_STOCK " + ex.toString());
        }
    }

    public void InsertMAPPING_T2P(MAPPINGT2PGetterSetter data) {
        db.delete("MAPPING_T2P", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("BRAND_ID", data.getBRAND_ID().get(i));
                values.put("DISPLAY_ID", data.getDISPLAY_ID().get(i));

                db.insert("MAPPING_T2P", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_T2P " + ex.toString());
        }
    }


    //Category List
    public ArrayList<CategoryGetterSetter> getCategoryListData(String keyAccountId, String storeTypeId, String classId) {
        ArrayList<CategoryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT CA.CATEGORY_ID,CA.CATEGORY " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where M.KEYACCOUNT_ID='" + keyAccountId + "' AND " +
                    "M.STORETYPE_ID='" + storeTypeId + "' AND " +
                    "M.CLASS_ID='" + classId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CategoryGetterSetter cd = new CategoryGetterSetter();

                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    cd.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY")));
                    cd.setCategory_img(-1);

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }
        return list;
    }

    //MSL_Availability
    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilityHeaderData(String category_id, String keyAccount_id, String storeType_id, String class_id) {
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where   M.MUST_HAVE=1 AND CA.CATEGORY_ID='" + category_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id +
                    "' AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "'" +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityGetterSetter cd = new MSL_AvailabilityGetterSetter();

                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilitySKUData(String category_id, String brand_id, String keyAccount_id, String storeType_id, String class_id) {
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where M.MUST_HAVE=1 AND " +
                    "CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityGetterSetter cd = new MSL_AvailabilityGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue("1");

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public void InsertMSL_Availability(String storeId, String categoryId, List<MSL_AvailabilityGetterSetter> hashMapListHeaderData,
                                       HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> hashMapListChildData) {
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    MSL_AvailabilityGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("Store_Id", storeId);
                    values.put("Category_Id", categoryId);
                    values.put("Brand_Id", hashMapListHeaderData.get(i).getBrand_id());
                    values.put("SKU_ID", data.getSku_id());
                    values.put("SKU", data.getSku());
                    values.put("SKU_SEQUENCE", data.getSku_sequence());
                    values.put("MBQ", data.getMbq());
                    values.put("TOGGLE_VALUE", data.getToggleValue());

                    db.insert(CommonString.TABLE_INSERT_MSL_AVAILABILITY, null, values);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }
    }

    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilitySKU_AfterSaveData(String category_id, String brand_id, String store_id) {
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Data " +
                    "where category_id='" + category_id + "' and Brand_Id='" + brand_id + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityGetterSetter cd = new MSL_AvailabilityGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOGGLE_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_Availability Sku After Save Data!" + e.toString());
            return list;
        }
        return list;
    }

    public boolean checkMsl_AvailabilityData(String store_id, String category_id) {
        Log.d("MSL_Availability ", "Stock data--------------->Start<------------");
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Data " +
                    "where category_id='" + category_id + "' and Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        MSL_AvailabilityGetterSetter sb = new MSL_AvailabilityGetterSetter();

                        sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                return list.size() > 0;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return false;
        }

        Log.d("MSL_Availability ", "midday---------------------->Stop<-----------");
        return false;
    }

    public void updateMSL_Availability(String storeId, String categoryId, List<MSL_AvailabilityGetterSetter> hashMapListHeaderData,
                                       HashMap<MSL_AvailabilityGetterSetter, List<MSL_AvailabilityGetterSetter>> hashMapListChildData) {
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    MSL_AvailabilityGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("TOGGLE_VALUE", data.getToggleValue());

                    db.update(CommonString.TABLE_INSERT_MSL_AVAILABILITY, values,
                            "Brand_Id ='" + hashMapListHeaderData.get(i).getBrand_id() + "' AND SKU_ID ='" + data.getSku_id() +
                                    "' AND Category_Id='" + categoryId + "' AND Store_Id='" + storeId + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }
    }

    //Stock_facing
    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingHeaderData(String category_id, String keyAccount_id, String storeType_id, String class_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            /*dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND,BR.COMPANY_ID " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.COMPANY_ID,BR.BRAND", null);*/

            dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND,BR.COMPANY_ID ," +
                    " (SELECT SUM(SOS_TARGET) FROM MAPPING_SOS_TARGET WHERE STORE_ID = 1 AND BRAND_ID = BR.BRAND_ID)AS SOS_TARGET " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.KEYACCOUNT_ID = '" + keyAccount_id +
                    "' AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "'" +
                    "order by SB.SUB_CATEGORY,BR.COMPANY_ID,BR.BRAND", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    cd.setImage1("");
                    cd.setImage2("");

                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS_TARGET")) != null) {
                        if (!dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS_TARGET")).equals("")) {
                            cd.setSos_target(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS_TARGET")));
                        }
                    } else {
                        cd.setSos_target("-");
                    }


                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingSKUData(String category_id, String brand_id, String keyAccount_id, String storeType_id, String class_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ,BR.COMPANY_ID " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setStock("");
                    cd.setFacing("");

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public void InsertStock_Facing(String storeId, String categoryId, List<Stock_FacingGetterSetter> hashMapListHeaderData,
                                   HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> hashMapListChildData) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {
                Stock_FacingGetterSetter data1 = hashMapListHeaderData.get(i);

                values1.put("Store_Id", storeId);
                values1.put("Category_Id", categoryId);
                values1.put("COMPANY_ID", data1.getCompany_id());
                values1.put("SUB_CATEGORY_ID", data1.getSub_category_id());
                values1.put("SUB_CATEGORY", data1.getSub_category());
                values1.put("BRAND_ID", data1.getBrand_id());
                values1.put("BRAND", data1.getBrand());
                values1.put("IMAGE1", data1.getImage1());
                values1.put("IMAGE2", data1.getImage2());
                values1.put("SOS_TARGET", data1.getSos_target());

                db.insert(CommonString.TABLE_INSERT_STOCK_FACING_HEADER, null, values1);

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    Stock_FacingGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("Store_Id", storeId);
                    values.put("Category_Id", categoryId);
                    values.put("Brand_Id", hashMapListHeaderData.get(i).getBrand_id());
                    values.put("SKU_ID", data.getSku_id());
                    values.put("SKU", data.getSku());
                    values.put("SKU_SEQUENCE", data.getSku_sequence());
                    values.put("MBQ", data.getMbq());
                    values.put("COMPANY_ID", data.getCompany_id());
                    values.put("STOCK_VALUE", data.getStock());
                    values.put("FACEUP_VALUE", data.getFacing());

                    db.insert(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, null, values);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }
    }

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingHeader_AfterSaveData(String category_id, String store_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Header_Data " +
                    "where category_id='" + category_id + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    cd.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE1")));
                    cd.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE2")));
                    cd.setSos_target(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS_TARGET")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get after save Stock_FacingHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingSKU_AfterSaveData(String category_id, String brand_id, String store_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Child_Data " +
                    "where category_id='" + category_id + "' and Brand_Id='" + brand_id + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_VALUE")));
                    cd.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACEUP_VALUE")));

                    list.add(cd);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception ", "get Stock_Facing Sku After Save Data!" + e.toString());
            return list;
        }
        return list;
    }

    public long InsertCoverageData(CoverageBean data) {
        //db.delete(CommonString1.TABLE_COVERAGE_DATA, "STORE_ID" + "='" + data.getStoreId() + "'", null);
        ContentValues values = new ContentValues();

        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_IN_TIME, data.getInTime());
            values.put(CommonString.KEY_OUT_TIME, data.getOutTime());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_COVERAGE_STATUS, data.getStatus());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_GEO_TAG, data.getGEO_TAG());
            values.put(CommonString.KEY_CHECKOUT_IMAGE, data.getCheckOut_Image());

            return db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
        }
        return 0;
    }

    public boolean checkStockAndFacingData(String store_id, String category_id) {
        Log.d("Stock_Facing ", "Stock data--------------->Start<------------");
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Child_Data " +
                    "where category_id='" + category_id + "' and Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        Stock_FacingGetterSetter sb = new Stock_FacingGetterSetter();

                        sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                return list.size() > 0;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return false;
        }

        Log.d("Stock_Facing ", "midday---------------------->Stop<-----------");
        return false;
    }

    public void updateStockAndFacing(String storeId, String categoryId, List<Stock_FacingGetterSetter> hashMapListHeaderData,
                                     HashMap<Stock_FacingGetterSetter, List<Stock_FacingGetterSetter>> hashMapListChildData) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {
                Stock_FacingGetterSetter data1 = hashMapListHeaderData.get(i);

                values1.put("IMAGE1", data1.getImage1());
                values1.put("IMAGE2", data1.getImage2());

                //db.insert(CommonString.TABLE_INSERT_STOCK_FACING_HEADER, null, values1);
                db.update(CommonString.TABLE_INSERT_STOCK_FACING_HEADER, values1,
                        "Category_Id='" + categoryId + "' AND Store_Id='" + storeId + "' ", null);

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    Stock_FacingGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("STOCK_VALUE", data.getStock());
                    values.put("FACEUP_VALUE", data.getFacing());

                    //db.insert(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, null, values);
                    db.update(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, values,
                            "Brand_Id ='" + hashMapListHeaderData.get(i).getBrand_id() + "' AND SKU_ID ='" + data.getSku_id() +
                                    "' AND Category_Id='" + categoryId + "' AND Store_Id='" + storeId + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }

    }

    public void InsertMAPPING_ADDITIONAL_PROMOTION(MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter data) {
        db.delete("MAPPING_ADDITIONAL_PROMOTION", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("SKU_ID", data.getSKU_ID().get(i));
                values.put("SKU", data.getSKU().get(i));
                values.put("PROMO_ID", data.getPROMO_ID().get(i));
                values.put("PROMO", data.getPROMO().get(i));

                db.insert("MAPPING_ADDITIONAL_PROMOTION", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_ADDITIONAL_PROMOTION " + ex.toString());
        }
    }

    public void InsertMAPPING_PROMOTION(MappingPromotionGetterSetter data) {
        db.delete("MAPPING_PROMOTION", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("SKU_ID", data.getSKU_ID().get(i));
                values.put("SKU", data.getSKU().get(i));
                values.put("PROMO_ID", data.getPROMO_ID().get(i));
                values.put("PROMO", data.getPROMO().get(i));

                db.insert("MAPPING_PROMOTION", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_PROMOTION " + ex.toString());
        }
    }

    //Promo Compliance
    public ArrayList<Promo_Compliance_DataGetterSetter> getPromoComplianceSkuData(String store_id, String category_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select M.* " +
                    "from MAPPING_PROMOTION M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where    CA.CATEGORY_ID='" + category_id + "' AND M.STORE_ID = '" + store_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);

            /*dbcursor = db.rawQuery("Select * from MAPPING_PROMOTION " +
                    "where STORE_ID='" + store_id + "'", null);*/

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();

                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setIn_stock("0");
                    cd.setPromo_announcer("0");
                    cd.setRunning_pos("0");

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<Promo_Compliance_DataGetterSetter> getPromoSpinnerData(String store_id, String category_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            Promo_Compliance_DataGetterSetter promo = new Promo_Compliance_DataGetterSetter();
            promo.setPromo_id("0");
            promo.setPromo(context.getResources().getString(R.string.select_promo));

            list.add(promo);

            dbcursor = db.rawQuery("Select M.* " +
                    "from MAPPING_ADDITIONAL_PROMOTION M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where    CA.CATEGORY_ID='" + category_id + "' AND M.STORE_ID = '" + store_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);

            /*dbcursor = db.rawQuery("Select * from MAPPING_ADDITIONAL_PROMOTION " +
                    "where STORE_ID='" + store_id + "'", null);*/

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    public void InsertAdditionalPromoData(Promo_Compliance_DataGetterSetter data, String category_id) {
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_ID", Integer.parseInt(data.getStore_id()));
            values.put("CATEGORY_ID", Integer.parseInt(category_id));
            values.put("SKU_ID", Integer.parseInt(data.getSku_id()));
            values.put("SKU", data.getSku());
            values.put("PROMO_ID", Integer.parseInt(data.getPromo_id()));
            values.put("PROMO", data.getPromo());
            values.put("IN_STOCK_VALUE", Integer.parseInt(data.getIn_stock()));
            values.put("PROMO_ANNOUNCER_VALUE", Integer.parseInt(data.getPromo_announcer()));
            values.put("RUNNING_POS_VALUE", Integer.parseInt(data.getRunning_pos()));

            db.insert(CommonString.TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE, null, values);
        } catch (Exception ex) {
            Log.d("Exception ", " InsertAdditionalPromoData " + ex.toString());
        }
    }

    public ArrayList<Promo_Compliance_DataGetterSetter> getAdditionalPromoData(String store_id, String category_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Additional_Promo_Compliance_Data " +
                    "where STORE_ID='" + store_id + "' AND CATEGORY_ID='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();
                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setIn_stock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IN_STOCK_VALUE")));
                    cd.setPromo_announcer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ANNOUNCER_VALUE")));
                    cd.setRunning_pos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RUNNING_POS_VALUE")));
                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    public void InsertPromoSkuData(ArrayList<Promo_Compliance_DataGetterSetter> promoSkuListData, String category_id) {
        ContentValues values = new ContentValues();
        try {
            db.beginTransaction();
            for (int i = 0; i < promoSkuListData.size(); i++) {
                Promo_Compliance_DataGetterSetter data = promoSkuListData.get(i);

                values.put("STORE_ID", Integer.parseInt(data.getStore_id()));
                values.put("CATEGORY_ID", Integer.parseInt(category_id));
                values.put("SKU_ID", Integer.parseInt(data.getSku_id()));
                values.put("SKU", data.getSku());
                values.put("PROMO_ID", Integer.parseInt(data.getPromo_id()));
                values.put("PROMO", data.getPromo());
                values.put("IN_STOCK_VALUE", Integer.parseInt(data.getIn_stock()));
                values.put("PROMO_ANNOUNCER_VALUE", Integer.parseInt(data.getPromo_announcer()));
                values.put("RUNNING_POS_VALUE", Integer.parseInt(data.getRunning_pos()));

                db.insert(CommonString.TABLE_INSERT_PROMO_SKU, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " InsertAdditionalPromoData " + ex.toString());
        }
    }

    public boolean checkPromoComplianceData(String store_id, String category_id) {
        Log.d("PromoCompliance ", "PromoCompliance data--------------->Start<------------");
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Promo_SKU_Data " +
                    "where CATEGORY_ID='" + category_id + "' and STORE_ID='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        Promo_Compliance_DataGetterSetter sb = new Promo_Compliance_DataGetterSetter();

                        sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                return list.size() > 0;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return false;
        }

        Log.d("Stock_Facing ", "midday---------------------->Stop<-----------");
        return false;
    }

    //Gagan End Method

//Non Working data

    public void insertNonWorkingData(NonWorkingReasonGetterSetter data) {
        db.delete("NON_WORKING_REASON", null, null);
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getREASON_ID().size(); i++) {

                values.put("REASON_ID", Integer.parseInt(data.getREASON_ID().get(i)));
                values.put("REASON", data.getREASON().get(i));
                values.put("ENTRY_ALLOW", data.getENTRY_ALLOW().get(i));
                values.put("IMAGE_ALLOW", data.getIMAGE_ALLOW().get(i));

                db.insert("NON_WORKING_REASON", null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception  ", ex.toString());
        }

    }

    // get NonWorking data
    public ArrayList<NonWorkingReasonGetterSetter> getNonWorkingData() {

        ArrayList<NonWorkingReasonGetterSetter> list = new ArrayList<NonWorkingReasonGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM NON_WORKING_REASON", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonWorkingReasonGetterSetter sb = new NonWorkingReasonGetterSetter();

                    sb.setREASON_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_ID")));

                    sb.setREASON(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    sb.setENTRY_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW")));

                    sb.setIMAGE_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

    public void updateStoreStatusOnLeave(String storeid, String visitdate,
                                         String status) {

        try {
            ContentValues values = new ContentValues();
            values.put("UPLOAD_STATUS", status);

            db.update("JOURNEY_PLAN", values,
                    CommonString.KEY_STORE_ID + "='" + storeid + "' AND "
                            + CommonString.KEY_VISIT_DATE + "='" + visitdate
                            + "'", null);
        } catch (Exception e) {

        }
    }

    //get specific store data
    public StoreBean getSpecificStoreData(String date, String store_id) {
        //ArrayList<StoreBean> list = new ArrayList<StoreBean>();
        StoreBean sb = new StoreBean();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * from JOURNEY_PLAN  " +
                    "where VISIT_DATE ='" + date + "' AND STORE_ID='" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    sb.setSTORE_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_ID")));

                    sb.setEMP_ID((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("EMP_ID"))));

                    sb.setKEYACCOUNT(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("KEYACCOUNT")));

                    sb.setSTORE_NAME(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORE_NAME")));

                    sb.setADDRESS((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("ADDRESS"))));
                    sb.setCITY(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CITY")));

                    sb.setSTORETYPE(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORETYPE")));

                    sb.setCLASSIFICATION(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CLASSIFICATION")));

                    sb.setKEYACCOUNT_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("KEYACCOUNT_ID")));

                    sb.setSTORETYPE_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("STORETYPE_ID")));

                    sb.setCLASS_ID(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CLASS_ID")));

                    sb.setVISIT_DATE(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("VISIT_DATE")));

                    sb.setCAMERA_ALLOW(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CAMERA_ALLOW")));

                    sb.setUPLOAD_STATUS(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("UPLOAD_STATUS")));
                    sb.setCHECKOUT_STATUS(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("CHECKOUT_STATUS")));

                    sb.setGEO_TAG(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("GEO_TAG")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }

    //update coverage status

    public void updateCoverageStatus(String id, String status) {

        ContentValues values = new ContentValues();

        try {

            values.put(CommonString.KEY_COVERAGE_STATUS, status);

            db.update(CommonString.TABLE_COVERAGE_DATA, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);

        } catch (Exception e) {

            Log.d("Excep update checkout", e.toString());
        }

    }


    //Gagan start new code 1

    //Promo Compliance update data
    public void updatePromoComplianceSKU(ArrayList<Promo_Compliance_DataGetterSetter> promoSkuListData,
                                         String categoryId, String storeId) {
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < promoSkuListData.size(); i++) {
                Promo_Compliance_DataGetterSetter data = promoSkuListData.get(i);

                values.put("IN_STOCK_VALUE", Integer.parseInt(data.getIn_stock()));
                values.put("PROMO_ANNOUNCER_VALUE", Integer.parseInt(data.getPromo_announcer()));
                values.put("RUNNING_POS_VALUE", Integer.parseInt(data.getRunning_pos()));

                db.update(CommonString.TABLE_INSERT_PROMO_SKU, values,
                        "CATEGORY_ID='" + categoryId + "' AND STORE_ID='" + storeId +
                                "' AND SKU_ID='" + data.getSku_id() + "' AND PROMO_ID='" + data.getPromo_id() + "'", null);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }

    }

    public ArrayList<Promo_Compliance_DataGetterSetter> getPromoComplianceSkuAfterData(String store_id, String category_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Promo_SKU_Data " +
                    "where STORE_ID='" + store_id + "' AND CATEGORY_ID='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();

                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setIn_stock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IN_STOCK_VALUE")));
                    cd.setPromo_announcer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ANNOUNCER_VALUE")));
                    cd.setRunning_pos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RUNNING_POS_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuAfterData!" + e.toString());
            return list;
        }
        return list;
    }

    //MSL_Availability Server Upload Data
    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilityUploadServerData(String store_id) {
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Data " +
                    "where Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityGetterSetter cd = new MSL_AvailabilityGetterSetter();

                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOGGLE_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_Availability server upload Data!" + e.toString());
            return list;
        }
        return list;
    }

    //Stock Facing Server Upload Data
    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingHeaderServerUploadData(String store_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select H.BRAND_ID, H.IMAGE1, H.IMAGE2, C.SKU_ID, C.STOCK_VALUE, C.FACEUP_VALUE " +
                    "from Stock_Facing_Header_Data  H " +
                    "INNER JOIN Stock_Facing_Child_Data C " +
                    "ON H.STORE_ID = C.STORE_ID AND H.CATEGORY_ID = C.CATEGORY_ID AND H.BRAND_ID = C.BRAND_ID " +
                    "WHERE H.STORE_ID = '" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE1")));
                    cd.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE2")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_VALUE")));
                    cd.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACEUP_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get Stock_FacingHeader server upload !" + e.toString());
            return list;
        }
        return list;
    }

    /*public ArrayList<Stock_FacingGetterSetter> getStockAndFacingSKUServerUploadData(String category_id, String brand_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Child_Data " +
                    "where category_id='" + category_id + "' and Brand_Id='" + brand_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK_VALUE")));
                    cd.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACEUP_VALUE")));

                    list.add(cd);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception ", "get Stock_Facing Sku After Save Data!" + e.toString());
            return list;
        }
        return list;
    }*/

    //Promo Compliance Promotion Data
    public ArrayList<Promo_Compliance_DataGetterSetter> getPromoComplianceSkuServerUploadData(String store_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Promo_SKU_Data " +
                    "where STORE_ID='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();

                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setIn_stock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IN_STOCK_VALUE")));
                    cd.setPromo_announcer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ANNOUNCER_VALUE")));
                    cd.setRunning_pos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RUNNING_POS_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuAfterData!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<Promo_Compliance_DataGetterSetter> getAdditionalPromotionServerUploadData(String store_id) {
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Additional_Promo_Compliance_Data " +
                    "where STORE_ID='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Promo_Compliance_DataGetterSetter cd = new Promo_Compliance_DataGetterSetter();

                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setPromo_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ID")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setIn_stock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IN_STOCK_VALUE")));
                    cd.setPromo_announcer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO_ANNOUNCER_VALUE")));
                    cd.setRunning_pos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RUNNING_POS_VALUE")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    //Store wise Performance
    public void InsertSTORE_PERFORMANCE(STORE_PERFORMANCE_MasterGetterSetter data) {
        db.delete("STORE_PERFORMANCE_NEW", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("CATEGORY_ID", data.getCATEGORY_ID().get(i));
                values.put("PERIOD", data.getPERIOD().get(i));
                values.put("MSL", data.getMSL_AVAILABILITY().get(i));
                values.put("SOS", data.getSOS().get(i));
                values.put("T2P", data.getT2P().get(i));
                values.put("PROMO", data.getPROMO().get(i));
                values.put("OSS", data.getOSS().get(i));
                values.put("ORDERID", data.getORDERID().get(i));
                values.put("PLANOGRAM", data.getPLANOGRAM().get(i));

                db.insert("STORE_PERFORMANCE_NEW", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " STORE_PERFORMANCE_NEW " + ex.toString());
        }
    }

    //Category wise Performance
    public ArrayList<CategoryWisePerformaceGetterSetter> getCategoryWisePerformance(String store_id, String category_id) {
        ArrayList<CategoryWisePerformaceGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from STORE_PERFORMANCE_NEW " +
                    "where STORE_ID='" + store_id + "' and CATEGORY_ID='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CategoryWisePerformaceGetterSetter cd = new CategoryWisePerformaceGetterSetter();

                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    cd.setPeriod(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PERIOD")));
                    cd.setMsl_availability(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MSL")));
                    cd.setSos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS")));
                    cd.setT2p(dbcursor.getString(dbcursor.getColumnIndexOrThrow("T2P")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setOss(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OSS")));
                    cd.setOrder_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ORDERID")));
                    cd.setPLANOGRAM(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PLANOGRAM")));
                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<StoreWisePerformaceGetterSetter> getStoreWisePerformance(String store_id) {
        ArrayList<StoreWisePerformaceGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select PERIOD, ROUND(avg(MSL),1) as MSL,ROUND(avg(sos),1) as SOS ," +
                    " ROUND(avg(t2p),1) as T2P,ROUND(avg(pROMO),1) as PROMO , ROUND(avg(pLANOGRAM),1) as PLANOGRAM, ROUND(SUM(oss),1) AS OSS " +
                    "from STORE_PERFORMANCE_NEW " +
                    "where  STORE_ID='" + store_id + "' " +
                    "GROUP BY PERIOD " +
                    "ORDER BY ORDERID ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreWisePerformaceGetterSetter cd = new StoreWisePerformaceGetterSetter();

/*                    cd.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));*/
                    cd.setPeriod(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PERIOD")));
                    cd.setMsl_availability(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MSL")));
                    cd.setSos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOS")));
                    cd.setT2p(dbcursor.getString(dbcursor.getColumnIndexOrThrow("T2P")));
                    cd.setPromo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROMO")));
                    cd.setOss(dbcursor.getString(dbcursor.getColumnIndexOrThrow("OSS")));
                    cd.setPLANOGRAM(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PLANOGRAM")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "getPromoComplianceSkuData!" + e.toString());
            return list;
        }
        return list;
    }

    //Gagan end new code 1

    public void InsertStockDialog(AdditionalDialogGetterSetter data) {
        ContentValues values = new ContentValues();

        try {


            values.put(CommonString.KEY_STORE_ID, data.getStore_id());
            values.put(CommonString.KEY_BRAND, data.getBrand());
            values.put(CommonString.KEY_BRAND_ID, data.getBrand_id());

            // values.put(CommonString.KEY_DISPLAY_ID, data.getDisplay_id());

            values.put(CommonString.KEY_QUANTITY, data.getQuantity());
            values.put(CommonString.KEY_SKU_ID, data.getSku_id());
            values.put(CommonString.KEY_SKUNAME, data.getSku_name());
            // values.put(CommonString.UNIQUE_KEY_ID, data.getUnique_id());

            // values.put(CommonString.KEY_CATEGORY_ID, data.getCategory_id());

            // values.put(CommonString.KEY_PROCESS_ID, data.getProcess_id());


            db.insert(CommonString.TABLE_INSERT_STOCK_DIALOG, null, values);


        } catch (Exception ex) {
            Log.d("Database Exception ", ex.getMessage());
        }

    }

    public ArrayList<AdditionalDialogGetterSetter> getDialogStock(String keyid) {
        Cursor cursordata = null;
        ArrayList<AdditionalDialogGetterSetter> productData = new ArrayList<AdditionalDialogGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT * FROM STOCK_DIALOG_MAIN WHERE COMMON_ID = '" + keyid + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AdditionalDialogGetterSetter sb = new AdditionalDialogGetterSetter();

                    sb.setKEY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_ID)));

                    sb.setCOMMON_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_Common_ID)));

                    sb.setStore_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));

                    sb.setBrand_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));

                    sb.setBrand(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_BRAND)));

                    sb.setQuantity(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_QUANTITY)));

                    sb.setSku_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_SKU_ID)));

                    sb.setSku_name(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_SKUNAME)));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }

    public void deletedialogStockEntry(String id) {
        try {
            db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG, "Id" + "='" + id + "'", null);
        } catch (Exception e) {
            System.out.println("" + e);
        }

    }

    public void deleteStockEntry(String id) {
        try {
            db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL, "KEY_ID" + "='" + id + "'", null);
            db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, "KEY_ID" + "='" + id + "'", null);


        } catch (Exception e) {
            System.out.println("" + e);
        }

    }

    public void deleteStockEntryall(String storeid, String categoryid) {
        try {

         /*   db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);
            db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);
*/
            db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);

            db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);

        } catch (Exception e) {
            System.out.println("" + e);
        }

    }


    public void deleteStockEntryMainTable(String storeid, String categoryid) {
        try {

            db.delete(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);

            db.delete(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, "Store_Id" + "='" + storeid + "'AND categoryId" + "='" + categoryid + "'", null);

        } catch (Exception e) {
            System.out.println("" + e);
        }

    }


    public ArrayList<BrandMasterGetterSetter> getBrandMasterData(String store_id, String category_id) {
        Cursor cursordata = null;
        ArrayList<BrandMasterGetterSetter> Data = new ArrayList<BrandMasterGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT  BR.BRAND_ID,  SB.SUB_CATEGORY||'-'||BR.BRAND AS BRAND FROM BRAND_MASTER BR INNER JOIN SUB_CATEGORY_MASTER SB  ON BR.SUB_CATEGORY_ID =  SB.SUB_CATEGORY_ID INNER JOIN CATEGORY_MASTER CA ON  SB.CATEGORY_ID =CA.CATEGORY_ID WHERE COMPANY_ID =1 AND SB.CATEGORY_ID ='" + category_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    BrandMasterGetterSetter sb = new BrandMasterGetterSetter();

                    sb.setBRAND_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND_ID")));

                    sb.setBRAND(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND")));


                    /*sb.setSUB_CATEGORY_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("SUB_CATEGORY_ID")));

                    sb.setSUB_CATEGORY_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("SUB_CATEGORY_ID")));

                    sb.setCOMPANY_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("COMPANY_ID")));

                    sb.setBRAND_SEQUENCE(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND_SEQUENCE")));*/

                    Data.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return Data;

    }


    public ArrayList<SkuMasterGetterSetter> getSKUMasterData(String store_id) {
        Cursor cursordata = null;
        ArrayList<SkuMasterGetterSetter> Data = new ArrayList<SkuMasterGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT * FROM SKU_MASTER ", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    SkuMasterGetterSetter sb = new SkuMasterGetterSetter();

                    sb.setSKU_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("SKU_ID")));

                    sb.setSKU(cursordata.getString(cursordata.getColumnIndexOrThrow("SKU")));

                    sb.setBRAND_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND_ID")));


                    sb.setMRP(cursordata.getString(cursordata.getColumnIndexOrThrow("MRP")));

                    sb.setSKU_SEQUENCE(cursordata.getString(cursordata.getColumnIndexOrThrow("SKU_SEQUENCE")));

                    Data.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return Data;

    }


    public void InsertAdditionalData(AddittionalGetterSetter data, ArrayList<AdditionalDialogGetterSetter> dialog, String categoryId) {

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        try {
            values.put("Store_Id", data.getStore_id());
            values.put("categoryId", categoryId);
            values.put("brand_name", data.getBrand());
            values.put("brand_id", data.getBrand_id());
            values.put("image_url", data.getImage());
            values.put("image_url2", data.getImage2());
            values.put("image_url3", data.getImage3());
            values.put("sku_id", data.getSku_id());
            values.put("sku_name", data.getSku());
            values.put("toggle_value", data.getBtn_toogle());

            long key_id = db.insert(CommonString.TABLE_INSERT_STOCK_ADDITIONAL, null, values);

            for (int i = 0; i < dialog.size(); i++) {
                values1.put(CommonString.KEY_Common_ID, key_id);
                values1.put(CommonString.KEY_STORE_ID, dialog.get(i).getStore_id());
                values1.put("categoryId", categoryId);
                values1.put(CommonString.KEY_BRAND, dialog.get(i).getBrand());
                values1.put(CommonString.KEY_BRAND_ID, dialog.get(i).getBrand_id());
                values1.put(CommonString.KEY_QUANTITY, dialog.get(i).getQuantity());
                values1.put(CommonString.KEY_SKU_ID, dialog.get(i).getSku_id());
                values1.put(CommonString.KEY_SKUNAME, dialog.get(i).getSku_name());


                db.insert(CommonString.TABLE_INSERT_STOCK_DIALOG, null, values1);

            }

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.getMessage());
        }

    }


    public ArrayList<AddittionalGetterSetter> getAdditionalStock(String store_id, String categoryId) {
        Cursor cursordata = null;
        ArrayList<AddittionalGetterSetter> productData = new ArrayList<AddittionalGetterSetter>();

        try {

            // cursordata = db.rawQuery("SELECT * FROM Stock_Additional_visibility WHERE Store_Id = '"+store_id + "'categoryId = '"+categoryId + "'", null);
            cursordata = db.rawQuery("Select * from Stock_Additional_visibility_Main " + "where categoryId='" + categoryId + "' and Store_Id='" + store_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AddittionalGetterSetter sb = new AddittionalGetterSetter();


                    sb.setKey_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("KEY_ID")));

                    sb.setStore_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Store_Id")));

                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));

                    sb.setBrand_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_id")));

                    sb.setBrand(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_name")));


                    sb.setImage(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url")));
                    sb.setImage2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url2")));
                    sb.setImage3(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url3")));

                    sb.setSku_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_id")));

                    sb.setSku(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_name")));
                    sb.setBtn_toogle(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("toggle_value")));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }

    public void updateStoreStatus(String storeid, String visitdate,
                                  String status) {

        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_CHECKOUT_STATUS, status);

            db.update("JOURNEY_PLAN", values, CommonString.KEY_STORE_ID + "='" + storeid + "' AND " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);
        } catch (Exception e) {


        }
    }

    public void InsertT2PData(ArrayList<T2PGetterSetter> data, String store_id, String category_id) {
        db.delete(CommonString.TABLE_INSERT_T2P_COMPLIANCE, CommonString.KEY_STORE_ID + "='" + store_id + "' AND " + CommonString.KEY_CATEGORY_ID + "='" + category_id + "'", null);

        long l = 0;

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        try {
            for (int i = 0; i < data.size(); i++) {

                values.put(CommonString.KEY_STORE_ID, store_id);
                values.put(CommonString.KEY_CATEGORY_ID, category_id);
                values.put(CommonString.KEY_DISPLAY_ID, data.get(i).getDisplay_id());
                values.put(CommonString.KEY_BRAND_ID, data.get(i).getBrand_id());
                values.put(CommonString.KEY_BRAND, data.get(i).getBrand());
                values.put(CommonString.KEY_DISPLAY, data.get(i).getDisplay());
                values.put(CommonString.KEY_IMAGE_URL, data.get(i).getRef_image_url());
                values.put(CommonString.KEY_IMAGE_PATH, data.get(i).getRef_image_path());
                values.put(CommonString.KEY_IMAGE, data.get(i).getImage());
                values.put(CommonString.KEY_IMAGE1, data.get(i).getImage1());
                values.put(CommonString.KEY_IMAGE2, data.get(i).getImage2());
                values.put(CommonString.KEY_REMARK, data.get(i).getRemark());
                values.put(CommonString.KEY_PRESENT, data.get(i).isPresent());

                l = db.insert(CommonString.TABLE_INSERT_T2P_COMPLIANCE, null, values);

                ArrayList<GapsChecklistGetterSetter> gaps = data.get(i).getGapsChecklist();

                for (int j = 0; j < gaps.size(); j++) {

                    values1.put(CommonString.KEY_COMMON_ID, l);
                    values1.put(CommonString.KEY_CHECKLIST_ID, gaps.get(j).getChecklist_id());
                    values1.put(CommonString.KEY_DISPLAY_ID, gaps.get(j).getDisplay_id());
                    values1.put(CommonString.KEY_CHECKLIST, gaps.get(j).getChecklist());
                    values1.put(CommonString.KEY_PRESENT, gaps.get(j).isPresent());

                    db.insert(CommonString.TABLE_INSERT_T2P_GAPS, null, values1);

                }

                ArrayList<SkuGetterSetter> sku = data.get(i).getSkulist();

                for (int k = 0; k < sku.size(); k++) {

                    values2.put(CommonString.KEY_COMMON_ID, l);
                    values2.put(CommonString.KEY_SKU_ID, sku.get(k).getSKU_ID());
                    values2.put(CommonString.KEY_BRAND_ID, sku.get(k).getBRAND_ID());
                    values2.put(CommonString.KEY_STOCK, sku.get(k).getSTOCK());
                    values2.put(CommonString.KEY_BRAND, sku.get(k).getBRAND());
                    values2.put(CommonString.KEY_SKU, sku.get(k).getSKU());

                    db.insert(CommonString.TABLE_INSERT_T2P_SKU, null, values2);

                }


                ArrayList<BrandAvabilityGetterSetter> brand = data.get(i).getBrandlist();

                for (int p = 0; p < brand.size(); p++) {

                    values3.put("COMMON_ID", l);
                    values3.put("BRAND_NAME", brand.get(p).getBRAND());
                    values3.put("BRAND_ID", brand.get(p).getBRAND_ID());

                    db.insert(CommonString.TABLE_INSERT_BRAND_AVAIBILITY_DATA, null, values3);

                }


            }
        } catch (Exception ex) {
            Log.d("Exception ", " in T2P_COMPLIANCE " + ex.toString());
        }
    }


    public boolean additionalVisibilitydata(String store_id, String category_id) {
        Log.d("AdditionalVisibility ", "AdditionalVisibility data--------------->Start<------------");
        ArrayList<AddittionalGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Additional_visibility_Main " + "where categoryId='" + category_id + "' and Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        AddittionalGetterSetter sb = new AddittionalGetterSetter();

                        sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("brand_id")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!" + e.toString());
            return false;
        }

        return false;
    }

    public ArrayList<AddittionalGetterSetter> getAdditionalStockUpload(String store_id) {
        Cursor cursordata = null;
        ArrayList<AddittionalGetterSetter> productData = new ArrayList<AddittionalGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT * FROM Stock_Additional_visibility_Main WHERE Store_Id = '" + store_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AddittionalGetterSetter sb = new AddittionalGetterSetter();


                    sb.setKey_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("KEY_ID")));

                    sb.setStore_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Store_Id")));

                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));

                    sb.setBrand_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_id")));

                    sb.setBrand(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_name")));


                    sb.setImage(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url")));
                    sb.setImage2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url2")));
                    sb.setImage3(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url3")));

                    sb.setSku_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_id")));

                    sb.setSku(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_name")));
                    sb.setBtn_toogle(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("toggle_value")));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }

    /*public void InsertMainListAdditionalData(AddittionalGetterSetter Mainlist, ArrayList<AdditionalDialogGetterSetter> skulist, String categoryId) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        try {

            values.put("Store_Id", Mainlist.getStore_id());
            values.put("categoryId", categoryId);
            values.put("brand_name", Mainlist.getBrand());
            values.put("brand_id", Mainlist.getBrand_id());
            values.put("image_url", Mainlist.getImage());
            values.put("image_url2", Mainlist.getImage2());
            values.put("image_url3", Mainlist.getImage3());
            values.put("sku_id", Mainlist.getSku_id());
            values.put("sku_name", Mainlist.getSku());
            values.put("toggle_value", Mainlist.getBtn_toogle());

            long key_id = db.insert(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, null, values);

            if (skulist != null) {

                for (int j = 0; j < skulist.size(); j++) {
                    values1.put(CommonString.KEY_Common_ID, key_id);
                    values1.put(CommonString.KEY_STORE_ID, skulist.get(j).getStore_id());
                    values1.put("categoryId", categoryId);
                    values1.put(CommonString.KEY_BRAND, skulist.get(j).getBrand());
                    values1.put(CommonString.KEY_BRAND_ID, skulist.get(j).getBrand_id());
                    values1.put(CommonString.KEY_QUANTITY, skulist.get(j).getQuantity());
                    values1.put(CommonString.KEY_SKU_ID, skulist.get(j).getSku_id());
                    values1.put(CommonString.KEY_SKUNAME, skulist.get(j).getSku_name());

                    db.insert(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, null, values1);
                }
            }

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.getMessage());
        }

    }*/

    public void InsertMainListAdditionalData(ArrayList<AddittionalGetterSetter> Mainlist, String categoryId) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        try {
            for (int i = 0; i < Mainlist.size(); i++) {
                AddittionalGetterSetter data = Mainlist.get(i);

                values.put("Store_Id", data.getStore_id());
                values.put("categoryId", categoryId);
                values.put("brand_name", data.getBrand());
                values.put("brand_id", data.getBrand_id());
                values.put("image_url", data.getImage());
                values.put("image_url2", data.getImage2());
                values.put("image_url3", data.getImage3());
                values.put("sku_id", data.getSku_id());
                values.put("sku_name", data.getSku());
                values.put("toggle_value", data.getBtn_toogle());

                long key_id = db.insert(CommonString.TABLE_INSERT_STOCK_ADDITIONAL_MAIN, null, values);

                ArrayList<AdditionalDialogGetterSetter> skulist = data.getSkuDialogList();
                if (skulist != null) {

                    for (int j = 0; j < skulist.size(); j++) {
                        values1.put(CommonString.KEY_Common_ID, key_id);
                        values1.put(CommonString.KEY_STORE_ID, skulist.get(j).getStore_id());
                        values1.put("categoryId", categoryId);
                        values1.put(CommonString.KEY_BRAND, skulist.get(j).getBrand());
                        values1.put(CommonString.KEY_BRAND_ID, skulist.get(j).getBrand_id());
                        values1.put(CommonString.KEY_QUANTITY, skulist.get(j).getQuantity());
                        values1.put(CommonString.KEY_SKU_ID, skulist.get(j).getSku_id());
                        values1.put(CommonString.KEY_SKUNAME, skulist.get(j).getSku_name());

                        db.insert(CommonString.TABLE_INSERT_STOCK_DIALOG_MAIN, null, values1);
                    }
                }
            }

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.getMessage());
        }

    }

    public ArrayList<AdditionalDialogGetterSetter> getDialogStockUpload(String keyid) {
        Cursor cursordata = null;
        ArrayList<AdditionalDialogGetterSetter> productData = new ArrayList<AdditionalDialogGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT * FROM STOCK_DIALOG_MAIN WHERE COMMON_ID = '" + keyid + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AdditionalDialogGetterSetter sb = new AdditionalDialogGetterSetter();

                    sb.setKEY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_ID)));

                    sb.setCOMMON_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_Common_ID)));

                    sb.setStore_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));

                    sb.setBrand_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));

                    sb.setBrand(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_BRAND)));

                    sb.setQuantity(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_QUANTITY)));

                    sb.setSku_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_SKU_ID)));

                    sb.setSku_name(cursordata.getString(cursordata
                            .getColumnIndexOrThrow(CommonString.KEY_SKUNAME)));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }


    // get T2P Compliance data
    public ArrayList<T2PGetterSetter> getT2pComplianceData(String store_id, String category_id) {

        ArrayList<T2PGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            if (category_id == null) {
                dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_COMPLIANCE + " where " +
                        CommonString.KEY_STORE_ID + "='" + store_id + "'", null);
            } else {
                dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_COMPLIANCE + " where " +
                        CommonString.KEY_STORE_ID + "='" + store_id + "' AND " +
                        CommonString.KEY_CATEGORY_ID + "='" + category_id + "'", null);
            }


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    T2PGetterSetter tp = new T2PGetterSetter();

                    tp.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    tp.setDisplay_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_DISPLAY_ID)));
                    tp.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));
                    tp.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND)));
                    tp.setDisplay(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_DISPLAY)));
                    tp.setRef_image_url(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE_URL)));
                    tp.setRef_image_path(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE_PATH)));
                    tp.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    tp.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE1)));
                    tp.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE2)));
                    tp.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REMARK)));
                    tp.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CATEGORY_ID)));
                    tp.setPresent((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PRESENT)).equalsIgnoreCase("1")));

                    list.add(tp);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    // get T2P Compliance store wise data
    public ArrayList<T2PGetterSetter> getT2pComplianceStorewiseData(String store_id) {

        ArrayList<T2PGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {


            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_COMPLIANCE + " where " +
                    CommonString.KEY_STORE_ID + "='" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    T2PGetterSetter tp = new T2PGetterSetter();

                    tp.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    tp.setDisplay_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_DISPLAY_ID)));
                    tp.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));
                    tp.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND)));
                    tp.setDisplay(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_DISPLAY)));
                    tp.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    tp.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REMARK)));
                    tp.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CATEGORY_ID)));
                    tp.setPresent((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PRESENT)).equalsIgnoreCase("1")));

                    list.add(tp);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    // get Gaps T2P data
    public ArrayList<GapsChecklistGetterSetter> getGapsData(String common_id) {

        ArrayList<GapsChecklistGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_GAPS + " where " +
                    CommonString.KEY_COMMON_ID + "='" + common_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GapsChecklistGetterSetter gp = new GapsChecklistGetterSetter();

                    gp.setDisplay_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_DISPLAY_ID)));
                    gp.setChecklist_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKLIST_ID)));
                    gp.setChecklist(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKLIST)));
                    gp.setPresent((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PRESENT)).equalsIgnoreCase("1")));

                    list.add(gp);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    // get T2P SKU data
    public ArrayList<SkuGetterSetter> getT2PSKUData(String common_id) {

        ArrayList<SkuGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_SKU + " where " +
                    CommonString.KEY_COMMON_ID + "='" + common_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SkuGetterSetter gp = new SkuGetterSetter();

                    gp.setSKU_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_SKU_ID)));
                    gp.setBRAND_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));
                    gp.setSTOCK(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STOCK)));
                    gp.setBRAND(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND)));
                    gp.setSKU(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_SKU)));

                    list.add(gp);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    //Gagan start new code 2

    public void InsertMAPPING_PLANOGRAM(MAPPING_PLANOGRAM_MasterGetterSetter data) {
        db.delete("MAPPING_PLANOGRAM", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getKEYACCOUNT_ID().size(); i++) {

                values.put("KEYACCOUNT_ID", data.getKEYACCOUNT_ID().get(i));
                values.put("STORETYPE_ID", data.getSTORETYPE_ID().get(i));
                values.put("CLASS_ID", data.getCLASS_ID().get(i));
                values.put("CATEGORY_ID", data.getCATEGORY_ID().get(i));
                values.put("PLANOGRAM_IMAGE", data.getPLANOGRAM_IMAGE().get(i));
                values.put("IMAGE_PATH", data.getIMAGE_PATH().get(i));

                db.insert("MAPPING_PLANOGRAM", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " MAPPING_PLANOGRAM " + ex.toString());
        }
    }

    public ArrayList<MAPPING_PLANOGRAM_DataGetterSetter> getMappingPlanogramData(String category_id) {
        ArrayList<MAPPING_PLANOGRAM_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM MAPPING_PLANOGRAM where CATEGORY_ID='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MAPPING_PLANOGRAM_DataGetterSetter mp = new MAPPING_PLANOGRAM_DataGetterSetter();

                    mp.setKEYACCOUNT_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEYACCOUNT_ID")));
                    mp.setSTORETYPE_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORETYPE_ID")));
                    mp.setCLASS_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CLASS_ID")));
                    mp.setCATEGORY_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    mp.setPLANOGRAM_IMAGE(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PLANOGRAM_IMAGE")));
                    mp.setIMAGE_PATH(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_PATH")));

                    list.add(mp);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    //Gagan end new code 2

    //T2P is filled
    public boolean isFilledT2P(String store_id, String category_id) {
        Log.d("T2P ", "T2P data--------------->Start<------------");
        ArrayList<T2PGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_COMPLIANCE + " where " +
                    CommonString.KEY_STORE_ID + "='" + store_id + "' AND " +
                    CommonString.KEY_CATEGORY_ID + "='" + category_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        T2PGetterSetter tp = new T2PGetterSetter();

                        tp.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_ID)));
                        list.add(tp);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!" + e.toString());
            return false;
        }

        return false;
    }

    public CoverageBean getCoverageSpecificData(String visitdate, String store_id) {

        CoverageBean sb = new CoverageBean();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where "
                            + CommonString.KEY_VISIT_DATE + "='" + visitdate + "' AND " + CommonString.KEY_STORE_ID + " ='" + store_id + "'",
                    null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    // CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setReason((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));
                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    if (dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor
                                .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }

                    //list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }
        return sb;
    }

    //update out time
    public void updateCheckoutOuttime(String id, String out_time, String status, String checkOutImagePath) {
        ContentValues values = new ContentValues();

        try {
            values.put(CommonString.KEY_OUT_TIME, out_time);
            values.put(CommonString.KEY_COVERAGE_STATUS, status);
            values.put(CommonString.KEY_CHECKOUT_IMAGE, checkOutImagePath);

            db.update(CommonString.TABLE_COVERAGE_DATA, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            Log.d("Exception in ", "checkOutTime! " + ex.toString());
        }
    }


    public ArrayList<AddittionalGetterSetter> getAdditionalMainStock(String store_id, String categoryId) {
        Cursor cursordata = null;
        ArrayList<AddittionalGetterSetter> productData = new ArrayList<AddittionalGetterSetter>();

        try {

            // cursordata = db.rawQuery("SELECT * FROM Stock_Additional_visibility WHERE Store_Id = '"+store_id + "'categoryId = '"+categoryId + "'", null);
            cursordata = db.rawQuery("Select * from Stock_Additional_visibility_Main " + "where categoryId='" + categoryId + "' and Store_Id='" + store_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    AddittionalGetterSetter sb = new AddittionalGetterSetter();


                    sb.setKey_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("KEY_ID")));

                    sb.setStore_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("Store_Id")));

                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));

                    sb.setBrand_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_id")));

                    sb.setBrand(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("brand_name")));


                    sb.setImage(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url")));
                    sb.setImage2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url2")));
                    sb.setImage3(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("image_url3")));

                    sb.setSku_id(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_id")));

                    sb.setSku(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("sku_name")));
                    sb.setBtn_toogle(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("toggle_value")));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }

    public void InsertADDITIONAL_DISPLAY(ADDITIONAL_DISPLAY_MASTERGetterSetter data) {
        db.delete("ADDITIONAL_DISPLAY_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getDISPLAY_ID().size(); i++) {

                values.put("DISPLAY_ID", data.getDISPLAY_ID().get(i));
                values.put("DISPLAY", data.getDISPLAY().get(i));
                values.put("IMAGE_PATH", data.getIMAGE_PATH().get(i));
                values.put("IMAGE_URL", data.getIMAGE_URL().get(i));
                db.insert("ADDITIONAL_DISPLAY_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " ADDITIONAL_DISPLAY_MASTER " + ex.toString());
        }
    }

    public ArrayList<ADDITIONAL_DISPLAY_MASTERGetterSetter> getADDITIONAL_DISPLAYData(String store_id) {
        Cursor cursordata = null;
        ArrayList<ADDITIONAL_DISPLAY_MASTERGetterSetter> Data = new ArrayList<ADDITIONAL_DISPLAY_MASTERGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT * FROM ADDITIONAL_DISPLAY_MASTER ", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    ADDITIONAL_DISPLAY_MASTERGetterSetter sb = new ADDITIONAL_DISPLAY_MASTERGetterSetter();
                    sb.setDISPLAY_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("DISPLAY_ID")));
                    sb.setDISPLAY(cursordata.getString(cursordata.getColumnIndexOrThrow("DISPLAY")));
                    sb.setIMAGE_URL(cursordata.getString(cursordata.getColumnIndexOrThrow("IMAGE_URL")));
                    sb.setIMAGE_PATH(cursordata.getString(cursordata.getColumnIndexOrThrow("IMAGE_PATH")));


                    Data.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return Data;

    }

    public ArrayList<CoverageBean> getPreviousCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where "
                    + CommonString.KEY_VISIT_DATE + "<>'" + visitdate + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();

                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setReason((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));
                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))))));

                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", " PreviousCoverageData Upload " + e.toString());
            return list;
        }
        return list;
    }

    //check if table is empty
    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA "
                    + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public boolean isMappingPromotionData(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select M.* " +
                    "from MAPPING_PROMOTION M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.STORE_ID = '" + store_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND ", null);

            //dbcursor = db.rawQuery("SELECT * FROM MAPPING_PROMOTION ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public boolean isMappingStockDataMSL_Availability(String category_id, String keyAccount_id, String storeType_id, String class_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            //dbcursor = db.rawQuery("SELECT * FROM MAPPING_STOCK ", null);
            dbcursor = db.rawQuery("Select M.* from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where M.MUST_HAVE=1 AND CA.CATEGORY_ID='" + category_id + "' " +
                    "AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id + "' " +
                    "AND M.CLASS_ID = '" + class_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public boolean isMappingStockDataStockFacing(String category_id, String keyAccount_id, String storeType_id, String class_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            //dbcursor = db.rawQuery("SELECT * FROM MAPPING_STOCK ", null);
            dbcursor = db.rawQuery("Select M.* from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' " +
                    "AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }


    public boolean isMappingT2PData(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM MAPPING_T2P M INNER JOIN BRAND_MASTER BR ON M.BRAND_ID = BR.BRAND_ID " +
                    "INNER JOIN SUB_CATEGORY_MASTER SB ON BR.SUB_CATEGORY_ID = SB.SUB_CATEGORY_ID " +
                    "INNER JOIN CATEGORY_MASTER CA ON SB.CATEGORY_ID = CA.CATEGORY_ID " +
                    "WHERE M.STORE_ID = '" + store_id + "' AND CA.CATEGORY_ID= '" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public boolean isMappingAdditionalPromotionData(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select M.* " +
                    "from MAPPING_ADDITIONAL_PROMOTION M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.STORE_ID = '" + store_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND ", null);

            //dbcursor = db.rawQuery("SELECT * FROM MAPPING_ADDITIONAL_PROMOTION ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public boolean checkAdditionalPromoComplianceData(String store_id, String category_id) {
        Log.d("PromoCompliance ", "AdditionalPromoCompliance data--------------->Start<------------");
        ArrayList<Promo_Compliance_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Additional_Promo_Compliance_Data " +
                    "where CATEGORY_ID='" + category_id + "' and STORE_ID='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        Promo_Compliance_DataGetterSetter sb = new Promo_Compliance_DataGetterSetter();

                        sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                return list.size() > 0;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return false;
        }

        Log.d("Stock_Facing ", "midday---------------------->Stop<-----------");
        return false;
    }

    public void InsertMAPPING_SOS_TARGET(MAPPING_SOS_TARGET_MasterGetterSetter data) {
        db.delete("MAPPING_SOS_TARGET", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSTORE_ID().size(); i++) {

                values.put("STORE_ID", data.getSTORE_ID().get(i));
                values.put("BRAND_ID", data.getBRAND_ID().get(i));
                values.put("SOS_TARGET", data.getSOS_TARGET().get(i));

                db.insert("MAPPING_SOS_TARGET", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " MAPPING_SOS_TARGET " + ex.toString());
        }
    }


    public ArrayList<BrandAvabilityGetterSetter> getBrandAvailbilitydata(String store_id, String category_id, String keyAccount_id, String class_id, String storeType_id) {
        Cursor cursordata = null;
        ArrayList<BrandAvabilityGetterSetter> Data = new ArrayList<BrandAvabilityGetterSetter>();

        try {

            cursordata = db.rawQuery("SELECT DISTINCT BR.BRAND_ID, BR.BRAND FROM SKU_MASTER SK INNER JOIN BRAND_MASTER BR ON SK.BRAND_ID = BR.BRAND_ID " +
                    "INNER JOIN SUB_CATEGORY_MASTER SB ON BR.SUB_CATEGORY_ID = SB.SUB_CATEGORY_ID " +
                    "INNER JOIN CATEGORY_MASTER CA ON SB.CATEGORY_ID = CA.CATEGORY_ID " +
                    "INNER JOIN " +
                    "(SELECT DISTINCT SKU_ID FROM MAPPING_STOCK WHERE KEYACCOUNT_ID = '" + keyAccount_id + "' AND STORETYPE_ID = '" + storeType_id + "' AND CLASS_ID = '" + class_id + "') A " +
                    "ON SK.SKU_ID = A.SKU_ID " +
                    "WHERE CA.CATEGORY_ID = '" + category_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    BrandAvabilityGetterSetter sb = new BrandAvabilityGetterSetter();

                    sb.setBRAND_ID(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND_ID")));

                    sb.setBRAND(cursordata.getString(cursordata.getColumnIndexOrThrow("BRAND")));

                    Data.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return Data;

    }

    public void InsertBrandAvabilitydata(BrandAvabilityGetterSetter data, ArrayList<BrandAvabilityGetterSetter> list) {

        db.delete("Camera_Not_Allowed ", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < list.size(); i++) {

                values.put("STORETYPE_ID", data.getStoreType_id());
                values.put("class_id", data.getClass_id());
                values.put("CATEGORY_ID", data.getCategoryId());
                values.put("keyAccount_id", data.getKeyAccount_id());
                values.put("STORE_ID", data.getStore_id());
                values.put("BRAND_NAME", list.get(i).getBRAND());
                values.put("BRAND_ID", list.get(i).getBRAND_ID());


                db.insert(CommonString.TABLE_INSERT_BRAND_AVAIBILITY_DATA, null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " Camera_Not_Allowed " + ex.toString());
        }
    }


    // get T2P brand data
    public ArrayList<BrandAvabilityGetterSetter> getT2BrandData(String common_id) {

        ArrayList<BrandAvabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_BRAND_AVAIBILITY_DATA + " where " +
                    CommonString.KEY_COMMON_ID + "='" + common_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandAvabilityGetterSetter BG = new BrandAvabilityGetterSetter();

                    BG.setBRAND(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_NAME")));
                    BG.setBRAND_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));

                    list.add(BG);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
        }
        return list;
    }

    //Stock Facing Planogram Tracker
    public void InsertSHELF_MASTER(ShelfMasterGetterSetter data) {
        db.delete("SHELF_MASTER", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSHELF_ID().size(); i++) {

                values.put("SHELF_ID", data.getSHELF_ID().get(i));
                values.put("SHELF", data.getSHELF().get(i));

                db.insert("SHELF_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " SHELF_MASTER " + ex.toString());
        }
    }

    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getSHELF_MASTERData() {
        Cursor cursordata = null;
        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> Data = new ArrayList<>();

        try {
            StockFacing_PlanogramTrackerDataGetterSetter sb1 = new StockFacing_PlanogramTrackerDataGetterSetter();
            sb1.setShelf_id("0");
            sb1.setShelf(context.getString(R.string.select));

            Data.add(sb1);

            cursordata = db.rawQuery("SELECT * FROM SHELF_MASTER ", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter sb = new StockFacing_PlanogramTrackerDataGetterSetter();
                    sb.setShelf_id(cursordata.getString(cursordata.getColumnIndexOrThrow("SHELF_ID")));
                    sb.setShelf(cursordata.getString(cursordata.getColumnIndexOrThrow("SHELF")));

                    Data.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();
            }
        } catch (Exception ex) {
            Log.d("Exception ", " Get SHELF_MASTER " + ex.toString());
        }
        return Data;
    }

    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getStockAndFacingPlanogramDefaultSKUData(
            String category_id, String brand_id, String keyAccount_id, String storeType_id, String class_id) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ,BR.COMPANY_ID " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    /*cd.setStock("");
                    cd.setFacing("");*/
                    cd.setCheckbox_sku("0");

                    list.add(cd);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
        }
        return list;
    }

    public void InsertStock_Facing_PlanogramTracker(String storeId, String categoryId, String company_id, String brand_id, String sub_category_id,
                                                    List<StockFacing_PlanogramTrackerDataGetterSetter> hashMapListHeaderData,
                                                    HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> hashMapListChildData) {

        //Delete child sku data
        for (int i1 = 0; i1 < hashMapListHeaderData.size(); i1++) {
            db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_CHILD,
                    "common_id='" + hashMapListHeaderData.get(i1).getKey_id() + "'", null);
        }
        //Delete Header shelf data
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_HEADER,
                "store_id='" + storeId + "' And category_id='" + categoryId + "' ", null);


        //Insert the data
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {
                StockFacing_PlanogramTrackerDataGetterSetter data1 = hashMapListHeaderData.get(i);

                values1.put("store_id", storeId);
                values1.put("category_id", categoryId);
                values1.put("company_id", company_id);
                values1.put("brand_id", brand_id);
                values1.put("sub_category_id", sub_category_id);
                values1.put("Shelf", data1.getSp_addShelf());
                values1.put("Shelf_id", data1.getSp_addShelf_id());
                values1.put("Shelf_Position", data1.getSp_shelfPosition());

                long pos = db.insert(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_HEADER, null, values1);

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    StockFacing_PlanogramTrackerDataGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("common_id", pos);
                    values.put("Shelf", data1.getSp_addShelf());
                    values.put("Shelf_id", data.getSp_addShelf_id());
                    values.put("Shelf_Position", data1.getSp_shelfPosition());
                    values.put("sku", data.getSku());
                    values.put("sku_id", data.getSku_id());
                    values.put("checkbox_sku", data.getCheckbox_sku());

                    db.insert(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_CHILD, null, values);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert Stock Facing Planogram Tracker " + ex.toString());
        }
    }

    //Stock Facing Planogram Server Upload Data
    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getStockAndFacingPlanogramServerUploadData(String store_id) {
        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select C.Shelf_id,C.Shelf_Position,C.sku_id,C.checkbox_sku,H.category_id,H.company_id,H.brand_id,H.sub_category_id " +
                    "from Stock_Facing_Planogram_Header_Data H " +
                    "inner join Stock_Facing_Planogram_Child_Data C " +
                    "on H.KEY_ID=C.common_id AND H.Shelf_id=C.Shelf_id " +
                    "where H.store_id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setSp_addShelf_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_id")));
                    cd.setSp_shelfPosition(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_Position")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sku_id")));
                    cd.setCheckbox_sku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("checkbox_sku")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("category_id")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("company_id")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("brand_id")));
                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sub_category_id")));

                    list.add(cd);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get Stock Facing Planogram server upload !" + e.toString());
        }
        return list;
    }


    public void InsertStore_wise_camera(Store_wise_camera_DataGetterSetter data) {

        ContentValues values = new ContentValues();
        try {
            values.put("Store_id", data.getStore_id());
            values.put("Category_id", data.getCategory_id());
            values.put("Camera1", data.getCamera1());
            values.put("Camera2", data.getCamera2());
            values.put("Camera3", data.getCamera3());
            values.put("Camera4", data.getCamera4());
            values.put("checkSaveStatus", data.getCheckSaveStatus());

            db.insert(CommonString.TABLE_INSERT_STORE_CAMERA, null, values);
        } catch (Exception ex) {
            Log.d("Exception ", " Store_wise_camera " + ex.toString());
        }
    }

    public Store_wise_camera_DataGetterSetter getStore_wise_camera(String store_id, String category_id) {
        Store_wise_camera_DataGetterSetter data = new Store_wise_camera_DataGetterSetter();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Store_wise_camera " +
                    "where Store_id='" + store_id + "' and Category_id='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    data.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_id")));
                    data.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_id")));
                    data.setCamera1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera1")));
                    data.setCamera2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera2")));
                    data.setCamera3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera3")));
                    data.setCamera4(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera4")));
                    data.setCheckSaveStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("checkSaveStatus")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return data;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get Stock Facing Planogram server upload !" + e.toString());
            return data;
        }
        return data;
    }

    //check if table is empty
    public boolean isStorewiseCameraSave(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select checkSaveStatus from Store_wise_camera " +
                    "where Store_id='" + store_id + "' and Category_id='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow("checkSaveStatus"));

                if (value.equals("1")) {
                    filled = true;
                } else {
                    filled = false;
                }
                //dbcursor.close();
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public void updateStore_wise_camera(Store_wise_camera_DataGetterSetter data) {

        ContentValues values = new ContentValues();
        try {
            //values.put("Store_id", data.getStore_id());
            //values.put("Category_id", data.getCategory_id());
            values.put("Camera1", data.getCamera1());
            values.put("Camera2", data.getCamera2());
            values.put("Camera3", data.getCamera3());
            values.put("Camera4", data.getCamera4());

            db.update(CommonString.TABLE_INSERT_STORE_CAMERA, values,
                    " Store_id='" + data.getStore_id() + "' and Category_id='" + data.getCategory_id() + "'", null);
        } catch (Exception ex) {
            Log.d("Exception ", " Store_wise_camera " + ex.toString());
        }
    }

    public void deleteStore_wise_camera(String store_id, String category_id) {
        db.delete(CommonString.TABLE_INSERT_STORE_CAMERA, null, null);
    }

    //Stock Facing Planogram Server Upload Data
    public ArrayList<Store_wise_camera_DataGetterSetter> getStoreWiseCameraServerUploadData(String store_id) {
        ArrayList<Store_wise_camera_DataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Store_wise_camera " +
                    "where Store_id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Store_wise_camera_DataGetterSetter data = new Store_wise_camera_DataGetterSetter();

                    data.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_id")));
                    data.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_id")));
                    data.setCamera1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera1")));
                    data.setCamera2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera2")));
                    data.setCamera3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera3")));
                    data.setCamera4(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Camera4")));

                    list.add(data);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get Stock Facing Planogram server upload !" + e.toString());
            return list;
        }
        return list;
    }

    //Stock_facing Planogram Add Sku
    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getPlanogramAddSkuHeaderData(
            String category_id, String keyAccount_id, String storeType_id, String class_id) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND,BR.COMPANY_ID ," +
                    " (SELECT SUM(SOS_TARGET) FROM MAPPING_SOS_TARGET WHERE STORE_ID = 1 AND BRAND_ID = BR.BRAND_ID)AS SOS_TARGET " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.KEYACCOUNT_ID = '" + keyAccount_id +
                    "' AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "' AND BR.COMPANY_ID = 1 " +
                    "order by SB.SUB_CATEGORY,BR.COMPANY_ID,BR.BRAND", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get Planogram Add Sku Header!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getPlanogramAddSkuChildData(
            String category_id, String brand_id, String keyAccount_id, String storeType_id, String class_id) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ,BR.COMPANY_ID " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setCheckbox_sku("0");

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());

        }
        return list;
    }

    // get T2P Compliance data
    public ArrayList<T2PGetterSetter> getT2pCompliancedaletedata(String store_id) {

        ArrayList<T2PGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_T2P_COMPLIANCE + " where " +
                    CommonString.KEY_STORE_ID + "='" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    T2PGetterSetter tp = new T2PGetterSetter();

                    tp.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    list.add(tp);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
        }
        return list;
    }


    // get CATEGORY PICTURE data
    public ArrayList<CategoryPictureGetterSetter> getCategoryPicturedata(String categoryId, String key_account_id, String store_type_id, String class_id) {

        ArrayList<CategoryPictureGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("SELECT  DISTINCT SB.SUB_CATEGORY_ID, MIA.IMAGE_ALLOW,SB.SUB_CATEGORY FROM MAPPING_STOCK M INNER JOIN SKU_MASTER SK ON M.SKU_ID = SK.SKU_ID" +
                    " INNER JOIN BRAND_MASTER BR ON SK.BRAND_ID = BR.BRAND_ID" +
                    " INNER JOIN SUB_CATEGORY_MASTER SB ON BR.SUB_CATEGORY_ID = SB.SUB_CATEGORY_ID" +
                    " INNER JOIN CATEGORY_MASTER CA ON SB.CATEGORY_ID = CA.CATEGORY_ID" +
                    " INNER JOIN MAPPING_SUB_CATEGORY_IMAGE_ALLOW MIA ON SB.SUB_CATEGORY_ID=MIA.SUB_CATEGORY_ID " +
                    " WHERE M.KEYACCOUNT_ID = '" + key_account_id + " 'AND M.STORETYPE_ID = '" + store_type_id + "' AND M.CLASS_ID = '" + class_id + "' AND CA.CATEGORY_ID = '" + categoryId + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {

                    CategoryPictureGetterSetter CPGS = new CategoryPictureGetterSetter();

                    CPGS.setSUB_CATEGORY(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    CPGS.setSUB_CATEGORY_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    CPGS.setImage_allow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));

                    CPGS.setSubCategoryCamera1("");
                    CPGS.setSubCategoryCamera2("");

                    list.add(CPGS);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get Planogram Shelf Header After Save !" + e.toString());
            //   return list;
        }
        return list;
    }

    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getPlanogramAddShelfHeaderAfterSaveData(
            String store_id, String category_id) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Planogram_Header_Data " +
                    "where Store_id='" + store_id + "' and category_id='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("category_id")));
                    cd.setSp_addShelf_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_id")));
                    cd.setSp_addShelf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf")));
                    cd.setSp_shelfPosition(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_Position")));

                    list.add(cd);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            Log.d("Exception ", "get Planogram Shelf Header After Save !" + e.toString());

        }
        return list;
    }


    public void InsertCategoryPictureData(CategoryPictureGetterSetter gettersetter, ArrayList<CategoryPictureGetterSetter> skulist, String categoryId) {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        try {

            values.put("Store_Id", gettersetter.getStore_ID());
            values.put("categoryId", categoryId);
            values.put("CategoryImage1", gettersetter.getCategoryImage1());
            values.put("CategoryImage2", gettersetter.getCategoryImage2());
            values.put("CategoryImage3", gettersetter.getCategoryImage3());
            values.put("CategoryImage4", gettersetter.getCategoryImage4());
            values.put("camera_allow", gettersetter.getCamera_allow());


            long key_id = db.insert(CommonString.TABLE_INSERT_CATEGORY_PICTURE, null, values);

            if (skulist != null) {

                for (int j = 0; j < skulist.size(); j++) {
                    values1.put(CommonString.KEY_Common_ID, key_id);
                    values1.put("Store_Id", gettersetter.getStore_ID());
                    values1.put("categoryId", categoryId);
                    values1.put("SUB_CategoryImage1", skulist.get(j).getSubCategoryCamera1());
                    values1.put("SUB_CategoryImage2", skulist.get(j).getSubCategoryCamera2());
                    values1.put("SUB_Category", skulist.get(j).getSUB_CATEGORY());
                    values1.put("SUB_Category_ID", skulist.get(j).getSUB_CATEGORY_ID());


                    db.insert(CommonString.TABLE_INSERT_CATEGORY_PICTURE_LIST, null, values1);
                }
            }

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.getMessage());
        }

    }


    public ArrayList<CategoryPictureGetterSetter> getCategoryPictureData(String store_id, String categoryId) {
        Cursor cursordata = null;
        ArrayList<CategoryPictureGetterSetter> productData = new ArrayList<CategoryPictureGetterSetter>();

        try {

            cursordata = db.rawQuery("Select * from Stock_CATEGORY_PICTURE  " + "where categoryId='" + categoryId + "' and Store_Id='" + store_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    CategoryPictureGetterSetter sb = new CategoryPictureGetterSetter();


                    sb.setKEY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("KEY_ID")));

                    sb.setCamera_allow(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("camera_allow")));

                    sb.setCategoryImage1(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage1")));

                    sb.setCategoryImage2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage2")));

                    sb.setCategoryImage3(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage3")));


                    sb.setCategoryImage4(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage4")));


                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }


    public ArrayList<CategoryPictureGetterSetter> getCategoryPictureListData(String store_id, String categoryId, String key_id) {
        Cursor cursordata = null;
        ArrayList<CategoryPictureGetterSetter> productData = new ArrayList<CategoryPictureGetterSetter>();

        try {

            cursordata = db.rawQuery("Select * from Stock_CATEGORY_PICTURE_LIST  " + "where categoryId = '" + categoryId + "' and Store_Id = '" + store_id + "' and COMMON_ID = '" + key_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    CategoryPictureGetterSetter sb = new CategoryPictureGetterSetter();


                    sb.setSubCategoryCamera1(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_CategoryImage1")));

                    sb.setSubCategoryCamera2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_CategoryImage2")));

                    sb.setSUB_CATEGORY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_Category_ID")));

                    sb.setSUB_CATEGORY(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_Category")));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }


    public ArrayList<CategoryPictureGetterSetter> getCategoryPictureUpload(String store_id) {
        Cursor cursordata = null;
        ArrayList<CategoryPictureGetterSetter> productData = new ArrayList<CategoryPictureGetterSetter>();

        try {

            cursordata = db.rawQuery("Select * from Stock_CATEGORY_PICTURE  " + "where Store_Id='" + store_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    CategoryPictureGetterSetter sb = new CategoryPictureGetterSetter();


                    sb.setKEY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("KEY_ID")));

                    sb.setCamera_allow(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("camera_allow")));

                    sb.setCategoryId(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("categoryId")));


                    sb.setCategoryImage1(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage1")));

                    sb.setCategoryImage2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage2")));

                    sb.setCategoryImage3(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage3")));


                    sb.setCategoryImage4(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("CategoryImage4")));


                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }


    public ArrayList<CategoryPictureGetterSetter> getCategoryPictureListUploaded(String key_id) {
        Cursor cursordata = null;
        ArrayList<CategoryPictureGetterSetter> productData = new ArrayList<CategoryPictureGetterSetter>();

        try {

            cursordata = db.rawQuery("Select * from Stock_CATEGORY_PICTURE_LIST  " + "where COMMON_ID = '" + key_id + "'", null);

            if (cursordata != null) {
                cursordata.moveToFirst();
                while (!cursordata.isAfterLast()) {
                    CategoryPictureGetterSetter sb = new CategoryPictureGetterSetter();

                    sb.setCOMMON_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("COMMON_ID")));

                    sb.setSubCategoryCamera1(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_CategoryImage1")));

                    sb.setSubCategoryCamera2(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_CategoryImage2")));

                    sb.setSUB_CATEGORY_ID(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_Category_ID")));

                    sb.setSUB_CATEGORY(cursordata.getString(cursordata
                            .getColumnIndexOrThrow("SUB_Category")));

                    productData.add(sb);
                    cursordata.moveToNext();
                }
                cursordata.close();

            }


        } catch (Exception ex) {

        }
        return productData;

    }


    public boolean isCategoryPictureData(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_CATEGORY_PICTURE  " + "where categoryId='" + category_id + "' and Store_Id='" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }


    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getStockAndFacingPlanogramAfterSKUData(
            String key_id) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            //Select * from Stock_Facing_Planogram_Child_Data  where Shelf_id=1 and Shelf_Position=3

            dbcursor = db.rawQuery("Select * from Stock_Facing_Planogram_Child_Data  " +
                    "where common_id='" + key_id + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setSp_addShelf_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_id")));
                    cd.setSp_addShelf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf")));
                    cd.setSp_shelfPosition(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_Position")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sku_id")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sku")));
                    cd.setCheckbox_sku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("checkbox_sku")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> getStockAndFacingPlanogramAfterSKU_PerShelfData(
            String shelf_id, String shelf_position) {

        ArrayList<StockFacing_PlanogramTrackerDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Planogram_Child_Data " +
                    "where Shelf_id='" + shelf_id + "' and Shelf_Position='" + shelf_position + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockFacing_PlanogramTrackerDataGetterSetter cd = new StockFacing_PlanogramTrackerDataGetterSetter();

                    cd.setSp_addShelf_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_id")));
                    cd.setSp_addShelf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf")));
                    cd.setSp_shelfPosition(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Shelf_Position")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sku_id")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("sku")));
                    cd.setCheckbox_sku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("checkbox_sku")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public void deletePlanogramListStoreAndCategorywise(String storeId, String categoryId,
                                                        List<StockFacing_PlanogramTrackerDataGetterSetter> hashMapListHeaderData,
                                                        HashMap<StockFacing_PlanogramTrackerDataGetterSetter, ArrayList<StockFacing_PlanogramTrackerDataGetterSetter>> hashMapListChildData) {
        for (int i1 = 0; i1 < hashMapListHeaderData.size(); i1++) {
            db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_CHILD,
                    "common_id='" + hashMapListHeaderData.get(i1).getKey_id() + "'", null);
        }
        //Delete Header shelf data
        db.delete(CommonString.TABLE_INSERT_STOCK_FACING_PLANOGRAM_TRACKER_HEADER,
                "store_id='" + storeId + "' And category_id='" + categoryId + "' ", null);
    }


    // get NonWorking data
    public ArrayList<NonWorkingReasonGetterSetter> getNonWorkingEntryAllowData() {

        ArrayList<NonWorkingReasonGetterSetter> list = new ArrayList<NonWorkingReasonGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM NON_WORKING_REASON WHERE ENTRY_ALLOW=1", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonWorkingReasonGetterSetter sb = new NonWorkingReasonGetterSetter();

                    sb.setREASON_ID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON_ID")));

                    sb.setREASON(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REASON")));

                    sb.setENTRY_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENTRY_ALLOW")));

                    sb.setIMAGE_ALLOW(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

    public ArrayList<CoverageBean> getCoverageWithStoreID_Data(String store_id) {

        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_STORE_ID + "='" + store_id + "'",
                    null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();

                    sb.setStoreId(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_USER_ID))));
                    sb.setInTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IN_TIME)))));
                    sb.setOutTime(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_OUT_TIME)))));
                    sb.setVisitDate((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE))))));
                    sb.setLatitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LATITUDE)))));
                    sb.setLongitude(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)))));
                    sb.setStatus((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_STATUS))))));
                    sb.setImage((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_IMAGE))))));
                    sb.setReason((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON))))));
                    sb.setReasonid((((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_REASON_ID))))));
                    sb.setMID(Integer.parseInt(((dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_ID))))));
                    if (dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)) == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark((((dbcursor.getString(dbcursor
                                .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))))));
                    }

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }
        return list;
    }


    public boolean isPlanogramAddShelfSaveData(String store_id, String category_id) {
        boolean filled = false;
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Planogram_Header_Data " +
                    "where Store_id='" + store_id + "' and category_id='" + category_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public void InsertMappingSubCategoryImageAllow(MappingSubCategoryImageAllowGetterSetter data) {
        db.delete("MAPPING_SUB_CATEGORY_IMAGE_ALLOW", null, null);

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getSUB_CATEGORY_ID().size(); i++) {

                values.put("COUNTRY_ID", data.getCOUNTRY_ID().get(i));
                values.put("SUB_CATEGORY_ID", data.getSUB_CATEGORY_ID().get(i));
                values.put("IMAGE_ALLOW", data.getIMAGE_ALLOW().get(i));

                db.insert("MAPPING_SUB_CATEGORY_IMAGE_ALLOW", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in MAPPING_SUB_CATEGORY_IMAGE_ALLOW " + ex.toString());
        }
    }


    //14-03-2017
    //MSL_Availability_StockFacing
    public ArrayList<MSL_AvailabilityStockFacingGetterSetter> getMSL_Availability_StockFacingHeaderData(
            String category_id, String keyAccount_id, String storeType_id, String class_id) {

        ArrayList<MSL_AvailabilityStockFacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND,BR.COMPANY_ID ," +
                    " (SELECT SUM(SOS_TARGET) FROM MAPPING_SOS_TARGET WHERE STORE_ID = 1 AND BRAND_ID = BR.BRAND_ID)AS SOS_TARGET " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND M.KEYACCOUNT_ID = '" + keyAccount_id +
                    "' AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "'" +
                    "order by SB.SUB_CATEGORY,BR.COMPANY_ID,BR.BRAND", null);

            /*dbcursor = db.rawQuery("Select DISTINCT SB.SUB_CATEGORY_ID,SB.SUB_CATEGORY,BR.BRAND_ID,BR.BRAND " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where   M.MUST_HAVE=1 AND CA.CATEGORY_ID='" + category_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id +
                    "' AND M.STORETYPE_ID = '" + storeType_id + "' AND M.CLASS_ID = '" + class_id + "'" +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);*/

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityStockFacingGetterSetter cd = new MSL_AvailabilityStockFacingGetterSetter();

                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));


                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<MSL_AvailabilityStockFacingGetterSetter> getMSL_Availability_StockFacingSKUData(
            String category_id, String brand_id, String keyAccount_id, String storeType_id, String class_id) {

        ArrayList<MSL_AvailabilityStockFacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ,BR.COMPANY_ID  " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);

            /*dbcursor = db.rawQuery("Select DISTINCT SK.SKU_ID,SK.SKU,SK.MRP,SK.SKU_SEQUENCE,M.MBQ " +
                    "from MAPPING_STOCK M " +
                    "inner join SKU_MASTER SK " +
                    "on M.SKU_ID=SK.SKU_ID " +
                    "inner join BRAND_MASTER BR " +
                    "on SK.BRAND_ID=BR.BRAND_ID " +
                    "inner join SUB_CATEGORY_MASTER SB " +
                    "on BR.SUB_CATEGORY_ID=SB.SUB_CATEGORY_ID " +
                    "inner join CATEGORY_MASTER CA " +
                    "on SB.CATEGORY_ID=CA.CATEGORY_ID " +
                    "where M.MUST_HAVE=1 AND " +
                    "CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id +
                    "' AND M.KEYACCOUNT_ID = '" + keyAccount_id + "' AND M.STORETYPE_ID = '" + storeType_id +
                    "' AND M.CLASS_ID = '" + class_id + "'", null);*/

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityStockFacingGetterSetter cd = new MSL_AvailabilityStockFacingGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));
                    cd.setToggleValue("1");
                    cd.setFacing("");
                    cd.setStock("");

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_AvailabilityHeader!" + e.toString());
            return list;
        }
        return list;
    }

    public ArrayList<MSL_AvailabilityStockFacingGetterSetter> getMSL_Availability_StockFacingSKU_AfterSaveData(
            String category_id, String brand_id, String store_id) {

        ArrayList<MSL_AvailabilityStockFacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Stock_Facing_Data " +
                    "where category_id='" + category_id + "' and Brand_Id='" + brand_id + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityStockFacingGetterSetter cd = new MSL_AvailabilityStockFacingGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOGGLE_VALUE")));
                    cd.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")));
                    cd.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    cd.setCompany_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("COMPANY_ID")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_Availability Sku After Save Data!" + e.toString());
            return list;
        }
        return list;
    }

    public void InsertMSL_Availability_StockFacing(
            String storeId, String categoryId, List<MSL_AvailabilityStockFacingGetterSetter> hashMapListHeaderData,
            HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> hashMapListChildData) {
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    MSL_AvailabilityStockFacingGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("Store_Id", storeId);
                    values.put("Category_Id", categoryId);
                    values.put("Brand_Id", hashMapListHeaderData.get(i).getBrand_id());
                    values.put("SKU_ID", data.getSku_id());
                    values.put("SKU", data.getSku());
                    values.put("SKU_SEQUENCE", data.getSku_sequence());
                    values.put("MBQ", data.getMbq());
                    values.put("TOGGLE_VALUE", data.getToggleValue());
                    values.put("FACING", data.getFacing());
                    values.put("STOCK", data.getStock());
                    values.put("COMPANY_ID", data.getCompany_id());

                    db.insert(CommonString.TABLE_INSERT_MSL_AVAILABILITY_STOCK_FACING, null, values);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability_StockFacing " + ex.toString());
        }
    }

    public void updateMSL_Availability_StockFacing(
            String storeId, String categoryId, List<MSL_AvailabilityStockFacingGetterSetter> hashMapListHeaderData,
            HashMap<MSL_AvailabilityStockFacingGetterSetter, List<MSL_AvailabilityStockFacingGetterSetter>> hashMapListChildData) {

        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < hashMapListHeaderData.size(); i++) {

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    MSL_AvailabilityStockFacingGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("TOGGLE_VALUE", data.getToggleValue());
                    values.put("FACING", data.getFacing());
                    values.put("STOCK", data.getStock());

                    db.update(CommonString.TABLE_INSERT_MSL_AVAILABILITY_STOCK_FACING, values,
                            "Brand_Id ='" + hashMapListHeaderData.get(i).getBrand_id() + "' AND SKU_ID ='" + data.getSku_id() +
                                    "' AND Category_Id='" + categoryId + "' AND Store_Id='" + storeId + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability_StockFacing " + ex.toString());
        }
    }

    @SuppressLint("LongLogTag")
    public boolean checkMsl_Availability_StockFacingData(String store_id, String category_id) {
        Log.d("MSL_Availability ", "Stock data--------------->Start<------------");
        ArrayList<MSL_AvailabilityStockFacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Stock_Facing_Data " +
                    "where category_id='" + category_id + "' and Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                if (dbcursor.moveToFirst()) {
                    do {
                        MSL_AvailabilityStockFacingGetterSetter sb = new MSL_AvailabilityStockFacingGetterSetter();

                        sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                        list.add(sb);
                    } while (dbcursor.moveToNext());
                }
                dbcursor.close();

                return list.size() > 0;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching Records!!!!!!!!!!!!!!!!!!!!!" + e.toString());
            return false;
        }

        Log.d("MSL_Availability_StockFacing ", "midday---------------------->Stop<-----------");
        return false;
    }

    //MSL_Availability_StockFacing  Server Upload Data
    public ArrayList<MSL_AvailabilityStockFacingGetterSetter> getMSL_Availability_StockFacing_UploadServerData(String store_id) {
        ArrayList<MSL_AvailabilityStockFacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Stock_Facing_Data " +
                    "where Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityStockFacingGetterSetter cd = new MSL_AvailabilityStockFacingGetterSetter();

                    cd.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id")));
                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TOGGLE_VALUE")));
                    cd.setFacing(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FACING")));
                    cd.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));

                    list.add(cd);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "get MSL_Availability server upload Data!" + e.toString());
            return list;
        }
        return list;
    }
}
