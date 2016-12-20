package org.esiea.pinchon_leborgne.appliandroid;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

public class GetBiersServices extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ALL_BIERS = "org.esiea.pinchon_leborgne.appliandroid.action.GET_ALL_BIERS";
    int flag=0;

    public GetBiersServices() {
        super("GetBiersServices");
    }

    public static void startActionBiers(Context context) {
        Intent intent = new Intent(context, GetBiersServices.class);
        intent.setAction(ACTION_GET_ALL_BIERS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_BIERS.equals(action)) {
                handleActionBiers();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBiers() {
        Log.i("D", "Handled properly");
        URL url;
        try {
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "bieres.json"));
                Log.d("Debug-HTTP", "Bieres.json downloaded!");
                if(flag==0){
                    flag=1;
                    NotificationCompat.Builder maNotification= new NotificationCompat.Builder(this);
                    maNotification.setContentTitle("Téléchargement terminé!");
                    maNotification.setContentText("Informations des bières téléchargées avec succès.");
                    maNotification.setSmallIcon(R.drawable.ic_download);
                    maNotification.setColor(Color.argb(50, 224, 224, 39));
                    NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.notify(1, maNotification.build());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondaryActivity.BIERS_UPDATE));
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
