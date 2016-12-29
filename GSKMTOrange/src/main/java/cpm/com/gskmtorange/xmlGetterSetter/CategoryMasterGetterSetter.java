package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 29-12-2016.
 */

public class CategoryMasterGetterSetter {

    String table_CATEGORY_MASTER;

    ArrayList<String> CATEGORY_ID = new ArrayList<>();
    ArrayList<String> CATEGORY = new ArrayList<>();
    ArrayList<String> CATEGORY_SEQUENCE = new ArrayList<>();

    public String getTable_CATEGORY_MASTER() {
        return table_CATEGORY_MASTER;
    }

    public void setTable_CATEGORY_MASTER(String table_CATEGORY_MASTER) {
        this.table_CATEGORY_MASTER = table_CATEGORY_MASTER;
    }

    public ArrayList<String> getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID.add(CATEGORY_ID);
    }

    public ArrayList<String> getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY.add(CATEGORY);
    }

    public ArrayList<String> getCATEGORY_SEQUENCE() {
        return CATEGORY_SEQUENCE;
    }

    public void setCATEGORY_SEQUENCE(String CATEGORY_SEQUENCE) {
        this.CATEGORY_SEQUENCE.add(CATEGORY_SEQUENCE);
    }
}
