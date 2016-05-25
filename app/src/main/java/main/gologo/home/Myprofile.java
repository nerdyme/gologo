package main.gologo.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import main.gologo.R;
import main.gologo.constants.Constants;

public class Myprofile extends Activity {

    TextView nv, av, gv, dv, sv, cv;
    String nv1,av1,gv1,dv1,sv1,cv1;
    Button b1;
    String getadminroute=Constants.getadmininfo+"?gcmid="+Constants.gcmRegId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        nv=(TextView)findViewById(R.id.name_value);
        av=(TextView)findViewById(R.id.age_value);
        gv=(TextView)findViewById(R.id.gender_value);
        dv=(TextView)findViewById(R.id.district_value);
        sv=(TextView)findViewById(R.id.state_value);
        cv=(TextView)findViewById(R.id.number_value);
        b1=(Button)findViewById(R.id.back);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MenuOptions.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        volleyrequest();
    }

    void volleyrequest() {

        StringRequest sr = new StringRequest(Request.Method.GET,getadminroute, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());
                try {
                    JSONObject ob1 = new JSONObject(response);

                    nv1 = ob1.get("name").toString();
                    gv1 = ob1.get("gender").toString();
                    av1 = ob1.get("age").toString();
                    dv1 = ob1.get("block").toString() + ", " +ob1.get("district").toString();
                    sv1 = ob1.get("state").toString();
                    cv1 = ob1.get("contact").toString();

                    nv.setText(nv1);
                    gv.setText(gv1);
                    dv.setText(dv1);
                    av.setText(av1);
                    cv.setText(cv1);
                    sv.setText(sv1);
                    Snackbar.make(findViewById(android.R.id.content), R.string.details_displayed+"\n"+response.toString(), Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());
                Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server + "\n" + error.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
        VolleyApplication.getInstance().getRequestQueue().add(sr);
    }
}