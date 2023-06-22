package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PulsActivity extends AppCompatActivity {

    TextView messwert;
    Button startMessung;
    Button speichern;
    BluetoothLeActivity bluetoothLeActivity;
    private int puls = 10;

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
                messwert.setText(String.valueOf(puls));
            }
        });
        speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public void setPuls(int puls) {
        this.puls = puls;
    }
}