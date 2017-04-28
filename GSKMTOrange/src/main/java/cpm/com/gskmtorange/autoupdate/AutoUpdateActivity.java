package cpm.com.gskmtorange.autoupdate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;

import cpm.com.gskmtorange.LoginActivity;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;

public class AutoUpdateActivity extends AppCompatActivity {

    String versionCode;
    int length;
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private Data data;

    String path = "", p, s;

    ProgressBar progressBar;
    private boolean status;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();

        path = intent.getStringExtra(CommonString.KEY_PATH);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parinaam");
        builder.setMessage(getString(R.string.new_update_available))
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences preferences = PreferenceManager
                                .getDefaultSharedPreferences(AutoUpdateActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

						/*new File(
                                "/data/data/com.cpm.gsk_mt/databases/GTMT_DATABASE")
								.delete();*/

                        new DownloadTask(AutoUpdateActivity.this).execute();

                    }
                });

        AlertDialog alert = builder.create();

        alert.show();

    }

    private class DownloadTask extends AsyncTask<Void, Data, String> {

        private Context context;

        DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_layout);
            dialog.setTitle("Download");
            dialog.setCancelable(false);
            dialog.show();

            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);

        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                data = new Data();
                data.name = "Downloading Application";
                publishProgress(data);

                versionCode = getPackageManager().getPackageInfo(
                        getPackageName(), 0).versionName;

                data.name = "Upgrading Version : " + versionCode;
                publishProgress(data);

                // download application
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);
                c.getResponseCode();
                c.connect();
                length = c.getContentLength();

                String size = new DecimalFormat("##.##")
                        .format((double) ((double) length / 1024) / 1024)
                        + " MB";

                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "app.apk");
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                int bytes = 0;
                byte[] buffer = new byte[1024];
                int len1 = 0;

                while ((len1 = is.read(buffer)) != -1) {

                    bytes = (bytes + len1);

                    s = new DecimalFormat("##.##")
                            .format((double) ((double) (bytes / 1024)) / 1024);

                    p = s.length() == 3 ? s + "0" : s;

                    p = p + " MB";
                    data.value = (int) ((double) (((double) bytes) / length) * 100);

                    data.name = "Download " + p + "/" + size;
                    publishProgress(data);

                    fos.write(buffer, 0, len1);

                }
                fos.close();
                is.close();

                return CommonString.KEY_SUCCESS;

            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
              /*  final AlertMessage message = new AlertMessage(
                        AutoUpdateActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });
            } catch (MalformedURLException e) {

               /* final AlertMessage message = new AlertMessage(
                        AutoUpdateActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });

            } catch (IOException e) {
              /*  final AlertMessage message = new AlertMessage(
                        AutoUpdateActivity.this,
                        AlertMessage.MESSAGE_SOCKETEXCEPTION, "update", e);*/
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showAlert(CommonString.MESSAGE_SOCKETEXCEPTION);
                    }
                });
            } catch (Exception e) {
              /*  final AlertMessage message = new AlertMessage(
                        AutoUpdateActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/
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
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub

            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equals(CommonString.KEY_SUCCESS)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory()
                                + "/download/"
                                + "app.apk")),
                        "application/vnd.android.package-archive");
                startActivity(i);

                AutoUpdateActivity.this.finish();
            }

        }

    }

    class Data {
        int value;
        String name;
    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AutoUpdateActivity.this);
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

        } else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_ARABIC_UAE)) {
            lang = CommonString.KEY_RETURE_LANGUAGE_UAE_ARABIC;
        }else if (language.equalsIgnoreCase(CommonString.KEY_LANGUAGE_OMAN)) {
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
