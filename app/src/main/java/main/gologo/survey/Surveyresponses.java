package main.gologo.survey;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.gologo.R;
import main.gologo.home.VolleyApplication;

public class Surveyresponses extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyresponses);
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
                        Log.d("error", error.toString());
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                    }
                }
        );

        VolleyApplication.getInstance().getRequestQueue().add(request1);

    }
}
