package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 29-12-2016.
 */

public class DisplayMasterGetterSetter {

    String table_DISPLAY_MASTER;

    ArrayList<String> DISPLAY_ID = new ArrayList<>();
    ArrayList<String> DISPLAY = new ArrayList<>();
    ArrayList<String> IMAGE_URL = new ArrayList<>();
    ArrayList<String> IMAGE_PATH = new ArrayList<>();

    public String getTable_DISPLAY_MASTER() {
        return table_DISPLAY_MASTER;
    }

    public void setTable_DISPLAY_MASTER(String table_DISPLAY_MASTER) {
        this.table_DISPLAY_MASTER = table_DISPLAY_MASTER;
    }

    public ArrayList<String> getDISPLAY_ID() {
        return DISPLAY_ID;
    }

    public void setDISPLAY_ID(String DISPLAY_ID) {
        this.DISPLAY_ID.add(DISPLAY_ID);
    }

    public ArrayList<String> getDISPLAY() {
        return DISPLAY;
    }

    public void setDISPLAY(String DISPLAY) {
        this.DISPLAY.add(DISPLAY);
    }

    public ArrayList<String> getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL.add(IMAGE_URL);
    }

    public ArrayList<String> getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public void setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH.add(IMAGE_PATH);
    }
}
