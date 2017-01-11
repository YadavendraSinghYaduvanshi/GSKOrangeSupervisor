package cpm.com.gskmtorange.constant;

import android.os.Environment;

/**
 * Created by yadavendras on 19-12-2016.
 */

public class CommonString {

    // webservice constants

    // preferenec keys
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DATE = "date";
    public static final String KEY_STOREVISITED_STATUS = "STOREVISITED_STATUS";


    public static final String KEY_PATH = "path";
    public static final String KEY_VERSION = "APP_VERSION";


    public static final String KEY_LANGUAGE = "LANGUAGE";
    public static final String KEY_NOTICE_BOARD_LINK = "NOTICE_BOARD_LINK";
    public static final String KEY_LOGIN_DATA = "LOGIN_DATA";
    public static final String KEY_CULTURE_ID = "CULTURE_ID";
    public static final String KEY_STORE_ID = "STORE_ID";
    public static final String KEY_Common_ID = "COMMON_ID";

    public static final String KEY_STORE_NAME = "STORE_NAME";
    public static final String KEY_VISIT_DATE = "VISIT_DATE";
    public static final String KEY_CAMERA_ALLOW = "CAMERA_ALLOW";
    public static final String KEY_CHECKOUT_STATUS = "CHECKOUT_STATUS";
    public static final String KEY_CLASS_ID = "CLASS_ID";
    public static final String KEY_EMP_ID = "EMP_ID";
    public static final String KEY_GEO_TAG = "GEO_TAG";
    public static final String KEY_KEYACCOUNT_ID = "KEYACCOUNT_ID";
    public static final String KEY_STORETYPE_ID = "STORETYPE_ID";
    public static final String KEY_UPLOAD_STATUS = "UPLOAD_STATUS";
    public static final String KEY_STORE_IN_TIME = "STORE_IN_TIME";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_IN_TIME = "IN_TIME";
    public static final String KEY_OUT_TIME = "OUT_TIME";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_COVERAGE_STATUS = "Coverage";
    public static final String KEY_REASON_ID = "REASON_ID";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_COVERAGE_REMARK = "REMARK";
    public static final String KEY_IMAGE = "IMAGE";
    public static final String KEY_IMAGE_URL = "IMAGE_URL";
        public static final String KEY_ID = "Id";
    public static final String KEY_MERCHANDISER_ID = "MERCHANDISER_ID";

    public static final String KEY_BRAND = "BRAND";
    public static final String KEY_BRAND_ID = "BRAND_ID";
    public static final String KEY_DISPLAY_ID = "DISPLAY_ID";
    public static final String KEY_QUANTITY = "QUANTITY";
    public static final String KEY_SKU_ID = "SKU_ID";
    public static final String KEY_SKUNAME = "SKUNAME";
    public static final String UNIQUE_KEY_ID = "UNIQUE_KEY_ID";
    public static final String KEY_CATEGORY_ID = "CATEGORY_ID";
    public static final String KEY_PROCESS_ID = "PROCESS_ID";

    public static final String KEY_JOURNEY_PLAN = "JOURNEY_PLAN";
    public static final String TABLE_INSERT_STOCK_DIALOG = "STOCK_DIALOG";


    public static final String KEY_P = "P";
    public static final String KEY_D = "D";
    public static final String KEY_U = "U";
    public static final String KEY_C = "C";
    public static final String KEY_L = "Leave";
    public static final String KEY_N = "NOT_VISITED";
    public static final String KEY_INVALID = "INVALID";
    public static final String STORE_STATUS_LEAVE = "L";
    public static final String KEY_VALID = "Valid";
    public static final String DATA_DELETE_ALERT_MESSAGE = "Saved data will be lost - Do you want to continue?";
    public static final String KEY_CHECK_IN = "I";
    // webservice constants

    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_FAILURE = "Failure";
    public static final String KEY_FALSE = "False";
    public static final String KEY_CHANGED = "Changed";

    public static final String KEY_NO_DATA = "NODATA";

    public static String URL = "http://gskme.parinaam.in/Gskwebservice.asmx";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String METHOD_LOGIN = "UserLoginDetail";
    public static final String SOAP_ACTION_LOGIN = "http://tempuri.org/"
            + METHOD_LOGIN;

