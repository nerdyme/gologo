package main.gologo.gcm;

/**
 * Created by surbhi on 4/2/16.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

import main.gologo.R;
import main.gologo.home.MenuOptions;
import main.gologo.sendoptions.ContactOptions;

public class GcmIntentService extends IntentService {

    private final String TAG = "GcmIntentService";
    PendingIntent contentIntent;
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        Log.d("Message :: ",messageType);   // returned gcm as answer
        if (!extras.isEmpty()) {
            Bundle bundle;
            bundle=intent.getExtras();
            // read extras as sent from server
           // String message = extras.getString("message");
            //String serverTime = extras.getString("timestamp");
            int code = bundle.getInt("code");
            sendNotification(code,bundle);
            Log.d(TAG, "Received: " + extras.toString());
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(int code,Bundle bundle) {
        NotificationCompat.Builder mBuilder;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (code == 1)
        {
            Log.d("Survey launch",bundle.toString());
            //PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);`
            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ContactOptions.class), Intent.FLAG_ACTIVITY_NEW_TASK);
            String msg=bundle.getString("extras");
            String msg1=bundle.getString("message");
            String data[]=msg.split("-");

            String survey_name=data[0],survey_id=data[1],form_id=data[2];

            Log.d("Survey_name=", survey_name+".. Form id :: "+ form_id + "" + "Survey_id ::" + survey_id);
             mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Gologo")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg1))
                    .setContentText(msg);
        }
        else
        {
             Log.d("Normal Use case",bundle.toString());
             contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MenuOptions.class), 0);
            String msg=bundle.getString("message");
             mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Gologo")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                     .setContentText(msg);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        //Vibration
        //mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mBuilder.setAutoCancel(true);
        //LED
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setContentIntent(contentIntent);
        Random random = new Random();
        NOTIFICATION_ID = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
/*public class GcmIntentService extends Service {
    public GcmIntentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}*/
