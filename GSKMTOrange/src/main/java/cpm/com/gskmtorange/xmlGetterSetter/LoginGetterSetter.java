package cpm.com.gskmtorange.xmlGetterSetter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yadavendras on 21-12-2016.
 */

public class LoginGetterSetter implements Serializable{

    String RIGHT_NAME, APP_VERSION, APP_PATH, CURRENTDATE,COUNTRY_ID;

    ArrayList<String> CULTURE_ID = new ArrayList<>();
    ArrayList<String> CULTURE_NAME = new ArrayList<>();
    ArrayList<String> NOTICE_URL = new ArrayList<>();


    public String getRIGHT_NAME() {
        return RIGHT_NAME;
    }

    public void setRIGHT_NAME(String rIGHT_NAME) {
        RIGHT_NAME = rIGHT_NAME;
    }

    public String getAPP_VERSION() {
        return APP_VERSION;
    }

    public void setAPP_VERSION(String aPP_VERSION) {
        APP_VERSION = aPP_VERSION;
    }

    public String getAPP_PATH() {
        return APP_PATH;
    }

    public void setAPP_PATH(String aPP_PATH) {
        APP_PATH = aPP_PATH;
    }

    public String getCURRENTDATE() {
        return CURRENTDATE;
    }

    public void setCURRENTDATE(String cURRENTDATE) {
        CURRENTDATE = cURRENTDATE;
    }

    public ArrayList<String> getCULTURE_ID() {
        return CULTURE_ID;
    }

    public void setCULTURE_ID(String CULTURE_ID) {
        this.CULTURE_ID.add(CULTURE_ID);
    }

    public ArrayList<String> getCULTURE_NAME() {
        return CULTURE_NAME;
    }

    public void setCULTURE_NAME(String CULTURE_NAME) {
        this.CULTURE_NAME.add(CULTURE_NAME);
    }

    public ArrayList<String> getNOTICE_URL() {
        return NOTICE_URL;
    }

    public void setNOTICE_URL(String NOTICE_URL) {
        this.NOTICE_URL.add(NOTICE_URL);
    }

    public String getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID = COUNTRY_ID;
    }
}