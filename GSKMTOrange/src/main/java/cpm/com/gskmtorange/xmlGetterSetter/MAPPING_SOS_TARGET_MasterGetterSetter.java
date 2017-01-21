package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by gagang on 21-01-2017.
 */

public class MAPPING_SOS_TARGET_MasterGetterSetter {
    String table_MAPPING_SOS_TARGET;

    ArrayList<String> STORE_ID = new ArrayList<>();
    ArrayList<String> BRAND_ID = new ArrayList<>();
    ArrayList<String> SOS_TARGET = new ArrayList<>();

    public String getTable_MAPPING_SOS_TARGET() {
        return table_MAPPING_SOS_TARGET;
    }

    public void setTable_MAPPING_SOS_TARGET(String table_MAPPING_SOS_TARGET) {
        this.table_MAPPING_SOS_TARGET = table_MAPPING_SOS_TARGET;
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

    public ArrayList<String> getSOS_TARGET() {
        return SOS_TARGET;
    }

    public void setSOS_TARGET(String SOS_TARGET) {
        this.SOS_TARGET.add(SOS_TARGET);
    }
}
