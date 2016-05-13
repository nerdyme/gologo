package main.gologo.survey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class Viewsurveys extends BaseActionbar {


    private ArrayList<Viewsurveydata> list;
    String view_survey_url="";
    Viewsurveyadapter rvAdapter;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsurveys);

        Bundle bundle = getIntent().getExtras();
        String form_id = bundle.getString("form_id");

        view_survey_url = Constants.get_survey_questions + "/?form_id="+form_id;

        list = new ArrayList<Viewsurveydata>();


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv1);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());


        rvAdapter = new Viewsurveyadapter(list, Viewsurveys.this);
        rv.setAdapter(rvAdapter);

        progress = ProgressDialog.show(Viewsurveys.this, "Please Wait ... ", "Fetching Questions", true);
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
        JsonObjectRequest request1 = new JsonObjectRequest(view_survey_url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                                    progress.dismiss();
                        try {

                            JSONObject js= (JSONObject) response.get("message");
                            JSONArray ar1 = js.getJSONArray("objects");

                            int len=ar1.length();

                            for(int i=0;i<len;++i)
                            {
                                JSONObject info = (JSONObject) ar1.get(i);
                                JSONObject js1= (JSONObject) info.get("question");
                                String s1= js1.get("text").toString();

                                Viewsurveydata ob=new Viewsurveydata(s1,i+1);
                                list.add(ob);
                                rvAdapter.notifyDataSetChanged();
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
                            progress.dismiss();
                        Log.d("error",error.toString());
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                    }
                }
        );

        VolleyApplication.getInstance().getRequestQueue().add(request1);

    }
}
