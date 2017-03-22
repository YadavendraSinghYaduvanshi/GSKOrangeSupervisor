package cpm.com.gskmtorange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cpm.com.gskmtorange.autoupdate.AutoUpdateActivity;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.FailureGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.LoginGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.versionCode;

/**
 * A login screen that offers login via userid/password.
 */
public class LoginActivity extends AppCompatActivity {

    TextView tv_version;
    String app_ver;

    LoginGetterSetter lgs = null;

    static int counter = 1;

    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    String lat = "0.0";
    String lon = "0.0";

    // UI references.
    private AutoCompleteTextView museridView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String userid, password, p_username, p_password;

    private int versionCode;

    int eventType;

    Button museridSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        TextView tv_version = (TextView) findViewById(R.id.tv_version_code);

        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            // login_version.setText("Parinaam Version " + app_ver);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tv_version.setText("Version/Versiyon - " + app_ver+"T1");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        // Set up the login form.
        museridView = (AutoCompleteTextView) findViewById(R.id.userid);
        //populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);

       /* museridView.setText("testmer");
        mPasswordView.setText("cpm123");
*/
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            // tv_version.setText("Version " + app_ver);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        museridSignInButton = (Button) findViewById(R.id.user_login_button);
        museridSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        // Create a Folder for Images


