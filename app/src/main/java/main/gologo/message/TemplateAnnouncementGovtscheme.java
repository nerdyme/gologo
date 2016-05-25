package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.gologo.R;
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementGovtscheme extends BaseActionbar implements View.OnClickListener {

    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private EditText et,start;
    Spinner sp1;

    Button b1;
    String date,ben,scheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_govtscheme);

        b1=(Button)findViewById(R.id.pick11);
        et=(EditText) findViewById(R.id.et1);
        sp1=(Spinner)findViewById(R.id.sp1);
        start=(EditText) findViewById(R.id.start);
        start.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        et.setText("National Women Employment Act");

        ib = (ImageButton) findViewById(R.id.calendaricon);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        ib.setOnClickListener(this);

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
        showDialog(0);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            start.setText(selectedYear + "-" + (selectedMonth + 1) + "-"
                    + selectedDay);
        }
    };
}
