package cpm.com.gskmtorange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.GeotaggingBeans;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;


import cpm.com.gskmtorange.constant.CommonString;
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

        db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
        db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);


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

    public void InsertSTOREgeotag(String storeid, double lat, double longitude, String path,String status) {

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

        try {

            values.put("CHECKOUT_STATUS", status);

            db.update(CommonString.KEY_JOURNEY_PLAN, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);

        } catch (Exception ex) {

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

        }
        finally {
            if (dbcursor != null && !dbcursor.isClosed()) {
                dbcursor.close();
            }
        }


        return geodata;

    }


    public void updateGeoTagData(String storeid,String status) {

        try {

            ContentValues values = new ContentValues();
            values.put("GEO_TAG", status);

            int l = db.update(CommonString.TABLE_STORE_GEOTAGGING, values,
                    CommonString.KEY_STORE_ID + "=?", new String[] { storeid });
            System.out.println("update : " + l);
        } catch (Exception e) {
            Log.d("Database Data ", e.toString());

        }
    }

    public void updateDataStatus(String id,String status) {

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
                    if(dbcursor.getString(dbcursor
                            .getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK))==null){
                        sb.setRemark("");
                    }
                    else{
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
            Log.d("Exception ", e.toString());

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

            return db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
        }
        return 0;
    }


}
