package org.esiea.pinchon_leborgne.appliandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SecondaryActivity extends AppCompatActivity {

    public static final String BIERS_UPDATE = "org.esiea.pinchon_leborgne.appliandroid.BIERS_UPDATE";
    RecyclerView rv;
    IntentFilter intentFilter;
    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder>{
        private JSONArray biers;
        public class BierHolder extends RecyclerView.ViewHolder{
            public TextView name;
            public BierHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_bier_element_name);
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
                Log.d("DEBUG-Adapter", name);
                holder.name.setText(name);
            }catch(Exception e) {
                e.fillInStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            Log.d("DEBUG-Adapter", "BiersLength:"+biers.length());
            return biers.length();
        }

        public void setNewBier(JSONArray biers){
            this.biers=biers;
            notifyDataSetChanged();
            Log.d("DEBUG-Adapter", this.biers.toString());
        }
    }
    public class BierUpdate extends BroadcastReceiver {
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
        setContentView(R.layout.activity_secondary);
        rv = (RecyclerView) findViewById(R.id.rv_bier);
        rv.setAdapter(new BiersAdapter(new JSONArray()));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);
        GetBiersServices.startActionBiers(this);
    }
}
