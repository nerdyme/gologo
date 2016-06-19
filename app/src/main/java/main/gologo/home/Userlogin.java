package main.gologo.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import main.gologo.R;
import main.gologo.constants.Constants;

public class Userlogin extends Activity {

    EditText e1, e2;
    TextView tv1;
    Button b1;
    String pinno = "",phoneno="";
    ProgressDialog progress;
   // String gcmRegId=Constants.gcmRegId;
    private SharedPreferences prefs;

    void makerequest()
    {
        String req = pinno+"  "+phoneno+ "  " + Constants.gcmRegId;
        Log.d("TAG", "Register Request: " + req);

        HashMap<String, String> loginparams = new HashMap<String, String>();
        loginparams.put("pin", pinno);
        loginparams.put("phone",phoneno);
        loginparams.put("gcmid",Constants.gcmRegId);

        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.login,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject response1=new JSONObject(response);
                            String s1 = response1.get("message").toString();
                            if(s1.equalsIgnoreCase("Successfully logged in"))
                            {
                                Constants.phone=phoneno;
                                SharedPreferences.Editor editor = getSharedPreferences().edit();
                                editor.putString("phoneno", Constants.phone);
                                editor.apply();
                                Log.d("pin register", response);
                                Snackbar.make(findViewById(android.R.id.content), R.string.Successfully_logged_in, Snackbar.LENGTH_LONG).show();
                                Intent i= new Intent(getApplicationContext(),MenuOptions.class);
                                finish();
                                startActivity(i);

                            }
                            else
                                Snackbar.make(findViewById(android.R.id.content), s1, Snackbar.LENGTH_LONG).show();
                            }

                        catch (JSONException e) {
                            Log.d("Error",e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.error_network_timeout, Snackbar.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_details_phone, Snackbar.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_details_pin, Snackbar.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.network_error, Snackbar.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.parse_error, Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("error",error.toString());
                            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("pin",pinno);
                params.put("phone",phoneno);
                params.put("gcmid",Constants.gcmRegId);
                return params;
            }

        };

        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        e1 = (EditText) findViewById(R.id.phone_editText);
        e2 = (EditText) findViewById(R.id.pin_editText);
        tv1 = (TextView) findViewById(R.id.forgetpin);
        b1 = (Button) findViewById(R.id.signin_button);
        progress = new ProgressDialog(this);
        //progress.setMessage(R.string.Pleasewait);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                phoneno = e1.getText().toString();
                pinno = e2.getText().toString();


                if (phoneno.equals("") || phoneno.equals(null)) {
                    Toast.makeText(getBaseContext(), R.string.Phone_Number_cant_be_empty, Toast.LENGTH_LONG).show();
                    e1.requestFocus(0);
                } else if (phoneno.contains("[0-9]+") == false && phoneno.length() != 10) {
                    Toast.makeText(getBaseContext(), R.string.Enter_valid_phone_number, Toast.LENGTH_LONG).show();

                } else if (pinno.equals("") || pinno.equals(null)) {
                    Toast.makeText(getBaseContext(), R.string.PIN_Number_cant_be_empty, Toast.LENGTH_LONG).show();
                    e1.requestFocus(0);
                } else if (pinno.contains("[0-9]+") == false && pinno.length() != 6) {
                    Toast.makeText(getBaseContext(), R.string.Enter_valid_PIN_Number, Toast.LENGTH_LONG).show();
                } else {

                    if(!AppStatus.getInstance(getApplicationContext()).isOnline())
                    {
                        Snackbar.make(findViewById(android.R.id.content), R.string.check_your_network, Snackbar.LENGTH_LONG).show();
                    }
                      else {// progress = ProgressDialog.show(this, R.string.Pleasewait,R.string.Validating_credentials, true);
                        progress = ProgressDialog.show(Userlogin.this, "Please Wait ... ", "Validating credentials", true);
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
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent phone = new Intent(getApplicationContext(),Phoneverify.class);
                startActivity(phone);
            }
        });
    }
    private SharedPreferences getSharedPreferences() {
        if (prefs == null) {
            prefs = getApplicationContext().getSharedPreferences("Gologo",
                    Context.MODE_PRIVATE);
        }
        return prefs;
    }


    }
//975424 - Pin, 9718658816
