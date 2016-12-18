package org.esiea.pinchon_leborgne.appliandroid;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                tv_hw.setText(year+"/"+month+"/"+day);
            }
        };
        dpd = new DatePickerDialog(this, odsl, year, month, day);
    }

    public void button1Clicked(View v){
        Toast.makeText(getApplicationContext(),getString(R.string.msg),Toast.LENGTH_LONG).show();
    }
}
