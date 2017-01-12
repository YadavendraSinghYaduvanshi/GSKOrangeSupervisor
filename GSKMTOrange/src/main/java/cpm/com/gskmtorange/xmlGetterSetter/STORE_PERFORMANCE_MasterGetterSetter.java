package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by gagang on 11-01-2017.
 */

public class STORE_PERFORMANCE_MasterGetterSetter {
    String table_STORE_PERFORMANCE;

    ArrayList<String> STORE_ID = new ArrayList<>();
    ArrayList<String> CATEGORY_ID = new ArrayList<>();
    ArrayList<String> PERIOD = new ArrayList<>();
    ArrayList<String> SOS = new ArrayList<>();
    ArrayList<String> T2P = new ArrayList<>();
    ArrayList<String> PROMO = new ArrayList<>();
    ArrayList<String> MSL_AVAILABILITY = new ArrayList<>();
    ArrayList<String> OSS = new ArrayList<>();
    ArrayList<String> ORDERID = new ArrayList<>();

    public String getTable_STORE_PERFORMANCE() {
        return table_STORE_PERFORMANCE;
    }

    public void setTable_STORE_PERFORMANCE(String table_STORE_PERFORMANCE) {
        this.table_STORE_PERFORMANCE = table_STORE_PERFORMANCE;
    }

    public ArrayList<String> getSTORE_ID() {
        return STORE_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID.add(STORE_ID);
    }

    public ArrayList<String> getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID.add(CATEGORY_ID);
    }

    public ArrayList<String> getPERIOD() {
        return PERIOD;
    }

    public void setPERIOD(String PERIOD) {
        this.PERIOD.add(PERIOD);
    }

    public ArrayList<String> getSOS() {
        return SOS;
    }

    public void setSOS(String SOS) {
        this.SOS.add(SOS);
    }

    public ArrayList<String> getT2P() {
        return T2P;
    }

    public void setT2P(String t2P) {
        this.T2P.add(t2P);
    }

    public ArrayList<String> getPROMO() {
        return PROMO;
    }

    public void setPROMO(String PROMO) {
        this.PROMO.add(PROMO);
    }

    public ArrayList<String> getMSL_AVAILABILITY() {
        return MSL_AVAILABILITY;
    }

    public void setMSL_AVAILABILITY(String MSL_AVAILABILITY) {
        this.MSL_AVAILABILITY.add(MSL_AVAILABILITY);
    }

    public ArrayList<String> getOSS() {
        return OSS;
    }

    public void setOSS(String OSS) {
        this.OSS.add(OSS);
    }

    public ArrayList<String> getORDERID() {
        return ORDERID;
    }

    public void setORDERID(String ORDERID) {
        this.ORDERID.add(ORDERID);
    }
}
