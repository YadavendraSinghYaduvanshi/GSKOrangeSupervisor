package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 03-01-2017.
 */

public class MappingDisplayChecklistGetterSetter {

    String table_MAPPING_DISPLAY_CHECKLIST;

    ArrayList<String> DISPLAY_ID = new ArrayList<>();
    ArrayList<String> CHECKLIST_ID = new ArrayList<>();

    public String getTable_MAPPING_DISPLAY_CHECKLIST() {
        return table_MAPPING_DISPLAY_CHECKLIST;
    }

    public void setTable_MAPPING_DISPLAY_CHECKLIST(String table_MAPPING_DISPLAY_CHECKLIST) {
        this.table_MAPPING_DISPLAY_CHECKLIST = table_MAPPING_DISPLAY_CHECKLIST;
    }

    public ArrayList<String> getDISPLAY_ID() {
        return DISPLAY_ID;
    }

    public void setDISPLAY_ID(String DISPLAY_ID) {
        this.DISPLAY_ID.add(DISPLAY_ID);
    }

    public ArrayList<String> getCHECKLIST_ID() {
        return CHECKLIST_ID;
    }

    public void setCHECKLIST_ID(String CHECKLIST_ID) {
        this.CHECKLIST_ID.add(CHECKLIST_ID);
    }
}
