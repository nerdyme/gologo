package main.gologo.sendoptions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.gologo.R;
import main.gologo.audio.MultipartUtility;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class MVCallers extends BaseActionbar {


    //********************
    EditText et1,et2;
    Button b1;
    String start,end;
    ImageButton img1,img2;
    private Calendar cal;
    private int day,day1;
    private int month,month1;
    private int year,year1;

    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;
    //************************

    String contactlist = "";
    Bundle bundle;
    String actname = "", form_id = "", filepath = "", audiofile = "";
    StringBuilder checkedcontacts;

    ProgressDialog progress = null;
    JSONObject jsonObject = null;
    String survey_name = "";
    Button select;
    int msg_id = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvcallers);


        img1= (ImageButton) findViewById(R.id.calendaricon);
        img2=  (ImageButton) findViewById(R.id.calendaricon1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        // img1.setOnClickListener(this);
        // img2.setOnClickListener(this);

        addListenerOnButton();
        b1= (Button) findViewById(R.id.pick20);
        et1 = (EditText) findViewById(R.id.selectstartdateformv);
        et2=(EditText) findViewById(R.id.selectenddateformv);


        // setCurrentDateOnView();
        //addListenerOnButton();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start = et1.getText().toString();
                end = et2.getText().toString();


                if (start.equals("") || start.equals(null))
                    Toast.makeText(getBaseContext(), R.string.startdatetoast, Toast.LENGTH_LONG).show();
                else if (end.equals("") || end.equals(null))
                    Toast.makeText(getBaseContext(), R.string.enddatetoast, Toast.LENGTH_LONG).show();
                else {
                    bundle = getIntent().getExtras();
                    actname = bundle.getString("ActivityName");

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
                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
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
                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
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

                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
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
                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Launching Survey", true);
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
                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Your audio is being uploaded", true);
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
                }
            }
        });

        }


    public void addListenerOnButton() {
        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });
        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID2);

            }   });

    }

    public String photoUpload(String url, String imageFilePath) {
        if (url == null) {
            return null;
        }

        String response = "";


        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            File imageFile = new File(imageFilePath);
            audiofile = imageFile.getName();

            FileBody fileBody = new FileBody(imageFile);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("gcmid", new StringBody(Constants.gcmRegId));
            builder.addPart("mv_caller", new StringBody("true"));
            builder.addPart("group_ids", new StringBody(""));
            builder.addPart("caller_ids", new StringBody(""));
            builder.addPart("filename", new StringBody(audiofile));
            builder.addPart("start_date", new StringBody(start));
            builder.addPart("end_date", new StringBody(end));
            builder.addPart("ai", new StringBody("10"));
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
       // photoUpload(Constants.recording,filepath);

                String charset = "UTF-8";
                File uploadFile1 = new File(filepath);
                //File uploadFile2 = new File("e:/Test/PIC2.JPG");
                String requestURL = Constants.recording;

                try {
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");

                    multipart.addFormField("description", "Cool Pictures");
                    multipart.addFormField("keywords", "Java,upload,Spring");

                    multipart.addFilePart("fileUpload", uploadFile1);
                    //multipart.addFilePart("fileUpload", uploadFile2);

                    List<String> response = multipart.finish();

                    System.out.println("SERVER REPLIED:");

                    for (String line : response) {
                        System.out.println(line);
                    }
                } catch (IOException ex) {
                    System.err.println(ex);
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
                        Log.d("Launch Survey Error",error.toString());
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
                    params.put("group_ids", "");
                    params.put("mv_caller","true");
                    params.put("start_date",start);
                    params.put("end_date",end);
                    params.put("ai","10");

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
                        Log.d("TAG",error.toString());
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
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
                params.put("start_date",start);
                params.put("end_date",end);
                params.put("ai","10");
                params.put("mv_caller","true");
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

            case DATE_DIALOG_ID2:
                cur = DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
            default: break;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            if(cur == DATE_DIALOG_ID){
                // set selected date into edittext
                et1.setText(new StringBuilder().append(year)
                        .append("-").append(month+1).append("-").append(day)
                        .append(" "));
            }
            else if(cur==DATE_DIALOG_ID2){
                et2.setText(new StringBuilder().append(year)
                        .append("-").append(month+1).append("-").append(day)
                        .append(" "));
            }

        }
    };
}

