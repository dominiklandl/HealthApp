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

public class TemperaturActivity extends AppCompatActivity {

    TextView tempertaur;
    Button messungStarten;
    Button messwertSpeichern;
    BluetoothLeActivity bluetoothLeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatur);
        tempertaur = (TextView) findViewById(R.id.tv_temperatur);
        messungStarten = (Button) findViewById(R.id.btn_Messwert);
        messwertSpeichern = (Button) findViewById(R.id.btn_saveData);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        messwertSpeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kalender = Calendar.getInstance();
                String s = tempertaur.getText().toString();
                int t = Integer.valueOf(s);
                int i = kalender.get(Calendar.DAY_OF_MONTH);
                SharedPreferences shpData = getSharedPreferences("DataSaveCalender",Context.MODE_PRIVATE);
                SharedPreferences.Editor dataEdit = shpData.edit();
                String index = "temp"+String.valueOf(i);
                dataEdit.putInt(index,t);
                dataEdit.commit();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
        messungStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences shpRead = getSharedPreferences("SensorDataSharePref", Context.MODE_PRIVATE);
                int temp = shpRead.getInt("temp",0);
                tempertaur.setText(String.valueOf(temp));
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