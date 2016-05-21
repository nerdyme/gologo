package main.gologo.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.contact.Locationdata;

public class Splashscreen extends Activity {

    ImageView img;
    TextView tv;
    private final String TAG = "GCM Demo Activity";
    private final int ACTION_PLAY_SERVICES_DIALOG = 100;

    private GoogleCloudMessaging gcm;
    private SharedPreferences prefs;
    String gcmRegId = Constants.gcmRegId;


    private int currentAppVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        img=(ImageView) findViewById(R.id.login_imageView);
        tv=(TextView) findViewById(R.id.textView1);

        Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.animation);
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        img.startAnimation(scaleAnim);
        tv.startAnimation(animFadeIn);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{


                    SharedPreferences sharedpreferences = getSharedPreferences("Gologo", Context.MODE_PRIVATE);
                    Constants.gcmRegId = sharedpreferences.getString("gcmid", "");
                    Constants.phone=sharedpreferences.getString("phoneno","");

                    Log.d("value",Constants.gcmRegId+"   :: "+ Constants.phone+'\n');
                    // register if saved registration id not found
                    if (Constants.phone.isEmpty() || Constants.phone.equalsIgnoreCase(""))
                    {
                        if (checkPlayServices()) {
                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                            // Get current Application version
                            currentAppVersion = getAppVersion(getApplicationContext());

                            registerInBackground();

                        } else {

                            Toast.makeText(getApplicationContext(), R.string.Google_Play_Services_not_available, Toast.LENGTH_LONG).show();

                        }

                        Intent i =new Intent(Splashscreen.this,Userlogin.class);
                        startActivity(i);
                    } else {
                        Log.d("Details are :: ", "GCM ID" + Constants.gcmRegId );
                        Intent menu = new Intent(getApplicationContext(),MenuOptions.class);
                        startActivity(menu);

                    }

                }
            }
        };
        timerThread.start();

        if(Constants.locationlist==null) {
            Constants.locationlist = new ArrayList<Locationdata>();

            //volleyrequest();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        ACTION_PLAY_SERVICES_DIALOG).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new RegistrationTask().execute(null, null, null);
    }

    // Async task to register in background
    private class RegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging
                            .getInstance(getApplicationContext());
                }
                Constants.gcmRegId = gcm.register(Constants.senderid);

				/*
				 * At this point, you have gcm registration id. Save this
				 * registration id in your users table against this user. This
				 * id will be used to send push notifications from server.
				 */

                msg = "Successfully Registered for GCM";
                Log.d("Success in GCM", "Success while registering for GCM." + Constants.gcmRegId);

            } catch (IOException ex) {
                ex.printStackTrace();
                msg = "Error while registering for GCM.";
                Log.d("Error in GCm", "Error while registering for GCM.");
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {

            if (!Constants.gcmRegId.isEmpty()) {
                // save gcm reg id and registered app version in shared
                // preferences
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString("gcmid", Constants.gcmRegId);
                editor.putInt("appversion", currentAppVersion);
                editor.commit();
                // Put values in Constant variables
                Constants.gcmRegId = getSharedPreferences().getString("gcmid", "");
                Constants.appversion = getSharedPreferences().getInt("appversion", Integer.MIN_VALUE);

                String mesg = "Obtained new GCM Registartion Id " + Constants.gcmRegId;
                Toast.makeText(getApplicationContext(), mesg, Toast.LENGTH_LONG).show();
                Log.d("GCM ID is ", Constants.gcmRegId);
            }

        }
    }

        private SharedPreferences getSharedPreferences() {
            if (prefs == null) {
                prefs = getApplicationContext().getSharedPreferences("Gologo",
                        Context.MODE_PRIVATE);
            }
            return prefs;
        }

    void volleyrequest()
    {
        JsonObjectRequest request1 = new JsonObjectRequest(Constants.location, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Constants.locationlist.clear();

                        Log.d("locationresponse",response.toString());
                        try {
                            JSONObject js1 = (JSONObject) response.get("message");
                            JSONArray ar1 = js1.getJSONArray("objects");
                            Locationdata ob1=new Locationdata(" Choose Location :: ","");
                            Constants.locationlist.add(ob1);
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
    }
