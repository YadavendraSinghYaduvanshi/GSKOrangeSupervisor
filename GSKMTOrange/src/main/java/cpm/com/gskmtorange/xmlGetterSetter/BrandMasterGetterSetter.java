package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 28-12-2016.
 */

public class BrandMasterGetterSetter {

    String table_BRAND_MASTER;

    ArrayList<String> BRAND_ID = new ArrayList<>();
    ArrayList<String> BRAND = new ArrayList<>();
    ArrayList<String> SUB_CATEGORY_ID = new ArrayList<>();
    ArrayList<String> COMPANY_ID = new ArrayList<>();
    ArrayList<String> BRAND_SEQUENCE = new ArrayList<>();

    public String getTable_BRAND_MASTER() {
        return table_BRAND_MASTER;
    }

    public void setTable_BRAND_MASTER(String table_BRAND_MASTER) {
        this.table_BRAND_MASTER = table_BRAND_MASTER;
    }

    public ArrayList<String> getBRAND_ID() {
        return BRAND_ID;
    }

    public void setBRAND_ID(String BRAND_ID) {
        this.BRAND_ID.add(BRAND_ID);
    }

    public ArrayList<String> getBRAND() {
        return BRAND;
    }

    public void setBRAND(String BRAND) {
        this.BRAND.add(BRAND);
    }

    public ArrayList<String> getSUB_CATEGORY_ID() {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String SUB_CATEGORY_ID) {
        this.SUB_CATEGORY_ID.add(SUB_CATEGORY_ID);
    }

    public ArrayList<String> getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(String COMPANY_ID) {
        this.COMPANY_ID.add(COMPANY_ID);
    }

    public ArrayList<String> getBRAND_SEQUENCE() {
        return BRAND_SEQUENCE;
    }

    public void setBRAND_SEQUENCE(String BRAND_SEQUENCE) {
        this.BRAND_SEQUENCE.add(BRAND_SEQUENCE);
    }
}
