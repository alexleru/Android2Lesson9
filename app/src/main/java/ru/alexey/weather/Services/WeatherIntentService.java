package ru.alexey.weather.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

import ru.alexey.weather.MainActivity;
import ru.alexey.weather.R;

public class WeatherIntentService extends IntentService {

    public static final int NOTIFY_ID = 0;

    public WeatherIntentService() {
        super("Weather");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String data = intent.getStringExtra(MainActivity.DATA_FOR_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(getResources().getText(R.string.app_name))
                        .setContentText(data)
                        .setTicker(data)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        try {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }catch (NullPointerException e){
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
