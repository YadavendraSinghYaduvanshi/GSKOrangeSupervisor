package cpm.com.gskmtorange.dailyentry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import cpm.com.gskmtorange.MainActivity;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.fragment.SelectLanguageFragment;
import cpm.com.gskmtorange.fragment.SettingsActivityFragment;

public class SettingsActivity extends AppCompatActivity implements SelectLanguageFragment.OnFragmentInteractionListener {

    String selected_lang = "", culture_id, notice_url;

    FloatingActionButton fab;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor = null;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //preference data
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));

      /*  SettingsActivityFragment fragment = new SettingsActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment,fragment).commit();*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected_lang.equals("")) {

                    Snackbar.make(view, getString(R.string.select_language), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    updateResources(getApplicationContext(),selected_lang);

                    editor.putString(CommonString.KEY_LANGUAGE, selected_lang);
                    editor.putString(CommonString.KEY_CULTURE_ID, culture_id);
                    editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, notice_url);
                    editor.commit();

                    Intent startDownload = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(startDownload);
                    finish();
                }


            }
        });
    }

    @Override
    public void onFragmentInteraction(String selected_lang, String culture_id, String notice_url) {

        this.selected_lang = selected_lang;
        this.culture_id = culture_id;
        this.notice_url = notice_url;

        if (!selected_lang.equals("")) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            Intent startDownload = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startDownload);

            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent startDownload = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(startDownload);

        finish();

        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateResources(getApplicationContext(),preferences.getString(CommonString.KEY_LANGUAGE, ""));
        toolbar.setTitle(R.string.title_activity_settings);
    }

    private static boolean updateResources(Context context, String language) {
       

        String lang;

        if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ENGLISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ENGLISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_ARABIC_KSA;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_TURKISH)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_TURKISH;

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_OMAN)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_OMAN;
        }else{
            lang = CommonString.KEY_RETURN_LANGUAGE_DEFAULT;
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
