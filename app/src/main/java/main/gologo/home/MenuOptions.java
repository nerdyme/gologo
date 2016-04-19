package main.gologo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import main.gologo.R;
import main.gologo.adapter.Groupcontactdata;
import main.gologo.audio.Recordaudio;
import main.gologo.constants.Constants;
import main.gologo.contact.Createcontact;
import main.gologo.contact.Locationdata;
import main.gologo.group.Addgroup;
import main.gologo.message.Sendmessage;
import main.gologo.survey.Surveys;


public class MenuOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        if(Constants.locationlist==null) {
            Constants.locationlist = new ArrayList<Locationdata>();

            volleyrequest();
        }

        if(Constants.grouplist==null)
        {
            Constants.grouplist = new ArrayList<Groupcontactdata>();

            volleyrequest1();
        }
    }

    public void group(View v) {
       Intent i = new Intent(MenuOptions.this, Addgroup.class);
       startActivity(i);
    }

    public void survey(View v) {
       Intent i = new Intent(MenuOptions.this, Surveys.class);
        startActivity(i);
    }

    public void message(View v) {
       Intent i = new Intent(MenuOptions.this, Sendmessage.class);
        startActivity(i);
    }

    public void contact(View v) {
        Intent i = new Intent(MenuOptions.this, Createcontact.class);
        startActivity(i);
    }

    public void audio(View v) {
        Intent i = new Intent(MenuOptions.this, Recordaudio.class);
        startActivity(i);
    }

    void volleyrequest()
    {
        JsonObjectRequest request1 = new JsonObjectRequest(Constants.location1, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Constants.locationlist.clear();

                        Log.d("locationresponse",response.toString());
                        try {

                            JSONArray ar1= (JSONArray) response.get("objects");
                            int len=ar1.length();

                            for(int i=0;i<len;++i)
                            {
                                JSONObject info = (JSONObject) ar1.get(i);
                                String s1 = info.get("desc").toString();
                                String s2 = info.get("resource_uri").toString();

                                Log.d("Data", s2 + '\n');
                                Locationdata ob=new Locationdata(s1,s2);
                                Constants.locationlist.add(ob);
                            }

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    void volleyrequest1()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.creategroup, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("groupresponse",response.toString());
                        try {
                            JSONObject js1=(JSONObject)response.get("message");
                            JSONArray cast = js1.getJSONArray("objects");
                            int len=cast.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String name = actor.getString("name");
                                int id =actor.getInt("id");
                                Groupcontactdata cgd=new Groupcontactdata(name,id);
                                Constants.grouplist.add(cgd);
                                Log.d("Contactgroups", name);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Tag", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), R.string.error_in_fetching_groups, Toast.LENGTH_LONG).show();
            }
        });
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjReq);
    }
}
