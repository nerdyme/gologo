package main.gologo.survey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class Surveys extends BaseActionbar {

    ArrayList<Surveydata> temp;
    ListView lv;
    Surveyadapter rvAdapter=null;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());


        temp=new ArrayList<Surveydata>();
        rvAdapter = new Surveyadapter(temp,Surveys.this);
        rv.setAdapter(rvAdapter);
        progress = ProgressDialog.show(Surveys.this, "Please Wait ... ", "Fetching Surveys", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the thing that takes a long time
                volleyrequest();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();

    }

    void volleyrequest()
    {
        JsonObjectRequest request1 = new JsonObjectRequest(Constants.get_survey, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                                temp.clear();
                                progress.dismiss();
                        Log.d("response",response.toString());
                        try {
                            JSONObject js11=(JSONObject)response.get("message");
                            JSONArray ar1= (JSONArray) js11.get("objects");
                            int len=ar1.length();

                            for(int i=0;i<len;++i)
                            {
                                JSONObject info = (JSONObject) ar1.get(i);

                                String s1 = info.get("id").toString();
                                String s2 = info.get("name").toString().toUpperCase();
                                JSONObject js1= (JSONObject) info.get("form");
                                String s3= js1.get("id").toString();
                                Log.d("Data",s2+'\n');
                                Surveydata ob=new Surveydata(s2,s1,s3);
                                temp.add(ob);
                            }
                            rvAdapter.notifyDataSetChanged();

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
                        Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                }
        );
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }


}
