package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 03-01-2017.
 */

public class NonWorkingReasonGetterSetter {

    String table_NON_WORKING_REASON;

    ArrayList<String> REASON_ID = new ArrayList<>();
    ArrayList<String> REASON = new ArrayList<>();
    ArrayList<String> ENTRY_ALLOW = new ArrayList<>();
    ArrayList<String> IMAGE_ALLOW = new ArrayList<>();

    public String getTable_NON_WORKING_REASON() {
        return table_NON_WORKING_REASON;
    }

    public void setTable_NON_WORKING_REASON(String table_NON_WORKING_REASON) {
        this.table_NON_WORKING_REASON = table_NON_WORKING_REASON;
    }

    public ArrayList<String> getREASON_ID() {
        return REASON_ID;
    }

    public void setREASON_ID(String REASON_ID) {
        this.REASON_ID.add(REASON_ID);
    }

    public ArrayList<String> getREASON() {
        return REASON;
    }

    public void setREASON(String REASON) {
        this.REASON.add(REASON);
    }

    public ArrayList<String> getENTRY_ALLOW() {
        return ENTRY_ALLOW;
    }

    public void setENTRY_ALLOW(String ENTRY_ALLOW) {
        this.ENTRY_ALLOW.add(ENTRY_ALLOW);
    }

    public ArrayList<String> getIMAGE_ALLOW() {
        return IMAGE_ALLOW;
    }

    public void setIMAGE_ALLOW(String IMAGE_ALLOW) {
        this.IMAGE_ALLOW.add(IMAGE_ALLOW);
    }
}
