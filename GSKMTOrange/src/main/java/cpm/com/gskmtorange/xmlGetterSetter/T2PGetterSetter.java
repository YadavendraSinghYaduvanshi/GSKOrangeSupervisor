package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

import cpm.com.gskmtorange.GetterSetter.BrandAvabilityGetterSetter;

/**
 * Created by yadavendras on 02-01-2017.
 */

public class T2PGetterSetter {

    String brand_id, display_id, brand, display, image, remark, key_id, category_id, ref_image_url, ref_image_path, image1, image2, category_fixture;
    boolean isPresent;
    int present = -1;


    ArrayList<GapsChecklistGetterSetter> gapsChecklist = new ArrayList<>();
    ArrayList<SkuGetterSetter> skulist = new ArrayList<>();

    public ArrayList<BrandAvabilityGetterSetter> getBrandlist() {
        return brandlist;
    }

    public void setBrandlist(ArrayList<BrandAvabilityGetterSetter> brandlist) {
        this.brandlist = brandlist;
    }

    ArrayList<BrandAvabilityGetterSetter> brandlist = new ArrayList<>();

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

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getRef_image_url() {
        return ref_image_url;
    }

    public void setRef_image_url(String ref_image_url) {
        this.ref_image_url = ref_image_url;
    }

    public String getRef_image_path() {
        return ref_image_path;
    }

    public void setRef_image_path(String ref_image_path) {
        this.ref_image_path = ref_image_path;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getCategory_fixture() {
        return category_fixture;
    }

    public void setCategory_fixture(String category_fixture) {
        this.category_fixture = category_fixture;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }
}
