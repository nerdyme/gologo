package main.gologo.group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;


public class Addgroup extends BaseActionbar {

    public EditText msgvalue=null;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Button b1 =null;
    String contactgroupname=null;
    ProgressDialog myDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);

        msgvalue= (EditText)findViewById(R.id.group_editText);
        b1 = (Button) findViewById(R.id.creategroup_button);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak1);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        Log.d("GCM ID :::: ",Constants.gcmRegId);
        b1.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                contactgroupname = msgvalue.getText().toString();

                System.out.println("value of message" + contactgroupname);

                if (contactgroupname.equals(null) || contactgroupname.trim().equalsIgnoreCase("")) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_groupname_before_creating, Snackbar.LENGTH_LONG).show();
                } else {
                    myDialog = new ProgressDialog(Addgroup.this);
                    myDialog.setTitle("Adding new group");
                    myDialog.setMessage("Please Wait... ");
                    myDialog.setCancelable(false);
                    myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    myDialog.show();
                    createvolleyrequest();
                }
            }
        });
    }

    void createvolleyrequest()
    {
        StringRequest sr = new StringRequest(Request.Method.POST,Constants.creategroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    myDialog.dismiss();
                    Log.d("TAG", "Create Group Response: " + response.toString());
                    try {
                    JSONObject js=new JSONObject(response.toString());
                    String msg=js.getString("message");
                    successmsg(R.string.Contact_Group_Created);
                } catch (JSONException e) {
                    Log.d("Error",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myDialog.dismiss();
                Log.d("Error", "Not created Group Response: " + error.toString());
                errormsg(R.string.Error_in_creating_Contact_Group);
            }
        })  {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", Constants.capitalizeString(contactgroupname));
                params.put("gcmid", Constants.gcmRegId);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(Constants.timeout, Constants.retrypolicy,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                    msgvalue.setText(result.get(0));
                }
                break;
            }
        }
    }
    void errormsg(int msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Addgroup.this);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setTitle(R.string.error_in);
        builder1.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.create().show();
        Snackbar.make(findViewById(android.R.id.content), R.string.check_your_server, Snackbar.LENGTH_LONG).show();
    }

    void successmsg(int msg)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Addgroup.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(R.string.success);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}


