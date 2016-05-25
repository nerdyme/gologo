package main.gologo.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import main.gologo.R;

public class AppInfo extends Activity {
        ScrollView sc=null;
        LinearLayout ll=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        sc=(ScrollView)findViewById(R.id.scroll);
        ll=(LinearLayout)findViewById(R.id.ll);
        int c=ll.getChildCount();
        for ( int i = 0; i < c;  i++ ){
            View view = ll.getChildAt(i);
            view.setEnabled(false); // Or whatever you want to do with the view.
        }

       /* ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        ll.setEnabled(false);*/

    }
}