        File file = new File(Environment.getExternalStorageDirectory(), "GSK_MT_ORANGE_IMAGES");
        if (!file.isDirectory()) {
            file.mkdir();
        }

    }

  /*  private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(museridView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid userid, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
      /*  if (mAuthTask != null) {
            return;
        }
*/
        // Reset errors.
        museridView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        userid = museridView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userid address.
        if (TextUtils.isEmpty(userid)) {
            museridView.setError(getString(R.string.error_field_required));
            focusView = museridView;
            cancel = true;
        } /*else if (!isuseridValid(userid)) {
            museridView.setError(getString(R.string.error_invalid_userid));
            focusView = museridView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else if(!isuseridValid(userid)){
            Snackbar.make(museridView,getString(R.string.error_incorrect_username),Snackbar.LENGTH_SHORT).show();
        }else if(!isPasswordValid(password)){
            Snackbar.make(museridView,getString(R.string.error_incorrect_password),Snackbar.LENGTH_SHORT).show();
        }else{

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            new AuthenticateTask().execute();
        }
    }

    private boolean isuseridValid(String userid) {
        //TODO: Replace this with your own logic

        boolean flag = true;

        String u_id = preferences.getString(CommonString.KEY_USERNAME, "");

        if (!u_id.equals("") && !userid.equals(u_id)) {
            flag = false;
        }

        return flag;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        boolean flag = true;

        String pw = preferences.getString(CommonString.KEY_PASSWORD, "");

        if(!pw.equals("") &&!password.equals(pw)){
            flag = false;
        }

        return flag;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

  /*  @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only userid addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.userid
                .CONTENT_ITEM_TYPE},

                // Show primary userid addresses first. Note that there won't be
                // a primary userid address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> userids = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userids.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        adduseridsToAutoComplete(userids);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/

    private void adduseridsToAutoComplete(List<String> useridAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, useridAddressCollection);

        museridView.setAdapter(adapter);
    }


  /*  private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.userid.ADDRESS,
                ContactsContract.CommonDataKinds.userid.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }*/

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private class AuthenticateTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Login");
            dialog.setMessage("Authenticating....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            try {

                versionCode = getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionCode;

                String userauth_xml = "[DATA]" + "[USER_DATA][USER_ID]"
                        + userid + "[/USER_ID]" + "[PASSWORD]" + password
                        + "[/PASSWORD]" + "[IN_TIME]" + getCurrentTime()
                        + "[/IN_TIME]" + "[LATITUDE]" + lat
                        + "[/LATITUDE]" + "[LONGITUDE]" + lon
                        + "[/LONGITUDE]" + "[APP_VERSION]" + app_ver
                        + "[/APP_VERSION]" + "[ATT_MODE]OnLine[/ATT_MODE]"
                        + "[NETWORK_STATUS]" + "LoginStatus"
                        + "[/NETWORK_STATUS]" + "[/USER_DATA][/DATA]";

                SoapObject request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_LOGIN);
                request.addProperty("onXML", userauth_xml);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        CommonString.URL);

                androidHttpTransport.call(CommonString.SOAP_ACTION_LOGIN,
                        envelope);

                Object result = (Object) envelope.getResponse();

                if (result.toString()
                        .equalsIgnoreCase(CommonString.KEY_FAILURE)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            showAlert(CommonString.MESSAGE_FAILURE);
                        }
                    });

                } else if (result.toString().equalsIgnoreCase(
                        CommonString.KEY_FALSE)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            showAlert(CommonString.MESSAGE_FALSE);
                        }
                    });

                } else if (result.toString().equalsIgnoreCase(
                        CommonString.KEY_CHANGED)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            showAlert(CommonString.MESSAGE_CHANGED);
                        }
                    });

                } else {

                    XmlPullParserFactory factory = XmlPullParserFactory
                            .newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    final FailureGetterSetter failureGetterSetter = XMLHandlers
                            .failureXMLHandler(xpp, eventType);

                    if (failureGetterSetter.getStatus().equalsIgnoreCase(
                            CommonString.KEY_FAILURE)) {
                       /* final AlertMessage message = new AlertMessage(
                                LoginActivity.this, CommonString.METHOD_LOGIN
                                + failureGetterSetter.getErrorMsg(),
                                "login", null);*/
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                showAlert(CommonString.METHOD_LOGIN
                                        + failureGetterSetter.getErrorMsg());
                            }
                        });
                    } else {

                        try {
                            // For String source

                            xpp.setInput(new StringReader(result.toString()));
                            xpp.next();
                            eventType = xpp.getEventType();
                            lgs = XMLHandlers.loginXMLHandler(xpp, eventType);

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // PUT IN PREFERENCES
                        editor.putString(CommonString.KEY_USERNAME, userid);
                        editor.putString(CommonString.KEY_PASSWORD, password);
                        editor.putString(CommonString.KEY_VERSION, lgs.getAPP_VERSION());

                        editor.putString(CommonString.KEY_PATH, lgs.getAPP_PATH());
                        editor.putString(CommonString.KEY_DATE, lgs.getCURRENTDATE());
                        //editor.putString(CommonString.KEY_DATE, "01/21/2017");

                        editor.commit();

                        return CommonString.KEY_SUCCESS;

                    }
                }

                return "";

            } catch (MalformedURLException e) {

            /*    final AlertMessage message = new AlertMessage(
                        LoginActivity.this, AlertMessage.MESSAGE_EXCEPTION,
                        "acra_login", e);*/

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });

            } catch (IOException e) {
               /* final AlertMessage message = new AlertMessage(
                        LoginActivity.this,
                        AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket_login", e);*/

                counter++;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (counter < 3) {
                            new AuthenticateTask().execute();
                        } else {
                            showAlert(CommonString.MESSAGE_SOCKETEXCEPTION);
                            counter = 1;
                        }
                    }
                });
            } catch (Exception e) {
              /*  final AlertMessage message = new AlertMessage(
                        LoginActivity.this, AlertMessage.MESSAGE_EXCEPTION,
                        "acra_login", e);*/
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
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (result.equals(CommonString.KEY_SUCCESS)) {

//				database.open();
                if (preferences.getString(CommonString.KEY_VERSION, "").equals(
                        Integer.toString(versionCode))) {

                    if (preferences.getString(CommonString.KEY_LANGUAGE, "").equals("")) {

                        Intent intent = new Intent(getBaseContext(),
                                SelectLanguageActivity.class);

                        intent.putExtra(CommonString.KEY_LOGIN_DATA, lgs);
                        startActivity(intent);

                        finish();
                    } else {

                        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

                        Intent intent = new Intent(getBaseContext(),
                                MainActivity.class);
                        startActivity(intent);

                        finish();
                    }


                } else {
                    Intent intent = new Intent(getBaseContext(),
                            AutoUpdateActivity.class);

                    intent.putExtra(CommonString.KEY_PATH,
                            preferences.getString(CommonString.KEY_PATH, ""));
                    startActivity(intent);
                    finish();
                }

            }

            dialog.dismiss();
        }

    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    /*public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String intime = formatter.format(m_cal.getTime());

        return intime;
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

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());

        if (preferences.getString(CommonString.KEY_LANGUAGE, "").equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_KSA)) {
            cdate = arabicToenglish(cdate);
        }

        return cdate;
    }

    private static boolean updateResources(Context context, String language) {

        /*String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("ARABIC-KSA")) {
            lang = "AR";
        } else {
            lang = "TR";
        }*/

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

