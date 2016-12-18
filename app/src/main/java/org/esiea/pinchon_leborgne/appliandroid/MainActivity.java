package org.esiea.pinchon_leborgne.appliandroid;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tv_hw;
    String now;
    DatePickerDialog dpd;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        now= DateUtils.formatDateTime(getApplicationContext(),(new Date()).getTime(), DateFormat.FULL);
        tv_hw=(TextView) findViewById(R.id.tv_hello_world);
        tv_hw.setText(now);
        DatePickerDialog.OnDateSetListener odsl= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tv_hw.setText(year+"/"+(month+1)+"/"+day);
            }
        };
        dpd = new DatePickerDialog(this, odsl, year, month, day);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toastMenu:
                toastMenuClicked();
        }
        return super.onOptionsItemSelected(item);
    }
    public void button1Clicked(View v){
        Toast.makeText(getApplicationContext(),getString(R.string.msg),Toast.LENGTH_LONG).show();
        NotificationCompat.Builder maNotification= new NotificationCompat.Builder(this);
        maNotification.setContentTitle("Hello World!");
        maNotification.setContentText("Bonjour le monde!");
        maNotification.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(1, maNotification.build());
    }
    public void textViewClicked(View v){
        dpd.show();
    }
    public void toastMenuClicked(){
        Toast.makeText(getApplicationContext(),getString(R.string.msgMenu),Toast.LENGTH_LONG).show();
    }
}
