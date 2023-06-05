package com.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.healthapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        setTitle("Home");

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    getSupportActionBar().setTitle("Home");
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    getSupportActionBar().setTitle("Profile");
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    getSupportActionBar().setTitle("Settings");
                    break;

            }


            return true;
        });


    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();


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
