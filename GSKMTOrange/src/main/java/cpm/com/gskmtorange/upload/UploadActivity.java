package cpm.com.gskmtorange.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.GetterSetter.StoreBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;

public class UploadActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    GSKOrangeDB db;
    ArrayList<CoverageBean> coverageList;

    private SharedPreferences preferences;
    String date, userId, app_version;

    StoreBean storeData;
    String datacheck = "";
    String[] words;
    String validity;
    int mid;
    private int factor, k=0;

    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);

        db = new GSKOrangeDB(getApplicationContext());

        //start upload
        new UploadTask(getApplicationContext()).execute();

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

                        if (storeData.getCHECKOUT_STATUS().equals(CommonString.KEY_C) || storeData.getCHECKOUT_STATUS().equals(CommonString.KEY_L) ||
                                !storeData.getUPLOAD_STATUS().equals(CommonString.KEY_U)) {

                            String camera_allow = storeData.getCAMERA_ALLOW();

                            String onXML = "[DATA][USER_DATA][STORE_CD]"
                                    + coverageList.get(i).getStoreId()
                                    + "[/STORE_CD]" + "[VISIT_DATE]"
                                    + coverageList.get(i).getVisitDate()
                                    + "[/VISIT_DATE][LATITUDE]"
                                    + coverageList.get(i).getLatitude()
                                    + "[/LATITUDE][APP_VERSION]"
                                    + app_version
                                    + "[/APP_VERSION][LONGITUDE]"
                                    + coverageList.get(i).getLongitude()
                                    + "[/LONGITUDE][IN_TIME]"
                                    + coverageList.get(i).getInTime()
                                    + "[/IN_TIME][OUT_TIME]"
                                    + coverageList.get(i).getOutTime()
                                    + "[/OUT_TIME][UPLOAD_STATUS]"
                                    + "N"
                                    + "[/UPLOAD_STATUS][USER_ID]" + userId
                                    + "[/USER_ID][IMAGE_URL]" + coverageList.get(i).getImage()
                                    + "[/IMAGE_URL][REASON_ID]"
                                    + coverageList.get(i).getReasonid()
                                    + "[/REASON_ID][REASON_REMARK]"
                                    + coverageList.get(i).getRemark()
                                    + "[/REASON_REMARK][CAMERA_ALLOWED]"
                                    + camera_allow
                                    + "[/CAMERA_ALLOWED][/USER_DATA][/DATA]";



                            SoapObject request = new SoapObject(
                                    CommonString.NAMESPACE,
                                    CommonString.METHOD_UPLOAD_COVERAGE);
                            request.addProperty("onXML", onXML);

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER11);
                            envelope.dotNet = true;
                            envelope.setOutputSoapObject(request);

                            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                                    CommonString.URL);


                            androidHttpTransport
                                    .call(CommonString.SOAP_ACTION_UPLOAD_STORE_COVERAGE,
                                            envelope);

                            Object result = (Object) envelope.getResponse();


                            datacheck = result.toString();
                            words = datacheck.split("\\;");
                            validity = (words[0]);

                            if (validity
                                    .equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                db.updateCoverageStatus(coverageList
                                        .get(i).getStoreId(), CommonString.KEY_P);

                                db.updateStoreStatusOnLeave(
                                        coverageList.get(i).getStoreId(),
                                        date, CommonString.KEY_P);
                            } else {

                                return CommonString.METHOD_UPLOAD_COVERAGE;

                            }
                        }

                        mid = Integer.parseInt((words[1]));

                        k = k + factor;
                        data.value = k;
                        data.name = "Uploading";

                        publishProgress(data);

                        String final_xml = "";
                        // Add below------------------


                        // SET COVERAGE STATUS

                         final_xml = "";
                        String onXML = "";
                        onXML = "[COVERAGE_STATUS][STORE_ID]"
                                + coverageList.get(i).getStoreId()
                                + "[/STORE_ID]"
                                + "[VISIT_DATE]"
                                + coverageList.get(i).getVisitDate()
                                + "[/VISIT_DATE]"
                                + "[USER_ID]"
                                + coverageList.get(i).getUserId()
                                + "[/USER_ID]"
                                + "[STATUS]"
                                + CommonString.KEY_U
                                + "[/STATUS]"
                                + "[/COVERAGE_STATUS]";

                        final_xml = final_xml + onXML;

                        final String sos_xml = "[DATA]" + final_xml
                                + "[/DATA]";

                        SoapObject request = new SoapObject(
                                CommonString.NAMESPACE,
                                CommonString.METHOD_UPLOAD_COVERAGE_STATUS);
                        request.addProperty("onXML", sos_xml);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                                SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                                CommonString.URL);

                        androidHttpTransport.call(
                                CommonString.SOAP_ACTION+CommonString.METHOD_UPLOAD_COVERAGE_STATUS,
                                envelope);
                        Object result = (Object) envelope.getResponse();

                        if (!result.toString().equalsIgnoreCase(
                                CommonString.KEY_SUCCESS)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }

                        if (result.toString().equalsIgnoreCase(
                                CommonString.KEY_NO_DATA)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }

                        if (result.toString().equalsIgnoreCase(
                                CommonString.KEY_FAILURE)) {
                            return CommonString.METHOD_UPLOAD_COVERAGE_STATUS;
                        }

                        db.open();

                        db.updateCoverageStatus(coverageList.get(i)
                                .getStoreId(), CommonString.KEY_U);
                        db.updateStoreStatusOnLeave(coverageList.get(i)
                                .getStoreId(), coverageList.get(i)
                                .getVisitDate(), CommonString.KEY_U);

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
