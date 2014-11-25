package android.cousera.dailyselfie.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.cousera.dailyselfie.R;
import android.cousera.dailyselfie.activities.MainListActivity;
import android.graphics.Color;
import android.net.Uri;


public class AlarmReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 0;
    private final long[] mVibratePattern = { 0, 200, 200, 300 };
    private final Uri soundURI = Uri.parse("android.resource://android.cousera.dailyselfie/" + R.raw.selfie_music);

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent newIntent = new Intent(context.getApplicationContext(), MainListActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, newIntent, 0 );

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(context.getApplicationContext())
                .setContentIntent(pendingIntent)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie")
                .setTicker("Selfie Time")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_selfie_png_64p)
                .setSound(soundURI)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(mVibratePattern);

        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

}