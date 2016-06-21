package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementGovtscheme extends BaseActionbar implements View.OnClickListener {

    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private final int REQ_CODE_SPEECH_INPUT = 100;
   static  private EditText et,start;
    ImageButton btnSpeak;
    Spinner sp1;

    Button b1;
    String date,ben,scheme;
    private DatePickerDialogFragment mDatePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_govtscheme);

        b1=(Button)findViewById(R.id.pick11);
        et=(EditText) findViewById(R.id.et1);
        sp1=(Spinner)findViewById(R.id.sp1);
        mDatePickerDialogFragment = new DatePickerDialogFragment();
        start=(EditText) findViewById(R.id.start);
        start.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        et.setText(R.string.National_Employment_Act);

        ib = (ImageButton) findViewById(R.id.calendaricon);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        ib.setOnClickListener(this);


        btnSpeak=(ImageButton)findViewById(R.id.mic12);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                date = start.getText().toString();
                ben=sp1.getSelectedItem().toString();
                scheme=et.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dNow = new Date();
                String cur_date=sdf.format(dNow);

                Date st=new Date();
                Date end=new Date();
                int invalid=0;

                try {
                    end = sdf.parse(date);
                    st = sdf.parse(cur_date);
                } catch (ParseException p) {
                    invalid=1;
                }

                if(ben.equals("") || ben.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.govtschemebeneficiarytoast, Snackbar.LENGTH_LONG).show();
                else if (scheme.equals("")||scheme.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.govtschemenametoast, Snackbar.LENGTH_LONG).show();
                else if (date.equals(null) || date.trim().equals("") || date.equals("") || date.equalsIgnoreCase(" "))
                    Snackbar.make(findViewById(android.R.id.content), R.string.govtschemestartdatetoast, Snackbar.LENGTH_LONG).show();
                else if (invalid==1)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if (Constants.isValidDate(date)==false)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if(st.after(end)){ //then false
                    Snackbar.make(findViewById(android.R.id.content), R.string.date_less_current, Snackbar.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), ContactOptions.class);
                    i.putExtra("ActivityName", "TemplateAnnouncementGovtscheme");
                    i.putExtra("scheme",scheme);
                    i.putExtra("beneficiary",ben);
                    i.putExtra("date", date);
                    startActivity(i);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.calendaricon) {
            mDatePickerDialogFragment.show(this.getFragmentManager(), "datePicker");
        }
    }


    static public class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            start.setText(format.format(calendar.getTime()));
        }
    }

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
                    et.setText(result.get(0));
                }
                break;
            }
        }
    }
}
