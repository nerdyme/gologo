package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

import main.gologo.R;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementSurvey extends AppCompatActivity implements View.OnClickListener {

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
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                date = et.getText().toString();

                if(date.equals(null) || date.trim().equals("") ||date.equals("") || date.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getApplicationContext(), R.string.Please_enter_valid_date_before_sending, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(),ContactOptions.class);
                    i.putExtra("ActivityName","TemplateAnnouncementSurvey");
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
            et.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };

}
