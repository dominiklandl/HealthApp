package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PulsActivity extends AppCompatActivity {

    TextView messwert;
    TextView messwertspo2;
    Button startMessung;
    Button speichern;
    BluetoothLeActivity bluetoothLeActivity;
    private int puls;
    private int spo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puls);
        messwert = (TextView) findViewById(R.id.tv_messwert);
        messwertspo2 = (TextView) findViewById(R.id.tv_messwertspo2);
        startMessung = (Button) findViewById(R.id.btn_startMessung);
        speichern = (Button) findViewById(R.id.btn_speichern);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        startMessung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences shpRead = getSharedPreferences("SensorDataSharePref", Context.MODE_PRIVATE);
                puls = shpRead.getInt("puls",0);
                messwert.setText(String.valueOf(puls));
                spo2 = shpRead.getInt("spo2",0);
                messwertspo2.setText(String.valueOf(spo2));
            }
        });
        speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                String p = messwert.getText().toString();
                String s = messwertspo2.getText().toString();
                int p2 = Integer.valueOf(p);
                int s2 = Integer.valueOf(s);
                int i = kalender.get(Calendar.DAY_OF_MONTH);
                SharedPreferences shpData = getSharedPreferences("DataSaveCalender",Context.MODE_PRIVATE);
                SharedPreferences.Editor dataEdit = shpData.edit();
                String indexP = "puls"+String.valueOf(i);
                String indexS = "spo2"+String.valueOf(i);
                dataEdit.putInt(indexP,p2);
                dataEdit.putInt(indexS,s2);
                dataEdit.commit();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}