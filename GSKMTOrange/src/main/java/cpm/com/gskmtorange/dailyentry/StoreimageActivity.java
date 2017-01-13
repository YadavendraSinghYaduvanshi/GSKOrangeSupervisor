package cpm.com.gskmtorange.dailyentry;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.GetterSetter.CoverageBean;
import cpm.com.gskmtorange.gsk_dailyentry.CategoryListActivity;
import cpm.com.gskmtorange.gsk_dailyentry.StoreWisePerformanceActivity;

/**
 * Created by ashishc on 31-05-2016.
 */
public class StoreimageActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String gallery_package = "";
    Uri outputFileUri;

    ImageView img_cam, img_clicked;
    Button btn_save;

    String _pathforcheck, _path, str;

    String store_id, visit_date, username, intime, date;
    private SharedPreferences preferences;
    AlertDialog alert;
    String img_str;
    private GSKOrangeDB database;

    String lat, lon;
    GoogleApiClient mGoogleApiClient;
    ArrayList<CoverageBean> coverage_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeimage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_cam = (ImageView) findViewById(R.id.img_selfie);
        img_clicked = (ImageView) findViewById(R.id.img_cam_selfie);

        btn_save = (Button) findViewById(R.id.btn_save_selfie);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

        store_id = preferences.getString(CommonString.KEY_STORE_ID, null);

        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);

        intime = preferences.getString(CommonString.KEY_STORE_IN_TIME, "");

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

                _pathforcheck = store_id + getResources().getString(R.string.store)
                        + getResources().getString(R.string.image) + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";

                _path = CommonString.FILE_PATH + _pathforcheck;

                intime = getCurrentTime();

                startCameraActivity();

                break;

            case R.id.btn_save_selfie:

                if (img_str != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreimageActivity.this);
                    builder.setMessage(getResources().getString(R.string.title_activity_save_data))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                    CoverageBean cdata = new CoverageBean();
                                    cdata.setStoreId(store_id);
                                    cdata.setVisitDate(visit_date);
                                    cdata.setUserId(username);
                                    cdata.setInTime(intime);
                                    cdata.setReason("");
                                    cdata.setReasonid("0");
                                    cdata.setLatitude(lat);
                                    cdata.setLongitude(lon);
                                    cdata.setImage(img_str);
                                    cdata.setRemark("");
                                    cdata.setStatus(CommonString.KEY_INVALID);

                                    database.InsertCoverageData(cdata);

                                    database.updateCheckoutStatus(store_id, CommonString.KEY_INVALID);
                                            
                                           /* SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                            editor.putString(CommonString.KEY_STORE_IN_TIME, "");

                                            editor.commit();*/


                                    //Intent in = new Intent(StoreimageActivity.this, CategoryListActivity.class);
                                    Intent in = new Intent(StoreimageActivity.this, StoreWisePerformanceActivity.class);
                                    startActivity(in);
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
                    if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
                        gallery_package = list.get(n).packageName;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
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

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

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
        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
