package cpm.com.gskmtorange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayChecklistMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.GapsChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingDisplayChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPromotionGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.T2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;

import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;

import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SubCategoryMasterGetterSetter;

/**
 * Created by ashishc on 29-12-2016.
 */

public class GSKOrangeDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GSK_ORANGE";
    public static final int DATABASE_VERSION = 13;
    private SQLiteDatabase db;
    TableBean tableBean;

    public GSKOrangeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

        db.execSQL(TableBean.getJourneyPlan());
        db.execSQL(TableBean.getBrandMaster());
        db.execSQL(TableBean.getSkuMaster());
        db.execSQL(TableBean.getCategoryMaster());
        db.execSQL(TableBean.getSubCategoryMaster());
        db.execSQL(TableBean.getDisplayMaster());

        db.execSQL(TableBean.getMappingStock());
        db.execSQL(TableBean.getMappingT2p());
        db.execSQL(TableBean.getDisplayChecklistMaster());
        db.execSQL(TableBean.getMappingDisplayChecklist());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableBean.getJourneyPlan());
    }

    public void deleteTableWithStoreID(String storeid, String process_id) {
    }

    public void deleteAllTables() {

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

    public ArrayList<T2PGetterSetter> getT2PDefaultData(String store_id){

        ArrayList<T2PGetterSetter> t2PList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select  BM.BRAND As BRAND, BM.BRAND_ID As BRAND_ID, DM.DISPLAY As DISPLAY, DM.DISPLAY_ID As DISPLAY_ID from BRAND_MASTER BM INNER JOIN MAPPING_T2P T ON BM.BRAND_ID = T.BRAND_ID INNER JOIN  DISPLAY_MASTER DM  ON T.DISPLAY_ID= DM.DISPLAY_ID WHERE T.STORE_ID = '"+ store_id +"'",null);
            if(dbcursor != null){
                dbcursor.moveToFirst();
                while(!dbcursor.isAfterLast()){

                    T2PGetterSetter t2p = new T2PGetterSetter();

                    t2p.setBrand_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND_ID")));

                    t2p.setBrand(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("BRAND")));

                    t2p.setDisplay_id(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DISPLAY_ID")));

                    t2p.setDisplay(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow("DISPLAY")));

                    t2p.setImage("");

                    t2p.setRemark("");

                    t2p.isPresent();

                    t2PList.add(t2p);

                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return t2PList;
            }
        }
        catch (Exception e){

            Log.d("Exception get T2P", e.toString());
            return t2PList;
        }

        return t2PList;
    }

    //get Gaps data
    public ArrayList<GapsChecklistGetterSetter> getGapsDefaultData(String display_id){

        ArrayList<GapsChecklistGetterSetter> checkList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select DC.CHECKLIST_ID As CHECKLIST_ID, DC.CHECKLIST As CHECKLIST, C.DISPLAY_ID As DISPLAY_ID from MAPPING_DISPLAY_CHECKLIST C INNER JOIN DISPLAY_CHECKLIST_MASTER DC ON C.CHECKLIST_ID= DC.CHECKLIST_ID WHERE C.DISPLAY_ID = '"+ display_id +"'",null);
            if(dbcursor != null){
                dbcursor.moveToFirst();
                while(!dbcursor.isAfterLast()){

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
        }
        catch (Exception e){

            Log.d("Exception get T2P", e.toString());
            return checkList;
        }

        return checkList;
    }

    //get Brand data for T2P
    public ArrayList<BrandMasterGetterSetter> getBrandT2PData(String store_type_id, String class_id, String key_account_id ){

        ArrayList<BrandMasterGetterSetter> brandList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * FROM(SELECT DISTINCT BR.BRAND_ID, SCM.SUB_CATEGORY||'-'||BR.BRAND AS BRAND FROM MAPPING_STOCK MS INNER JOIN SKU_MASTER SM ON MS.SKU_ID = SM.SKU_ID  INNER JOIN BRAND_MASTER BR ON SM.BRAND_ID=BR.BRAND_ID INNER JOIN SUB_CATEGORY_MASTER SCM ON  BR.SUB_CATEGORY_ID = SCM.SUB_CATEGORY_ID WHERE MS.KEYACCOUNT_ID ='"+key_account_id +"' AND STORETYPE_ID ='" + store_type_id + "' AND CLASS_ID = '"+ class_id +"') As Brand",null);
            if(dbcursor != null){
                dbcursor.moveToFirst();
                while(!dbcursor.isAfterLast()){

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
        }
        catch (Exception e){

            Log.d("Exception get T2P", e.toString());
            return brandList;
        }

        return brandList;
    }

    //get Sku data for T2P
    public ArrayList<SkuGetterSetter> getSkuT2PData(String store_type_id, String class_id, String key_account_id , String brand_id){

        ArrayList<SkuGetterSetter> skuList = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT DISTINCT SM.SKU, SM.SKU_ID, BR.BRAND_ID FROM MAPPING_STOCK MS INNER JOIN SKU_MASTER SM ON MS.SKU_ID = SM.SKU_ID  INNER JOIN BRAND_MASTER BR ON SM.BRAND_ID=BR.BRAND_ID INNER JOIN SUB_CATEGORY_MASTER SCM ON  BR.SUB_CATEGORY_ID = SCM.SUB_CATEGORY_ID WHERE MS.KEYACCOUNT_ID ='"+key_account_id +"' AND STORETYPE_ID ='" + store_type_id + "' AND CLASS_ID = '"+ class_id +"' AND SM.BRAND_ID='"+ brand_id + "'",null);
            if(dbcursor != null){
                dbcursor.moveToFirst();
                while(!dbcursor.isAfterLast()){

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
        }
        catch (Exception e){

            Log.d("Exception get T2P", e.toString());
            return skuList;
        }

        return skuList;
    }

    public ArrayList<StoreBean> getStoreData(String date) {


        ArrayList<StoreBean> list = new ArrayList<StoreBean>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT  * from JOURNEY_PLAN  where VISIT_DATE ='" + date + "'", null);

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

    //Gagan Goel
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

                db.insert("DISPLAY_MASTER", null, values);
            }
        } catch (Exception ex) {
            Log.d("Exception ", " in DISPLAY_MASTER " + ex.toString());
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
}
