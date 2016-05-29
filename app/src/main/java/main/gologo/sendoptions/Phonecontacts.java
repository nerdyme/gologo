package main.gologo.sendoptions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.gologo.R;
import main.gologo.adapter.Phonecomparator;
import main.gologo.adapter.Phonecontactdata;
import main.gologo.adapter.Phonecontactlistadapter;
import main.gologo.audio.MultipartUtility;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;



public class Phonecontacts extends BaseActionbar implements AdapterView.OnItemClickListener {

    ArrayList<Phonecontactdata> phonelist = null;

    String contactlist = "";
    Bundle bundle;
    String actname = "", form_id = "", filepath = "", audiofile = "";
    StringBuilder checkedcontacts;
    EditText searchbar;
    int contentlen;

    ProgressDialog progress = null;
    Phonecontactlistadapter ma;
    JSONObject jsonObject = null;
    String survey_name = "";
    Button select;
    int msg_id = 24;

    private ProgressDialog pDialog;


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
        lv.setDivider(null);
        lv.setDividerHeight(0);
        searchbar = (EditText) findViewById(R.id.search_editText);
        ma = new Phonecontactlistadapter(phonelist, Phonecontacts.this);

        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);

        select = (Button) findViewById(R.id.button1);

        searchbar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.d("***","Text changed called");
                Phonecontacts.this.ma.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

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
                        progress = ProgressDialog.show(Phonecontacts.this, "Please Wait ... ", "Launching Survey", true);
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

    void sendaudio() {
        //photoUpload(Constants.recording,filepath);
        String charset = "UTF-8";
        File uploadFile1 = new File(filepath);
        String requestURL = Constants.recording;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            /*new MultipartUtility.ProgressListener() {

                @Override
                public void transferred(long num) {
                    publishProgress((int) ((num / (float) totalSize) * 100));
                }
            }));*/

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            multipart.addFormField("gcmid",Constants.gcmRegId);
            multipart.addFormField("group_ids","");
            multipart.addFormField("mv_caller", "false");
            multipart.addFormField("caller_ids", contactlist);
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
            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
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
                            Log.d("Launch Message Error", response1.toString());
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Phonecontacts.this);
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
                            Snackbar.make(findViewById(android.R.id.content), response1.toString(), Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.d("Launch Message Error", error.toString());
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Phonecontacts.this);
                        dlgAlert.setMessage(R.string.error_in_sending_message);
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

                       /* if(networkResponse.statusCode == 500)
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        else */Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
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
        Log.d("Calling", "*****Inside Send Survey *****");
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launch_survey,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            Log.d("Calling", "*****Inside Send Survey 1 *****");
                            JSONObject response1 = new JSONObject(response);
                            String s1 = response1.get("message").toString();
                            Log.d("Surveyresponse", s1);
                            Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                            /*if (s1.equalsIgnoreCase("Recording Schedule created sucessfully!")) {
                                Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.recording_error, Toast.LENGTH_LONG).show();
                            }*/
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // progress.dismiss();
                        Log.d("TAG", error.toString());
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse.statusCode == 500)
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        else Toast.makeText(getApplicationContext(),R.string.check_your_server,Toast.LENGTH_LONG).show();
                        finish();

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


