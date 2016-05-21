package main.gologo.sendoptions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
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
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class ContactOptions extends BaseActionbar {

    Button b1, b2, b3;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_options);
        bundle = getIntent().getExtras();

        if (Constants.grouplist == null) {
            Constants.grouplist = new ArrayList<Groupcontactdata>();
            volleyrequest1();
        }
    }


    public void Phonecontacts(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), Phonecontacts.class);
        i.putExtras(bundle);
        startActivity(i);
    }


    public void GVGroups(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), GVGroups.class);
        i.putExtras(bundle);
        startActivity(i);

    }


    public void MVCallers(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), MVCallers.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    void volleyrequest1() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.creategroup, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("groupresponse", response.toString());
                        try {
                            JSONObject js1 = (JSONObject) response.get("message");
                            JSONArray cast = js1.getJSONArray("objects");
                            int len = cast.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String name = actor.getString("name");
                                int id = actor.getInt("id");
                                Groupcontactdata cgd = new Groupcontactdata(name, id);
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
                Snackbar.make(findViewById(android.R.id.content), R.string.error_in_fetching_groups, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
                //Toast.makeText(getApplicationContext(), R.string.error_in_fetching_groups, Toast.LENGTH_LONG).show();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjReq);

    }
}