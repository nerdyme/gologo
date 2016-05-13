package main.gologo.message;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import main.gologo.R;
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class TemplateAnnouncementCamp extends BaseActionbar {

    Spinner sp;
    EditText et1,et2,et3;
    Button b1;
    String start,end,campname,venuename;
    ImageButton img1,img2;
    private Calendar cal;
    private int day,day1;
    private int month,month1;
    private int year,year1;

    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_announcement_camp);


        img1= (ImageButton) findViewById(R.id.calendaricon);
        img2=  (ImageButton) findViewById(R.id.calendaricon1);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

       // img1.setOnClickListener(this);
       // img2.setOnClickListener(this);

        addListenerOnButton();
        b1= (Button) findViewById(R.id.pick10);
        sp=(Spinner) findViewById(R.id.camp_value);
        et1 = (EditText) findViewById(R.id.selectstartdateforcamp);
        et2=(EditText) findViewById(R.id.selectenddateforcamp);
        et3=(EditText) findViewById(R.id.campvenue_value);

       // setCurrentDateOnView();
        //addListenerOnButton();

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

   public void addListenerOnButton() {



        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });


        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID2);

            }

        });

    }

    /*@Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.calendaricon:
                showDialog(DATE_DIALOG_ID);
                break;

            case R.id.calendaricon1:
                showDialog(DATE_DIALOG_ID2);
                break;

            default:
                break;
        }
    }*/

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

            case DATE_DIALOG_ID2:
                cur = DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
            default: break;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            if(cur == DATE_DIALOG_ID){
                // set selected date into edittext
                et1.setText(new StringBuilder().append(day)
                        .append("-").append(month+1).append("-").append(year)
                        .append(" "));
            }
            else if(cur==DATE_DIALOG_ID2){
                et2.setText(new StringBuilder().append(day)
                        .append("-").append(month+1).append("-").append(year)
                        .append(" "));
            }

        }
    };
}

