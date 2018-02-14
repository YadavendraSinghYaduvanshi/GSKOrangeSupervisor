package cpm.com.gskmtorange.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.download.DownloadActivity;

public class CheckoutActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private String username, visit_date, store_id, store_intime;
    private Data data;
    private SharedPreferences preferences = null;

    GSKOrangeDB db;
    String userId, culture_id;

    CoverageBean coverageBean;

    String lat, lon, checkOutImagePath = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new GSKOrangeDB(this);
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);

        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        store_id = getIntent().getStringExtra(CommonString.KEY_STORE_ID);
        checkOutImagePath = getIntent().getStringExtra(CommonString.KEY_CHECKOUT_IMAGE);

        coverageBean = db.getCoverageSpecificData(visit_date, store_id);
        lat = coverageBean.getLatitude();
        lon = coverageBean.getLongitude();
        if (lat == null || lat.equals("")) {
            lat = "0.0";
        }
        if (lon == null || lon.equals("")) {
            lon = "0.0";
        }
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        new BackgroundTask(CheckoutActivity.this).execute();
    }

    private class BackgroundTask extends AsyncTask<Void, Data, String> {
        private Context context;

        BackgroundTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getString(R.string.title_activity_checkout));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... params) {
            try {
                //String result = "";
                data = new Data();

                data.value = 20;
                data.name = "Checked out Data Uploading";
                publishProgress(data);

                String onXML =
                        "[STORE_CHECK_OUT_STATUS]"
                                + "[USER_ID]" + username + "[/USER_ID]"
                                + "[STORE_ID]" + store_id + "[/STORE_ID]"
                                + "[LATITUDE]" + lat + "[/LATITUDE]"
                                + "[LOGITUDE]" + lon + "[/LOGITUDE]"
                                + "[CHECKOUT_DATE]" + visit_date + "[/CHECKOUT_DATE]"
                                + "[CHECK_OUTTIME]" + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()) + "[/CHECK_OUTTIME]"
                                + "[CHECK_INTIME]" + coverageBean.getInTime() + "[/CHECK_INTIME]"
                                + "[CREATED_BY]" + username + "[/CREATED_BY]"
                                + "[/STORE_CHECK_OUT_STATUS]";

                final String sos_xml = "[DATA]" + onXML + "[/DATA]";

                SoapObject request = new SoapObject(CommonString.NAMESPACE, "Upload_Store_ChecOut_Status");
                request.addProperty("onXML", sos_xml);
                /*request.addProperty("KEYS", "CHECKOUT_STATUS");
                request.addProperty("USERNAME", username);*/
                //request.addProperty("MID", mid);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION + "Upload_Store_ChecOut_Status", envelope);

                Object result = (Object) envelope.getResponse();

                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                    return "Upload_Store_ChecOut_Status";
                }
                if (result.toString().equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
                    return "Upload_Store_ChecOut_Status";
                }
                if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                    return "Upload_Store_ChecOut_Status";
                }

                data.value = 100;
                data.name = "Checkout Done";
                publishProgress(data);

                if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                    db.updateCheckoutOuttime(store_id, CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()), CommonString.KEY_Y, checkOutImagePath);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(CommonString.KEY_STORE_ID, "");
                    editor.putString(CommonString.KEY_STORE_NAME, "");
                    editor.putString(CommonString.KEY_VISIT_DATE, "");
                    editor.putString(CommonString.KEY_CAMERA_ALLOW, "");
                    editor.putString(CommonString.KEY_CHECKOUT_STATUS, "");
                    editor.putString(CommonString.KEY_CLASS_ID, "");
                    editor.putString(CommonString.KEY_EMP_ID, "");
                    editor.putString(CommonString.KEY_GEO_TAG, "");
                    editor.putString(CommonString.KEY_KEYACCOUNT_ID, "");
                    editor.putString(CommonString.KEY_STORETYPE_ID, "");
                    editor.putString(CommonString.KEY_UPLOAD_STATUS, "");

                    editor.commit();

                    db.updateCheckoutStatus(store_id, CommonString.KEY_Y);

                } else {
                    if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                        return "Upload_Store_ChecOut_Status";
                    }
                }
                return CommonString.KEY_SUCCESS;

            } catch (MalformedURLException e) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });

            } catch (IOException e) {
                // counter++;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showAlert(CommonString.MESSAGE_SOCKETEXCEPTION);
                        // TODO Auto-generated method stub
                        /*
                         * if (counter < 10) { new
						 * BackgroundTask(CheckOutUploadActivity
						 * .this).execute(); } else { message.showMessage();
						 * counter =1; }
						 */
                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
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
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            dialog.dismiss();

            if (result.equals(CommonString.KEY_SUCCESS)) {

                showAlert(getString(R.string.checkout_successful));

                finish();

            } else if (!result.equals("")) {
                /*AlertMessage message = new AlertMessage(
                        CheckOutStoreActivity.this, CommonString1.ERROR + result, "success", null);
				message.showMessage();*/

                Toast.makeText(getApplicationContext(), "Network Error Try Again", Toast.LENGTH_SHORT).show();
                finish();

            }

        }

    }

    class Data {
        int value;
        String name;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(getResources().getString(R.string.title_activity_checkout));
    }

    /*public String getCurrentTimeNotUsed() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

        return cdate;
    }*/

    private static String arabicToenglish(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }

        return new String(chars);
    }

    public String getCurrentTimeNotUsed() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

        if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            cdate = arabicToenglish(cdate);
        }else if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_UAE)) {
            cdate = arabicToenglish(cdate);
        }


        return cdate;
    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       /* Intent i = new Intent(activity, StorelistActivity.class);
                        activity.startActivity(i);
                        activity.finish();*/
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
