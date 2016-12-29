package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 28-12-2016.
 */

public class SkuMasterGetterSetter {

    String table_SKU_MASTER;

    ArrayList<String> SKU_ID = new ArrayList<>();
    ArrayList<String> SKU = new ArrayList<>();
    ArrayList<String> BRAND_ID = new ArrayList<>();
    ArrayList<String> MRP = new ArrayList<>();
    ArrayList<String> SKU_SEQUENCE = new ArrayList<>();

    public ArrayList<String> getSKU_ID() {
        return SKU_ID;
    }

    public void setSKU_ID(String SKU_ID) {
        this.SKU_ID.add(SKU_ID);
    }

    public ArrayList<String> getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU.add(SKU);
    }

    public ArrayList<String> getBRAND_ID() {
        return BRAND_ID;
    }

    public void setBRAND_ID(String BRAND_ID) {
        this.BRAND_ID.add(BRAND_ID);
    }

    public ArrayList<String> getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP.add(MRP);
    }

    public ArrayList<String> getSKU_SEQUENCE() {
        return SKU_SEQUENCE;
    }

    public void setSKU_SEQUENCE(String SKU_SEQUENCE) {
        this.SKU_SEQUENCE.add(SKU_SEQUENCE);
    }

    public String getTable_SKU_MASTER() {
        return table_SKU_MASTER;
    }

    public void setTable_SKU_MASTER(String table_SKU_MASTER) {
        this.table_SKU_MASTER = table_SKU_MASTER;
    }
}
