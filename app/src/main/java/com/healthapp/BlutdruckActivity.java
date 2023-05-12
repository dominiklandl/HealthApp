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

    Button button;


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
        button = (Button) findViewById(R.id.button);
        button.setText("erneut Bestätigen");
        if (Blutdrucks.getText().toString().isEmpty())
            Textfieldblutdrucks.setText("Ungülige Eingabe, geben Sie einen Wert ein");
        else
      Textfieldblutdrucks.setText("Ihr systolischer Blutdruck beträgt "+Blutdrucks.getText());
        if (Blutdruckd.getText().toString().isEmpty())
            Textfieldblutdruckd.setText("Ungülige Eingabe, geben Sie einen Wert ein");
        else
      Textfieldblutdruckd.setText("Ihr diastolischer Blutdruck beträgt "+Blutdruckd.getText());

    }
}