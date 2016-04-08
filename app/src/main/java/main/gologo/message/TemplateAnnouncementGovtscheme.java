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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import main.gologo.R;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementGovtscheme extends AppCompatActivity implements View.OnClickListener {

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

                if(ben.equals("") || ben.equals(null))
                {
                    Toast.makeText(getApplicationContext(), R.string.govtschemebeneficiarytoast, Toast.LENGTH_LONG).show();
                }
                else if (scheme.equals("")||scheme.equals(null))
                {
                    Toast.makeText(getApplicationContext(), R.string.govtschemenametoast, Toast.LENGTH_LONG).show();
                }
                else if (date.equals(null) || date.trim().equals("") || date.equals("") || date.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getApplicationContext(), R.string.govtschemestartdatetoast, Toast.LENGTH_LONG).show();
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
            start.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };
}
