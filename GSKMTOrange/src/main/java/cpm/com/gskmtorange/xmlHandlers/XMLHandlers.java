package cpm.com.gskmtorange.xmlHandlers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayChecklistMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingDisplayChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPromotionGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NonWorkingReasonGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.STORE_PERFORMANCE_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SubCategoryMasterGetterSetter;

/**
 * Created by yadavendras on 21-12-2016.
 */

public class XMLHandlers {


    // FAILURE XML HANDLER
    public static FailureGetterSetter failureXMLHandler(XmlPullParser xpp,
                                                        int eventType) {
        FailureGetterSetter failureGetterSetter = new FailureGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("STATUS")) {
                        failureGetterSetter.setStatus(xpp.nextText());
                    }
                    if (xpp.getName().equals("ERRORMSG")) {
                        failureGetterSetter.setErrorMsg(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return failureGetterSetter;
    }

    // LOGIN XML HANDLER
    public static LoginGetterSetter loginXMLHandler(XmlPullParser xpp,
                                                    int eventType) {
        LoginGetterSetter lgs = new LoginGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("RIGHT_NAME")) {
                        lgs.setRIGHT_NAME(xpp.nextText());
                    }
                    if (xpp.getName().equals("APP_VERSION")) {
                        lgs.setAPP_VERSION(xpp.nextText());
                    }
                    if (xpp.getName().equals("APP_PATH")) {
                        lgs.setAPP_PATH(xpp.nextText());
                    }
                    if (xpp.getName().equals("CURRENTDATE")) {
                        lgs.setCURRENTDATE(xpp.nextText());
                    }
                    if (xpp.getName().equals("CULTURE_ID")) {
                        lgs.setCULTURE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("CULTURE_NAME")) {
                        lgs.setCULTURE_NAME(xpp.nextText());
                    }
                    if (xpp.getName().equals("NOTICE_URL")) {
                        lgs.setNOTICE_URL(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return lgs;
    }

    // JCP XML HANDLER
    public static JourneyPlanGetterSetter JCPXMLHandler(XmlPullParser xpp, int eventType) {
        JourneyPlanGetterSetter jcpGetterSetter = new JourneyPlanGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {


                    if (xpp.getName().equals("META_DATA")) {
                        jcpGetterSetter.setTable_journey_plan(xpp.nextText());
                    }

                    if (xpp.getName().equals("STORE_ID")) {
                        jcpGetterSetter.setSTORE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("EMP_ID")) {
                        jcpGetterSetter.setEMP_ID(xpp.nextText());
                    }

                    if (xpp.getName().equals("STORE_NAME")) {
                        jcpGetterSetter.setSTORE_NAME(xpp.nextText());
                        //jcpGetterSetter.setSTORE_NAME("Dummy Store");
                    }
                    if (xpp.getName().equals("CITY")) {
                        jcpGetterSetter.setCITY(xpp.nextText());
                    }
                    if (xpp.getName().equals("VISIT_DATE")) {
                        jcpGetterSetter.setVISIT_DATE(xpp.nextText());
                    }
                    if (xpp.getName().equals("ADDRESS")) {
                        jcpGetterSetter.setADDRESS(xpp.nextText());
                        //jcpGetterSetter.setADDRESS("Dummy Address");
                    }
                    if (xpp.getName().equals("UPLOAD_STATUS")) {
                        jcpGetterSetter.setUPLOAD_STATUS(xpp.nextText());
                    }

                    if (xpp.getName().equals("STORETYPE")) {
                        jcpGetterSetter.setSTORETYPE(xpp.nextText());
                    }

                    if (xpp.getName().equals("KEYACCOUNT_ID")) {
                        jcpGetterSetter.setKEYACCOUNT_ID(xpp.nextText());
                    }

                    if (xpp.getName().equals("STORETYPE_ID")) {
                        jcpGetterSetter.setSTORETYPE_ID(xpp.nextText());
                    }

                    if (xpp.getName().equals("CHECKOUT_STATUS")) {
                        jcpGetterSetter.setCHECKOUT_STATUS(xpp.nextText());
//							jcpGetterSetter.setCHECKOUT_STATUS("N");
                    }

                    if (xpp.getName().equals("CLASSIFICATION")) {
                        jcpGetterSetter.setCLASSIFICATION(xpp.nextText());
                    }

                    if (xpp.getName().equals("KEYACCOUNT")) {
                        jcpGetterSetter.setKEYACCOUNT(xpp.nextText());
                    }

                    if (xpp.getName().equals("CLASS_ID")) {
                        jcpGetterSetter.setCLASS_ID(xpp.nextText());
                    }

                    if (xpp.getName().equals("CAMERA_ALLOW")) {
                        jcpGetterSetter.setCAMERA_ALLOW(xpp.nextText());
                    }

                    if (xpp.getName().equals("GEO_TAG")) {
                        jcpGetterSetter.setGEO_TAG(xpp.nextText());
                    }

                    if (xpp.getName().equals("CHANNEL_ID")) {
                        jcpGetterSetter.setCHANNEL_ID(xpp.nextText());
                    }


                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jcpGetterSetter;
    }

    // SKU_MASTER XML HANDLER
    public static SkuMasterGetterSetter skuMasterXMLHandler(XmlPullParser xpp,
                                                            int eventType) {
        SkuMasterGetterSetter sku = new SkuMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        sku.setTable_SKU_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU_ID")) {
                        sku.setSKU_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU")) {
                        sku.setSKU(xpp.nextText());
                    }
                    if (xpp.getName().equals("BRAND_ID")) {
                        sku.setBRAND_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("MRP")) {
                        sku.setMRP(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU_SEQUENCE")) {
                        sku.setSKU_SEQUENCE(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return sku;
    }

    // BRAND_MASTER XML HANDLER
    public static BrandMasterGetterSetter brandMasterXMLHandler(XmlPullParser xpp,
                                                                int eventType) {
        BrandMasterGetterSetter brand = new BrandMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        brand.setTable_BRAND_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("BRAND_ID")) {
                        brand.setBRAND_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("BRAND")) {
                        brand.setBRAND(xpp.nextText());
                    }
                    if (xpp.getName().equals("SUB_CATEGORY_ID")) {
                        brand.setSUB_CATEGORY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("COMPANY_ID")) {
                        brand.setCOMPANY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("BRAND_SEQUENCE")) {
                        brand.setBRAND_SEQUENCE(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return brand;
    }

    // SUB_CATEGORY_MASTER XML HANDLER
    public static SubCategoryMasterGetterSetter subCategoryMasterXMLHandler(XmlPullParser xpp,
                                                                            int eventType) {
        SubCategoryMasterGetterSetter category = new SubCategoryMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        category.setTable_SUB_CATEGORY_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("SUB_CATEGORY_ID")) {
                        category.setSUB_CATEGORY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SUB_CATEGORY")) {
                        category.setSUB_CATEGORY(xpp.nextText());
                    }
                    if (xpp.getName().equals("CATEGORY_ID")) {
                        category.setCATEGORY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SUB_CATEGORY_SEQUENCE")) {
                        category.setSUB_CATEGORY_SEQUENCE(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return category;
    }

    // CATEGORY_MASTER XML HANDLER
    public static CategoryMasterGetterSetter categoryMasterXMLHandler(XmlPullParser xpp,
                                                                      int eventType) {
        CategoryMasterGetterSetter category = new CategoryMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        category.setTable_CATEGORY_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("CATEGORY_ID")) {
                        category.setCATEGORY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("CATEGORY")) {
                        category.setCATEGORY(xpp.nextText());
                    }
                    if (xpp.getName().equals("CATEGORY_SEQUENCE")) {
                        category.setCATEGORY_SEQUENCE(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return category;
    }

    // DISPLAY_MASTER XML HANDLER
    public static DisplayMasterGetterSetter displayMasterXMLHandler(XmlPullParser xpp,
                                                                    int eventType) {
        DisplayMasterGetterSetter display = new DisplayMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        display.setTable_DISPLAY_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("DISPLAY_ID")) {
                        display.setDISPLAY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("DISPLAY")) {
                        display.setDISPLAY(xpp.nextText());
                    }
                    if (xpp.getName().equals("IMAGE_URL")) {
                        display.setIMAGE_URL(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return display;
    }

    // MAPPING_STOCK XML HANDLER
    public static MappingStockGetterSetter mappingStockXMLHandler(XmlPullParser xpp,
                                                                  int eventType) {
        MappingStockGetterSetter stock = new MappingStockGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        stock.setTable_MAPPING_STOCK(xpp.nextText());
                    }
                    if (xpp.getName().equals("KEYACCOUNT_ID")) {
                        stock.setKEYACCOUNT_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORETYPE_ID")) {
                        stock.setSTORETYPE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("CLASS_ID")) {
                        stock.setCLASS_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU_ID")) {
                        stock.setSKU_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("MUST_HAVE")) {
                        stock.setMUST_HAVE(xpp.nextText());
                    }
                    if (xpp.getName().equals("MBQ")) {
                        stock.setMBQ(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return stock;
    }

    // MAPPING_T2P XML HANDLER
    public static MAPPINGT2PGetterSetter mappingT2pXMLHandler(XmlPullParser xpp,
                                                              int eventType) {
        MAPPINGT2PGetterSetter t2p = new MAPPINGT2PGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        t2p.setTable_MAPPING_T2P(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORE_ID")) {
                        t2p.setSTORE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("BRAND_ID")) {
                        t2p.setBRAND_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("DISPLAY_ID")) {
                        t2p.setDISPLAY_ID(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return t2p;
    }

    // DISPLAY_CHECKLIST_MASTER XML HANDLER
    public static DisplayChecklistMasterGetterSetter mappingDisplayChecklistMasterXMLHandler(XmlPullParser xpp,
                                                                                             int eventType) {
        DisplayChecklistMasterGetterSetter checklist = new DisplayChecklistMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        checklist.setTable_DISPLAY_CHECKLIST_MASTER(xpp.nextText());
                    }
                    if (xpp.getName().equals("CHECKLIST_ID")) {
                        checklist.setCHECKLIST_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("CHECKLIST")) {
                        checklist.setCHECKLIST(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return checklist;
    }

    // MAPPING_DISPLAY_CHECKLIST XML HANDLER
    public static MappingDisplayChecklistGetterSetter mappingMappingDisplayChecklistXMLHandler(XmlPullParser xpp,
                                                                                               int eventType) {
        MappingDisplayChecklistGetterSetter checklist = new MappingDisplayChecklistGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        checklist.setTable_MAPPING_DISPLAY_CHECKLIST(xpp.nextText());
                    }
                    if (xpp.getName().equals("CHECKLIST_ID")) {
                        checklist.setCHECKLIST_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("DISPLAY_ID")) {
                        checklist.setDISPLAY_ID(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return checklist;
    }

    // NON_WORKING_REASON XML HANDLER
    public static NonWorkingReasonGetterSetter nonWorkingReasonXMLHandler(XmlPullParser xpp,
                                                                          int eventType) {
        NonWorkingReasonGetterSetter reason = new NonWorkingReasonGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        reason.setTable_NON_WORKING_REASON(xpp.nextText());
                    }
                    if (xpp.getName().equals("REASON_ID")) {
                        reason.setREASON_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("REASON")) {
                        reason.setREASON(xpp.nextText());
                    }
                    if (xpp.getName().equals("ENTRY_ALLOW")) {
                        reason.setENTRY_ALLOW(xpp.nextText());
                    }
                    if (xpp.getName().equals("IMAGE_ALLOW")) {
                        reason.setIMAGE_ALLOW(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return reason;
    }

    // MAPPING_PROMOTION XML HANDLER
    public static MappingPromotionGetterSetter mappingPromotionXMLHandler(XmlPullParser xpp, int eventType) {
        MappingPromotionGetterSetter t2p = new MappingPromotionGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        t2p.setTable_MAPPING_PROMOTION(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORE_ID")) {
                        t2p.setSTORE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU_ID")) {
                        t2p.setSKU_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU")) {
                        t2p.setSKU(xpp.nextText());
                    }
                    if (xpp.getName().equals("PROMO_ID")) {
                        t2p.setPROMO_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("PROMO")) {
                        t2p.setPROMO(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t2p;
    }

    //Gagan Start Code

    //MAPPING_ADDITIONAL_PROMOTION
    public static MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter mappingAdditionalPromotionXMLHandler(XmlPullParser xpp, int eventType) {
        MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter map = new MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        map.setTable_MAPPING_ADDITIONAL_PROMOTION(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORE_ID")) {
                        map.setSTORE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU_ID")) {
                        map.setSKU_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("SKU")) {
                        map.setSKU(xpp.nextText());
                    }
                    if (xpp.getName().equals("PROMO_ID")) {
                        map.setPROMO_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("PROMO")) {
                        map.setPROMO(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static STORE_PERFORMANCE_MasterGetterSetter STORE_PERFORMANCEXMLHandler(XmlPullParser xpp, int eventType) {
        STORE_PERFORMANCE_MasterGetterSetter st = new STORE_PERFORMANCE_MasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        st.setTable_STORE_PERFORMANCE(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORE_ID")) {
                        st.setSTORE_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("CATEGORY_ID")) {
                        st.setCATEGORY_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("PERIOD")) {
                        st.setPERIOD(xpp.nextText());
                    }
                    if (xpp.getName().equals("SOS")) {
                        st.setSOS(xpp.nextText());
                    }
                    if (xpp.getName().equals("T2P")) {
                        st.setT2P(xpp.nextText());
                    }
                    if (xpp.getName().equals("PROMO")) {
                        st.setPROMO(xpp.nextText());
                    }
                    if (xpp.getName().equals("MSL_AVAILABILITY")) {
                        st.setMSL_AVAILABILITY(xpp.nextText());
                    }
                    if (xpp.getName().equals("OSS")) {
                        st.setOSS(xpp.nextText());
                    }
                    if (xpp.getName().equals("ORDERID")) {
                        st.setORDERID(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st;
    }
    //Gagan End Code
}
