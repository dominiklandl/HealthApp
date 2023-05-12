package com.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BlutdruckActivity extends AppCompatActivity {


    EditText Blutdrucks;
    EditText Blutdruckd;
    TextView Textfieldblutdrucks;
    TextView Textfieldblutdruckd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blutdruck);
        Textfieldblutdrucks = (TextView) findViewById(R.id.Textfieldblutdrucks);
        Textfieldblutdruckd = (TextView) findViewById(R.id.Textfieldblutdruckd);
        Blutdrucks = (EditText) findViewById(R.id.Blutdrucks);
        Blutdruckd = (EditText) findViewById(R.id.Blutdruckd);
    }

    public void updateText(View view){
      Textfieldblutdrucks.setText("Ihr systolischer Blutdruck beträgt "+Blutdrucks.getText());
      Textfieldblutdruckd.setText("Ihr diastolischer Blutdruck beträgt "+Blutdruckd.getText());

    }
}