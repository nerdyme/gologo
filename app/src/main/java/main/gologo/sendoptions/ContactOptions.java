package main.gologo.sendoptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import main.gologo.R;

public class ContactOptions extends AppCompatActivity  {

    Button b1,b2,b3;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_options);
        bundle = getIntent().getExtras();
    }


            public void Phonecontacts(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), Phonecontacts.class);
                i.putExtras(bundle);
                startActivity(i);
            }


            public void GVGroups(View v)
            {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),GVGroups.class);
                i.putExtras(bundle);
                startActivity(i);

            }


            public void MVCallers(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),MVCallers.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
}
