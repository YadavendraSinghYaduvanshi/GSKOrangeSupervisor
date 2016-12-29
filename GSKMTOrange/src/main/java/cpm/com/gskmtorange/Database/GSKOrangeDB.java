package cpm.com.gskmtorange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.TableBean;

/**
 * Created by ashishc on 29-12-2016.
 */

public class GSKOrangeDB extends SQLiteOpenHelper{


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




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TableBean.getJourneyPlan());

    }

    public void deleteTableWithStoreID(String storeid, String process_id){

    }

    public void deleteAllTables(){

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

        }
        catch (Exception ex) {
            Log.d("Exception in JCP", ex.toString());
        }
    }





















}
