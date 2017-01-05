package cpm.com.gskmtorange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;

import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;

import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;
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

        //Gagan
        db.execSQL(TableBean.getBrandMaster());
        db.execSQL(TableBean.getSkuMaster());
        db.execSQL(TableBean.getCategoryMaster());
        db.execSQL(TableBean.getSubCategoryMaster());
        db.execSQL(TableBean.getDisplayMaster());
        db.execSQL(TableBean.getMappingStock());
        db.execSQL(TableBean.getMappingT2p());

        db.execSQL(CommonString.CREATE_TABLE_INSERT_MSL_AVAILABILITY);

        db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_HEADER);
        db.execSQL(CommonString.CREATE_TABLE_INSERT_STOCK_FACING_CHILD);
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
                    cd.setCategory_img("category");

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
    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilityHeaderData(String category_id) {
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
                    "where   M.MUST_HAVE=1 AND CA.CATEGORY_ID='" + category_id + "' " +
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

    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilitySKUData(String category_id, String brand_id) {
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
                    "CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MSL_AvailabilityGetterSetter cd = new MSL_AvailabilityGetterSetter();

                    cd.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_ID")));
                    cd.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    cd.setMrp(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MRP")));
                    cd.setSku_sequence(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_SEQUENCE")));
                    cd.setMbq(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MBQ")));
                    cd.setToggleValue("0");

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

    public ArrayList<MSL_AvailabilityGetterSetter> getMSL_AvailabilitySKU_AfterSaveData(String category_id, String brand_id) {
        ArrayList<MSL_AvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Msl_Availability_Data " +
                    "where category_id='" + category_id + "' and Brand_Id='" + brand_id + "'", null);

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

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }
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
                                    "' AND Category_Id='" + categoryId + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }
    }

    //Stock_facing
    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingHeaderData(String category_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
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
                    "where CA.CATEGORY_ID='" + category_id + "' " +
                    "order by SB.SUB_CATEGORY,BR.BRAND", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    cd.setImage1("");
                    cd.setImage2("");

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

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingSKUData(String category_id, String brand_id) {
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
                    "where CA.CATEGORY_ID='" + category_id + "' AND BR.BRAND_ID='" + brand_id + "'", null);

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
                values1.put("SUB_CATEGORY_ID", data1.getSub_category_id());
                values1.put("SUB_CATEGORY", data1.getSub_category());
                values1.put("BRAND_ID", data1.getBrand_id());
                values1.put("BRAND", data1.getBrand());
                values1.put("IMAGE1", data1.getImage1());
                values1.put("IMAGE2", data1.getImage2());

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

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingHeader_AfterSaveData(String category_id) {
        ArrayList<Stock_FacingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select * from Stock_Facing_Header_Data " +
                    "where category_id='" + category_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    Stock_FacingGetterSetter cd = new Stock_FacingGetterSetter();

                    cd.setSub_category_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY_ID")));
                    cd.setSub_category(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SUB_CATEGORY")));
                    cd.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID")));
                    cd.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    cd.setImage1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE1")));
                    cd.setImage2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE2")));

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

    public ArrayList<Stock_FacingGetterSetter> getStockAndFacingSKU_AfterSaveData(String category_id, String brand_id) {
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

                if (list.size() > 0) {
                    return true;
                } else {
                    return false;
                }
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
                        "Category_Id='" + categoryId + "'", null);

                for (int j = 0; j < hashMapListChildData.get(hashMapListHeaderData.get(i)).size(); j++) {
                    Stock_FacingGetterSetter data = hashMapListChildData.get(hashMapListHeaderData.get(i)).get(j);

                    values.put("STOCK_VALUE", data.getStock());
                    values.put("FACEUP_VALUE", data.getFacing());

                    //db.insert(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, null, values);
                    db.update(CommonString.TABLE_INSERT_STOCK_FACING_CHILD, values,
                            "Brand_Id ='" + hashMapListHeaderData.get(i).getBrand_id() + "' AND SKU_ID ='" + data.getSku_id() +
                                    "' AND Category_Id='" + categoryId + "'", null);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Exception ", " in Insert MSL_Availability " + ex.toString());
        }
    }
}
