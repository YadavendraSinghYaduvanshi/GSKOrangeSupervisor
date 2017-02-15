package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

public class ShelfMasterGetterSetter {
    String table_SHELF_MASTER;

    ArrayList<String> SHELF_ID = new ArrayList<>();
    ArrayList<String> SHELF = new ArrayList<>();

    public String getTable_SHELF_MASTER() {
        return table_SHELF_MASTER;
    }

    public void setTable_SHELF_MASTER(String table_SHELF_MASTER) {
        this.table_SHELF_MASTER = table_SHELF_MASTER;
    }

    public ArrayList<String> getSHELF_ID() {
        return SHELF_ID;
    }

    public void setSHELF_ID(String SHELF_ID) {
        this.SHELF_ID.add(SHELF_ID);
    }

    public ArrayList<String> getSHELF() {
        return SHELF;
    }

    public void setSHELF(String SHELF) {
        this.SHELF.add(SHELF);
    }
}
