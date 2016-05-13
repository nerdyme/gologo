package main.gologo.sendoptions;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.gologo.R;
import main.gologo.adapter.Phonecomparator;
import main.gologo.adapter.Phonecontactdata;
import main.gologo.adapter.Phonecontactlistadapter;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;



public class Phonecontacts extends BaseActionbar implements AdapterView.OnItemClickListener {

    ArrayList<Phonecontactdata> phonelist = null;

    String contactlist = "";
    Bundle bundle;
    String actname = "", form_id = "", filepath = "", audiofile = "";
    StringBuilder checkedcontacts;

    ProgressDialog progress = null;
    Phonecontactlistadapter ma;
    JSONObject jsonObject = null;
    String survey_name = "";
    Button select;
    int msg_id = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecontacts);

        bundle = getIntent().getExtras();
        actname = bundle.getString("ActivityName");
        phonelist = new ArrayList<Phonecontactdata>();

        getAllContacts(this.getContentResolver());
        Collections.sort(phonelist, new Phonecomparator());

        ListView lv = (ListView) findViewById(R.id.lv1);
        ma = new Phonecontactlistadapter(phonelist, Phonecontacts.this);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);

        select = (Button) findViewById(R.id.button1);
        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkedcontacts = new StringBuilder();
                int set = 0;

                for (int i = 0; i < phonelist.size(); i++)

                {
                    if (ma.mCheckStates.get(i) == true) {
                        set = 1;
                        checkedcontacts.append(phonelist.get(i).getPhone());
                        checkedcontacts.append(",");
                    } else {

                    }
                }
                if (set == 1) {
                    contactlist = checkedcontacts.toString();
                    contactlist = contactlist.substring(0, contactlist.length() - 1);

                    // Do Volley request
                    if (actname.equalsIgnoreCase("TemplateAnnouncementCamp") == true) {
                        msg_id = 41; ///Set as gramvaani gives
                        jsonObject = new JSONObject();

                        try {

                            jsonObject.put("camp_name", bundle.getString("campname"));
                            jsonObject.put("start_date", bundle.getString("start"));
                            jsonObject.put("end_date", bundle.getString("end"));
                            jsonObject.put("location", bundle.getString("venuename"));
                        } catch (JSONException e) {

                        }
                        progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Sending message", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
                                sendmessage(jsonObject);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();

                    } else if (actname.equalsIgnoreCase("TemplateAnnouncementGovtscheme") == true) {
                        msg_id = 42; ///Set as gramvaani gives
                        jsonObject = new JSONObject();
                        /*i.putExtra("ActivityName", "TemplateAnnouncementGovtscheme");
                        i.putExtra("scheme",scheme);
                        i.putExtra("beneficiary",ben);
                        i.putExtra("date", date);*/
                        try {
                            jsonObject.put("scheme_name", bundle.getString("scheme"));
                            jsonObject.put("beneficiaries", bundle.getString("beneficiary"));
                            jsonObject.put("scheme_date", bundle.getString("date"));
                        } catch (JSONException e) {

                        }
                        progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Sending message", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
                                sendmessage(jsonObject);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();
                        //Toast.makeText(getApplicationContext(),"Message is successfully delivered",Toast.LENGTH_LONG).show();
                        //finish();
                    } else if (actname.equalsIgnoreCase("TemplateAnnouncementSurvey") == true) {
                        msg_id = 40; ///Set as gramvaani gives
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("survey_date", bundle.getString("date"));
                        } catch (JSONException e) {

                        }

                        progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Sending message", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
                                sendmessage(jsonObject);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();
                        //Toast.makeText(getApplicationContext(),"Message is successfully delivered",Toast.LENGTH_LONG).show();
                        //finish();
                    } else if (actname.equalsIgnoreCase("LaunchSurvey") == true) {
                        form_id = bundle.getString("form_id");
                        survey_name = bundle.getString("survey_name");
                        Log.d("Data to survey", "Form id :: " + form_id + "  Survey name :::  " + survey_name + "  ");
                        //progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Launching Survey", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
                                Log.d("Calling", "***** Launching survey ****");
                                sendsurvey();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();
                        //Toast.makeText(getApplicationContext(),"Survey is successfully launched",Toast.LENGTH_LONG).show();
                        //finish();
                    } else if (actname.equalsIgnoreCase("Recordaudio") == true) {

                        filepath = bundle.getString("FileName"); //complete file path is fetched./*/storage/emulated/0/AudioRecorder/1462830570992.mp4*/
                        Log.d("filepath", "Inside send audio function" + filepath);
                        progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Your audio is being uploaded", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
                                sendaudio();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        }).start();

                        // Toast.makeText(getApplicationContext(),"Audio is successfully delivered",Toast.LENGTH_LONG).show();
                        // finish();
                    } else {

                    }
                } else
                    Toast.makeText(Phonecontacts.this, R.string.Select_atleast_one_contact, Toast.LENGTH_LONG).show();

            }
        });


    }

    public String photoUpload(String url, String imageFilePath) {
        if (url == null) {
            return null;
        }

        String response = "";
        //return NetworkUtils.syncPOSTFile(url, "picture", imageFilePath, null);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            File imageFile = new File(imageFilePath);
            audiofile = imageFile.getName();

            FileBody fileBody = new FileBody(imageFile);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("gcmid", new StringBody(Constants.gcmRegId));
            builder.addPart("mv_caller", new StringBody("false"));
            builder.addPart("group_ids", new StringBody(""));
            builder.addPart("caller_ids", new StringBody(contactlist));
            builder.addPart("filename", new StringBody(audiofile));
            builder.addPart("uploadedfile", fileBody);

            HttpEntity entity = builder.build();
            response = multiPost(url, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }

    private String multiPost(String urlString, HttpEntity reqEntity) {
        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // conn.setConnectTimeout(getTimeOut(UniversalHttpClient.RequestTimePreferences.Minimal, 1));
            //conn.setReadTimeout(getTimeOut(UniversalHttpClient.RequestTimePreferences.Minimal, 0));
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Connection", "Keep-Alive");
            //conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            InputStream is = conn.getInputStream();

            reqEntity.writeTo(os);
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.toString();
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_ENTITY_TOO_LARGE) {
                JSONObject response = new JSONObject();
                return response.put("gsc", "600").put("message", "File too large to upload, Try smaller file.").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void sendaudio() {
        photoUpload(Constants.recording,filepath);

    }

    void sendmessage(final JSONObject json)
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launchmessage,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                            progress.dismiss();
                            Log.d("TAG", response.toString());
                            finish();
                        try {

                            JSONObject response1=new JSONObject(response);
                            Toast.makeText(getBaseContext(), response1.toString(), Toast.LENGTH_LONG).show();
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.d("Launch Survey Error", error.toString());

                        if(networkResponse.statusCode == 500)
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        else Toast.makeText(getApplicationContext(),R.string.check_your_server,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                try {

                    params.put("gcmid", Constants.gcmRegId);
                    params.put("message_id", Integer.toString(msg_id));
                    params.put("caller_ids", contactlist);
                    params.put("group_ids", "");
                    params.put("mv_caller","false");

                    /*JSONObject jsonObject = new JSONObject();
                    jsonObject.put("club", "Hello all");
                    jsonObject.put("date", "15.07.2015");
                    jsonObject.put("callback_calls", 20);
                    jsonObject.put("club_contribs", 40);*/

                    params.put("message_args", json.toString());

                } catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),R.string.check_your_server,Toast.LENGTH_LONG).show();
                }

                return params;
            }

        };

        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    void sendsurvey()
    {
        Log.d("Calling","*****Inside Send Survey *****");
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launch_survey,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            Log.d("Calling","*****Inside Send Survey 1 *****");
                            JSONObject response1 = new JSONObject(response);
                            String s1 = response1.get("message").toString();
                            Log.d("Surveyresponse",s1);
                            if (s1.equalsIgnoreCase("Recording Schedule created sucessfully!")) {
                                Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.recording_error, Toast.LENGTH_LONG).show();
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("TAG", error.toString());
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse.statusCode == 500)
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        else Toast.makeText(getApplicationContext(),R.string.check_your_server,Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmid", Constants.gcmRegId);
                params.put("caller_ids",contactlist);
                params.put("group_ids","");
                params.put("form_id",form_id);
                params.put("schedule_name",survey_name);
                params.put("mv_caller","false");
                return params;
            }
        };

        VolleyApplication.getInstance().getRequestQueue().add(request1);

    }

    @Override
    public void onPause() {
        super.onPause();
        if ((progress != null) && progress.isShowing())
                progress.dismiss();
        progress = null;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ma.toggle(arg2);
    }

    public  void getAllContacts(ContentResolver cr) {

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Phonecontactdata ob = new Phonecontactdata(name, phoneNumber);
            phonelist.add(ob);
        }

        phones.close();
    }
}


