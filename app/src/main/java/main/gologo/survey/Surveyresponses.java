package main.gologo.survey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class Surveyresponses extends BaseActionbar{

    String response_url= Constants.responses1;
    ProgressDialog progress=null;
    private ArrayList<Viewsurveydata> list;
    Set<String> unique_response = new HashSet<String>();
    HashMap<Integer,Integer> hm=new HashMap();
    int total_res=0,total_questions=0;
    Surveyresponseadapter rvAdapter;
    TextView total_responses;

    //String tempurl = "http://internal.gramvaani.org:8081/vapp/api/v1/survey_record/cdr_records/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&ai_id=60&survey_id=195&limit=20&page=1";
    String surveyurl=Constants.get_survey_responses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyresponses);
        total_responses=(TextView) findViewById(R.id.totalresponses);

        Bundle bundle = getIntent().getExtras();
        final String form_id = bundle.getString("form_id");
        final String survey_id = bundle.getString("survey_id");
        list=bundle.getParcelableArrayList("question_list");

        surveyurl = surveyurl+survey_id;
       /* int id=list.get(0).getquestionid();
        hm.put(id, 0);
        Log.d("id is ", "ID" + id);
        id = list.get(1).getquestionid();
        hm.put(id,0);
        Log.d("id is ", "ID" + id);*/
        total_questions=list.size();
        Log.d("list Size", "Total questions are : " + total_questions);


        for(int i=0;i<total_questions;++i)
        {
            Log.d("\nquestion id in map", "id is ::");
            int id1=list.get(i).getquestionid();
            hm.put(id1,0);
            Log.d("\nquestion id in map", "id is ::" +id1);
        }
        //hm.put(243,0);
        //hm.put(216,0);

            // list.get(i).setresponses(0);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv1);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rvAdapter = new Surveyresponseadapter(list, Surveyresponses.this);
        rv.setAdapter(rvAdapter);

        response_url+=survey_id;
        progress = ProgressDialog.show(Surveyresponses.this, "Please Wait ... ", "Fetching Responses", true);
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
        JsonObjectRequest request1 = new JsonObjectRequest(surveyurl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int count=0;
                        progress.dismiss();
                        try {
                             JSONObject js1 = (JSONObject) response.get("message");
                             count = (int) js1.get("count");
                            if (count == 0)
                            {
                                Snackbar.make(findViewById(android.R.id.content), R.string.no_response, Snackbar.LENGTH_LONG).show();
                            } else
                            {
                                JSONArray ar1 = (JSONArray)js1.getJSONArray("data");

                                int len = ar1.length();
                                    Log.d("length of data","Data length :: " + len);
                                for (int i = 0; i < len; ++i) {
                                    JSONObject info1 = (JSONObject) ar1.get(i);
                                    JSONArray ar2 = (JSONArray) info1.getJSONArray("detail");

                                    JSONObject info2 = (JSONObject) ar2.get(0);
                                    String ph=info2.get("callerid").toString();
                                    int ques_id=info2.getInt("question_id");
                                    unique_response.add(ph);
                                        Log.d("question id", "id is ::" +ques_id);

                                    int cur_Res=hm.get(ques_id);
                                    hm.put(ques_id,cur_Res+1);
                                    rvAdapter.notifyDataSetChanged();
                                }
                                total_res=unique_response.size();
                                total_responses.setText("Total number of people responded :" + total_res);
                                for(int i=0;i<total_questions;++i)
                                {
                                    int res=hm.get(list.get(i).getquestionid());
                                    list.get(i).setresponses(res);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for(int key : hm.keySet())
                        {
                            Log.d("\nResponses", "key " + key + "value ::" + hm.get(key));
                        }
                        Log.d("People responded", "People" + total_res);
                               // if(count!=0)
                       /* for(int i=0;i<total_questions;++i)
                        {
                            int res=hm.get(list.get(i).getquestionid());
                            list.get(i).setresponses(res);

                        }*/
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("error", error.toString());
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                    }
                }
        );
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }
}
