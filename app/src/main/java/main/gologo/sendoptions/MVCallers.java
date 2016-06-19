package main.gologo.sendoptions;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.gologo.R;
import main.gologo.audio.MultipartUtility;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.home.VolleyApplication;

public class MVCallers extends BaseActionbar implements  View.OnClickListener{


    //********************
    static EditText et1,et2;
    Button b1;
    String start,end;
    ImageButton img1,img2;
    private Calendar cal;

    private DatePickerDialogFragment mDatePickerDialogFragment;
    int cur = 0;
    //************************

    String contactlist = "";
    Bundle bundle;
    String actname = "", form_id = "", filepath = "", audiofile = "";
    StringBuilder checkedcontacts;

    ProgressDialog progress = null;
    JSONObject jsonObject = null;
    String survey_name = "";
    Button select;
    int msg_id = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvcallers);

        img1= (ImageButton) findViewById(R.id.calendaricon);
        img2=  (ImageButton) findViewById(R.id.calendaricon1);
        cal = Calendar.getInstance();
        mDatePickerDialogFragment = new DatePickerDialogFragment();

        b1= (Button) findViewById(R.id.pick20);
        et1 = (EditText) findViewById(R.id.selectstartdateformv);
        et2=(EditText) findViewById(R.id.selectenddateformv);
        et1.setText("2016-01-01");
        et2.setText((new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start = et1.getText().toString();
                end = et2.getText().toString();
                int invalid=0;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1=new Date();
                Date date2=new Date();

                try {
                    date1 = sdf.parse(start);
                    date2 = sdf.parse(end);
                }
                catch (ParseException p) {
                    invalid=1;
                }

                if (start.equals("") || start.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.startdatetoast, Snackbar.LENGTH_LONG).show();
                else if (end.equals("") || end.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.enddatetoast, Snackbar.LENGTH_LONG).show();
                else if(invalid==1)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if(!Constants.isValidDate(start)|| !Constants.isValidDate(end))
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if(date2.before(date1)) //then false
                    Snackbar.make(findViewById(android.R.id.content), R.string.end_date_less_then_current_date, Snackbar.LENGTH_LONG).show();
                else {
                    bundle = getIntent().getExtras();
                    actname = bundle.getString("ActivityName");

                                // Do Volley request
                                if (actname.equalsIgnoreCase("TemplateAnnouncementCamp") == true) {
                                    msg_id = 41; ///Set as gramvaani gives
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("camp_name", bundle.getString("campname"));
                                        jsonObject.put("start_date", bundle.getString("start"));
                                        jsonObject.put("end_date", bundle.getString("end"));
                                        jsonObject.put("location", bundle.getString("venuename"));
                                    }
                                    catch (JSONException e) {
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // do the thing that takes a long time
                                            sendmessage(jsonObject);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
                                                }
                                            });
                                        }
                                    }).start();

                                } else if (actname.equalsIgnoreCase("TemplateAnnouncementGovtscheme") == true) {
                                    msg_id = 42; ///Set as gramvaani gives
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("scheme_name", bundle.getString("scheme"));
                                        jsonObject.put("beneficiaries", bundle.getString("beneficiary"));
                                        jsonObject.put("scheme_date", bundle.getString("date"));
                                    } catch (JSONException e) {

                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // do the thing that takes a long time
                                            sendmessage(jsonObject);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
                                                }
                                            });
                                        }
                                    }).start();

                                } else if (actname.equalsIgnoreCase("TemplateAnnouncementSurvey") == true) {
                                    msg_id = 40; ///Set as gramvaani gives
                                    jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("survey_date", bundle.getString("date"));
                                    } catch (JSONException e) {

                                    }
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // do the thing that takes a long time
                                            sendmessage(jsonObject);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Sending message", true);
                                                }
                                            });
                                        }
                                    }).start();

                                } else if (actname.equalsIgnoreCase("LaunchSurvey") == true) {
                                    form_id = bundle.getString("form_id");
                                    survey_name = bundle.getString("survey_name");
                                    Log.d("Data to survey", "Form id :: " + form_id + "  Survey name :::  " + survey_name + "  ");

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // do the thing that takes a long time
                                            Log.d("Calling", "***** Launching survey ****");
                                            sendsurvey();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Launching Survey", true);
                                                }
                                            });
                                        }
                                    }).start();
                                }
                                else if (actname.equalsIgnoreCase("Recordaudio") == true) {

                                    filepath = bundle.getString("FileName"); //complete file path is fetched./*/storage/emulated/0/AudioRecorder/1462830570992.mp4*/
                                    Log.d("filepath", "Inside send audio function" + filepath);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // do the thing that takes a long time
                                            Handler h = new Handler(Looper.getMainLooper());
                                            h.post(new Runnable() {
                                                public void run() {
                                                    progress = ProgressDialog.show(MVCallers.this, "Please Wait ... ", "Your audio is being uploaded", true);
                                                }
                                            });
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    sendaudio();
                                                }
                                            });
                                        }
                                    }).start();
                                }
                }
            }
        });

        }

    void sendaudio() {
        String charset = "UTF-8";
        File uploadFile1 = new File(filepath);
        String requestURL = Constants.recording;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            multipart.addFormField("gcmid", Constants.gcmRegId);
            multipart.addFormField("group_ids", "");
            multipart.addFormField("mv_caller", "true");
            multipart.addFormField("caller_ids", "");
            multipart.addFormField("filename", audiofile);
            multipart.addFormField("start_date", start);
            multipart.addFormField("end_date", end);
            multipart.addFormField("ai", "10");
            multipart.addFilePart("uploadedfile", uploadFile1);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");
            progress.dismiss();
            for (String line : response) {
                System.out.println(line);
            }
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        successmsg(R.string.recording_submitted);
                    }
                });
            }
        }
        catch (IOException ex) {
            Log.d("Error", "Inside Audio upload" + ex.toString());
            progress.dismiss();
            if (ex.toString().contains("403"))
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        errormsg(R.string.mvcallers_not_present);
                    }
                });
            }
            else
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        errormsg(R.string.error_in_sending_audio);
                    }
                });
            }


        } catch(Exception e)
        {
            Log.d("Error", "Inside Audio upload" + e.toString());
            progress.dismiss();
            if (e.toString().contains("403"))
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        errormsg(R.string.mvcallers_not_present);
                    }
                });
            }
            else
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        errormsg(R.string.error_in_sending_audio);
                    }
                });
            }

        }
    }

    void sendmessage(final JSONObject json)
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launchmessage,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.d("Inside Launch Message", response.toString());
                        try {
                            JSONObject response1=new JSONObject(response);
                            String msg=(String)response1.get("message");
                            if(msg.equalsIgnoreCase("People have not called Mobile Vaani in this duration!"))
                                errormsg(R.string.mvcallers_not_present);
                            else successmsg(R.string.message_submitted);
                        }
                        catch (JSONException e) {
                            Log.d("Error",e.toString());
                            errormsg(R.string.error_in_sending_message);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("Launch Message Error",error.toString());
                        errormsg(R.string.error_in_sending_message);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                    params.put("gcmid", Constants.gcmRegId);
                    params.put("message_id", Integer.toString(msg_id));
                    params.put("caller_ids", "");
                    params.put("group_ids", "");
                    params.put("mv_caller","true");
                    params.put("start_date",start);
                    params.put("end_date",end);
                    params.put("ai","10");
                    params.put("message_args", json.toString());
                    /*JSONObject jsonObject = new JSONObject();
                    jsonObject.put("club", "Hello all");
                    jsonObject.put("date", "15.07.2015");
                    jsonObject.put("callback_calls", 20);
                    jsonObject.put("club_contribs", 40);*/
                return params;
            }
        };
        request1.setRetryPolicy(new DefaultRetryPolicy(Constants.timeout, Constants.retrypolicy,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    void sendsurvey()
    {
        StringRequest request1 = new StringRequest(Request.Method.POST, Constants.launch_survey,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.d("Calling", "*****Inside Send Survey *****" + response.toString());
                        try {
                            JSONObject response1=new JSONObject(response);
                            String msg=(String)response1.get("message");
                            if(msg.equalsIgnoreCase("People have not called Mobile Vaani in this duration!"))
                             errormsg(R.string.mvcallers_not_present);
                            else successmsg(R.string.survey_submitted);

                        } catch (JSONException e) {
                            Log.d("Error",e.toString());
                            errormsg(R.string.error_in_sending_survey);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("Inside MV caller survey", error.toString());
                        errormsg(R.string.error_in_sending_survey);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmid", Constants.gcmRegId);
                params.put("caller_ids",contactlist);
                params.put("group_ids","");
                params.put("form_id",form_id);
                params.put("survey_name",survey_name);
                params.put("start_date",start);
                params.put("end_date",end);
                params.put("ai","10");
                params.put("mv_caller","true");
                return params;
            }
        };
        request1.setRetryPolicy(new DefaultRetryPolicy(Constants.timeout, Constants.retrypolicy,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getInstance().getRequestQueue().add(request1);
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((progress != null) && progress.isShowing())
            progress.dismiss();
        progress = null;
    }

    static public class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        private int flag = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setFlag(int i) {
            flag = i;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (flag == FLAG_START_DATE) {
                et1.setText(format.format(calendar.getTime()));
            } else if (flag == FLAG_END_DATE) {
                et2.setText(format.format(calendar.getTime()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.calendaricon) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_START_DATE);
            mDatePickerDialogFragment.show(this.getFragmentManager(), "datePicker");
        } else if (id == R.id.calendaricon1) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_END_DATE);
            mDatePickerDialogFragment.show(this.getFragmentManager(), "datePicker");
        }
    }

    void errormsg(int msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MVCallers.this);
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
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MVCallers.this);
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

