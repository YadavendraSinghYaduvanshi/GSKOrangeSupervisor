package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 29-12-2016.
 */

public class MAPPINGT2PGetterSetter {

    String table_MAPPING_T2P;

    ArrayList<String> STORE_ID = new ArrayList<>();
    ArrayList<String> BRAND_ID = new ArrayList<>();
    ArrayList<String> DISPLAY_ID = new ArrayList<>();
    ArrayList<String> CATEGORY_FIXTURE = new ArrayList<>();

    public String getTable_MAPPING_T2P() {
        return table_MAPPING_T2P;
    }

    public void setTable_MAPPING_T2P(String table_MAPPING_T2P) {
        this.table_MAPPING_T2P = table_MAPPING_T2P;
    }

    public ArrayList<String> getSTORE_ID() {
        return STORE_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID.add(STORE_ID);
    }

    public ArrayList<String> getBRAND_ID() {
        return BRAND_ID;
    }

    public void setBRAND_ID(String BRAND_ID) {
        this.BRAND_ID.add(BRAND_ID);
    }

    public ArrayList<String> getDISPLAY_ID() {
        return DISPLAY_ID;
    }

    public void setDISPLAY_ID(String DISPLAY_ID) {
        this.DISPLAY_ID.add(DISPLAY_ID);
    }

    public ArrayList<String> getCATEGORY_FIXTURE() {
        return CATEGORY_FIXTURE;
    }

    public void setCATEGORY_FIXTURE(String CATEGORY_FIXTURE) {
        this.CATEGORY_FIXTURE.add(CATEGORY_FIXTURE);
    }
}
