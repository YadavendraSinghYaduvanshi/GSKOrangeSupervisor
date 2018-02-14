package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 27-10-2017.
 */

public class SkuGroupMasterGetterSetter {

    public String getTable_SKUGROUP_MASTER() {
        return table_SKUGROUP_MASTER;
    }

    public void setTable_SKUGROUP_MASTER(String table_SKUGROUP_MASTER) {
        this.table_SKUGROUP_MASTER = table_SKUGROUP_MASTER;
    }

    String table_SKUGROUP_MASTER;

    ArrayList<String> SKUGROUP_ID = new ArrayList<>();
    ArrayList<String> SKUGROUP_NAME = new ArrayList<>();

    public ArrayList<String> getSKUGROUP_ID() {
        return SKUGROUP_ID;
    }

    public void setSKUGROUP_ID(String SKUGROUP_ID) {
        this.SKUGROUP_ID.add(SKUGROUP_ID);
    }

    public ArrayList<String> getSKUGROUP_NAME() {
        return SKUGROUP_NAME;
    }

    public void setSKUGROUP_NAME(String SKUGROUP_NAME) {
        this.SKUGROUP_NAME.add(SKUGROUP_NAME);
    }

    public ArrayList<String> getSUB_CATEGORY_ID() {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String SUB_CATEGORY_ID) {
        this.SUB_CATEGORY_ID.add(SUB_CATEGORY_ID);
    }

    public ArrayList<String> getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID.add(CATEGORY_ID);
    }

    public ArrayList<String> getSKUGROUP_SEQUENCE() {
        return SKUGROUP_SEQUENCE;
    }

    public void setSKUGROUP_SEQUENCE(String SKUGROUP_SEQUENCE) {
        this.SKUGROUP_SEQUENCE.add(SKUGROUP_SEQUENCE);
    }

    ArrayList<String> SUB_CATEGORY_ID = new ArrayList<>();
    ArrayList<String> CATEGORY_ID = new ArrayList<>();
    ArrayList<String> SKUGROUP_SEQUENCE = new ArrayList<>();

}
