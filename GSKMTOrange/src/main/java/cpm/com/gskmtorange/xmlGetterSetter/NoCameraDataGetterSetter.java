package cpm.com.gskmtorange.xmlGetterSetter;

/**
 * Created by yadavendras on 26-10-2017.
 */

public class NoCameraDataGetterSetter {

    String SKUGROUP_ID;
    String SKUGROUP_NAME;

    public String getSKUGROUP_ID() {
        return SKUGROUP_ID;
    }

    public void setSKUGROUP_ID(String SKUGROUP_ID) {
        this.SKUGROUP_ID = SKUGROUP_ID;
    }

    public String getSKUGROUP_NAME() {
        return SKUGROUP_NAME;
    }

    public void setSKUGROUP_NAME(String SKUGROUP_NAME) {
        this.SKUGROUP_NAME = SKUGROUP_NAME;
    }

    String CATEGORY_ID;
    String SUB_CATEGORY_ID;
    int facing, row_no, column_no;

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getRow_no() {
        return row_no;
    }

    public void setRow_no(int row_no) {
        this.row_no = row_no;
    }

    public int getColumn_no() {
        return column_no;
    }

    public void setColumn_no(int column_no) {
        this.column_no = column_no;
    }

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getSUB_CATEGORY_ID() {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String SUB_CATEGORY_ID) {
        this.SUB_CATEGORY_ID = SUB_CATEGORY_ID;
    }
}
