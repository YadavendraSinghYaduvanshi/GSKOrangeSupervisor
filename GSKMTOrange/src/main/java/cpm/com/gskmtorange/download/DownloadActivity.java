package cpm.com.gskmtorange.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.LoginActivity;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.TableBean;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

public class DownloadActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    Data data;
    int eventType;
    GSKOrangeDB db;
    private SharedPreferences preferences = null;
    String userId, culture_id;

    JourneyPlanGetterSetter jcpgettersetter;
    SkuMasterGetterSetter skumastergettersetter;
    BrandMasterGetterSetter brandMasterGetterSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        db = new GSKOrangeDB(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        culture_id = preferences.getString(CommonString.KEY_CULTURE_ID, null);
        new UploadTask(DownloadActivity.this).execute();
    }

    class Data {
        int value;
        String name;
    }

    private class UploadTask extends AsyncTask<Void , Data, String>{

        private Context context;

        UploadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_dialog_progress);
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                String resultHttp="";
                data = new Data();

                data.value = 10;
                data.name = "JCP Data Downloading";
                publishProgress(data);

                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "JOURNEY_PLAN");
                request.addProperty("cultureid", culture_id);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        CommonString.URL);

                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL,
                        envelope);
                Object result = (Object) envelope.getResponse();

                if(result.toString()!=null){

                    //InputStream stream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));

                   xpp.setInput(new StringReader(result.toString()));
                   // xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                   // xpp.setInput(stream,"UTF-8");
                    xpp.next();
                    eventType = xpp.getEventType();

                    jcpgettersetter = XMLHandlers.JCPXMLHandler(xpp, eventType);

                    if(jcpgettersetter.getSTORE_ID().size()>0){
                        resultHttp = CommonString.KEY_SUCCESS;
                        String jcpTable = jcpgettersetter.getTable_journey_plan();
                        TableBean.setJourneyPlan(jcpTable);

                    }else{
                        return "JOURNEY_PLAN";
                    }

                    data.value = 10;
                    data.name = "JCP Data Downloading";

                }
                 publishProgress(data);

                // Store List Master
                request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);

                request.addProperty("UserName", userId);
                request.addProperty("Type", "SKU_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result = (Object) envelope.getResponse();

                if(result.toString()!=null){
                   xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    skumastergettersetter =XMLHandlers.skuMasterXMLHandler(xpp, eventType);
                    if(skumastergettersetter.getSKU_ID().size()>0){
                        String skutable = skumastergettersetter.getTable_SKU_MASTER();
                        if(skutable!=null){
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSkuMaster(skutable);
                        }
                    }else{
                        return "SKU_MASTER";
                    }

                    data.value = 20;
                    data.name = "SKU_MASTER Data Download";
                }

                publishProgress(data);

                // BRAND_MASTER
                request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);

                request.addProperty("UserName", userId);
                request.addProperty("Type", "BRAND_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result = (Object) envelope.getResponse();

                if(result.toString()!=null){
                   xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    brandMasterGetterSetter =XMLHandlers.brandMasterXMLHandler(xpp, eventType);
                    if(brandMasterGetterSetter.getBRAND_ID().size()>0){
                        String brandtable = brandMasterGetterSetter.getTable_BRAND_MASTER();
                        if(brandtable!=null){
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setBrandMaster(brandtable);
                        }
                    }else{
                        return "BRAND_MASTER";
                    }

                    data.value = 30;
                    data.name = "BRAND_MASTER Data Download";
                }

                publishProgress(data);

                // SUB_CATEGORY_MASTER
                request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);

                request.addProperty("UserName", userId);
                request.addProperty("Type", "SUB_CATEGORY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result = (Object) envelope.getResponse();

                if(result.toString()!=null){
                  /*  xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    skumastergettersetter =XMLHandlers.storeListXML(xpp, eventType);
                    if(skumastergettersetter.getSku_cd().size()>0){
                        String skutable = skumastergettersetter.getSku_master_table();
                        if(skutable!=null){
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSkumastertable(skutable);
                        }
                    }else{
                        return "SKU_MASTER";
                    }

                    data.value = 20;
                    data.name = "Store Data Download";*/
                }

                publishProgress(data);

                // CATEGORY_MASTER
                request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);

                request.addProperty("UserName", userId);
                request.addProperty("Type", "CATEGORY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                result = (Object) envelope.getResponse();

                if(result.toString()!=null){
                  /*  xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    skumastergettersetter =XMLHandlers.storeListXML(xpp, eventType);
                    if(skumastergettersetter.getSku_cd().size()>0){
                        String skutable = skumastergettersetter.getSku_master_table();
                        if(skutable!=null){
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSkumastertable(skutable);
                        }
                    }else{
                        return "SKU_MASTER";
                    }

                    data.value = 20;
                    data.name = "Store Data Download";*/
                }

                publishProgress(data);







                db.open();

                db.InsertJCP(jcpgettersetter);













            }catch (MalformedURLException e) {

                /*final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });

            } catch (IOException e) {
               /* final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);*/

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showAlert(CommonString.MESSAGE_SOCKETEXCEPTION);

                    }
                });
            }

            catch (Exception e) {
             /*   final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/

               /* e.getMessage();
                e.printStackTrace();
                e.getCause();*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub

            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            finish();
        }

    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       /* Intent i = new Intent(activity, StorelistActivity.class);
                        activity.startActivity(i);
                        activity.finish();*/

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
