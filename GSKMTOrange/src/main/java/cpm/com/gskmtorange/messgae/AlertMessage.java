package cpm.com.gskmtorange.messgae;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import cpm.com.gskmtorange.GeoTag.GeoTagStoreList;

/**
 * Created by ashishc on 03-01-2017.
 */

public class AlertMessage {

    public static final String MESSAGE_ERROR = "Network Error , ";

    public static final String MESSAGE_SUCCESS = "Uploaded Data , ";

    public static final String MESSAGE_DATA_NOT = "Data Not Uploaded , ";

    private Exception exception;
    String value;
    private String data, condition,exceptionString;
    private Activity activity;
    private String error;



    public AlertMessage(Activity activity, String data, String condition,
                        Exception exception) {
        this.activity = activity;
        this.data = data;
        this.condition = condition;
        this.exception = exception;
    }

    public AlertMessage(Activity activity, String data, String condition,
                        String exception, String a) {
        this.activity = activity;
        this.data = data;
        this.condition = condition;
        this.exceptionString = exception;
        this.error = a;
    }


    public void showMessage() {

         if (condition.equals("success")) {

            ShowAlert1(data);
        }
        else if(condition.equals("failure"))
         {
             ShowAlertSocket(data);
         }



    }

    public void ShowAlert1(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(activity, GeoTagStoreList.class);
                        activity.startActivity(i);

                        activity.finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public void ShowAlertSocket(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(activity, GeoTagStoreList.class);
                        activity.startActivity(i);

                        activity.finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }






}