    public static final String METHOD_NAME_UNIVERSAL_DOWNLOAD = "Download_Universal";
    public static final String SOAP_ACTION_UNIVERSAL = "http://tempuri.org/"
            + METHOD_NAME_UNIVERSAL_DOWNLOAD;
    public static final String METHOD_UPLOAD_STOCK_XML_DATA = "DrUploadXml";
    public static final String SOAP_ACTION_UPLOAD_ASSET_XMLDATA = "http://tempuri.org/"
            + METHOD_UPLOAD_STOCK_XML_DATA;
    public static final String METHOD_UPLOAD_ASSET = "Upload_Stock_Availiablity_V1";
    public static final String METHOD_Get_DR_POSM_IMAGES = "GetImageNew";
    public static final String SOAP_ACTION_Get_DR_POSM_IMAGES = "http://tempuri.org/"
            + METHOD_Get_DR_POSM_IMAGES;
    public static final String METHOD_Get_DR_STORE_IMAGES_GEO = "Upload_StoreGeoTag_IMAGES";
    public static final String SOAP_ACTION_DR_STORE_IMAGES_GEO = "http://tempuri.org/"
            + METHOD_Get_DR_STORE_IMAGES_GEO;

    public static final String METHOD_UPLOAD_COVERAGE = "UPLOAD_COVERAGE";

    public static final String METHOD_UPLOAD_COVERAGE_STATUS = "UploadCoverage_Status";

    public static final String SOAP_ACTION = "http://tempuri.org/";

    public static final String SOAP_ACTION_UPLOAD_STORE_COVERAGE = "http://tempuri.org/"
            + METHOD_UPLOAD_COVERAGE;

    public static final String METHOD_UPLOAD_IMAGE = "GetImageWithFolderName";

    public static final String SOAP_ACTION_UPLOAD_IMAGE = "http://tempuri.org/" + METHOD_UPLOAD_IMAGE;

    //Alert Messages


    public static final String MESSAGE_FAILURE = "Server Error.Please Access After Some Time";
    public static final String MESSAGE_FALSE = "Invalid User";
    public static final String MESSAGE_CHANGED = "Invalid UserId Or Password / Password Has Been Changed.";

    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Check Your Network Connection";
    public static final String TABLE_STORE_GEOTAGGING = "STORE_GEOTAGGING";
    public static final String TABLE_COVERAGE_DATA = "COVERAGE_DATA";


    public static final String CREATE_TABLE_COVERAGE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_COVERAGE_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " VARCHAR,USER_ID VARCHAR, " + KEY_IN_TIME + " VARCHAR,"
            + KEY_OUT_TIME + " VARCHAR," + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_LATITUDE + " VARCHAR," + KEY_LONGITUDE + " VARCHAR," + KEY_MERCHANDISER_ID + " VARCHAR,"
            + KEY_COVERAGE_STATUS + " VARCHAR," + KEY_IMAGE + " VARCHAR,"
            + KEY_GEO_TAG + " VARCHAR,"
            + KEY_REASON_ID + " VARCHAR," + KEY_COVERAGE_REMARK
            + " VARCHAR," + KEY_REASON + " VARCHAR)";


    public static final String CREATE_TABLE_STORE_GEOTAGGING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STORE_GEOTAGGING
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_ID"
            + " VARCHAR,"

            + "LATITUDE"
            + " VARCHAR,"

            + "LONGITUDE"
            + " VARCHAR,"

            + "GEO_TAG"
            + " VARCHAR,"

            + "STATUS"
            + " VARCHAR,"

            + "FRONT_IMAGE" + " VARCHAR)";


    //Gagan Code Start

    //File Path
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/GSK_MT_ORANGE_IMAGES/";


    //Table
    public static final String TABLE_INSERT_MSL_AVAILABILITY = "Msl_Availability_Data";

    public static final String CREATE_TABLE_INSERT_MSL_AVAILABILITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_MSL_AVAILABILITY
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "Store_Id"
            + " VARCHAR,"

            + "Category_Id"
            + " VARCHAR,"

            + "Brand_Id"
            + " VARCHAR,"

            + "SKU_ID"
            + " VARCHAR,"

            + "SKU"
            + " VARCHAR,"

            + "SKU_SEQUENCE"
            + " VARCHAR,"

            + "MBQ"
            + " VARCHAR,"

            + "TOGGLE_VALUE"
            + " VARCHAR"

            + ")";


    public static final String TABLE_INSERT_STOCK_FACING_HEADER = "Stock_Facing_Header_Data";

