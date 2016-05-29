package main.gologo.sendoptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.gologo.R;
import main.gologo.adapter.Groupcomparator;
import main.gologo.adapter.Groupcontactlistadapter;
import main.gologo.audio.MultipartUtility;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class GVGroups extends BaseActionbar implements AdapterView.OnItemClickListener {

    String contactlist="";
    Bundle bundle;
    String actname="",form_id="",filepath="",audiofile="";
    StringBuilder checkedcontacts;

    ProgressDialog progress = null;
    JSONObject jsonObject=null;
    String survey_name="";
    Button select;
    int msg_id=24;
    Groupcontactlistadapter ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvgroups);

        bundle=getIntent().getExtras();
        actname=bundle.getString("ActivityName");

        Log.d("goup list size","Size :: " +Constants.grouplist.size());
        Collections.sort(Constants.grouplist, new Groupcomparator());

        ListView lv= (ListView) findViewById(R.id.lv2);
        ma = new Groupcontactlistadapter(Constants.grouplist,GVGroups.this);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);

        // adding
        select = (Button) findViewById(R.id.button2);
        select.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                checkedcontacts= new StringBuilder();
                int set=0;
                int l=Constants.grouplist.size();
                for(int i = 0; i < l; i++)

                {
                    if(ma.mCheckStates.get(i)==true)
                    {
                        set=1;
                        checkedcontacts.append(Constants.grouplist.get(i).getgroupid());
                        checkedcontacts.append(",");
                    }
                    else
                    {

                    }
                }
                if(set==1)
                {
                    contactlist= checkedcontacts.toString();
                    contactlist = contactlist.substring(0, contactlist.length()-1);

                    // Do Volley request
                    if(actname.equalsIgnoreCase("TemplateAnnouncementCamp")==true)
                    {
                        msg_id=41; ///Set as gramvaani gives
                        jsonObject = new JSONObject();

                        try {

                            jsonObject.put("camp_name", bundle.getString("campname"));
                            jsonObject.put("start_date",bundle.getString("start"));
                            jsonObject.put("end_date",bundle.getString("end"));
                            jsonObject.put("location",Constants.capitalizeString(bundle.getString("venuename")));
                        }
                        catch(JSONException e)
                        {

                        }
                        progress = ProgressDialog.show(GVGroups.this, "Please Wait ... ", "Sending message", true);
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

                    }
                    else if(actname.equalsIgnoreCase("TemplateAnnouncementGovtscheme")==true)
                    {
                        msg_id=42; ///Set as gramvaani gives
                        jsonObject = new JSONObject();
                        /*i.putExtra("ActivityName", "TemplateAnnouncementGovtscheme");
                        i.putExtra("scheme",scheme);
                        i.putExtra("beneficiary",ben);
                        i.putExtra("date", date);*/
                        try {
                            jsonObject.put("scheme_name",Constants.capitalizeString(bundle.getString("scheme")));
                            jsonObject.put("beneficiaries",bundle.getString("beneficiary"));
                            jsonObject.put("scheme_date", bundle.getString("date"));
                        }
                        catch(JSONException e) {

                        }
                        progress = ProgressDialog.show(GVGroups.this, "Please Wait ... ", "Sending message", true);
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
                    }
                    else if(actname.equalsIgnoreCase("TemplateAnnouncementSurvey")==true)
                    {
                        msg_id=40; ///Set as gramvaani gives
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("survey_date", bundle.getString("date"));
                        }
                        catch(JSONException e) {

                        }

                        progress = ProgressDialog.show(GVGroups.this, "Please Wait ... ", "Sending message", true);
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
                    }
                    else if (actname.equalsIgnoreCase("LaunchSurvey")==true)
                    {
                        form_id=bundle.getString("form_id");
                        survey_name=bundle.getString("survey_name");
                        progress = ProgressDialog.show(GVGroups.this, "Please Wait ... ", "Launching Survey", true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time
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
                    }
                    else if (actname.equalsIgnoreCase("Recordaudio")==true)
                    {

                        filepath=bundle.getString("FileName"); //complete file path is fetched./*/storage/emulated/0/AudioRecorder/1462830570992.mp4*/
                        Log.d("filepath","Inside send audio function" + filepath);
                        progress = ProgressDialog.show(GVGroups.this, "Please Wait ... ", "Your audio is being uploaded", true);
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
                    }
                    else
                    {

                    }
                }
                else
                    Snackbar.make(findViewById(android.R.id.content), R.string.Select_atleast_one_contact, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();

            }
        });


    }

    public  String photoUpload(String url, String imageFilePath) {
        if (url == null) {
            return null;
        }

        String response="";
        //return NetworkUtils.syncPOSTFile(url, "picture", imageFilePath, null);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            File imageFile = new File(imageFilePath);
            audiofile=imageFile.getName();

            FileBody fileBody = new FileBody(imageFile);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("uploadedfile", fileBody);
            builder.addPart("gcmid", new StringBody(Constants.gcmRegId));
            builder.addPart("group_ids",new StringBody(contactlist));
            builder.addPart("caller_ids", new StringBody(""));
            builder.addPart("filename", new StringBody(audiofile));
            builder.addPart("mv_caller", new StringBody("false"));
            HttpEntity entity = builder.build();
            response = multiPost(url, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }

    private  String multiPost(String urlString, HttpEntity reqEntity) {
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
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
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
                return  response.put("gsc","600").put("message","File too large to upload, Try smaller file.").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void sendaudio() {
        String charset = "UTF-8";
        File uploadFile1 = new File(filepath);
        String requestURL = Constants.recording;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            multipart.addFormField("gcmid", Constants.gcmRegId);
            multipart.addFormField("group_ids",contactlist);
            multipart.addFormField("mv_caller", "false");
            multipart.addFormField("caller_ids", "");
            multipart.addFormField("filename", audiofile);
            multipart.addFilePart("uploadedfile", uploadFile1);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                progress.dismiss();
                System.out.println(line);
            }
        } catch (IOException ex) {
            Log.d("Error", "Inside Audio upload" + ex.toString());
            System.err.println(ex);
            progress.dismiss();
            if (ex.toString().contains("403"))
                Snackbar.make(findViewById(android.R.id.content), R.string.mvcallers_not_present, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            else
                Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            finish();
        } catch(Exception e)
        {
            Log.d("Error",e.toString());
            progress.dismiss();
            finish();
        }

    }

    void sendmessage(final JSONObject json)
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launchmessage,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.d("TAG", response.toString());

                        try {

                            JSONObject response1=new JSONObject(response);
                            Log.d("Launch Message Error", response.toString());
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(GVGroups.this);
                            dlgAlert.setMessage(R.string.message_success);
                            //dlgAlert.setTitle("App Title");
                            dlgAlert.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //dismiss the dialog
                                            finish();
                                        }
                                    });
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
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
                        Log.d("TAG",error.toString());
                        Toast.makeText(getApplicationContext(),R.string.check_your_server,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                try {
                    params.put("gcmid", Constants.gcmRegId);
                    params.put("message_id", Integer.toString(msg_id));
                    params.put("caller_ids", "");
                    params.put("group_ids", contactlist);
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
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launch_survey,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {

                            JSONObject response1 = new JSONObject(response);
                            String s1 = response1.get("message").toString();
                            Log.d("Surveyresponse",s1);
                            Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                           /* if (s1.equalsIgnoreCase("Recording Schedule created sucessfully!")) {
                                Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.recording_error, Toast.LENGTH_LONG).show();
                            }*/
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
                        Log.d("TAG",error.toString());
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmid", Constants.gcmRegId);
                params.put("caller_ids","");
                params.put("group_ids",contactlist);
                params.put("form_id",form_id);
                params.put("survey_name",survey_name);
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
}




