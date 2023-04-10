package com.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
    }

    public void launchSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void launchPuls(View v){
        Intent i1 = new Intent(this, PulsActivity.class);
        startActivity(i1);
    }

    public void launchBlutdruck(View v){
        Intent i2 = new Intent(this, BlutdruckActivity.class);
        startActivity(i2);
    }

    public void launchTemp(View v){
        Intent i3 = new Intent(this, TemperaturActivity.class);
        startActivity(i3);
    }
}
