package main.gologo.contact;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import java.util.Calendar;
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
    ImageButton calicon1;
    ImageButton btnSpeak;
    ProgressDialog progress, progress1;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    int count=0;

    String clist_ids="";
    ArrayList<String> locations = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;

    protected CharSequence[] groups;
    protected ArrayList<CharSequence> selectedGroups = new ArrayList<CharSequence>();

    String nv = null, pv = null, av = null, gv = null, dv = null, sv = null, bv = null,disvalue=null, cgv = "",resource_uri="";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ArrayList<Locationdata> locationlist=new ArrayList<Locationdata>();
    ArrayList<Groupcontactdata> grouplist=new ArrayList<Groupcontactdata>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcontact);
        dis = (Spinner) findViewById(R.id.district_value);

        progress1 = ProgressDialog.show(Createcontact.this, "Please Wait ... ", "Fetching Groups & Locations", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the thing that takes a long time
                volleyrequest1();
                volleyrequest2();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();

          //For location :: ***************************
        adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locations.add("Choose Location");  /// Change it in case of hindi app

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the thing thing
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

        /*if(Constants.locationlist.size()==0) {
            Log.d("Calling","Volley for fetching locations");
            volleyrequest2();
        }*/

        dis.setAdapter(adapter);
        dis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        /*************************************************************************Location end***********************/


        name = (EditText) findViewById(R.id.name_value);
        age = (EditText) findViewById(R.id.age_value);
        phone = (EditText) findViewById(R.id.phone_value);
        gender = (Spinner) findViewById(R.id.gender_value);
        contactgroup=(EditText)findViewById(R.id.group_value);
        calicon1=(ImageButton)findViewById(R.id.calendaricon1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        calicon1.setOnClickListener(this);

        contactgroup.setInputType(InputType.TYPE_NULL);
        contactgroup.setTextIsSelectable(true);
        contactgroup.setOnClickListener(this);

        //name.setText(R.string.name1);
        //age.setText("1992-05-15");
        //phone.setText("8800244743");
       // contactgroup.setText("Delhi,Children");
        //clist_ids="Delhi,Children";

        btnSpeak=(ImageButton)findViewById(R.id.mic);
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
                cgv = contactgroup.toString();

                int ct=dis.getAdapter().getCount();
                Log.d("Size of locations", "Size :: " + ct);

                if (nv.equals("") || nv.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.Name_cant_be_empty, Snackbar.LENGTH_LONG).show();
                else if (nv.length() < 4)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Name_must_have_atleast_4_characters, Snackbar.LENGTH_LONG).show();
                else if (pv.equals("") || pv.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.Phone_Number_cant_be_empty, Snackbar.LENGTH_LONG).show();
                else if (pv.contains("[0-9]+") == false && pv.length() != 10)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Enter_valid_phone_number1, Snackbar.LENGTH_LONG).show();
                else if(age.equals("")|| age.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.enter_valid_dob, Snackbar.LENGTH_LONG).show();
                //else if()
                //  Snackbar.make(findViewById(android.R.id.content), R.string.enter_valid_dob, Snackbar.LENGTH_LONG).show();
                //else if (Integer.parseInt(av) < 0 || Integer.parseInt(av) > 150)
                 //   Snackbar.make(findViewById(android.R.id.content), R.string.Enter_valid_age, Snackbar.LENGTH_LONG).show();
                else if (ct==1)
                    Snackbar.make(findViewById(android.R.id.content), R.string.error_in_fetching_locatiosn, Snackbar.LENGTH_LONG).show();
                else if(dis.getSelectedItemPosition()==0)
                Snackbar.make(findViewById(android.R.id.content), R.string.select_valid_location, Snackbar.LENGTH_LONG).show();
                else
                {
                    if(dis!=null)
                    {
                        dv = dis.getSelectedItem().toString();
                        int pos = dis.getSelectedItemPosition();
                        resource_uri = locationlist.get(pos).getlocationURL();

                        if (dv.contains("-")) {
                            // Split it.
                            String[] parts = dv.split("-");
                            sv = parts[0];
                            disvalue = parts[1];
                            bv = parts[2];
                        }
                    }
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

       String st=stringBuilder.toString();

        st=st.substring(0,st.length()-1);
        contactgroup.setText(st);
    }

    protected void showSelectGroupsDialog() {

        DialogInterface.OnMultiChoiceClickListener groupsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                {
                    selectedGroups.add(groups[which]);
                    clist_ids+=grouplist.get(which).getgroupid();
                    clist_ids+=",";
                }

                else
                    selectedGroups.remove(groups[which]);
                onChangeSelectedGroups();
            }
        };

        int l=grouplist.size();
        boolean[] checkedGroups = new boolean[l];
        Log.d("length of groups", "length  " + l);

        if(l>0)
        for(int i = 0; i < l; i++)
            checkedGroups[i] = selectedGroups.contains(groups[i]);   //Exception comes here

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Select_Contact_Groups);
        builder.setMultiChoiceItems(groups, checkedGroups, groupsDialogListener);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
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
                        Snackbar.make(findViewById(android.R.id.content), R.string.Contact_created_successfully, Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                    else
                        Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server +"\n"+msg, Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());
                progress.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Createcontact.this);
                builder1.setMessage(R.string.error_in_adding_contact);
                builder1.setCancelable(false);
                builder1.setTitle(R.string.error_in);
                builder1.setNegativeButton( "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", Constants.capitalizeString(nv));
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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Snackbar.make(findViewById(android.R.id.content), R.string.speech_not_supported, Snackbar.LENGTH_LONG).show();
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
                Constants.contact_groups1, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if(count==0)
                            count+=1;
                        else
                        { progress1.dismiss(); count=0;}
                        Log.d("groupresponse", response.toString());
                        try {
                            //JSONObject js1 = (JSONObject) response.get("message");  //unchange it when route changes
                            JSONArray cast = response.getJSONArray("objects");
                            int len = cast.length();
                            groups=new CharSequence[len];
                            for (int i = 0; i < len; i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String name = actor.getString("name");
                                int id = actor.getInt("id");
                                Groupcontactdata cgd = new Groupcontactdata(name, id);
                                grouplist.add(cgd);
                                groups[i]=name;
                                //Log.d("Contactgroups", name);
                            }
                            Collections.sort(grouplist, new Groupcomparator());

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress1.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Createcontact.this);
                builder1.setMessage(R.string.error_in_fetching_groups);
                builder1.setCancelable(false);
                builder1.setTitle(R.string.error_in);
                builder1.setNegativeButton( "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                VolleyLog.d("Tag", "Error: " + error.getMessage());
                //Snackbar.make(findViewById(android.R.id.content), R.string.error_in_fetching_groups +"\n" +error.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjReq);
    }


    void volleyrequest2()
    {
        JsonObjectRequest request1 = new JsonObjectRequest(Constants.location, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if(count==0)
                            count+=1;
                        else
                        { progress1.dismiss(); count=0;}

                        Log.d("locationresponse",response.toString());
                        try {
                            JSONObject js1 = (JSONObject) response.get("message");
                            JSONArray ar1 = js1.getJSONArray("objects");
                            int len=ar1.length();
                            for(int i=0;i<len;++i)
                            {
                                JSONObject info = (JSONObject) ar1.get(i);
                                String s1 = info.get("desc").toString();
                                String s2 = info.get("resource_uri").toString();
                                //Log.d("Data", s2 + '\n');
                                Locationdata ob=new Locationdata(s1,s2);
                                locationlist.add(ob);
                                locations.add(s1);
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
                        progress1.dismiss();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Createcontact.this);
                        builder1.setMessage(R.string.error_in_fetching_locatiosn);
                        builder1.setCancelable(false);
                        builder1.setTitle(R.string.error_in);
                        builder1.setNegativeButton( "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        Log.d("error","Error in fetching locations ::" + error.toString());
                    }
                }
        );
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            age.setText(selectedYear + "-" + (selectedMonth + 1) + "-"
                    + selectedDay);
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.group_value:
                showSelectGroupsDialog();
                break;
            case R.id.calendaricon1 : showDialog(0);

            default:
                break;
        }
    }
}