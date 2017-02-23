package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;
import java.util.Comparator;

public class StockFacing_PlanogramTrackerDataGetterSetter {
    String shelf_id, shelf, sp_addShelf_id, sp_addShelf, sp_shelfPosition, checkbox_sku;
    String category_id, sub_category_id, sub_category, brand_id, brand,
            sku_id, sku, mrp, sku_sequence, stock, facing, mbq, company_id, image1, image2, sos_target, key_id;

    public String getShelf_id() {
        return shelf_id;
    }

    public void setShelf_id(String shelf_id) {
        this.shelf_id = shelf_id;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getSp_addShelf_id() {
        return sp_addShelf_id;
    }

    public void setSp_addShelf_id(String sp_addShelf_id) {
        this.sp_addShelf_id = sp_addShelf_id;
    }

    public String getSp_addShelf() {
        return sp_addShelf;
    }

    public void setSp_addShelf(String sp_addShelf) {
        this.sp_addShelf = sp_addShelf;
    }

    public String getSp_shelfPosition() {
        return sp_shelfPosition;
    }

    public void setSp_shelfPosition(String sp_shelfPosition) {
        this.sp_shelfPosition = sp_shelfPosition;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSku_sequence() {
        return sku_sequence;
    }

    public void setSku_sequence(String sku_sequence) {
        this.sku_sequence = sku_sequence;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getMbq() {
        return mbq;
    }

    public void setMbq(String mbq) {
        this.mbq = mbq;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
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

    public String getSos_target() {
        return sos_target;
    }

    public void setSos_target(String sos_target) {
        this.sos_target = sos_target;
    }

    public String getCheckbox_sku() {
        return checkbox_sku;
    }

    public void setCheckbox_sku(String checkbox_sku) {
        this.checkbox_sku = checkbox_sku;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }


    //Comparator Interface
    public static Comparator<StockFacing_PlanogramTrackerDataGetterSetter> shelfComparator =
            new Comparator<StockFacing_PlanogramTrackerDataGetterSetter>() {

                public int compare(StockFacing_PlanogramTrackerDataGetterSetter s1, StockFacing_PlanogramTrackerDataGetterSetter s2) {
                    String shelf1 = s1.getSp_addShelf_id();
                    String shelf2 = s2.getSp_addShelf_id();

                    //ascending order
                    return shelf1.compareTo(shelf2);

                    //descending order
                    //return StudentName2.compareTo(StudentName1);
                }
            };

    public static Comparator<StockFacing_PlanogramTrackerDataGetterSetter> shelfPositionComparator =
            new Comparator<StockFacing_PlanogramTrackerDataGetterSetter>() {

                public int compare(StockFacing_PlanogramTrackerDataGetterSetter s1, StockFacing_PlanogramTrackerDataGetterSetter s2) {
                    String shelf1 = s1.getSp_shelfPosition();
                    String shelf2 = s2.getSp_shelfPosition();

                    //ascending order
                    return shelf1.compareTo(shelf2);

                    //descending order
                    //return StudentName2.compareTo(StudentName1);
                }
            };
}
