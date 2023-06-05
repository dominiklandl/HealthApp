package com.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DateClickedActivity extends AppCompatActivity {

    private TextView thedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_clicked);

        thedate = (TextView) findViewById(R.id.date);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        thedate.setText(date);


    }
}