package cpm.com.gskmtorange.GeoTag;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import cpm.com.gskmtorange.R;

/**
 * Created by ashishc on 27-12-2016.
 */

public class GeoTagActivity   extends AppCompatActivity  //implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private GoogleMap mMap;
    double latitude =0.0;
    double longitude =0.0;
    LocationManager locationManager;
    private Location mLastLocation;
    private LocationManager locmanager = null;
    FloatingActionButton fab,fabcarmabtn;
    SupportMapFragment mapFragment;
    SharedPreferences preferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //username = preferences.getString(CommonString.KEY_USERNAME, null);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fabcarmabtn = (FloatingActionButton) findViewById(R.id.camrabtn);



        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);






    }






}
