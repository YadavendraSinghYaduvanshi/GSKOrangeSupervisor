package cpm.com.gskmtorange.xmlHandlers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;

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
}