    public static final String CREATE_TABLE_INSERT_STOCK_FACING_HEADER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_STOCK_FACING_HEADER
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "Store_Id"
            + " VARCHAR,"

            + "Category_Id"
            + " VARCHAR,"

            + "SUB_CATEGORY_ID"
            + " VARCHAR,"

            + "SUB_CATEGORY"
            + " VARCHAR,"

            + "BRAND_ID"
            + " VARCHAR,"

            + "BRAND"
            + " VARCHAR,"

            + "IMAGE1"
            + " VARCHAR,"

            + "IMAGE2"
            + " VARCHAR"

            + ")";

    public static final String TABLE_INSERT_STOCK_FACING_CHILD = "Stock_Facing_Child_Data";

    public static final String CREATE_TABLE_INSERT_STOCK_FACING_CHILD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_STOCK_FACING_CHILD
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "Store_Id"
            + " VARCHAR,"

            + "Category_Id"
            + " VARCHAR,"

            + "Brand_Id"
            + " VARCHAR,"

            + "SKU_ID"
            + " VARCHAR,"

            + "SKU"
            + " VARCHAR,"

            + "SKU_SEQUENCE"
            + " VARCHAR,"

            + "MBQ"
            + " VARCHAR,"

            + "COMPANY_ID"
            + " VARCHAR,"

            + "STOCK_VALUE"
            + " VARCHAR,"

            + "FACEUP_VALUE"
            + " VARCHAR"

            + ")";



    public static final String CREATE_TABLE_STOCK_DIALOG = "CREATE TABLE "
            + TABLE_INSERT_STOCK_DIALOG + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STORE_ID + " VARCHAR,"
            + KEY_BRAND_ID + " VARCHAR," + KEY_BRAND + " VARCHAR,"
            + KEY_DISPLAY_ID +" VARCHAR,"
            + KEY_SKU_ID + " VARCHAR," + KEY_QUANTITY + " VARCHAR," + KEY_Common_ID + " VARCHAR,"
            + KEY_CATEGORY_ID + " VARCHAR,"
            + KEY_SKUNAME + " VARCHAR,"
            + KEY_PROCESS_ID + " VARCHAR)";


    public static final String TABLE_INSERT_STOCK_ADDITIONAL = "Stock_Additional_visibility";

    public static final String CREATE_TABLE_INSERT_STOCK_ADDITIONAL_VISIBILITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_STOCK_ADDITIONAL
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "Store_Id"
            + " VARCHAR,"

            + "brand_name"
            + " VARCHAR,"

            + "brand_id"
            + " VARCHAR,"

            + "image_url"
            + " VARCHAR,"

            + "sku_id"
            + " VARCHAR,"

            + "sku_name"
            + " VARCHAR,"

            + "toggle_value"
            + " VARCHAR,"

            + "IMAGE2"
            + " VARCHAR"

            + ")";

    public static final String TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE = "Additional_Promo_Compliance_Data";

    public static final String CREATE_TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_ADDITIONAL_PROMO_COMPLIANCE
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_ID"
            + " INTEGER,"

            + "CATEGORY_ID"
            + " INTEGER,"

            + "SKU_ID"
            + " INTEGER,"

            + "SKU"
            + " VARCHAR,"

            + "PROMO_ID"
            + " INTEGER,"

            + "PROMO"
            + " VARCHAR,"

            + "IN_STOCK_VALUE"
            + " INTEGER,"

            + "PROMO_ANNOUNCER_VALUE"
            + " INTEGER,"

            + "RUNNING_POS_VALUE"
            + " INTEGER"

            + ")";

    public static final String TABLE_INSERT_PROMO_SKU = "Promo_SKU_Data";

    public static final String CREATE_TABLE_INSERT_PROMO_SKU = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_PROMO_SKU
            + "("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"

            + "STORE_ID"
            + " INTEGER,"

            + "CATEGORY_ID"
            + " INTEGER,"

            + "SKU_ID"
            + " INTEGER,"

            + "SKU"
            + " VARCHAR,"

            + "PROMO_ID"
            + " INTEGER,"

            + "PROMO"
            + " VARCHAR,"

            + "IN_STOCK_VALUE"
            + " INTEGER,"

            + "PROMO_ANNOUNCER_VALUE"
            + " INTEGER,"

            + "RUNNING_POS_VALUE"
            + " INTEGER"

            + ")";

    //Gagan Code End

}
