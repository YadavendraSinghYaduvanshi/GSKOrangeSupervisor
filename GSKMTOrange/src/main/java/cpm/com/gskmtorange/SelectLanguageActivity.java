package cpm.com.gskmtorange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;

public class SelectLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_lang_1, btn_lang_2;

    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;

    LoginGetterSetter login_data;
    ArrayList<String> language, culture_id;

    boolean selected_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        btn_lang_1 = (Button) findViewById(R.id.btn_language_one);
        btn_lang_2 = (Button) findViewById(R.id.btn_language_two);


        login_data = (LoginGetterSetter) getIntent().getSerializableExtra(CommonString.KEY_LOGIN_DATA);
        language = login_data.getCULTURE_NAME();
        culture_id = login_data.getCULTURE_ID();

        setDataFromSharedPreferences(login_data);

        if (language.size() > 1) {

            btn_lang_1.setText(language.get(0));
            btn_lang_2.setText(language.get(1));

            btn_lang_1.setOnClickListener(this);
            btn_lang_2.setOnClickListener(this);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected_flag) {
                    Intent intent = new Intent(getBaseContext(),
                            MainActivity.class);

                    intent.putExtra(CommonString.KEY_LOGIN_DATA, login_data);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(view, getString(R.string.select_language), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_language_one:

                selected_flag = true;

                CommonFunctions.updateLangResources(getApplicationContext(), language.get(0));

                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.grey_background));

                editor.putString(CommonString.KEY_LANGUAGE, language.get(0));
                editor.putString(CommonString.KEY_CULTURE_ID, culture_id.get(0));
                editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, login_data.getNOTICE_URL().get(0));
                editor.commit();

                break;

            case R.id.btn_language_two:

                selected_flag = true;

                CommonFunctions.updateLangResources(getApplicationContext(), language.get(1));

                btn_lang_1.setBackgroundColor(getResources().getColor(R.color.grey_background));
                btn_lang_2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                editor.putString(CommonString.KEY_LANGUAGE, language.get(1));
                editor.putString(CommonString.KEY_CULTURE_ID, culture_id.get(1));
                editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, login_data.getNOTICE_URL().get(1));
                editor.commit();

                break;
        }
    }


    private void setDataFromSharedPreferences(LoginGetterSetter lgs) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(lgs);

        //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CommonString.KEY_LOOGIN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(CommonString.KEY_LOOGIN_PREF, jsonCurProduct);
        editor.commit();
    }
}
