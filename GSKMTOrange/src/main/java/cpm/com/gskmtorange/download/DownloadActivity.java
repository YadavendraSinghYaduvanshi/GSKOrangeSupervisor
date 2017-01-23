package cpm.com.gskmtorange.download;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.ADDITIONAL_DISPLAY_MASTERGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayChecklistMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_PLANOGRAM_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_SOS_TARGET_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingDisplayChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPromotionGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NonWorkingReasonGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.STORE_PERFORMANCE_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SubCategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

public class DownloadActivity extends AppCompatActivity {

    Data data;
    int eventType;
    GSKOrangeDB db;
    String userId, culture_id;
    JourneyPlanGetterSetter jcpgettersetter;
    SkuMasterGetterSetter skumastergettersetter;
    BrandMasterGetterSetter brandMasterGetterSetter;
    SubCategoryMasterGetterSetter subCategoryMasterGetterSetter;
    CategoryMasterGetterSetter categoryMasterGetterSetter;
    DisplayMasterGetterSetter displayMasterGetterSetter;
    MappingStockGetterSetter mappingStockGetterSetter;
    MAPPINGT2PGetterSetter mappingt2PGetterSetter;
    DisplayChecklistMasterGetterSetter checklistMasterGetterSetter;
    MappingDisplayChecklistGetterSetter mappingChecklistGetterSetter;
    NonWorkingReasonGetterSetter nonWorkingReasonGetterSetter;
    MappingPromotionGetterSetter mappingPromotionGetterSetter;
    MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter mapping_additional_promotion_masterGetterSetter;
    STORE_PERFORMANCE_MasterGetterSetter store_performance_masterGetterSetter;
    ADDITIONAL_DISPLAY_MASTERGetterSetter additional_display_getter_setter;
    MAPPING_SOS_TARGET_MasterGetterSetter mapping_sos_target_masterGetterSetter;

