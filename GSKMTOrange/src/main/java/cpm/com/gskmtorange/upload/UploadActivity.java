package cpm.com.gskmtorange.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MSL_AvailabilityGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Promo_Compliance_DataGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.Stock_FacingGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.FailureXMLHandler;

public class UploadActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    GSKOrangeDB db;
    ArrayList<CoverageBean> coverageList;

    private FailureGetterSetter failureGetterSetter = null;
    private SharedPreferences preferences;
    String date, userId, app_version;

    StoreBean storeData;
    String datacheck = "";
    String[] words;
    String validity;
    int mid;
    private int factor, k = 0;
    String errormsg = "", Path;

    Data data;

    ArrayList<MSL_AvailabilityGetterSetter> msl_availabilityList;
    ArrayList<Stock_FacingGetterSetter> stock_facingHeaderList, stock_facingChildList;
    ArrayList<Promo_Compliance_DataGetterSetter> promotionSkuList, additionalPromotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);

        db = new GSKOrangeDB(this);
        db.open();

        Path = CommonString.FILE_PATH;

        //start upload
        new UploadTask(this).execute();
    }

    class Data {
        int value;
        String name;
    }

    private class UploadTask extends AsyncTask<Void, Data, String> {
        private Context context;

        UploadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("Uploading Data");
            dialog.setCancelable(false);
            dialog.show();

            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub

            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                data = new Data();
                coverageList = db.getCoverageData(date);

                if (coverageList.size() > 0) {
                    if (coverageList.size() == 1) {
                        factor = 50;
                    } else {
                        factor = 100 / (coverageList.size());
                    }
                }

                for (int i = 0; i < coverageList.size(); i++) {

                    storeData = db.getSpecificStoreData(date, coverageList.get(i).getStoreId());
                    if (storeData.getSTORE_ID() != null) {

                        /*if (storeData.getCHECKOUT_STATUS().equals(CommonString.KEY_C) || storeData.getCHECKOUT_STATUS().equals(CommonString.KEY_L) ||
                                !storeData.getUPLOAD_STATUS().equals(CommonString.KEY_U)) {*/

                        String camera_allow = storeData.getCAMERA_ALLOW();

                        String onXML = "[DATA]"
                                + "[USER_DATA]"
                                + "[STORE_CD]" + coverageList.get(i).getStoreId() + "[/STORE_CD]"
                                + "[VISIT_DATE]" + coverageList.get(i).getVisitDate() + "[/VISIT_DATE]"
                                + "[LATITUDE]" + coverageList.get(i).getLatitude() + "[/LATITUDE]"
                                + "[APP_VERSION]" + app_version + "[/APP_VERSION]"
                                + "[LONGITUDE]" + coverageList.get(i).getLongitude() + "[/LONGITUDE]"
                                + "[IN_TIME]" + coverageList.get(i).getInTime() + "[/IN_TIME]"
                                //+ "[OUT_TIME]" + coverageList.get(i).getOutTime() + "[/OUT_TIME]"
                                + "[OUT_TIME]" + "00:00" + "[/OUT_TIME]"
                                + "[UPLOAD_STATUS]" + "N" + "[/UPLOAD_STATUS]"
                                + "[USER_ID]" + userId + "[/USER_ID]"
                                + "[IMAGE_URL]" + coverageList.get(i).getImage() + "[/IMAGE_URL]"
                                + "[REASON_ID]" + coverageList.get(i).getReasonid() + "[/REASON_ID]"
                                + "[REASON_REMARK]" + coverageList.get(i).getRemark() + "[/REASON_REMARK]"
                                + "[CAMERA_ALLOWED]" + camera_allow + "[/CAMERA_ALLOWED]"
                                + "[/USER_DATA]"
                                + "[/DATA]";

                        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_COVERAGE);
                        request.addProperty("onXML", onXML);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_STORE_COVERAGE, envelope);

                        Object result = (Object) envelope.getResponse();

                        datacheck = result.toString();
                        words = datacheck.split("\\;");
                        validity = (words[0]);

                        if (validity.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                            db.updateCoverageStatus(coverageList.get(i).getStoreId(), CommonString.KEY_P);

                            db.updateStoreStatusOnLeave(coverageList.get(i).getStoreId(), date, CommonString.KEY_P);
                        } else {
                            return CommonString.METHOD_UPLOAD_COVERAGE;
                        }

                        mid = Integer.parseInt((words[1]));


                        //MSL_Availability
                        String mslAvailability_xml = "";
                        onXML = "";
                        msl_availabilityList = db.getMSL_AvailabilityUploadServerData(coverageList.get(i).getStoreId());

                        if (msl_availabilityList.size() > 0) {
                            for (int j = 0; j < msl_availabilityList.size(); j++) {
                                if (!msl_availabilityList.get(j).getSku_id().equals("0")) {

                                    onXML = "[MSL_AVAILABILITY_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[USER_ID]" + userId + "[/USER_ID]"
                                            + "[CATEGORY_ID]" + Integer.parseInt(msl_availabilityList.get(j).getCategory_id()) + "[/CATEGORY_ID]"
                                            + "[BRAND_ID]" + Integer.parseInt(msl_availabilityList.get(j).getBrand_id()) + "[/BRAND_ID]"
                                            + "[SKU_ID]" + Integer.parseInt(msl_availabilityList.get(j).getSku_id()) + "[/SKU_ID]"
                                            //+ "[SKU]" + msl_availabilityList.get(j).getSku() + "[/SKU]"
                                            + "[TOGGLE_VALUE]" + Integer.parseInt(msl_availabilityList.get(j).getToggleValue()) + "[/TOGGLE_VALUE]"
                                            + "[/MSL_AVAILABILITY_DATA]";

                                    mslAvailability_xml = mslAvailability_xml + onXML;
                                }
                            }

                            final String sos_xml = "[DATA]" + mslAvailability_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "MSL_AVAILABILITY_DATA");
                            request.addProperty("USERNAME", userId);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }
                        }
                        data.value = 10;
                        data.name = "MSL_Availability Uploading";
                        publishProgress(data);


                        //Stock and Facing
                        String stock_facing_xml = "";
                        onXML = "";
                        stock_facingHeaderList = db.getStockAndFacingHeaderServerUploadData(coverageList.get(i).getStoreId());

                        if (stock_facingHeaderList.size() > 0) {
                            for (int i1 = 0; i1 < stock_facingHeaderList.size(); i1++) {
                                if (!stock_facingHeaderList.get(i1).getCategory_id().equals("0")) {

                                    stock_facingChildList = db.getStockAndFacingSKUServerUploadData(
                                            stock_facingHeaderList.get(i1).getCategory_id(), stock_facingHeaderList.get(i1).getBrand_id());

                                    String stockChildXML = "";
                                    for (int j = 0; j < stock_facingChildList.size(); j++) {

                                        stockChildXML = "[STOCK_FACING_DATA]"
                                                + "[MID]" + mid + "[/MID]"
                                                + "[USER_ID]" + userId + "[/USER_ID]"
                                                + "[CATEGORY_ID]" + Integer.parseInt(stock_facingHeaderList.get(i1).getCategory_id()) + "[/CATEGORY_ID]"
                                                + "[SUB_CATEGORY_ID]" + Integer.parseInt(stock_facingHeaderList.get(i1).getSub_category_id()) + "[/SUB_CATEGORY_ID]"
                                                + "[BRAND_ID]" + Integer.parseInt(stock_facingHeaderList.get(i1).getBrand_id()) + "[/BRAND_ID]"
                                                + "[IAMGE1]" + stock_facingHeaderList.get(i1).getImage1() + "[/IAMGE1]"
                                                + "[IAMGE2]" + stock_facingHeaderList.get(i1).getImage2() + "[/IAMGE2]"
                                                + "[SKU_ID]" + Integer.parseInt(stock_facingChildList.get(j).getSku_id()) + "[/SKU_ID]"
                                                //+ "[SKU]" + stock_facingChildList.get(j).getSku() + "[/SKU]"
                                                + "[STOCK]" + Integer.parseInt(stock_facingChildList.get(j).getStock()) + "[/STOCK]"
                                                + "[FACEUP]" + Integer.parseInt(stock_facingChildList.get(j).getFacing()) + "[/FACEUP]"
                                                + "[/STOCK_FACING_DATA]";

                                        onXML = onXML + stockChildXML;
                                    }
                                    stock_facing_xml = stock_facing_xml + onXML;
                                }
                            }

                            final String sos_xml = "[DATA]" + stock_facing_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "STOCK_FACING_DATA");
                            request.addProperty("USERNAME", userId);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }
                        }
                        data.value = 20;
                        data.name = "Stock Facing Uploading";
                        publishProgress(data);


                        //Promo Compliance - Promotion Data
                        String promoition_data_xml = "";
                        onXML = "";
                        promotionSkuList = db.getPromoComplianceSkuServerUploadData(coverageList.get(i).getStoreId());

                        if (promotionSkuList.size() > 0) {
                            for (int i1 = 0; i1 < promotionSkuList.size(); i1++) {
                                if (!promotionSkuList.get(i1).getCategory_id().equals("0")) {

                                    onXML = "[PROMOTION_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[USER_ID]" + userId + "[/USER_ID]"
                                            + "[CATEGORY_ID]" + Integer.parseInt(promotionSkuList.get(i1).getCategory_id()) + "[/CATEGORY_ID]"
                                            + "[PROMO_ID]" + Integer.parseInt(promotionSkuList.get(i1).getPromo_id()) + "[/PROMO_ID]"
                                            //+ "[SKU]" + promotionSkuList.get(i1).getSku() + "[/SKU]"
                                            + "[SKU_ID]" + Integer.parseInt(promotionSkuList.get(i1).getSku_id()) + "[/SKU_ID]"
                                            + "[IN_STOCK]" + Integer.parseInt(promotionSkuList.get(i1).getIn_stock()) + "[/IN_STOCK]"
                                            + "[PROMO_ANNOUNCER]" + Integer.parseInt(promotionSkuList.get(i1).getPromo_announcer()) + "[/PROMO_ANNOUNCER]"
                                            + "[RUNNING_POS]" + Integer.parseInt(promotionSkuList.get(i1).getRunning_pos()) + "[/RUNNING_POS]"
                                            + "[/PROMOTION_DATA]";

                                    promoition_data_xml = promoition_data_xml + onXML;
                                }
                            }

                            final String sos_xml = "[DATA]" + promoition_data_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "PROMOTION_DATA");
                            request.addProperty("USERNAME", userId);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }
                        }
                        data.value = 30;
                        data.name = "Promotion Data Uploading";
                        publishProgress(data);


                        //Promo Compliance -Additional Promotion Data
                        String additional_promoition_data_xml = "";
                        onXML = "";
                        additionalPromotionList = db.getAdditionalPromotionServerUploadData(coverageList.get(i).getStoreId());

                        if (additionalPromotionList.size() > 0) {
                            for (int i1 = 0; i1 < additionalPromotionList.size(); i1++) {
                                if (!additionalPromotionList.get(i1).getCategory_id().equals("0")) {

                                    onXML = "[ADDITIONAL_PROMOTION_DATA]"
                                            + "[MID]" + mid + "[/MID]"
                                            + "[USER_ID]" + userId + "[/USER_ID]"
                                            + "[CATEGORY_ID]" + Integer.parseInt(additionalPromotionList.get(i1).getCategory_id()) + "[/CATEGORY_ID]"
                                            + "[PROMO_ID]" + Integer.parseInt(additionalPromotionList.get(i1).getPromo_id()) + "[/PROMO_ID]"
                                            + "[SKU_ID]" + Integer.parseInt(additionalPromotionList.get(i1).getSku_id()) + "[/SKU_ID]"
                                            //+ "[SKU]" + additionalPromotionList.get(i1).getSku() + "[/SKU]"
                                            + "[IN_STOCK]" + Integer.parseInt(additionalPromotionList.get(i1).getIn_stock()) + "[/IN_STOCK]"
                                            + "[PROMO_ANNOUNCER]" + Integer.parseInt(additionalPromotionList.get(i1).getPromo_announcer()) + "[/PROMO_ANNOUNCER]"
                                            + "[RUNNING_POS]" + Integer.parseInt(additionalPromotionList.get(i1).getRunning_pos()) + "[/RUNNING_POS]"
                                            + "[/ADDITIONAL_PROMOTION_DATA]";

                                    additional_promoition_data_xml = additional_promoition_data_xml + onXML;
                                }
                            }

                            final String sos_xml = "[DATA]" + additional_promoition_data_xml + "[/DATA]";

                            request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                            request.addProperty("XMLDATA", sos_xml);
                            request.addProperty("KEYS", "ADDITIONAL_PROMOTION_DATA");
                            request.addProperty("USERNAME", userId);
                            request.addProperty("MID", mid);

                            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            androidHttpTransport = new HttpTransportSE(CommonString.URL);
                            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);

                            result = (Object) envelope.getResponse();

                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }

                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return CommonString.METHOD_UPLOAD_STOCK_XML_DATA;
                            }
                        }
                        data.value = 35;
                        data.name = "Additional Promotion Data Uploading";
                        publishProgress(data);


                        //Image Upload

                        //Stock Facing Images Upload
                        if (stock_facingHeaderList.size() > 0) {
                            for (int i1 = 0; i1 < stock_facingHeaderList.size(); i1++) {

                                if (stock_facingHeaderList.get(i1).getImage1() != null && !stock_facingHeaderList.get(i1).getImage1().equals("")) {
                                    if (new File(CommonString.FILE_PATH + stock_facingHeaderList.get(i1).getImage1()).exists()) {

                                        try {
                                            result = UploadImage(stock_facingHeaderList.get(i1).getImage1(), "StockImages");

                                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                                return "StockImages";
                                            }

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    message.setText("StockFacing Images Uploaded");
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if (stock_facingHeaderList.get(i1).getImage2() != null && !stock_facingHeaderList.get(i1).getImage2().equals("")) {
                                    if (new File(CommonString.FILE_PATH + stock_facingHeaderList.get(i1).getImage2()).exists()) {

                                        try {
                                            result = UploadImage(stock_facingHeaderList.get(i1).getImage2(), "StockImages");
                                            if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                                return "StockImages";
                                            }

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    message.setText("StockFacing Images Uploaded");
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }


                        // SET COVERAGE STATUS
                        String final_xml = "";
                        onXML = "";
                        onXML = "[COVERAGE_STATUS]"
                                + "[STORE_ID]" + coverageList.get(i).getStoreId() + "[/STORE_ID]"
                                + "[VISIT_DATE]" + coverageList.get(i).getVisitDate() + "[/VISIT_DATE]"
                                + "[USER_ID]" + coverageList.get(i).getUserId() + "[/USER_ID]"
                                + "[STATUS]" + CommonString.KEY_U + "[/STATUS]"
                                + "[/COVERAGE_STATUS]";

                        final_xml = final_xml + onXML;

                        final String sos_xml = "[DATA]" + final_xml + "[/DATA]";

                        request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_COVERAGE_STATUS);
                        request.addProperty("onXML", sos_xml);

                        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_COVERAGE_STATUS, envelope);

                        result = (Object) envelope.getResponse();

                        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }
                        if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }
                        if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }

                        k = k + factor;
                        data.value = k;
                        data.name = "Uploading";
                        publishProgress(data);

                        db.open();
                        db.updateCoverageStatus(coverageList.get(i).getStoreId(), CommonString.KEY_U);
                        db.updateStoreStatusOnLeave(coverageList.get(i).getStoreId(), coverageList.get(i)
                                .getVisitDate(), CommonString.KEY_U);
                        // }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();
            if (result.equals("")) {
                finish();
            }
        }
    }

    public String UploadImage(String path, String folder_name) throws Exception {
        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Path + path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1639;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(Path + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);

        String[] split = path.split("/");
        String path1 = split[split.length - 1];

        request.addProperty("img", ba1);
        request.addProperty("name", path1);
        request.addProperty("FolderName", folder_name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);

        Object result = (Object) envelope.getResponse();

        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                return CommonString.KEY_FALSE;
            }

            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // for failure
            FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
            xmlR.setContentHandler(failureXMLHandler);

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.toString()));
            xmlR.parse(is);

            failureGetterSetter = failureXMLHandler.getFailureGetterSetter();

            if (failureGetterSetter.getStatus().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }
        } else {
            new File(Path + path).delete();
        }

        return result.toString();
    }
}
