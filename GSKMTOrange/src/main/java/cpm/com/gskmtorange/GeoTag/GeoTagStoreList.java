package cpm.com.gskmtorange.GeoTag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import cpm.com.gskmtorange.R;

/**
 * Created by ashishc on 27-12-2016.
 */

public class GeoTagStoreList extends AppCompatActivity {


    ListView lv;
    LinearLayout parent_linear,nodata_linear;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelistlayout);
        lv = (ListView) findViewById(R.id.list);
        nodata_linear = (LinearLayout) findViewById(R.id.no_data_lay);
        parent_linear = (LinearLayout) findViewById(R.id.parent_linear);







    }






}
