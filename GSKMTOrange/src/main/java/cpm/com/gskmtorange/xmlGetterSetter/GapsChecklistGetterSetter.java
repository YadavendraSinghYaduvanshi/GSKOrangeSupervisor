package cpm.com.gskmtorange.xmlGetterSetter;

/**
 * Created by yadavendras on 04-01-2017.
 */

public class GapsChecklistGetterSetter {

    String checklist, checklist_id, display_id;
    boolean present;

    public String getChecklist() {
        return checklist;
    }

    public void setChecklist(String checklist) {
        this.checklist = checklist;
    }

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getDisplay_id() {
        return display_id;
    }

    public void setDisplay_id(String display_id) {
        this.display_id = display_id;
    }
}
