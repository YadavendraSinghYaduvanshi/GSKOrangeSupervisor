package cpm.com.gskmtorange.GeoTag;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.GeotaggingBeans;
import cpm.com.gskmtorange.messgae.AlertMessage;
import cpm.com.gskmtorange.upload.Base64;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.FailureXMLHandler;

/**
 * Created by ashishc on 27-12-2016.
 */


public class GeoTagActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    String result, errormsg = "";
    ;
    private ProgressBar pb;
    private GoogleMap mMap;
    double latitude = 0.0;
    double longitude = 0.0;
    protected String diskpath = "", _path, _pathforcheck, img_str = "", status;
    private Location mLastLocation;
    private LocationManager locmanager = null;
    FloatingActionButton fab, fabcarmabtn;
    SupportMapFragment mapFragment;
    SharedPreferences preferences;
    String username, storeid, str, storename, visitData;
    GSKOrangeDB db;
    LocationManager locationManager;
    Geocoder geocoder;
    boolean enabled;
    private Dialog dialog;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 500; // 5 sec
    private static int FATEST_INTERVAL = 100; // 1 sec
    private static int DISPLACEMENT = 5; // 10 meters
    private static final String TAG = GeoTagActivity.class.getSimpleName();
    String defaultCameraPackage = "";
    File file;
    String gallery_package = "";
    Uri outputFileUri;
    private int factor, k;
    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();

    ArrayList<GeotaggingBeans> geotaglistImage = new ArrayList<GeotaggingBeans>();

    private TextView percentage, message;
    private FailureGetterSetter failureGetterSetter = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

        username = preferences.getString(CommonString.KEY_USERNAME, null);
        storeid = preferences.getString(CommonString.KEY_STORE_ID, null);
        storename = preferences.getString(CommonString.KEY_STORE_NAME, null);
        visitData = preferences.getString(CommonString.KEY_VISIT_DATE, null);


        final PackageManager packageManager = getPackageManager();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fabcarmabtn = (FloatingActionButton) findViewById(R.id.camrabtn);

        db = new GSKOrangeDB(GeoTagActivity.this);
        db.open();

        //storeid = getIntent().getStringExtra("Storeid");
        str = CommonString.FILE_PATH;

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        locmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    GeoTagActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));

            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event

                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!img_str.equalsIgnoreCase("")) {
                    status = "Y";
                    db.updateStatus(storeid, status, latitude, longitude);

                    db.InsertSTOREgeotag(storeid, latitude, longitude, img_str, status);
                    img_str = "";

                    new GeoTagUpload(GeoTagActivity.this).execute();


                } else {
                    Snackbar.make(view, getResources().getString(R.string.takeimage), Snackbar.LENGTH_LONG).setAction("Action", null).show();


                }

            }
        });

        fabcarmabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
                for (int n = 0; n < list.size(); n++) {
                    if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                        Log.d("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                        Log.d("TAG", "package name  : " + list.get(n).packageName);
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }

                _pathforcheck = storeid + getResources().getString(R.string.store) + getResources().getString(R.string.image) + getCurrentTime().replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck;


                startCameraActivity();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.notsuppoted)
                        , Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                mMap.setMyLocationEnabled(true);

                // Add a marker of latest location and move the camera
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }


        // if (mRequestingLocationUpdates) {
        startLocationUpdates();
        // }

        // startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
       /* checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

        return cdate;

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
                   /* Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);
*/
                    //temp value in case camera is gallery app above jellybean
                    String packag = list.get(n).loadLabel(packageManager).toString();
                    if (packag.equalsIgnoreCase("Gallery") || packag.equalsIgnoreCase("Galeri")) {
                        gallery_package = list.get(n).packageName;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {

                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera")) {

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
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 0);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:

                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {


                        fabcarmabtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.camera_icon_done));

                        fabcarmabtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DB6AC")));

                        img_str = _pathforcheck;
                        _pathforcheck = "";

                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GeoTag Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
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

                GSKOrangeDB db = new GSKOrangeDB(GeoTagActivity.this);
                db.open();

                geotaglist = db.getinsertGeotaggingData("Y");

                // uploading Geotag

                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                XMLReader xmlR = saxP.getXMLReader();


                String geo_xml = "";
                ArrayList<String> geotemplist = new ArrayList<String>();
                if (geotaglist.size() > 0) {

                    for (int i = 0; i < geotaglist.size(); i++) {

                       /* runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                                k = k + factor;
                                pb.setProgress(k);
                                percentage.setText(k + "%");
                                message.setText(getResources().getString(R.string.geotagdata));
                            }
                        });*/

                        String onXML = "[GeoTag_DATA][STORE_ID]"
                                + geotaglist.get(i).getStoreid()
                                + "[/STORE_ID]"
                                + "[LATTITUDE]"
                                + geotaglist.get(i).getLatitude()
                                + "[/LATTITUDE]"
                                + "[LONGITUDE]"
                                + geotaglist.get(i).getLongitude()
                                + "[/LONGITUDE]"
                                + "[FRONT_IMAGE]"
                                + geotaglist.get(i).getUrl1()
                                + "[/FRONT_IMAGE]"
                                + "[CREATED_BY]" + username
                                + "[/CREATED_BY][/GeoTag_DATA]";

                        geo_xml = geo_xml + onXML;

                      //  geotemplist.add(geotaglist.get(i).getStoreid());

                    }

                    geo_xml = "[DATA]" + geo_xml
                            + "[/DATA]";

                    SoapObject request = new SoapObject(CommonString.NAMESPACE,
                            CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                    request.addProperty("MID", "0");
                    request.addProperty("KEYS", "GEOTAG_NEW_DATA");
                    request.addProperty("USERNAME", username);

                    request.addProperty("XMLDATA", geo_xml);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            CommonString.URL);
                    androidHttpTransport.call(
                            CommonString.SOAP_ACTION_UPLOAD_ASSET_XMLDATA, envelope);
                    Object result = (Object) envelope.getResponse();

                    if (result.toString().equalsIgnoreCase(
                            CommonString.KEY_SUCCESS)) {
                        String statusD = "D";

                        for (int i = 0; i < geotaglist.size(); i++) {
                            db.updateGeoTagData(geotaglist.get(i).storeid, statusD);

                            db.updateDataStatus(geotaglist.get(i).getStoreid(), statusD);

                        }

                    } else {

                        if (result.toString().equalsIgnoreCase(
                                CommonString.KEY_FALSE)) {
                            return CommonString.METHOD_UPLOAD_ASSET;
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
                            return CommonString.METHOD_UPLOAD_ASSET + ","
                                    + failureGetterSetter.getErrorMsg();

                        } else {

                        }
                    }
                }


                return CommonString.KEY_SUCCESS;

            } catch (SocketException ex) {
                ex.printStackTrace();
                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();

            } catch (Exception ex) {


                ex.printStackTrace();
                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();
            }


            return "";

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result.equals(CommonString.KEY_SUCCESS)) {


                new GeoTagImageUpload(GeoTagActivity.this).execute();

              /*  Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();*/

            }
            else if(!result.equals(CommonString.KEY_SUCCESS))
            {

                AlertMessage message = new AlertMessage(
                        GeoTagActivity.this, AlertMessage.MESSAGE_DATA_NOT
                        + result, getResources().getString(R.string.failure), null);
                message.showMessage();


            }

            else if (!result.equals("")) {


            }

        }

    }

    public class GeoTagImageUpload extends AsyncTask<Void, Void, String> {

        private Context context;

        GeoTagImageUpload(Context context) {
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

                GSKOrangeDB db = new GSKOrangeDB(GeoTagActivity.this);
                db.open();


                geotaglistImage = db.getinsertGeotaggingData("D");

                // Uploading Geotag

                SAXParserFactory saxPF = SAXParserFactory.newInstance();
                SAXParser saxP = saxPF.newSAXParser();
                XMLReader xmlR = saxP.getXMLReader();


                if (geotaglistImage.size() > 0) {

                    for (int i = 0; i < geotaglistImage.size(); i++) {

                        runOnUiThread(new Runnable() {

                            public void run() {
                                // TODO Auto-generated method stub
                                k = k + factor;
                                pb.setProgress(k);
                                percentage.setText(k + "%");
                                message.setText(getResources().getString(R.string.uploadimge));
                            }
                        });

                        if (geotaglistImage.get(i).getUrl1() != null
                                && !geotaglistImage.get(i).getUrl1()
                                .equalsIgnoreCase("")) {

                            if (new File(Environment.getExternalStorageDirectory() + "/GSK_MT_ORANGE_IMAGES/"
                                    + geotaglistImage.get(i).getUrl1()).exists()) {
                                result = UploadGeoImage(geotaglistImage.get(i).getUrl1(), "GeotagImages");


                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                                    return "GeotagImages";
                                }


                                if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {

                                    return CommonString.METHOD_Get_DR_STORE_IMAGES_GEO;
                                } else if (result
                                        .equalsIgnoreCase(CommonString.KEY_FAILURE)) {

                                    return CommonString.METHOD_Get_DR_STORE_IMAGES_GEO + "," + errormsg;
                                }

                            }
                        }

                    }


                }


                return CommonString.KEY_SUCCESS;

            }
            catch (SocketException ex) {

                ex.printStackTrace();

                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();

            }

            catch (Exception ex) {
                ex.printStackTrace();


                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();

            }

            return "";

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {


                //Toast.makeText(getApplicationContext(),"GeoTag Uploaded ",Toast.LENGTH_LONG).show();

                String Statustag = "U";
                db.open();

                for (int i = 0; i < geotaglistImage.size(); i++) {


                    db.updateGeoTagData(geotaglistImage.get(i).getStoreid(), Statustag);

                    db.updateDataStatus(geotaglistImage.get(i).getStoreid(), Statustag);

                    db.deleteGeoTagData(geotaglistImage.get(i).getStoreid());


                }



               /* AlertMessage message = new AlertMessage(
                        GeoTagActivity.this, getResources().getString(R.string.uploadeddata)
                        + result, getResources().getString(R.string.success), null);
                message.showMessage();*/

                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();

            Toast.makeText(getApplicationContext(),getResources().getString(R.string.success),Toast.LENGTH_LONG).show();

            }
            else if(!result.equals(CommonString.KEY_SUCCESS))
            {

               /* AlertMessage message = new AlertMessage(
                        GeoTagActivity.this, AlertMessage.MESSAGE_DATA_NOT
                        + result, getResources().getString(R.string.failure), null);
                message.showMessage();*/
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.failure),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(
                        GeoTagActivity.this,
                        GeoTagStoreList.class);

                startActivity(intent);

                GeoTagActivity.this.finish();
            }



            else if (!result.equals("")) {

            }

        }

    }

    public String UploadGeoImage(String path, String folder) throws Exception {

        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/GSK_MT_ORANGE_IMAGES/" + path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

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
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/GSK_MT_ORANGE_IMAGES/" + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);

        request.addProperty("img", ba1);
        request.addProperty("name", path);
        request.addProperty("FolderName", folder);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                CommonString.URL);

        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);
        Object result = (Object) envelope.getResponse();

        if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

            new File(Environment.getExternalStorageDirectory() + "/GSK_MT_ORANGE_IMAGES/" + path).delete();



            /*SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // for failure
            FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
            xmlR.setContentHandler(failureXMLHandler);

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.toString()));
            xmlR.parse(is);

            failureGetterSetter = failureXMLHandler
                    .getFailureGetterSetter();

            if (failureGetterSetter.getStatus().equalsIgnoreCase(
                    CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }*/

        } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
            return CommonString.KEY_FALSE;
        } else {
            return CommonString.KEY_FAILURE;
        }

        return result.toString();
    }




    private static boolean updateResources(Context context, String language) {

        String lang ;

        if(language.equalsIgnoreCase("English")){
            lang = "EN";
        }
        else if(language.equalsIgnoreCase("UAE")) {
            lang = "AR";
        }
        else {
            lang = "TR";
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }
}
