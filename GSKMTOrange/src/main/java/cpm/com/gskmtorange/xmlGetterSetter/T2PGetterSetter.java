package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 02-01-2017.
 */

public class T2PGetterSetter {

    String brand_id, display_id, brand, display, image, remark;
    boolean isPresent;

    ArrayList<GapsChecklistGetterSetter> gapsChecklist = new ArrayList<>();
    ArrayList<SkuGetterSetter> skulist = new ArrayList<>();

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getDisplay_id() {
        return display_id;
    }

    public void setDisplay_id(String display_id) {
        this.display_id = display_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public ArrayList<GapsChecklistGetterSetter> getGapsChecklist() {
        return gapsChecklist;
    }

    public void setGapsChecklist(ArrayList<GapsChecklistGetterSetter> gapsChecklist) {
        this.gapsChecklist = gapsChecklist;
    }

    public ArrayList<SkuGetterSetter> getSkulist() {
        return skulist;
    }

    public void setSkulist(ArrayList<SkuGetterSetter> skulist) {
        this.skulist = skulist;
    }
}
