package cpm.com.gskmtorange.xmlHandlers;

/**
 * Created by yadavendras on 28-12-2016.
 */

public class TableBean {

    public static String JOURNEY_PLAN;
    public static String SKU_MASTER;
    public static String BRAND_MASTER;

    public static String getJourneyPlan() {
        return JOURNEY_PLAN;
    }

    public static void setJourneyPlan(String journeyPlan) {
        JOURNEY_PLAN = journeyPlan;
    }

    public static String getSkuMaster() {
        return SKU_MASTER;
    }

    public static void setSkuMaster(String skuMaster) {
        SKU_MASTER = skuMaster;
    }

    public static String getBrandMaster() {
        return BRAND_MASTER;
    }

    public static void setBrandMaster(String brandMaster) {
        BRAND_MASTER = brandMaster;
    }
}
