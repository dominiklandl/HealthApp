package com.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BlutdruckActivity extends AppCompatActivity {


    EditText Blutdrucks;
    EditText Blutdruckd;
    TextView Textfieldblutdrucks;
    TextView Textfieldblutdruckd;
    TextView Warning;

    Button button;
    int bluts;
    int blutd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blutdruck);
        Textfieldblutdrucks = (TextView) findViewById(R.id.Textfieldblutdrucks);
        Textfieldblutdruckd = (TextView) findViewById(R.id.Textfieldblutdruckd);
        Blutdrucks = (EditText) findViewById(R.id.Blutdrucks);
        Blutdruckd = (EditText) findViewById(R.id.Blutdruckd);
        Warning= (TextView) findViewById(R.id.Warning);
    }

    public void updateText(View view){
        button = (Button) findViewById(R.id.button);
        button.setText("erneut Bestätigen");
        if(Warning.getVisibility()==View.VISIBLE)
            Warning.setVisibility(View.INVISIBLE);
        if (Blutdrucks.getText().toString().isEmpty())
            Textfieldblutdrucks.setText("Ungülige Eingabe, geben Sie einen Wert ein");
        else {
            bluts = Integer.parseInt(Blutdrucks.getText().toString());
            if (bluts >= 400 || bluts <= 10)
                Textfieldblutdrucks.setText("Ungülige Eingabe, geben Sie einen neuen Wert ein");
            else if (bluts >= 140 || bluts <= 85) {
                Textfieldblutdrucks.setText("Ihr systolischer Blutdruck beträgt " + Blutdrucks.getText());
                Warning.setVisibility(View.VISIBLE);
            } else
                Textfieldblutdrucks.setText("Ihr systolischer Blutdruck beträgt " + Blutdrucks.getText());
        }

        if (Blutdruckd.getText().toString().isEmpty())
            Textfieldblutdruckd.setText("Ungülige Eingabe, geben Sie einen Wert ein");
        else {
            blutd = Integer.parseInt(Blutdruckd.getText().toString());
            if (blutd >= 400 || blutd <= 10)
                Textfieldblutdruckd.setText("Ungülige Eingabe, geben Sie einen neuen Wert ein");
            else if (blutd >= 90 || blutd <= 55) {
                Textfieldblutdruckd.setText("Ihr diastolischer Blutdruck beträgt " + Blutdruckd.getText());
                Warning.setVisibility(View.VISIBLE);
            } else
                Textfieldblutdruckd.setText("Ihr diastolischer Blutdruck beträgt " + Blutdruckd.getText());
        }

    }
}