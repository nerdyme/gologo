package main.gologo.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import main.gologo.R;

public class Sendmessage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);
    }
        public void TemplateAnnouncementSurvey(View v) {
            Intent i = new Intent(Sendmessage.this, TemplateAnnouncementSurvey.class);
            startActivity(i);
        }

        public void TemplateAnnouncementGovtscheme(View v) {
            Intent i = new Intent(Sendmessage.this, TemplateAnnouncementGovtscheme.class);
            startActivity(i);
        }

        public void TemplateAnnouncementCamp(View v) {
            Intent i = new Intent(Sendmessage.this, TemplateAnnouncementCamp.class);
            startActivity(i);
        }

}



