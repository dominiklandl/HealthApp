package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TemperaturActivity extends AppCompatActivity {

    TextView tempertaur;
    Button messungStarten;
    Button messwertSpeichern;
    BluetoothLeService bluetoothLeService;

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

            }
        });
        messungStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data = bluetoothLeService.getCharacteristics();
                int temp = ((int) data[0]);
                tempertaur.setText(temp);
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