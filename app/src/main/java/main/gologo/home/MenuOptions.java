package main.gologo.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import main.gologo.R;
import main.gologo.audio.Recordaudio;
import main.gologo.contact.Createcontact;
import main.gologo.group.Addgroup;
import main.gologo.message.Sendmessage;
import main.gologo.survey.Surveys;


public class MenuOptions extends AppCompatActivity {


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
       Intent i = new Intent(MenuOptions.this, Surveys.class);
        startActivity(i);
    }

    public void message(View v) {
       Intent i = new Intent(MenuOptions.this, Sendmessage.class);
        startActivity(i);
    }

    public void contact(View v) {
        Intent i = new Intent(MenuOptions.this, Createcontact.class);
        startActivity(i);
    }

    public void audio(View v) {
        Intent i = new Intent(MenuOptions.this, Recordaudio.class);
        startActivity(i);
    }
}
