package main.gologo.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import main.gologo.R;
import main.gologo.constants.Constants;

public class Phoneverify extends AppCompatActivity {

    Button b1 = null;
    EditText e1 =null;
    String phoneno="";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverify);

        e1 = (EditText) findViewById(R.id.phonevalue1);
        b1 = (Button) findViewById(R.id.phonesubmit);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub


                        phoneno = e1.getText().toString();

                        if (phoneno.equals("") || phoneno.equals(null)) {
                            Toast.makeText(getBaseContext(), R.string.Phone_Number_cant_be_empty, Toast.LENGTH_LONG).show();
                            e1.requestFocus(0);
                        } else if (phoneno.contains("[0-9]+") == false && phoneno.length() != 10) {
                            Toast.makeText(getBaseContext(), R.string.Enter_valid_phone_number, Toast.LENGTH_LONG).show();

                        } else {
                            //Do a volley request
                            progress = ProgressDialog.show(Phoneverify.this, "Please Wait ... ", "You will receive message shortly", true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // do the thing that takes a long time
                                    makerequest();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                            }).start();

                        }
                    }
                });
            }
       void makerequest()
       {

           StringRequest request1 = new StringRequest(Request.Method.POST, Constants.pinforget,
                   new Response.Listener<String>() {


                       @Override
                       public void onResponse(String response) {
                                            progress.dismiss();
                           try {

                               JSONObject response1 = new JSONObject(response);
                               String s1 = response1.get("message").toString();
                               if (s1.equalsIgnoreCase("Pin number is sent on the given phone number.")) {
                                   Toast.makeText(getBaseContext(), R.string.You_will_receive_pin_shortly, Toast.LENGTH_LONG).show();
                                   finish();
                               } else {
                                   Toast.makeText(getBaseContext(), R.string.Enter_valid_phone_number, Toast.LENGTH_LONG).show();
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

                           if (error.toString().equalsIgnoreCase("com.android.volley.AuthFailureError"))
                               Toast.makeText(getApplicationContext(), R.string.no_user_registered, Toast.LENGTH_LONG).show();
                           else
                               Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                       }
                   }) {
               @Override
               protected Map<String, String> getParams() {
                   Map<String, String> params = new HashMap<String, String>();
                   params.put("phone", phoneno);
                   params.put("gcmid", Constants.gcmRegId);
                   return params;
               }

           };

           VolleyApplication.getInstance().getRequestQueue().add(request1);


       }

    }

