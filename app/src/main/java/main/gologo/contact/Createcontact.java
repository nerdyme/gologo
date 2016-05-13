package main.gologo.contact;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import main.gologo.R;
import main.gologo.adapter.Groupcomparator;
import main.gologo.adapter.Groupcontactdata;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class Createcontact extends BaseActionbar implements View.OnClickListener {

    Button submit = null;
    Button cancel = null;
    EditText name = null;
    EditText age = null;
    EditText phone = null;
    Spinner gender = null;
    Spinner dis = null;
    EditText contactgroup = null;
    ImageButton btnSpeak;
    ProgressDialog progress;

    String clist_ids="";

    protected CharSequence[] groups ;
    protected ArrayList<CharSequence> selectedGroups = new ArrayList<CharSequence>();

    String nv = null, pv = null, av = null, gv = null, dv = null, sv = null, bv = null,disvalue=null, cgv = "",resource_uri="";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.group_value:
                showSelectGroupsDialog();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Constants.grouplist == null) {
            Constants.grouplist = new ArrayList<Groupcontactdata>();

            volleyrequest1();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcontact);
        name = (EditText) findViewById(R.id.name_value);
        age = (EditText) findViewById(R.id.age_value);
        phone = (EditText) findViewById(R.id.phone_value);
        gender = (Spinner) findViewById(R.id.gender_value);
        dis = (Spinner) findViewById(R.id.district_value);
        contactgroup=(EditText)findViewById(R.id.group_value);
        btnSpeak=(ImageButton)findViewById(R.id.mic);

        contactgroup.setOnClickListener(this);
        Collections.sort(Constants.grouplist, new Groupcomparator());

        int len = Constants.grouplist.size();
        groups=new CharSequence[len];

        for(int i=0;i<len;++i)
            groups[i]=Constants.grouplist.get(i).getgroupname();

        //city = (Spinner) findViewById(R.id.city_value);
       // state = (Spinner) findViewById(R.id.state_value);
        //contactgroup = (Spinner) findViewById(R.id.contactgroup_value);

        ArrayList<String> locations = new ArrayList<String>();

        int l=0;
        if(Constants.locationlist!=null)
        l=Constants.locationlist.size();

        Log.d("Size of location", String.valueOf(l) + '\n');

        for(int i=0;i<l;++i)
            locations.add(Constants.locationlist.get(i).getlocation());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item ,locations);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dis.setAdapter(adapter);

        dis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //Put validations here
                nv = name.getText().toString();
                av = age.getText().toString();
                pv = phone.getText().toString();
                gv = gender.getSelectedItem().toString();
                dv = dis.getSelectedItem().toString();

                int pos=dis.getSelectedItemPosition();
                resource_uri=Constants.locationlist.get(pos).getlocationURL();

                if (dv.contains("-")) {
                    // Split it.
                    String[] parts = dv.split("-");
                    sv = parts[0];
                    disvalue = parts[1];
                    bv = parts[2];
                }

                 cgv = contactgroup.toString();

                if (nv.equals("") || nv.equals(null)) {
                    Toast.makeText(getBaseContext(), R.string.Name_cant_be_empty, Toast.LENGTH_LONG).show();
                } else if (nv.length() < 4) {
                    Toast.makeText(getBaseContext(), R.string.Name_must_have_atleast_4_characters, Toast.LENGTH_LONG).show();
                } else if (pv.equals("") || pv.equals(null)) {
                    Toast.makeText(getBaseContext(), R.string.Phone_Number_cant_be_empty, Toast.LENGTH_LONG).show();

                } else if (pv.contains("[0-9]+") == false && pv.length() != 10) {
                    Toast.makeText(getBaseContext(), R.string.Enter_valid_phone_number1, Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(av) < 0 || Integer.parseInt(av) > 150) {
                    Toast.makeText(getBaseContext(), R.string.Enter_valid_age, Toast.LENGTH_LONG).show();
                } else
                {

                    clist_ids = clist_ids.substring(0, clist_ids.length()-1);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Createcontact.this);
                    builder1.setMessage(R.string.Press_Confirm);
                    builder1.setCancelable(false);
                    builder1.setTitle(R.string.Add_Contact_Details);
                    builder1.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    progress = ProgressDialog.show(Createcontact.this, "Please Wait ... ", "Adding new contact", true);
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
                            });
                    builder1.setNegativeButton( "Edit",
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


    protected void onChangeSelectedGroups() {
        StringBuilder stringBuilder = new StringBuilder();

        for(CharSequence group : selectedGroups)
            stringBuilder.append(group + ",");
        contactgroup.setText(stringBuilder.toString());
    }

    protected void showSelectGroupsDialog() {

        int l=Constants.grouplist.size();
        boolean[] checkedGroups = new boolean[l];

        if(l>0)
        for(int i = 0; i < l; i++)
            checkedGroups[i] = selectedGroups.contains(groups[i]);
        else
            {
                Snackbar.make(findViewById(android.R.id.content), R.string.error_in_fetching_groups, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            }

        DialogInterface.OnMultiChoiceClickListener groupsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                {
                    selectedGroups.add(groups[which]);
                    clist_ids+=Constants.grouplist.get(which).getgroupid();
                    clist_ids+=",";
                }

                else
                    selectedGroups.remove(groups[which]);

                onChangeSelectedGroups();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Select_Contact_Groups);
        builder.setMultiChoiceItems(groups, checkedGroups, groupsDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    void volleyrequest()
    {

        StringRequest sr = new StringRequest(Request.Method.POST, Constants.createcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    progress.dismiss();
                Log.d("TAG", "Register Response: " + response.toString());

                try {
                    JSONObject ob1 = new JSONObject(response);

                    String msg = ob1.get("message").toString();

                    if (msg.equalsIgnoreCase("Contact Created!"))
                    {
                        Toast.makeText(getApplicationContext(), R.string.Contact_created_successfully, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), R.string.check_your_server, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", nv);
                params.put("number", pv);
                params.put("gender", gv);
                params.put("gcmid", Constants.gcmRegId);
                params.put("clist_ids", clist_ids);
                params.put("resource_uri",resource_uri);
                params.put("age",av);
                params.put("district",disvalue);
                params.put("block",bv);
                params.put("state",sv);

                return params;
            }
        };

        VolleyApplication.getInstance().getRequestQueue().add(sr);
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

    void volleyrequest1() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.creategroup, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("groupresponse", response.toString());
                        try {
                            JSONObject js1 = (JSONObject) response.get("message");
                            JSONArray cast = js1.getJSONArray("objects");
                            int len = cast.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String name = actor.getString("name");
                                int id = actor.getInt("id");
                                Groupcontactdata cgd = new Groupcontactdata(name, id);
                                Constants.grouplist.add(cgd);
                                Log.d("Contactgroups", name);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Tag", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), R.string.error_in_fetching_groups, Toast.LENGTH_LONG).show();
            }
        });
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjReq);
    }

}