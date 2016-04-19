package main.gologo.sendoptions;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.gologo.R;
import main.gologo.adapter.Phonecomparator;
import main.gologo.adapter.Phonecontactdata;
import main.gologo.adapter.Phonecontactlistadapter;
import main.gologo.constants.Constants;
import main.gologo.home.VolleyApplication;



public class Phonecontacts extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Phonecontactdata> phonelist=null;

    String contactlist="";
    Bundle bundle;
    String actname="",form_id="",audiofile="";
    StringBuilder checkedcontacts;

    ProgressDialog progress = null;
    Phonecontactlistadapter ma;
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecontacts);

        bundle=getIntent().getExtras();
        actname=bundle.getString("ActivityName");
        phonelist=new ArrayList<Phonecontactdata>();

        getAllContacts(this.getContentResolver());
        Collections.sort(phonelist, new Phonecomparator());

        ListView lv= (ListView) findViewById(R.id.lv1);
        ma = new Phonecontactlistadapter(phonelist,Phonecontacts.this);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);
        // adding
        select = (Button) findViewById(R.id.button1);
        select.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                checkedcontacts= new StringBuilder();
                int set=0;

                for(int i = 0; i < phonelist.size(); i++)

                {
                    if(ma.mCheckStates.get(i)==true)
                    {
                        set=1;
                        checkedcontacts.append(phonelist.get(i).getPhone());
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


                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("campname", bundle.getString("campname"));
                            jsonObject.put("start",bundle.getString("start"));
                            jsonObject.put("end",bundle.getString("end"));
                            jsonObject.put("venuename",bundle.getString("venuename"));

                            //json = jsonObject.toString();
                            //sendmessage(json);

                            //jsonObject1.put("message_args", json);
                            //jsonObject1.put("authenticity_token",MainActivity.token);

                            //json1 = jsonObject1.toString();

                        }
                        catch(JSONException e)
                        {

                        }
                        sendmessage(jsonObject);
                        Toast.makeText(getApplicationContext(), "Message is successfully delivered", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(actname.equalsIgnoreCase("TemplateAnnouncementGovtscheme")==true)
                    {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("date", bundle.getString("date"));
                        }
                        catch(JSONException e)
                        {

                        }
                        sendmessage(jsonObject);
                        Toast.makeText(getApplicationContext(),"Message is successfully delivered",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(actname.equalsIgnoreCase("TemplateAnnouncementSurvey")==true)
                    {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("date", bundle.getString("date"));
                        }
                        catch(JSONException e)
                        {

                        }
                        sendmessage(jsonObject);
                       // Toast.makeText(getApplicationContext(),"Message is successfully delivered",Toast.LENGTH_LONG).show();
                        finish();
                    }

                    else if (actname.equalsIgnoreCase("LaunchSurvey")==true)
                    {
                        form_id=bundle.getString("form_id");
                        sendsurvey();
                        Toast.makeText(getApplicationContext(),"Survey is successfully launched",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if (actname.equalsIgnoreCase("Recordaudio")==true)
                    {
                        audiofile=bundle.getString("FileName");
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
                    }
                    else
                    {

                    }
                }
                else
                    Toast.makeText(Phonecontacts.this, R.string.Select_atleast_one_contact,Toast.LENGTH_LONG).show();

            }
        });


    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ma.toggle(arg2);
    }

    public  void getAllContacts(ContentResolver cr) {

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Phonecontactdata ob=new Phonecontactdata(name,phoneNumber);
            phonelist.add(ob);
        }

        phones.close();
    }

    void sendaudio()
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.recording,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {

                            JSONObject response1 = new JSONObject(response);
                            String s1 = response1.get("message").toString();
                            Log.d("Audioresponse",s1);
                            if (s1.equalsIgnoreCase("Recording Schedule created sucessfully!")) {
                                Toast.makeText(getBaseContext(), R.string.recording_submitted, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.recording_error, Toast.LENGTH_LONG).show();
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmid", Constants.gcmRegId);
                params.put("caller_ids",contactlist);
                params.put("filename",audiofile);

                return params;
            }

        };

        VolleyApplication.getInstance().getRequestQueue().add(request1);


    }

    void sendmessage(JSONObject json)
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launchmessage,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        // pb.setVisibility(View.GONE);
                        try {

                            JSONObject response1=new JSONObject(response);
                            Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getBaseContext(),"Error oh no :: " + error, Toast.LENGTH_LONG).show();
                        if(error.toString().equalsIgnoreCase("com.android.volley.AuthFailureError"))
                            Toast.makeText(getApplicationContext(),R.string.no_user_registered,Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),R.string.check_your_network,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                String json="";

                try {
                    JSONObject js1=new JSONObject();
                    js1.put("gcmid", Constants.gcmRegId);
                    js1.put("message_id",24);
                    js1.put("caller_ids","9718658816,9891127941");
                    js1.put("group_ids","");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("club", "Hello all");
                    jsonObject.put("date", "15.07.2015");
                    jsonObject.put("callback_calls", 20);
                    jsonObject.put("club_contribs", 40);

                    js1.put("message_args",jsonObject.toString());
                    json=js1.toString();

                } catch(JSONException e)
                {

                }

                return params;
            }

        };

        VolleyApplication.getInstance().getRequestQueue().add(request1);

    }

    void sendsurvey()
    {

    }
}


