package cpm.com.gskmtorange.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

import cpm.com.gskmtorange.Database.GSKOrangeDB;
import cpm.com.gskmtorange.R;
import cpm.com.gskmtorange.constant.CommonString;
import cpm.com.gskmtorange.xmlGetterSetter.BrandMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.CategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayChecklistMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.DisplayMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.JourneyPlanGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPINGT2PGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MAPPING_ADDITIONAL_PROMOTION_MasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingDisplayChecklistGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingPromotionGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.MappingStockGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.NonWorkingReasonGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SkuMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.SubCategoryMasterGetterSetter;
import cpm.com.gskmtorange.xmlGetterSetter.TableBean;
import cpm.com.gskmtorange.xmlHandlers.XMLHandlers;

public class DownloadActivity extends AppCompatActivity {

    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    Data data;
    int eventType;
    GSKOrangeDB db;
    private SharedPreferences preferences = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        db = new GSKOrangeDB(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                data.name = "JCP Data Downloading";
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

                Object result = (Object) envelope.getResponse();

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
                    data.name = "JCP Data Downloading";
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

                result = (Object) envelope.getResponse();

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
                    data.name = "SKU_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

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

                    data.value = 30;
                    data.name = "BRAND_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

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
                    data.value = 40;
                    data.name = "SUB_CATEGORY_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

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
                    data.value = 50;
                    data.name = "CATEGORY_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

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
                    data.value = 60;
                    data.name = "DISPLAY_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingStockGetterSetter = XMLHandlers.mappingStockXMLHandler(xpp, eventType);
                    if (mappingStockGetterSetter.getSKU_ID().size() > 0) {
                        String stocktable = mappingStockGetterSetter.getTable_MAPPING_STOCK();
                        if (stocktable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setMappingStock(stocktable);
                        }
                    } else {
                        return "MAPPING_STOCK";
                    }
                    data.value = 80;
                    data.name = "MAPPING_STOCK Data Download";
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

                result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingt2PGetterSetter = XMLHandlers.mappingT2pXMLHandler(xpp, eventType);
                    if (mappingt2PGetterSetter.getSTORE_ID().size() > 0) {
                        String t2ptable = mappingt2PGetterSetter.getTable_MAPPING_T2P();
                        if (t2ptable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setMappingT2p(t2ptable);
                        }
                    } else {
                        //return "MAPPING_T2P";
                    }
                    data.value = 100;
                    data.name = "MAPPING_T2P Data Download";
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

                result = (Object) envelope.getResponse();

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
                    data.value = 100;
                    data.name = "DISPLAY_CHECKLIST_MASTER Data Download";
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

                result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingChecklistGetterSetter = XMLHandlers.mappingMappingDisplayChecklistXMLHandler(xpp, eventType);
                    if (mappingChecklistGetterSetter.getCHECKLIST_ID().size() > 0) {
                        String mapping_display_checklisttable = mappingChecklistGetterSetter.getTable_MAPPING_DISPLAY_CHECKLIST();
                        if (mapping_display_checklisttable != null) {
                            resultHttp = CommonString.KEY_SUCCESS;
                            TableBean.setMappingDisplayChecklist(mapping_display_checklisttable);
                        }
                    } else {
                        //return "MAPPING_DISPLAY_CHECKLIST";
                    }
                    data.value = 100;
                    data.name = "MAPPING_DISPLAY_CHECKLIST Data Download";
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

                result = (Object) envelope.getResponse();

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
                    data.value = 100;
                    data.name = "NON_WORKING_REASON Data Download";
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

                result = (Object) envelope.getResponse();

                if (result.toString() != null) {
                    xpp.setInput(new StringReader(result.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    mappingPromotionGetterSetter = XMLHandlers.mappingPromotionXMLHandler(xpp, eventType);
                    //if (mappingPromotionGetterSetter.getSTORE_ID().size() > 0) {
                    String mapping_promotion_table = mappingPromotionGetterSetter.getTable_MAPPING_PROMOTION();
                    if (mapping_promotion_table != null) {
                        resultHttp = CommonString.KEY_SUCCESS;
                        TableBean.setMappingPromotion(mapping_promotion_table);
                    }
                    /*} else {
                        //return "MAPPING_PROMOTION";
                    }*/
                    data.value = 100;
                    data.name = "MAPPING_PROMOTION Data Download";
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

                result = (Object) envelope.getResponse();

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
                    /*} else {
                        //return "MAPPING_ADDITIONAL_PROMOTION";
                    }*/
                    data.value = 100;
                    data.name = "MAPPING_ADDITIONAL_PROMOTION Data Download";
                }
                publishProgress(data);

                //Gagan end code


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
            } catch (Exception e) {
             /*   final AlertMessage message = new AlertMessage(
                        CompleteDownloadActivity.this,
                        AlertMessage.MESSAGE_EXCEPTION, "download", e);*/

               /* e.getMessage();
                e.printStackTrace();
                e.getCause();*/
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            finish();
        }

    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
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
}
