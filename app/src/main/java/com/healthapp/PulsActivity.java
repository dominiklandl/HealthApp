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
    Button startMessung;
    Button speichern;
    BluetoothLeActivity bluetoothLeActivity;
    private int puls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puls);
        messwert = (TextView) findViewById(R.id.tv_messwert);
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
            }
        });
        speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                String s = messwert.getText().toString();
                int p = Integer.valueOf(s);
                int i = kalender.get(Calendar.DAY_OF_MONTH);
                SharedPreferences shpData = getSharedPreferences("DataSaveCalender",Context.MODE_PRIVATE);
                SharedPreferences.Editor dataEdit = shpData.edit();
                String index = "puls"+String.valueOf(i);
                dataEdit.putInt(index,p);
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