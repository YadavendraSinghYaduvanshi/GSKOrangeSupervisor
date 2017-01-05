package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 03-01-2017.
 */

public class MappingPromotionGetterSetter {

    String table_MAPPING_PROMOTION;

    ArrayList<String> STORE_ID = new ArrayList<>();
    ArrayList<String> SKU_ID = new ArrayList<>();
    ArrayList<String> SKU = new ArrayList<>();
    ArrayList<String> PROMO_ID = new ArrayList<>();
    ArrayList<String> PROMO = new ArrayList<>();

    public String getTable_MAPPING_PROMOTION() {
        return table_MAPPING_PROMOTION;
    }

    public void setTable_MAPPING_PROMOTION(String table_MAPPING_PROMOTION) {
        this.table_MAPPING_PROMOTION = table_MAPPING_PROMOTION;
    }

    public ArrayList<String> getSTORE_ID() {
        return STORE_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID.add(STORE_ID);
    }

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

    public ArrayList<String> getPROMO_ID() {
        return PROMO_ID;
    }

    public void setPROMO_ID(String PROMO_ID) {
        this.PROMO_ID.add(PROMO_ID);
    }

    public ArrayList<String> getPROMO() {
        return PROMO;
    }

    public void setPROMO(String PROMO) {
        this.PROMO.add(PROMO);
    }
}
