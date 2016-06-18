package main.gologo.sendoptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import main.gologo.R;
import main.gologo.home.BaseActionbar;

public class ContactOptions extends BaseActionbar {

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


    public void GVGroups(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), GVGroups.class);
        i.putExtras(bundle);
        startActivity(i);
    }


    public void MVCallers(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), MVCallers.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}