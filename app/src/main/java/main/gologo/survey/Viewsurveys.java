package main.gologo.survey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    Button b1;
    TextView surveyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsurveys);

        Bundle bundle = getIntent().getExtras();
        final String form_id = bundle.getString("form_id");
        final String survey_id=bundle.getString("survey_id");
        final String survey_name=bundle.getString("survey_name");

        view_survey_url = Constants.get_survey_questions + "/?form_id="+form_id;

        list = new ArrayList<Viewsurveydata>();

        Log.d("View Survey", "form id :: " + form_id + "  Survey_id :: " + survey_id);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv1);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());


        rvAdapter = new Viewsurveyadapter(list, Viewsurveys.this);
        rv.setAdapter(rvAdapter);

        b1=(Button) findViewById(R.id.responseb);
        surveyname=(TextView) findViewById(R.id.surveyname);
        surveyname.setText("Survey Name - " + survey_name);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(),Surveyresponses.class);
                Bundle b=new Bundle();
                b.putString("survey_id",survey_id);
                b.putString("form_id", form_id);
                b.putString("survey_name",survey_name);
                b.putParcelableArrayList("question_list", list);
                i.putExtras(b);
                startActivity(i);
            }
        });
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
                                int a1=(int)js1.get("id");

                                Viewsurveydata ob=new Viewsurveydata(s1,i+1,a1,0);
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
