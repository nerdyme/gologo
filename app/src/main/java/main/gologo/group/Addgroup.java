package main.gologo.group;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import main.gologo.home.VolleyApplication;


public class Addgroup extends AppCompatActivity {

    public EditText msgvalue=null;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Button b1 =null;
    String contactgroupname=null;


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

        b1.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                contactgroupname = msgvalue.getText().toString();

                System.out.println("value of message" + contactgroupname);

                if(contactgroupname.equals(null) || contactgroupname.trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(), R.string.Please_enter_groupname_before_creating, Toast.LENGTH_LONG).show();
                }
                else
                {
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

                Log.d("TAG", "Create Group Response: " + response.toString());
                try {
                    JSONObject js=new JSONObject(response.toString());
                    String msg=js.getString("message");
                    if (msg.equalsIgnoreCase("Contact List Created!")==true)
                    {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Addgroup.this);
                        dlgAlert.setMessage(R.string.Contact_Group_Created);
                        //dlgAlert.setTitle("App Title");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                        finish();
                                    }
                                });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        //Toast.makeText(getApplicationContext(),R.string.Contact_Group_Created,Toast.LENGTH_LONG).show();
                        //finish();

                    }
                    else
                    {AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Addgroup.this);
                        dlgAlert.setMessage(R.string.Error_in_creating_Contact_Group);
                        //dlgAlert.setTitle("App Title");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                        finish();
                                    }
                                });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        Toast.makeText(getApplicationContext(),R.string.Error_in_creating_Contact_Group,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),R.string.Error_in_creating_Contact_Group,Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Addgroup.this);
                dlgAlert.setMessage(R.string.Error_in_creating_Contact_Group);
                //dlgAlert.setTitle("App Title");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                                finish();
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                Log.d("TAG", "Not created Group Response: " + error.toString());
            }
        })  {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",contactgroupname);
                params.put("gcmid", Constants.gcmRegId);
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
                    msgvalue.setText(result.get(0));
                }
                break;
            }

        }
    }
}


