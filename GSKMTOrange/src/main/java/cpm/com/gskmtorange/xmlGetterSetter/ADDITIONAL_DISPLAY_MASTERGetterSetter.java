package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by ashishc on 18-01-2017.
 */

public class ADDITIONAL_DISPLAY_MASTERGetterSetter {


    ArrayList<String> DISPLAY_ID = new ArrayList<>();

    public String getTable_STORE_ADDITIONAL_DISPLAY() {
        return table_STORE_ADDITIONAL_DISPLAY;
    }

    public void setTable_STORE_ADDITIONAL_DISPLAY(String table_STORE_ADDITIONAL_DISPLAY) {
        this.table_STORE_ADDITIONAL_DISPLAY = table_STORE_ADDITIONAL_DISPLAY;
    }

    String table_STORE_ADDITIONAL_DISPLAY;


    public ArrayList<String> getDISPLAY() {
        return DISPLAY;
    }

    public void setDISPLAY(String DISPLAY) {
        this.DISPLAY.add(DISPLAY);
    }

    public ArrayList<String> getDISPLAY_ID() {
        return DISPLAY_ID;
    }

    public void setDISPLAY_ID(String DISPLAY_ID) {
        this.DISPLAY_ID.add(DISPLAY_ID);
    }

    public ArrayList<String> getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL.add(IMAGE_URL);
    }

    ArrayList<String> DISPLAY = new ArrayList<>();
    ArrayList<String> IMAGE_URL = new ArrayList<>();

    public ArrayList<String> getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public void setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH.add(IMAGE_PATH);
    }

    ArrayList<String> IMAGE_PATH = new ArrayList<>();



}
