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

public class TemplateAnnouncementCamp extends AppCompatActivity  implements View.OnClickListener {



    Spinner sp;
    EditText et1,et2,et3;
    Button b1;
    String start,end,campname,venuename;
    ImageButton img1,img2;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_camp);


        img1= (ImageButton) findViewById(R.id.calendaricon);
        img2=   (ImageButton) findViewById(R.id.calendaricon1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

        b1= (Button) findViewById(R.id.pick10);
        sp=(Spinner) findViewById(R.id.camp_value);
        et1 = (EditText) findViewById(R.id.selectstartdateforcamp);
        et2=(EditText) findViewById(R.id.selectenddateforcamp);
        et3=(EditText) findViewById(R.id.campvenue_value);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                campname = sp.getSelectedItem().toString();
                start = et1.getText().toString();
                end = et2.getText().toString();
                venuename = et3.getText().toString();

                if (campname.equals("") || campname.equals(null))
                    Toast.makeText(getBaseContext(), R.string.campnametoast, Toast.LENGTH_LONG).show();
                else if (start.equals("") || start.equals(null))
                    Toast.makeText(getBaseContext(), R.string.startdatetoast, Toast.LENGTH_LONG).show();
                else if (end.equals("") || end.equals(null))
                    Toast.makeText(getBaseContext(), R.string.enddatetoast, Toast.LENGTH_LONG).show();
                else if (venuename.equals("") || venuename.equals(null))
                    Toast.makeText(getBaseContext(), R.string.venuenametoast, Toast.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(getApplicationContext(), ContactOptions.class);
                    Bundle bundle = new Bundle();


                    bundle.putString("campname", campname);
                    bundle.putString("start", start);
                    bundle.putString("end", end);
                    bundle.putString("venuename", venuename);
                    bundle.putString("ActivityName", "TemplateAnnouncementCamp");

                    //Add the bundle to the intent
                    i.putExtras(bundle);
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
            et1.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);

            et2.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };


}

