package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 30-12-2017.
 */

public class MappingCategoryImageAllowGetterSetter {

    String table_MAPPING_CATEGORY_IMAGE_ALLOW;

    ArrayList<String> COUNTRY_ID = new ArrayList<>();
    ArrayList<String> CATEGORY_ID = new ArrayList<>();
    ArrayList<String> IMAGE1_ALLOW = new ArrayList<>();
    ArrayList<String> IMAGE2_ALLOW = new ArrayList<>();
    ArrayList<String> IMAGE3_ALLOW = new ArrayList<>();
    ArrayList<String> IMAGE4_ALLOW = new ArrayList<>();


    public String getTable_MAPPING_CATEGORY_IMAGE_ALLOW() {
        return table_MAPPING_CATEGORY_IMAGE_ALLOW;
    }

    public void setTable_MAPPING_CATEGORY_IMAGE_ALLOW(String table_MAPPING_CATEGORY_IMAGE_ALLOW) {
        this.table_MAPPING_CATEGORY_IMAGE_ALLOW = table_MAPPING_CATEGORY_IMAGE_ALLOW;
    }

    public ArrayList<String> getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID.add(COUNTRY_ID);
    }

    public ArrayList<String> getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID.add(CATEGORY_ID);
    }

    public ArrayList<String> getIMAGE1_ALLOW() {
        return IMAGE1_ALLOW;
    }

    public void setIMAGE1_ALLOW(String IMAGE1_ALLOW) {
        this.IMAGE1_ALLOW.add(IMAGE1_ALLOW);
    }

    public ArrayList<String> getIMAGE2_ALLOW() {
        return IMAGE2_ALLOW;
    }

    public void setIMAGE2_ALLOW(String IMAGE2_ALLOW) {
        this.IMAGE2_ALLOW.add(IMAGE2_ALLOW);
    }

    public ArrayList<String> getIMAGE3_ALLOW() {
        return IMAGE3_ALLOW;
    }

    public void setIMAGE3_ALLOW(String IMAGE3_ALLOW) {
        this.IMAGE3_ALLOW.add(IMAGE3_ALLOW);
    }

    public ArrayList<String> getIMAGE4_ALLOW() {
        return IMAGE4_ALLOW;
    }

    public void setIMAGE4_ALLOW(String IMAGE4_ALLOW) {
        this.IMAGE4_ALLOW.add(IMAGE4_ALLOW);
    }
}
