package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementSurvey extends BaseActionbar implements View.OnClickListener {

    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    static private EditText et;
    Button b1;
    String date;
    private DatePickerDialogFragment mDatePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_survey);
        mDatePickerDialogFragment = new DatePickerDialogFragment();

        ib = (ImageButton) findViewById(R.id.calendaricon);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        et = (EditText) findViewById(R.id.selectdateforsurvey);
        et.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ib.setOnClickListener(this);
        b1=(Button)findViewById(R.id.pick1);


        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                date = et.getText().toString();
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

                if (date.equals(null) || date.trim().equals("") || date.equals("") || date.equalsIgnoreCase(" "))
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if (invalid==1)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if (Constants.isValidDate(date)==false)
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG).show();
                else if(st.after(end)){ //then false
                    Snackbar.make(findViewById(android.R.id.content), R.string.date_less_current, Snackbar.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), ContactOptions.class);
                    i.putExtra("ActivityName", "TemplateAnnouncementSurvey");
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
            et.setText(format.format(calendar.getTime()));
        }
    }

}
