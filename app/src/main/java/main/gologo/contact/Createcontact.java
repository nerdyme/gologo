package main.gologo.contact;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.VolleyApplication;

public class Createcontact extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter<String> adapter;
    List <String> groupNames;
    List <String> groupIDs;
    Button submit = null;
    Button cancel = null;
    EditText name = null;
    EditText age = null;
    EditText phone = null;
    Spinner gender = null;
    Spinner dis = null;
    Spinner state = null;
    Spinner city = null;
    Spinner contactgroup = null;
    ImageButton btnSpeak;

    String Grouplist;

    //MyAdapter ma;

    ProgressBar pb ;

    int grpsize=0;
    String nv = null, pv = null, av = null, gv = null, dv = null, sv = null, cv = null, cgv = "";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    // String tok = MainActivity.token;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
       // ma.toggle(arg2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //ma = new MyAdapter();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcontact);
        name = (EditText) findViewById(R.id.name_value);
        age = (EditText) findViewById(R.id.age_value);
        phone = (EditText) findViewById(R.id.phone_value);
        gender = (Spinner) findViewById(R.id.gender_value);
        dis = (Spinner) findViewById(R.id.district_value);
        //city = (Spinner) findViewById(R.id.city_value);
        state = (Spinner) findViewById(R.id.state_value);
        //contactgroup = (Spinner) findViewById(R.id.contactgroup_value);

       // pb.setVisibility(View.GONE);


        btnSpeak = (ImageButton) findViewById(R.id.mic);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //promptSpeechInput();
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                finish();
            }
        });

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                //Put validations here



                nv = name.getText().toString();
                av = age.getText().toString();
                pv = phone.getText().toString();

                gv = gender.getSelectedItem().toString();
                dv = dis.getSelectedItem().toString();
               // cv = city.getSelectedItem().toString();
                sv = state.getSelectedItem().toString();
                cgv = contactgroup.getSelectedItem().toString();
		
		/*gender.setOnItemSelectedListener(new OnItemSelectedListener(AdapterView<?> parent, View view, int pos, long id) 
		{
			gv = parent.getItemAtPosition(pos).toString();
		});*/

                int set = 0;
                Log.w("send", "Call to async task");

                if (nv.equals("") || nv.equals(null)) {
                    Toast.makeText(getBaseContext(), "Name can't be empty", Toast.LENGTH_LONG).show();
                } else if (nv.length() < 5) {
                    Toast.makeText(getBaseContext(), "Name must have atleast 5 characters", Toast.LENGTH_LONG).show();
                } else if (pv.equals("") || pv.equals(null)) {
                    Toast.makeText(getBaseContext(), "Phone Number can't be empty", Toast.LENGTH_LONG).show();

                } else if (pv.contains("[0-9]+") == false && pv.length() != 10) {
                    Toast.makeText(getBaseContext(), "Enter valid phone number", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(av) < 0 || Integer.parseInt(av) > 150) {
                    Toast.makeText(getBaseContext(), "Enter valid age(0-150)", Toast.LENGTH_LONG).show();
                } else    //if(pv.contains("[0-9]+") == true && pv.length() ==10)
                {


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Createcontact.this);
                    builder1.setMessage("Press Confirm to send, Press edit to change");
                    builder1.setCancelable(false);
                    builder1.setTitle("Add Contact Details");
                    builder1.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    pb.setVisibility(View.VISIBLE);
                                    StringRequest sr = new StringRequest(Request.Method.POST, Constants.createcontact, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            Log.d("TAG", "Register Response: " + response.toString());

                                            pb.setVisibility(View.GONE);
                                            try {
                                                JSONObject ob1 = new JSONObject(response);

                                                String msg = ob1.get("message").toString();

                                                if (msg.equalsIgnoreCase("Contact Created!"))
                                                    Toast.makeText(getApplicationContext(), "Contact created successfully", Toast.LENGTH_LONG).show();
                                                else
                                                    Toast.makeText(getApplicationContext(), "Connection Error, Try Again", Toast.LENGTH_LONG).show();

                                               // ma.notifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("name", nv);
                                            params.put("number", pv);
                                            params.put("gender", gv);
                                            params.put("gcmid", Constants.gcmRegId);
                                            params.put("clist_ids", nv);
                                            return params;
                                        }
                                    };

                                    VolleyApplication.getInstance().getRequestQueue().add(sr);
                                }
                            });
                    builder1.setNegativeButton("Edit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    name.setText(result.get(0));
                }
                break;
            }

        }
    }


}