package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 03-01-2017.
 */

public class DisplayChecklistMasterGetterSetter {

    String table_DISPLAY_CHECKLIST_MASTER;

    public String getTable_DISPLAY_CHECKLIST_MASTER() {
        return table_DISPLAY_CHECKLIST_MASTER;
    }

    public void setTable_DISPLAY_CHECKLIST_MASTER(String table_DISPLAY_CHECKLIST_MASTER) {
        this.table_DISPLAY_CHECKLIST_MASTER = table_DISPLAY_CHECKLIST_MASTER;
    }

    public ArrayList<String> getCHECKLIST_ID() {
        return CHECKLIST_ID;
    }

    public void setCHECKLIST_ID(String CHECKLIST_ID) {
        this.CHECKLIST_ID.add(CHECKLIST_ID);
    }

    public ArrayList<String> getCHECKLIST() {
        return CHECKLIST;
    }

    public void setCHECKLIST(String CHECKLIST) {
        this.CHECKLIST.add(CHECKLIST);
    }

    ArrayList<String> CHECKLIST_ID = new ArrayList<>();
    ArrayList<String> CHECKLIST = new ArrayList<>();

}
