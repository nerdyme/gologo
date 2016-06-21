package main.gologo.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import main.gologo.R;
import main.gologo.audio.Recordaudio;
import main.gologo.contact.Createcontact;
import main.gologo.group.Addgroup;
import main.gologo.message.Sendmessage;
import main.gologo.survey.Surveyai;


public class MenuOptions extends BaseActionbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);
    }

    public void group(View v) {
       Intent i = new Intent(MenuOptions.this, Addgroup.class);
       startActivity(i);
    }

    public void survey(View v) {
        if(AppStatus.getInstance(this).isOnline()) {
            Intent i = new Intent(MenuOptions.this, Surveyai.class);
            startActivity(i);
        }
        else
        Snackbar.make(findViewById(android.R.id.content), R.string.check_your_network, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();

    }

    public void message(View v) {
       Intent i = new Intent(MenuOptions.this, Sendmessage.class);
        startActivity(i);
    }

    public void contact(View v) {
        if(AppStatus.getInstance(this).isOnline()) {
            Intent i = new Intent(MenuOptions.this, Createcontact.class);
            startActivity(i);
        }
        else
            Snackbar.make(findViewById(android.R.id.content), R.string.check_your_network, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
    }

    public void audio(View v) {
        Intent i = new Intent(MenuOptions.this, Recordaudio.class);
        startActivity(i);
    }



}
