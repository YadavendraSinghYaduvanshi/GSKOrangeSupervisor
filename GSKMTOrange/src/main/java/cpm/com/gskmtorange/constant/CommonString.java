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

    //Alert Messages

    public static final String MESSAGE_FAILURE = "Server Error.Please Access After Some Time";
    public static final String MESSAGE_FALSE = "Invalid User";
    public static final String MESSAGE_CHANGED = "Invalid UserId Or Password / Password Has Been Changed.";

    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Check Your Network Connection";

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

}
