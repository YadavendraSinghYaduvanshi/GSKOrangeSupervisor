package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 05-01-2018.
 */

public class MappingPlanogramCountrywiseGetterSetter {

    String table_MAPPING_COUNTRYWISE_PLANOGRAM;

    ArrayList<String> COUNTRY_ID = new ArrayList<>();
    ArrayList<String> FILE_PATH = new ArrayList<>();
    ArrayList<String> PLANOGRAM_URL = new ArrayList<>();

    public String getTable_MAPPING_COUNTRYWISE_PLANOGRAM() {
        return table_MAPPING_COUNTRYWISE_PLANOGRAM;
    }

    public void setTable_MAPPING_COUNTRYWISE_PLANOGRAM(String table_MAPPING_COUNTRYWISE_PLANOGRAM) {
        this.table_MAPPING_COUNTRYWISE_PLANOGRAM = table_MAPPING_COUNTRYWISE_PLANOGRAM;
    }

    public ArrayList<String> getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID.add(COUNTRY_ID);
    }

    public ArrayList<String> getFILE_PATH() {
        return FILE_PATH;
    }

    public void setFILE_PATH(String FILE_PATH) {
        this.FILE_PATH.add(FILE_PATH);
    }

    public ArrayList<String> getPLANOGRAM_URL() {
        return PLANOGRAM_URL;
    }

    public void setPLANOGRAM_URL(String PLANOGRAM_URL) {
        this.PLANOGRAM_URL.add(PLANOGRAM_URL);
    }
}