    MAPPING_PLANOGRAM_MasterGetterSetter mapping_planogram_masterGetterSetter;
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private SharedPreferences preferences = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new GSKOrangeDB(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));

        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        culture_id = preferences.getString(CommonString.KEY_CULTURE_ID, "");
        new UploadTask(DownloadActivity.this).execute();
    }


    class Data {
        int value;
        String name;
    }

    private class UploadTask extends AsyncTask<Void, Data, String> {
        private Context context;

        UploadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_dialog_progress);
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resultHttp = "";
                data = new Data();

                data.value = 10;
                data.name = "JCP " + getResources().getString(R.string.download_data);
                publishProgress(data);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "JOURNEY_PLAN");
                request.addProperty("cultureid", culture_id);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                Object result = envelope.getResponse();

                if (result.toString() != null) {
                    //InputStream stream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));

                    xpp.setInput(new StringReader(result.toString()));
                    // xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    // xpp.setInput(stream,"UTF-8");
                    xpp.next();
                    eventType = xpp.getEventType();

                    jcpgettersetter = XMLHandlers.JCPXMLHandler(xpp, eventType);

                    if (jcpgettersetter.getSTORE_ID().size() > 0) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        String jcpTable = jcpgettersetter.getTable_journey_plan();
                        TableBean.setJourneyPlan(jcpTable);
                    } else {
                        return "JOURNEY_PLAN";
                    }

                    data.value = 10;
                    data.name = "JCP " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // Store List Master
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "SKU_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    skumastergettersetter = XMLHandlers.skuMasterXMLHandler(xpp, eventType);
                    if (skumastergettersetter.getSKU_ID().size() > 0) {
                        String skutable = skumastergettersetter.getTable_SKU_MASTER();
                        if (skutable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSkuMaster(skutable);
                        }
                    } else {
                        return "SKU_MASTER";
                    }

                    data.value = 20;
                    data.name = "SKU_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // BRAND_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "BRAND_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    brandMasterGetterSetter = XMLHandlers.brandMasterXMLHandler(xpp, eventType);
                    if (brandMasterGetterSetter.getBRAND_ID().size() > 0) {
                        String brandtable = brandMasterGetterSetter.getTable_BRAND_MASTER();
                        if (brandtable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setBrandMaster(brandtable);
                        }
                    } else {
                        return "BRAND_MASTER";
                    }

                    data.value = 25;
                    data.name = "BRAND_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // SUB_CATEGORY_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "SUB_CATEGORY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    subCategoryMasterGetterSetter = XMLHandlers.subCategoryMasterXMLHandler(xpp, eventType);
                    if (subCategoryMasterGetterSetter.getSUB_CATEGORY_ID().size() > 0) {
                        String categorytable = subCategoryMasterGetterSetter.getTable_SUB_CATEGORY_MASTER();
                        if (categorytable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setSubCategoryMaster(categorytable);
                        }
                    } else {
                        return "SUB_CATEGORY_MASTER";
                    }
                    data.value = 30;
                    data.name = "SUB_CATEGORY_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // CATEGORY_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "CATEGORY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    categoryMasterGetterSetter = XMLHandlers.categoryMasterXMLHandler(xpp, eventType);
                    if (categoryMasterGetterSetter.getCATEGORY_ID().size() > 0) {
                        String skutable = categoryMasterGetterSetter.getTable_CATEGORY_MASTER();
                        if (skutable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setCategoryMaster(skutable);
                        }
                    } else {
                        return "CATEGORY_MASTER";
                    }
                    data.value = 35;
                    data.name = "CATEGORY_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // DISPLAY_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "DISPLAY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    displayMasterGetterSetter = XMLHandlers.displayMasterXMLHandler(xpp, eventType);
                    if (displayMasterGetterSetter.getDISPLAY_ID().size() > 0) {
                        String display_table = displayMasterGetterSetter.getTable_DISPLAY_MASTER();
                        if (display_table != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setDisplayMaster(display_table);
                        }
                    } else {
                        return "DISPLAY_MASTER";
                    }
                    data.value = 40;
                    data.name = "DISPLAY_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);

                // MAPPING_STOCK
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_STOCK");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingStockGetterSetter = XMLHandlers.mappingStockXMLHandler(xpp, eventType);

                    String stocktable = mappingStockGetterSetter.getTable_MAPPING_STOCK();
                    if (stocktable != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingStock(stocktable);
                    }

                    if (mappingStockGetterSetter.getSKU_ID().size() > 0) {
                        data.value = 45;
                        data.name = "MAPPING_STOCK " + getResources().getString(R.string.download_data);
                    } else {
                        // return "MAPPING_STOCK";
                    }

                }
                publishProgress(data);


                // MAPPING_T2P
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_T2P");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingt2PGetterSetter = XMLHandlers.mappingT2pXMLHandler(xpp, eventType);

                    String t2ptable = mappingt2PGetterSetter.getTable_MAPPING_T2P();
                    if (t2ptable != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingT2p(t2ptable);
                    }

                    if (mappingt2PGetterSetter.getSTORE_ID().size() > 0) {
                        data.value = 50;
                        data.name = "MAPPING_T2P " + getResources().getString(R.string.download_data);

                    } else {
                        //return "MAPPING_T2P";
                    }

                }
                publishProgress(data);

                // DISPLAY_CHECKLIST_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "DISPLAY_CHECKLIST_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    checklistMasterGetterSetter = XMLHandlers.mappingDisplayChecklistMasterXMLHandler(xpp, eventType);
                    if (checklistMasterGetterSetter.getCHECKLIST_ID().size() > 0) {
                        String checklist_master_table = checklistMasterGetterSetter.getTable_DISPLAY_CHECKLIST_MASTER();
                        if (checklist_master_table != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setDisplayChecklistMaster(checklist_master_table);
                        }
                    } else {
                        return "DISPLAY_CHECKLIST_MASTER";
                    }
                    data.value = 55;
                    data.name = "DISPLAY_CHECKLIST_MASTER " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);

                // MAPPING_DISPLAY_CHECKLIST
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_DISPLAY_CHECKLIST");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingChecklistGetterSetter = XMLHandlers.mappingMappingDisplayChecklistXMLHandler(xpp, eventType);

                    String mapping_display_checklisttable = mappingChecklistGetterSetter.getTable_MAPPING_DISPLAY_CHECKLIST();
                    if (mapping_display_checklisttable != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingDisplayChecklist(mapping_display_checklisttable);
                    }

                    if (mappingChecklistGetterSetter.getCHECKLIST_ID().size() > 0) {
                        data.value = 60;
                        data.name = "MAPPING_DISPLAY_CHECKLIST " + getResources().getString(R.string.download_data);
                    } else {
                        //return "MAPPING_DISPLAY_CHECKLIST";
                    }

                }
                publishProgress(data);


                // NON_WORKING_REASON
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "NON_WORKING_REASON");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    nonWorkingReasonGetterSetter = XMLHandlers.nonWorkingReasonXMLHandler(xpp, eventType);
                    if (nonWorkingReasonGetterSetter.getREASON_ID().size() > 0) {
                        String reasontable = nonWorkingReasonGetterSetter.getTable_NON_WORKING_REASON();
                        if (reasontable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setNonWorkingReason(reasontable);
                        }
                    } else {
                        return "NON_WORKING_REASON";
                    }
                    data.value = 65;
                    data.name = "NON_WORKING_REASON " + getResources().getString(R.string.download_data);
                }
                publishProgress(data);


                // MAPPING_PROMOTION
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_PROMOTION");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingPromotionGetterSetter = XMLHandlers.mappingPromotionXMLHandler(xpp, eventType);

                    String mapping_promotion_table = mappingPromotionGetterSetter.getTable_MAPPING_PROMOTION();
                    if (mapping_promotion_table != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingPromotion(mapping_promotion_table);
                    }

                    if (mappingPromotionGetterSetter.getSTORE_ID().size() > 0) {
                        data.value = 70;
                        data.name = "MAPPING_PROMOTION " + getResources().getString(R.string.download_data);
                    }


                }
                publishProgress(data);


                //Gagan start code

                // MAPPING_ADDITIONAL_PROMOTION
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_ADDITIONAL_PROMOTION");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mapping_additional_promotion_masterGetterSetter = XMLHandlers.mappingAdditionalPromotionXMLHandler(xpp, eventType);

                    //if (mapping_additional_promotion_masterGetterSetter.getSTORE_ID().size() > 0) {
                    String mapping_additional_promotion_table = mapping_additional_promotion_masterGetterSetter.getTable_MAPPING_ADDITIONAL_PROMOTION();
                    if (mapping_additional_promotion_table != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingAdditionalPromotion(mapping_additional_promotion_table);
                    }

                    if (mapping_additional_promotion_masterGetterSetter.getSTORE_ID().size() > 0) {
                        data.value = 75;
                        data.name = "MAPPING_ADDITIONAL_PROMOTION " + getResources().getString(R.string.download_data);
                    }
                }
                publishProgress(data);


                //STORE_PERFORMANCE
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "STORE_PERFORMANCE");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    store_performance_masterGetterSetter = XMLHandlers.STORE_PERFORMANCEXMLHandler(xpp, eventType);

                    String table_store_performace = store_performance_masterGetterSetter.getTable_STORE_PERFORMANCE();
                    if (table_store_performace != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setStorePerformance(table_store_performace);
                    }

                    if (store_performance_masterGetterSetter.getSTORE_ID().size() > 0) {
                        data.value = 80;
                        data.name = "STORE_PERFORMANCE Data Download";
                    } else {
                        //return "STORE_PERFORMANCE";
                    }

                }
                publishProgress(data);


                //ADDITIONAL_DISPLAY_MASTER
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "ADDITIONAL_DISPLAY_MASTER");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    additional_display_getter_setter = XMLHandlers.ADDITIONAL_DISPLAY_MASTERXMLHandler(xpp, eventType);

                    if (additional_display_getter_setter.getDISPLAY_ID().size() > 0) {
                        String table_store_display = additional_display_getter_setter.getTable_STORE_ADDITIONAL_DISPLAY();
                        if (table_store_display != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setAdditionalDisplay(table_store_display);
                        }
                    } else {
                        //return "ADDITIONAL_DISPLAY_MASTER";
                    }
                    data.value = 100;
                    data.name = "ADDITIONAL_DISPLAY_MASTER Data Download";
                }
                publishProgress(data);


                //MAPPING_PLANOGRAM
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_PLANOGRAM");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mapping_planogram_masterGetterSetter = XMLHandlers.MAPPING_PLANOGRAM_XMLHandler(xpp, eventType);

                    String table_mapping_planogram = mapping_planogram_masterGetterSetter.getTable_MAPPING_PLANOGRAM();
                    if (table_mapping_planogram != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingPlanogram(table_mapping_planogram);
                    }
                    if (mapping_planogram_masterGetterSetter.getKEYACCOUNT_ID().size() > 0) {
                        data.value = 85;
                        data.name = "MAPPING_PLANOGRAM Data Download";
                    } else {
                        //return "MAPPING_PLANOGRAM";
                    }
                }
                publishProgress(data);


                // MAPPING_SOS_TARGET
                request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", userId);
                request.addProperty("Type", "MAPPING_SOS_TARGET");
                request.addProperty("cultureid", culture_id);

                envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);

                result = envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mapping_sos_target_masterGetterSetter = XMLHandlers.MAPPING_SOS_TARGETXMLHandler(xpp, eventType);

                    //if (mapping_additional_promotion_masterGetterSetter.getSTORE_ID().size() > 0) {
                    String table_mapping_sos_target = mapping_sos_target_masterGetterSetter.getTable_MAPPING_SOS_TARGET();
                    if (table_mapping_sos_target != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingSosTarget(table_mapping_sos_target);
                    }

                    if (mapping_sos_target_masterGetterSetter.getSTORE_ID().size() > 0) {
                        data.value = 75;
                        data.name = "MAPPING_SOS_TARGET " + getResources().getString(R.string.download_data);
                    }
                }
                publishProgress(data);


                //MAPPING_PLANOGRAM Image save into folder
                if (mapping_planogram_masterGetterSetter != null) {

                    for (int i = 0; i < mapping_planogram_masterGetterSetter.getIMAGE_PATH().size(); i++) {
                        //publishing image download
                        data.value = data.value + 1;
                        if (data.value < 100) {
                            publishProgress(data);
                        }

                        String image_name = mapping_planogram_masterGetterSetter.getPLANOGRAM_IMAGE().get(i);
                        String path = mapping_planogram_masterGetterSetter.getIMAGE_PATH().get(i);

                        if (!image_name.equalsIgnoreCase("NA") && !image_name.equalsIgnoreCase("")) {
                            URL url = new URL(path + "/" + image_name);
                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("GET");
                            c.getResponseCode();
                            c.connect();

                            if (c.getResponseCode() == 200) {
                                int length = c.getContentLength();

                                String size = new DecimalFormat("##.##").format((double) length / 1024) + " KB";

                                //String PATH = Environment.getExternalStorageDirectory() + "/Download/GT_GSK_Images/";
                                String PATH = CommonString.FILE_PATH;
                                File file = new File(PATH);
                                if (!file.isDirectory()) {
                                    file.mkdir();
                                }

                                //  Environment.getExternalStorageDirectory() + "/GT_GSK_Images/" + _pathforcheck1;
                                if (!new File(PATH + image_name).exists() && !size.equalsIgnoreCase("0 KB")) {
                                    File outputFile = new File(file, image_name);
                                    FileOutputStream fos = new FileOutputStream(outputFile);
                                    InputStream is1 = c.getInputStream();

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
                        }
                    }
                }

                //Gagan end code

                //Display Master Image save into folder
                if (displayMasterGetterSetter != null) {

                    for (int i = 0; i < displayMasterGetterSetter.getIMAGE_URL().size(); i++) {
                        //publishing image download
                        data.value = data.value + 1;
                        if (data.value < 100) {
                            publishProgress(data);
                        }

                        String image_name = displayMasterGetterSetter.getIMAGE_URL().get(i);
                        String img_url = displayMasterGetterSetter.getIMAGE_PATH().get(i);

                        if (!img_url.equalsIgnoreCase("") && !image_name.equalsIgnoreCase("")) {
                            URL url = new URL(img_url + image_name);
                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("GET");
                            c.getResponseCode();
                            c.connect();

                            if (c.getResponseCode() == 200) {
                                int length = c.getContentLength();

                                String size = new DecimalFormat("##.##").format((double) length / 1024) + " KB";

                                //String PATH = Environment.getExternalStorageDirectory() + "/Download/GT_GSK_Images/";
                                String PATH = CommonString.FILE_PATH;
                                File file = new File(PATH);
                                if (!file.isDirectory()) {
                                    file.mkdir();
                                }

                                //  Environment.getExternalStorageDirectory() + "/GT_GSK_Images/" + _pathforcheck1;
                                if (!new File(PATH + image_name).exists() && !size.equalsIgnoreCase("0 KB")) {
                                    File outputFile = new File(file, image_name);
                                    FileOutputStream fos = new FileOutputStream(outputFile);
                                    InputStream is1 = c.getInputStream();

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
                        }
                    }
                }

                db.open();
                db.InsertJCP(jcpgettersetter);
                db.InsertCategory(categoryMasterGetterSetter);
                db.InsertSubCategoryMaster(subCategoryMasterGetterSetter);
                db.InsertBrandMaster(brandMasterGetterSetter);
                db.InsertSkuMaster(skumastergettersetter);
                db.InsertDisplayMaster(displayMasterGetterSetter);
                db.InsertMAPPING_T2P(mappingt2PGetterSetter);
                db.InsertMappingStock(mappingStockGetterSetter);
                db.InsertDisplayChecklistMaster(checklistMasterGetterSetter);
                db.InsertMappingDisplayChecklist(mappingChecklistGetterSetter);
                db.InsertMAPPING_PROMOTION(mappingPromotionGetterSetter);
                db.InsertMAPPING_ADDITIONAL_PROMOTION(mapping_additional_promotion_masterGetterSetter);

                db.insertNonWorkingData(nonWorkingReasonGetterSetter);

                db.insertNonWorkingData(nonWorkingReasonGetterSetter);

                db.InsertSTORE_PERFORMANCE(store_performance_masterGetterSetter);
                db.InsertMAPPING_PLANOGRAM(mapping_planogram_masterGetterSetter);
                db.InsertADDITIONAL_DISPLAY(additional_display_getter_setter);
                db.InsertMAPPING_SOS_TARGET(mapping_sos_target_masterGetterSetter);

            } catch (MalformedURLException e) {
                /*final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showAlert(CommonString.MESSAGE_EXCEPTION);
                    }
                });
            } catch (IOException e) {
               /* final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);*/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(CommonString.MESSAGE_SOCKETEXCEPTION);
                    }
                });
            } catch (XmlPullParserException e) {
                e.printStackTrace();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            showAlert(getString(R.string.data_downloaded_successfully));

        }

    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setTitle(getString(R.string.main_menu_activity_name));
        updateResources(getApplicationContext(), preferences.getString(CommonString.KEY_LANGUAGE, ""));
    }


    private static boolean updateResources(Context context, String language) {

        String lang;

        if (language.equalsIgnoreCase("English")) {
            lang = "EN";
        } else if (language.equalsIgnoreCase("UAE")) {
            lang = "AR";
        } else {
            lang = "TR";
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
