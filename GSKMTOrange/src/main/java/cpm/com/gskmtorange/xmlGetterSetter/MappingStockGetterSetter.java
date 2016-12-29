package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 29-12-2016.
 */

public class MappingStockGetterSetter {

    String table_MAPPING_STOCK;

    ArrayList<String> KEYACCOUNT_ID = new ArrayList<>();
    ArrayList<String> STORETYPE_ID = new ArrayList<>();
    ArrayList<String> CLASS_ID = new ArrayList<>();
    ArrayList<String> SKU_ID = new ArrayList<>();
    ArrayList<String> MUST_HAVE = new ArrayList<>();
    ArrayList<String> MBQ = new ArrayList<>();

    public String getTable_MAPPING_STOCK() {
        return table_MAPPING_STOCK;
    }

    public void setTable_MAPPING_STOCK(String table_MAPPING_STOCK) {
        this.table_MAPPING_STOCK = table_MAPPING_STOCK;
    }

    public ArrayList<String> getKEYACCOUNT_ID() {
        return KEYACCOUNT_ID;
    }

    public void setKEYACCOUNT_ID(String KEYACCOUNT_ID) {
        this.KEYACCOUNT_ID.add(KEYACCOUNT_ID);
    }

    public ArrayList<String> getSTORETYPE_ID() {
        return STORETYPE_ID;
    }

    public void setSTORETYPE_ID(String STORETYPE_ID) {
        this.STORETYPE_ID.add(STORETYPE_ID);
    }

    public ArrayList<String> getCLASS_ID() {
        return CLASS_ID;
    }

    public void setCLASS_ID(String CLASS_ID) {
        this.CLASS_ID.add(CLASS_ID);
    }

    public ArrayList<String> getSKU_ID() {
        return SKU_ID;
    }

    public void setSKU_ID(String SKU_ID) {
        this.SKU_ID.add(SKU_ID);
    }

    public ArrayList<String> getMUST_HAVE() {
        return MUST_HAVE;
    }

    public void setMUST_HAVE(String MUST_HAVE) {
        this.MUST_HAVE.add(MUST_HAVE);
    }

    public ArrayList<String> getMBQ() {
        return MBQ;
    }

    public void setMBQ(String MBQ) {
        this.MBQ.add(MBQ);
    }
}
