package cpm.com.gskmtorange.dailyentry;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Locale;

import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonFunctions;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.messgae.AlertMessage;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

public class FutureJCPActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    //ImageButton calenderBtn;
    TextView txt_date;
    RecyclerView futureJcpList;
    Calendar c;
    int year;
    int month;
    int day;
    SharedPreferences preferences;
    String _UserId;
    ProgressDialog progressDialog;
    int eventType;
    JourneyPlanGetterSetter journeyPlanPreviousGetterSetter;
    String culture_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_jcp);
        declaration();

        //calenderBtn.setOnClickListener(this);
        fab.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iconCalender:
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                showDatePickerDialog(year, month, day);
                break;
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

        }

    }

    protected void showDatePickerDialog(int year, int month, int day) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, pickerListener, year, month, day);
        // ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
        //datePickerDialog.findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.setTitle("");
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            futureJcpList.removeAllViewsInLayout();
            futureJcpList.invalidate();
            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            String day_str = String.valueOf(day);
            day_str = "00" + day_str;
            day_str = day_str.substring(day_str.length() - 2, day_str.length());


            String month_str = String.valueOf(month);
            month_str = "00" + month_str;
            month_str = month_str.substring(month_str.length() - 2, month_str.length());

            String yeat_str = String.valueOf(year);

            txt_date.setText(new StringBuilder().append(month_str).append("/").append(day_str).append("/").append(yeat_str)
            );
            new Task().execute(txt_date.getText().toString());

        }
    };


    void declaration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //calenderBtn = (ImageButton) findViewById(R.id.iconCalender);
        txt_date = (TextView) findViewById(R.id.txt_date);
        futureJcpList = (RecyclerView) findViewById(R.id.futureJcpList);
        context = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        culture_id = preferences.getString(CommonString.KEY_CULTURE_ID, "");
        progressDialog = new ProgressDialog(FutureJCPActivity.this);

        CommonFunctions.updateLangResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }

    // AsyncTask asyncTask = new AsyncTask<String, String, String>() {
    class Task extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching Data..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory
                        .newInstance();

                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                SoapSerializationEnvelope envelope;
                HttpTransportSE androidHttpTransport;
                SoapObject request;

                // Brand Master data
                request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", _UserId);
                request.addProperty("Type", "JOURNEY_SEARCH:" + params[0]);
                request.addProperty("cultureid", culture_id);
                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                androidHttpTransport = new HttpTransportSE(CommonString.URL);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                Object resultFuturedata = (Object) envelope.getResponse();

                if (resultFuturedata.toString() != null) {

                    xpp.setInput(new StringReader(resultFuturedata.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    journeyPlanPreviousGetterSetter = XMLHandlers.JCPXMLHandler(xpp, eventType);

                }
                return "Success";
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return "failure";
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
                return "failure";
            } catch (IOException e) {
                e.printStackTrace();
                return getResources().getString(R.string.nonetwork);
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }


        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            if (o.equalsIgnoreCase("Success")) {
                if (journeyPlanPreviousGetterSetter.getSTORE_ID().size() > 0) {

                    MyListAdapter adapter = new MyListAdapter(context, journeyPlanPreviousGetterSetter);
                    futureJcpList.setLayoutManager(new LinearLayoutManager(context));
                    futureJcpList.setAdapter(adapter);

                } else {

                    Snackbar.make(futureJcpList,R.string.no_route_plan_for_day,Snackbar.LENGTH_SHORT).show();
                }
            } else {

                Snackbar.make(futureJcpList,o,Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.date_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.iconCalender){
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            showDatePickerDialog(year, month, day);

        }else if (id == android.R.id.home) {

            // NavUtils.navigateUpFromSameTask(this);
            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }


    class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
        LayoutInflater layoutInflater;
        JourneyPlanGetterSetter journeyPlanPreviousGetterSetter;

        MyListAdapter(Context context, JourneyPlanGetterSetter journeyPlanPreviousGetterSetter) {
            layoutInflater = LayoutInflater.from(context);
            this.journeyPlanPreviousGetterSetter = journeyPlanPreviousGetterSetter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_future_jcp_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (holder != null) {
                holder.txt_store_cd.setText(journeyPlanPreviousGetterSetter.getSTORE_ID().get(position));
                holder.txt_keyacct.setText(journeyPlanPreviousGetterSetter.getKEYACCOUNT().get(position));
                holder.txt_storename.setText(journeyPlanPreviousGetterSetter.getSTORE_NAME().get(position));
                holder.txt_city.setText(journeyPlanPreviousGetterSetter.getCITY().get(position));
                holder.txt_storetype.setText(journeyPlanPreviousGetterSetter.getSTORETYPE().get(position));

            }
        }


        @Override
        public int getItemCount() {

            return journeyPlanPreviousGetterSetter.getSTORE_ID().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_store_cd, txt_keyacct, txt_storename, txt_city, txt_storetype;
            LinearLayout ll_itemfutureJCP;

            public ViewHolder(View view) {
                super(view);
                txt_store_cd = (TextView) view.findViewById(R.id.txt_store_cd);
                txt_keyacct = (TextView) view.findViewById(R.id.txt_keyacct);
                txt_storename = (TextView) view.findViewById(R.id.txt_storename);
                txt_city = (TextView) view.findViewById(R.id.txt_city);
                txt_storetype = (TextView) view.findViewById(R.id.txt_storetype);
                ll_itemfutureJCP = (LinearLayout) view.findViewById(R.id.ll_itemfutureJCP);
            }
        }
    }

}
