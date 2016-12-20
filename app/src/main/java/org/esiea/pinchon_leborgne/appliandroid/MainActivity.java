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

    public static final String BIERS_UPDATE = "org.esiea.pinchon_leborgne.appliandroid.BIERS_UPDATE";
    RecyclerView rv;
    TextView tv_hw;
    String now;
    DatePickerDialog dpd;
    IntentFilter intentFilter;
    int year;
    int month;
    int day;
    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder>{

        private JSONArray biers;
        public class BierHolder extends RecyclerView.ViewHolder{
            public TextView name;
            public BierHolder(View itemView) {
                super(itemView);
            }
        }

        public BiersAdapter(JSONArray biers){
            Log.d("DEBUG-Adapter", "BiersAdapter");
            this.biers=biers;
        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View view = li.inflate(R.layout.rv_bier_element, parent, false);
            BierHolder bh= new BierHolder(view);
            return bh;
        }

        @Override
        public void onBindViewHolder(BierHolder holder, int position) {
            try{
                JSONObject obj = biers.getJSONObject(position);
                String name= obj.getString("name");
                holder.name.setText(name);
            }catch(Exception e) {
                e.fillInStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return biers.length();
        }

        public void setNewBier(JSONArray biers){
            this.biers=biers;
            notifyDataSetChanged();
            Log.d("DEBUG-Adapter", this.biers.toString());
        }
    }
    public class BierUpdate extends BroadcastReceiver{

        public String loadJSON() {
            String json = null;
            try {
                FileInputStream fis = new FileInputStream(new File(getCacheDir(), "bieres.json"));
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                json = new String(buffer, "UTF-8");
                Log.d("t","loadJSON");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                JSONArray jArray = new JSONArray(loadJSON());
                ((BiersAdapter) rv.getAdapter()).setNewBier(jArray);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

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
        rv = (RecyclerView) findViewById(R.id.rv_bier);
        rv.setAdapter(new BiersAdapter(new JSONArray()));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);
        GetBiersServices.startActionBiers(this);
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
                switchActivities();
                break;
            case R.id.geolocMenu:
                mapMenuClicked();
                break;
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
    public void switchActivities(){
        Intent myIntent = new Intent(this, SecondaryActivity.class);
        startActivity(myIntent);
    }
    public void mapMenuClicked(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Londres")));
    }
}

