package cpm.com.gskmtorange.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.gsk_dailyentry.StoreWisePerformanceActivity;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.FailureXMLHandler;

/**
 * Created by ashishc on 31-05-2016.
 */
public class StoreCheckoutImageActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String gallery_package = "";
    Uri outputFileUri;

    ImageView img_cam, img_clicked;
    Button btn_save;
    private Dialog dialog;
    private TextView percentage, message;
    private ProgressBar pb;
    private FailureGetterSetter failureGetterSetter = null;
    String _pathforcheck, _path, str;

    String store_id, visit_date, username, intime, date, _UserId;
    private SharedPreferences preferences;
    AlertDialog alert;
    String img_str, strflag;
    private GSKOrangeDB database;

    String lat, lon;
    GoogleApiClient mGoogleApiClient;
    ArrayList<CoverageBean> coverage_list;
    Toolbar toolbar;
    boolean ResultFlag = true;
    String checkOutStore_id = "";
    ArrayList<CoverageBean> coverage = new ArrayList<CoverageBean>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_checkout_image);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_cam = (ImageView) findViewById(R.id.img_selfie);
        img_clicked = (ImageView) findViewById(R.id.img_cam_selfie);

        btn_save = (Button) findViewById(R.id.btn_save_selfie);

        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

        checkOutStore_id = getIntent().getStringExtra(CommonString.KEY_STORE_ID);

        str = CommonString.FILE_PATH;

        database = new GSKOrangeDB(this);
        database.open();

        coverage_list = database.getCoverageData(date);

        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(this, DailyEntryScreen.class);
        startActivity(i);*/

        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_cam_selfie:

                _pathforcheck = checkOutStore_id + "CHK_SI_" + visit_date.replace("/", "") + "_" + CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext()).replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = CommonFunctions.getCurrentTimeWithLanguage(getApplicationContext());

                startCameraActivity();
                break;

            case R.id.btn_save_selfie:

                if (img_str != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreCheckoutImageActivity.this);
                    builder.setMessage(getResources().getString(R.string.title_activity_save_data))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                    Intent i = new Intent(StoreCheckoutImageActivity.this, CheckoutActivity.class);
                                    i.putExtra(CommonString.KEY_STORE_ID, checkOutStore_id);
                                    i.putExtra(CommonString.KEY_CHECKOUT_IMAGE, img_str);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.clickimage), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    protected void startCameraActivity() {
        try {
            /*Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(intent, 0);*/

            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);

                    //temp value in case camera is gallery app above jellybean
                    String packag = list.get(n).loadLabel(packageManager).toString();
                    if (packag.equalsIgnoreCase("Gallery") || packag.equalsIgnoreCase("Galeri") || packag.equalsIgnoreCase("الاستوديو")) {
                        gallery_package = list.get(n).packageName;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("الكاميرا")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {

                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("الكاميرا")) {

                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            //com.android.gallery3d

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {

            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
                        img_cam.setImageBitmap(bmp);

                        img_clicked.setVisibility(View.GONE);
                        img_cam.setVisibility(View.VISIBLE);

                        img_str = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(R.string.title_activity_store_checkout_image);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        private Context context;

        GeoTagUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                GSKOrangeDB db = new GSKOrangeDB(StoreCheckoutImageActivity.this);
                db.open();

                coverage = db.getCoverageWithStoreID_Data(store_id);

                // uploading Geotag

                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                XMLReader xmlR = saxP.getXMLReader();


                String current_xml = "";

                if (coverage.size() > 0) {

                    for (int i = 0; i < coverage.size(); i++) {


                        String onXML = "[Coverage_Intime][USER_ID]"
                                + _UserId
                                + "[/USER_ID]"
                                + "[STORE_ID]"
                                + coverage.get(i).getStoreId()
                                + "[/STORE_ID]"
                                + "[VISIT_DATE]"
                                + coverage.get(i).getVisitDate()
                                + "[/VISIT_DATE]"
                                + "[IN_TIME]"
                                + coverage.get(i).getInTime()
                                + "[/IN_TIME]"
                                + "[LATITUDE]"
                                + coverage.get(i).getLatitude()
                                + "[/LATITUDE]"
                                + "[LONGITUDE ]"
                                + coverage.get(i).getLongitude()
                                + "[/LONGITUDE ]"
                                + "[REASON_ID]"
                                + coverage.get(i).getReasonid()
                                + "[/REASON_ID]"
                                + "[REMARK]"
                                + coverage.get(i).getReason()
                                + "[/REMARK][/Coverage_Intime]";

                        current_xml = current_xml + onXML;


                    }

                    current_xml = "[DATA]" + current_xml
                            + "[/DATA]";

                    SoapObject request = new SoapObject(CommonString.NAMESPACE,
                            CommonString.METHOD_UPLOAD_CURRENT_DATA);
                    //request.addProperty("MID", "0");
                    // request.addProperty("KEYS", "CURRENT_DATA");
                    // request.addProperty("USERNAME", username);

                    request.addProperty("onXML", current_xml);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            CommonString.URL);
                    androidHttpTransport.call(
                            CommonString.SOAP_ACTION_UPLOAD_CURRRENT_DATA, envelope);
                    Object result = (Object) envelope.getResponse();

                    if (result.toString().equalsIgnoreCase(
                            CommonString.KEY_SUCCESS)) {


                    } else {

                        if (result.toString().equalsIgnoreCase(
                                CommonString.KEY_FALSE)) {
                            return CommonString.METHOD_UPLOAD_CURRENT_DATA;
                        }

                        // for failure
                        FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
                        xmlR.setContentHandler(failureXMLHandler);

                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(result
                                .toString()));
                        xmlR.parse(is);

                        failureGetterSetter = failureXMLHandler
                                .getFailureGetterSetter();

                        if (failureGetterSetter.getStatus().equalsIgnoreCase(
                                CommonString.KEY_FAILURE)) {
                            return CommonString.METHOD_UPLOAD_CURRENT_DATA + ","
                                    + failureGetterSetter.getErrorMsg();

                        } else {

                        }
                    }
                }


                return CommonString.KEY_SUCCESS;

            } catch (MalformedURLException e) {

                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;

            } catch (SocketTimeoutException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;

            } catch (InterruptedIOException e) {

                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;


            } catch (IOException e) {

                ResultFlag = false;
                strflag = CommonString.MESSAGE_SOCKETEXCEPTION;

            } catch (XmlPullParserException e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_XmlPull;

            } catch (Exception e) {
                ResultFlag = false;
                strflag = CommonString.MESSAGE_EXCEPTION;

            }

            if (ResultFlag) {
                return CommonString.KEY_SUCCESS;

            } else {

                return strflag;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                dialog.dismiss();

                Intent in = new Intent(StoreCheckoutImageActivity.this, StoreWisePerformanceActivity.class);
                startActivity(in);
                finish();


                //showAlert(getString(R.string.data_downloaded_successfully));
            } else {

                GSKOrangeDB db = new GSKOrangeDB(StoreCheckoutImageActivity.this);
                db.open();

                dialog.dismiss();
                db.deleteTableWithStoreID(store_id);

                showAlert(getString(R.string.datanotfound) + " " + result);
            }
        }

    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StoreCheckoutImageActivity.this);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

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
}
