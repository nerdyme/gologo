package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementSurvey extends BaseActionbar implements View.OnClickListener {

    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private EditText et;
    Button b1;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_survey);

        ib = (ImageButton) findViewById(R.id.calendaricon);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        et = (EditText) findViewById(R.id.selectdateforsurvey);
        ib.setOnClickListener(this);
        b1=(Button)findViewById(R.id.pick1);


        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                date = et.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                String format = sdf.format(date);


                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                String cur_date=ft.format(dNow);
                Date date1=new Date();
                Date date2=new Date();

                try {
                     date1 = sdf.parse(format);
                     date2 = sdf.parse(cur_date);


                } catch (ParseException p) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }

                if(date1.before(date2)){ //then false
                    Snackbar.make(findViewById(android.R.id.content), R.string.date_less_current, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                else if (date.equals(null) || date.trim().equals("") || date.equals("") || date.equalsIgnoreCase(" ")) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.Please_enter_valid_date_before_sending, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
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
            et.setText(selectedYear + "-" + (selectedMonth + 1) + "-"
                    + selectedDay);
        }
    };

}
