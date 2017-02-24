package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 28-12-2016.
 */

public class MappingSubCategoryImageAllowGetterSetter {

    String table_MAPPING_SUB_CATEGORY_IMAGE_ALLOW;

    ArrayList<String> COUNTRY_ID = new ArrayList<>();
    ArrayList<String> SUB_CATEGORY_ID = new ArrayList<>();
    ArrayList<String> IMAGE_ALLOW = new ArrayList<>();

    public String getTable_MAPPING_SUB_CATEGORY_IMAGE_ALLOW() {
        return table_MAPPING_SUB_CATEGORY_IMAGE_ALLOW;
    }

    public void setTable_MAPPING_SUB_CATEGORY_IMAGE_ALLOW(String table_MAPPING_SUB_CATEGORY_IMAGE_ALLOW) {
        this.table_MAPPING_SUB_CATEGORY_IMAGE_ALLOW = table_MAPPING_SUB_CATEGORY_IMAGE_ALLOW;
    }

    public ArrayList<String> getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID.add(COUNTRY_ID);
    }

    public ArrayList<String> getSUB_CATEGORY_ID() {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String SUB_CATEGORY_ID) {
        this.SUB_CATEGORY_ID.add(SUB_CATEGORY_ID);
    }

    public ArrayList<String> getIMAGE_ALLOW() {
        return IMAGE_ALLOW;
    }

    public void setIMAGE_ALLOW(String IMAGE_ALLOW) {
        this.IMAGE_ALLOW.add(IMAGE_ALLOW);
    }
}
