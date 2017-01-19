package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by gagang on 12-01-2017.
 */

public class MAPPING_PLANOGRAM_MasterGetterSetter {
    String table_MAPPING_PLANOGRAM;

    ArrayList<String> KEYACCOUNT_ID = new ArrayList<>();
    ArrayList<String> STORETYPE_ID = new ArrayList<>();
    ArrayList<String> CATEGORY_ID = new ArrayList<>();
    ArrayList<String> CLASS_ID = new ArrayList<>();
    ArrayList<String> PLANOGRAM_IMAGE = new ArrayList<>();
    ArrayList<String> IMAGE_PATH = new ArrayList<>();

    public String getTable_MAPPING_PLANOGRAM() {
        return table_MAPPING_PLANOGRAM;
    }

    public void setTable_MAPPING_PLANOGRAM(String table_MAPPING_PLANOGRAM) {
        this.table_MAPPING_PLANOGRAM = table_MAPPING_PLANOGRAM;
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

    public ArrayList<String> getPLANOGRAM_IMAGE() {
        return PLANOGRAM_IMAGE;
    }

    public void setPLANOGRAM_IMAGE(String PLANOGRAM_IMAGE) {
        this.PLANOGRAM_IMAGE.add(PLANOGRAM_IMAGE);
    }

    public ArrayList<String> getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public void setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH.add(IMAGE_PATH);
    }

    public ArrayList<String> getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID.add(CATEGORY_ID);
    }
}
