package cpm.com.gskmtorange.dailyentry;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPlanogramCountrywiseGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

public class PlanogramPDFActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private Data data;
    int eventType;
    GSKOrangeDB db;
    String userId, culture_id;
    private SharedPreferences preferences = null;
    FloatingActionButton fab;

    MappingPlanogramCountrywiseGetterSetter document;
    MyRecyclerAdapter adapter;
    RecyclerView rec;
    String Path = Environment.getExternalStorageDirectory().toString() + "/Planogram_Documents/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planogram_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rec = (RecyclerView) findViewById(R.id.rec);

        fab  = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNetIsAvailable()){
                    new DowloadAsync(getApplicationContext()).execute();
                }
                else {
                    Snackbar.make(fab, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        culture_id = preferences.getString(CommonString.KEY_CULTURE_ID, "");
        new DowloadAsync(getApplicationContext()).execute();
    }

    //region Download doc
    private class DowloadAsync extends AsyncTask<Void, Data, String> {

        private Context context;

        DowloadAsync(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(PlanogramPDFActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom);
            //dialog.setTitle("Download Files");
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected String doInBackground(Void... params) {

            data = new Data();
            String resultHttp = "";
            boolean flag = true;
            // JCP

            try {

                XmlPullParserFactory factory = null;
                factory = XmlPullParserFactory
                        .newInstance();

                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_COUNTRYWISE_PLANOGRAM");
                request.addProperty("cultureid", culture_id);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        CommonString.URL);

                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL,
                        envelope);
                Object result = (Object) envelope.getResponse();

                if (result.toString() != null) {

                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();

                    document = XMLHandlers.MAPPING_COUNTRYWISE_PLANOGRAM_XMLHandler(xpp, eventType);

                    if (document.getCOUNTRY_ID().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String document_Table = document.getTable_MAPPING_COUNTRYWISE_PLANOGRAM();
                        TableBean.setMappingCountrywisePlanogram(document_Table);

                    } else {
                        return "HR_DOCUMENTS";
                    }

                    data.value = 10;
                    data.name = "JCP Data Downloading";

                    if(document.getCOUNTRY_ID().size()>0){

                        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                        File folder = new File(extStorageDirectory, "Planogram_Documents");
                        folder.mkdir();

                        for(int i = 0; i<document.getFILE_PATH().size();i++){
                            flag = downloadFile(document.getFILE_PATH().get(i), document.getPLANOGRAM_URL().get(i), folder);

                            if(!flag)
                                //return CommonString.KEY_SUCCESS+ ":"+ folder.getAbsolutePath()+"/"+document.getPLANOGRAM_URL().get(0);
                                return getString(R.string.Download_pdf_Error);
                        }

                    }

                   /* db.open();
                    db.InsertMappingCountrywisePlanogram(document);*/

                }

                publishProgress(data);

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                resultHttp = getString(R.string.nonetwork);
                flag = false;
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
                resultHttp = getString(R.string.nonetwork);
                flag = false;
            } catch (IOException e) {
                e.printStackTrace();
                resultHttp = getString(R.string.nonetwork);
                flag = false;
            }
            catch ( Exception e){
                resultHttp = getString(R.string.nonetwork);
                flag = false;
            }

            if(flag)
                return CommonString.KEY_SUCCESS;
            else
                return resultHttp;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.cancel();

            if (result.contains(CommonString.KEY_SUCCESS)) {

                if(document.getPLANOGRAM_URL().size()>0){
                    adapter = new MyRecyclerAdapter(getApplicationContext(), document);
                    rec.setAdapter(adapter);
                    rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }


            } else {
                Snackbar.make(fab, result, Toast.LENGTH_SHORT).show();
            }

            //finish();
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            // TODO Auto-generated method stub

            pb.setProgress(values[0].value);
            percentage.setText(values[0].value + "%");
            message.setText(values[0].name);

        }
    }
    //endregion

    class Data {
        int value;
        String name;
    }

    public boolean downloadFile(String fileUrl, String directory, File folder_path) {

        boolean flag = true;

        try {
            final int MEGABYTE = 1024 * 1024;
            URL url = new URL(fileUrl + directory);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.getResponseCode();
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                int length = urlConnection.getContentLength();

                String size = new DecimalFormat("##.##")
                        .format((double) ((double) length / 1024))
                        + " KB";

       /* String PATH = Environment
                .getExternalStorageDirectory()
                + "/GT_GSK_Images/";*/

                if (!new File( folder_path.getPath()+"/" + directory).exists()
                        && !size.equalsIgnoreCase("0 KB")) {

                    File outputFile = new File(folder_path,
                            directory);
                    FileOutputStream fos = new FileOutputStream(
                            outputFile);
                    InputStream is1 = (InputStream) urlConnection
                            .getInputStream();

                    int bytes = 0;
                    byte[] buffer = new byte[1024];
                    int len1 = 0;

                    while ((len1 = is1.read(buffer)) != -1) {

                        bytes = (bytes + len1);

                        // data.value = (int) ((double) (((double)
                        // bytes) / length) * 100);

                        fos.write(buffer, 0, len1);

                    }

                    fos.close();
                    is1.close();

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            flag = false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            flag = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
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

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private LayoutInflater inflator;

        MappingPlanogramCountrywiseGetterSetter data = new MappingPlanogramCountrywiseGetterSetter();

        public MyRecyclerAdapter(Context context, MappingPlanogramCountrywiseGetterSetter data) {

            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.pdf_planogram_item, parent, false);

            MyRecyclerAdapter.MyViewHolder holder = new MyRecyclerAdapter.MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            //final MappingPlanogramCountrywiseGetterSetter current = data.get(position);

            final String name = data.getPLANOGRAM_URL().get(position);

            holder.name.setText(name);
            //holder.detail.setText(current.getDocument_descriiption().get(0));

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String file_path = Path + name;

                    File file = new File(file_path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    //finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }
            });
        }

        @Override
        public int getItemCount() {
            return document.getFILE_PATH().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name, detail;
            LinearLayout parent_layout;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_name);
                detail = (TextView) itemView.findViewById(R.id.tv_details);

                parent_layout = (LinearLayout) itemView.findViewById(R.id.layout_parent);

            }

        }
    }
}
