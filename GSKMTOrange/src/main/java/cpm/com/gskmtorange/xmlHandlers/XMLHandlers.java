package cpm.com.gskmtorange.xmlHandlers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;

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
}
