package main.gologo.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import main.gologo.R;


public class BaseActionbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_actionbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);

        //SearchView searchView = (SearchView) menu.findItem(R.id.menu_action_search).getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.gramvaani)
        {
            String url = "http://www.gramvaani.org";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return true;
        }
        else if(id == R.id.home1)
        {
            Intent i = new Intent(getApplicationContext(),MenuOptions.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if (id == R.id.open_windows_explorer)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                    + "/AudioRecorder/");
            intent.setDataAndType(uri, "*/*");
            startActivity(Intent.createChooser(intent, "Open folder"));

        	/*Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/AudioRecorder/");
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setDataAndType(selectedUri, "resource/folder");
        	startActivity(intent);*/
            return true;
        }
        else if (id == R.id.share_app)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Gologo");
            String sAux = "\nInstall 'Gologo' application by clicking on the below link : \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n"; //Change the link by gdrive
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose the application to share 'Gologo'"));
            return true;
        }
        else if (id==R.id.app_info)
        {
            Intent i=new Intent(getApplicationContext(),AppInfo.class);
            startActivity(i);
            return true;
        }
        else if(id==R.id.my_profile)
        {
            Intent i=new Intent(getApplicationContext(),Myprofile.class);
            startActivity(i);
            return true;
        }
        else
        {
           // return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
