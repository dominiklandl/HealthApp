package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DateClickedActivity extends AppCompatActivity {

    private TextView thedate;
    private TextView puls;
    private TextView temp;
    private TextView syst;
    private TextView diast;
    private TextView warning1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_clicked);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        thedate = (TextView) findViewById(R.id.date);
        puls = (TextView) findViewById(R.id.tv_pulsdata);
        temp = (TextView) findViewById(R.id.tv_tempdata);
        syst = (TextView) findViewById(R.id.tv_syst);
        diast = (TextView) findViewById(R.id.tv_diast);
        warning1 = (TextView) findViewById(R.id.achtung);


        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        thedate.setText(date);

        String[] arrSplit = date.split("/");
        String indexPuls = "puls" + arrSplit[2];
        String indexTemp = "temp" + arrSplit[2];
        String indexsyst = "bluts" + arrSplit[2];
        String indexdiast = "blutd" + arrSplit[2];

        SharedPreferences shpRead = getSharedPreferences("DataSaveCalender", Context.MODE_PRIVATE);
        int tempValue = shpRead.getInt(indexTemp, 0);
        int pulsValue = shpRead.getInt(indexPuls, 0);
        int blutsValue = shpRead.getInt(indexsyst, 0);
        int blutdValue = shpRead.getInt(indexdiast, 0);

        puls.setText(String.valueOf(pulsValue) + " bpm");
        temp.setText(String.valueOf(tempValue) + " °C");
        syst.setText(String.valueOf(blutsValue) + " mmHg");
        diast.setText(String.valueOf(blutdValue) + " mmHg");


        String warnanzeige = "Achtung! Folgende Werte weichen von den Normwerten ab, ist dies über einen längeren Zeitraum der Fall, wird empfohlen einen Arzt aufzusuchen -> ";
        int a=0;
        SharedPreferences shRead = getSharedPreferences("UserDataSharedPref", Context.MODE_PRIVATE);
        String ageShared = shRead.getString("age", "");
        int alter= Integer.parseInt(ageShared);
        if(alter<=1) {
            if (pulsValue < 140 && pulsValue > 120 || pulsValue == 0) {
                warning1.setVisibility(View.INVISIBLE);
            } else {
                warning1.setText(warnanzeige + "Puls ");
                a = 1;
            }
        }else if(alter<=6) {
            if (pulsValue < 120 && pulsValue > 100 || pulsValue == 0) {
                warning1.setVisibility(View.INVISIBLE);
            } else {
                warning1.setText(warnanzeige + "Puls ");
                a = 1;
            }
        } else if(alter<=18) {
            if (pulsValue < 100 && pulsValue > 80 || pulsValue == 0) {
                warning1.setVisibility(View.INVISIBLE);
            } else {
                warning1.setText(warnanzeige + "Puls ");
                a = 1;
            }
        } else {
        if (pulsValue < 80 && pulsValue > 60 || pulsValue == 0) {
            warning1.setVisibility(View.INVISIBLE);
        } else {
            warning1.setText(warnanzeige + "Puls ");
            a=1;
        }
        }

        
        if (blutsValue > 85 && blutsValue < 140 || blutsValue == 0){
        }else {
            warning1.setVisibility(View.VISIBLE);
            if (a==1) warning1.setText(warning1.getText()+", Blutdruck ");
            else {
                warning1.setText(warnanzeige+"Blutdruck ");
                a=1;
            }
        }
        if (tempValue<37||tempValue==0) {
        } else {
            if (a==1) warning1.setText(warning1.getText()+", Temperatur");
            else {
                warning1.setVisibility(View.VISIBLE);
                warning1.setText(warnanzeige+"Temperatur");
            }
        }


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