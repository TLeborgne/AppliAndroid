package org.esiea.pinchon_leborgne.appliandroid;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        tv_hw=(TextView) findViewById(R.id.tv_welcome);
        tv_hw.setText(tv_hw.getText()+" "+now+" "+getResources().getString(R.string.tv_welcome2));
        DatePickerDialog.OnDateSetListener odsl= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tv_hw.setText(getResources().getString(R.string.tv_welcome1)+" "+year+"/"+(month+1)+"/"+day+" "+getResources().getString(R.string.tv_welcome2));
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
                break;
            case R.id.switchActivitiesMenu:
                switchActivity();
                break;
            case R.id.geolocMenu:
                mapMenuClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void switchActivity(View view){
        Intent myIntent = new Intent(this, SecondaryActivity.class);
        startActivity(myIntent);
    }
    public void switchActivity(){
        Intent myIntent = new Intent(this, SecondaryActivity.class);
        startActivity(myIntent);
    }
    public void textViewClicked(View v){
        dpd.show();
    }
    public void toastMenuClicked(){
        Toast.makeText(getApplicationContext(),getString(R.string.msgMenu),Toast.LENGTH_LONG).show();
    }
    public void mapMenuClicked(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Ouy2ocDbFYs")));
    }
}

