package main.gologo.survey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import main.gologo.R;

public class Surveyai extends Activity {

    Spinner sp;
    Button b1;
    String aivalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyai);
        sp=(Spinner) findViewById(R.id.ai_value);
        b1= (Button) findViewById(R.id.pick90);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aivalue = sp.getSelectedItem().toString();

                if (aivalue.equals("") || aivalue.equals(null))
                    Snackbar.make(findViewById(android.R.id.content), R.string.aitoast, Snackbar.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(getApplicationContext(), Surveys.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ai_id", aivalue);
                    //Add the bundle to the intent
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });
    }
    }




