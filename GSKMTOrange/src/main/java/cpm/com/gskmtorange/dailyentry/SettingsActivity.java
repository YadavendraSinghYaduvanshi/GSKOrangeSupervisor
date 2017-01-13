package cpm.com.gskmtorange.dailyentry;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.fragment.SelectLanguageFragment;
import cpm.com.gskmtorange.fragment.SettingsActivityFragment;

public class SettingsActivity extends AppCompatActivity implements SelectLanguageFragment.OnFragmentInteractionListener {

    boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SettingsActivityFragment fragment = new SettingsActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment,fragment).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSelected){
                    finish();
                }
                else {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
    }

    @Override
    public void onFragmentInteraction(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
