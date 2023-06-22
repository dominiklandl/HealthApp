package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DateClickedActivity extends AppCompatActivity {

    private TextView thedate;
    private TextView puls;
    private TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_clicked);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        thedate = (TextView) findViewById(R.id.date);
        puls = (TextView) findViewById(R.id.tv_pulsdata);
        temp = (TextView) findViewById(R.id.tv_tempdata);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        thedate.setText(date);

        String[] arrSplit = date.split("/");
        String indexPuls = "puls"+arrSplit[2];
        String indexTemp = "temp"+arrSplit[2];

        SharedPreferences shpRead = getSharedPreferences("DataSaveCalender", Context.MODE_PRIVATE);
        int tempValue = shpRead.getInt(indexTemp,0);
        int pulsValue = shpRead.getInt(indexPuls,0);

        puls.setText(String.valueOf(pulsValue));
        temp.setText(String.valueOf(tempValue));

        actionBar.setTitle(date);
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