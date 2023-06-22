package com.healthapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        setProfile();


        Button button = view.findViewById(R.id.buttonApplyUsername);
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameText = view.findViewById(R.id.usernameInput);
                EditText ageText = view.findViewById(R.id.ageInput);
                SharedPreferences shWrite = getActivity().getSharedPreferences("UserDataSharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = shWrite.edit();

                String nameInput =  usernameText.getText().toString();
                String ageInput = ageText.getText().toString();

                if (nameInput.isEmpty() && ageInput.isEmpty()){
                    Toast.makeText(getActivity(),"Geben Sie bitte Name und Alter ein!", Toast.LENGTH_SHORT).show();
                }else if (nameInput.isEmpty()){
                    Toast.makeText(getActivity(),"Geben Sie bitte Ihren Namen ein!", Toast.LENGTH_SHORT).show();
                }else if (ageInput.isEmpty()){
                    Toast.makeText(getActivity(),"Geben Sie bitte Ihr Alter ein", Toast.LENGTH_SHORT).show();
                }else {
                    myEdit.putString("name", nameInput);
                    myEdit.putInt("age", Integer.parseInt(ageInput));
                    myEdit.commit();
                    usernameText.setText("");
                    ageText.setText("");
                }
                mgr.hideSoftInputFromWindow(ageText.getWindowToken(), 0);
                setProfile();
            }
        });


        Button buttonDelete = view.findViewById(R.id.buttonDeletePref);
        Button buttonApplyDelete = view.findViewById(R.id.buttonApplyDeletePrefs);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            buttonApplyDelete.setVisibility(View.VISIBLE);
            }
        });

        buttonApplyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePreferences();
                setProfile();
                buttonApplyDelete.setVisibility(View.INVISIBLE);
            }
        });





        return view;
    }

    public void setProfile(){
        TextView profilView = view.findViewById(R.id.profilTV);
        TextView nameView = view.findViewById(R.id.nameTV);
        TextView ageView = view.findViewById(R.id.ageTV);

        SharedPreferences shReadProfile = getActivity().getSharedPreferences("UserDataSharedPref", Context.MODE_PRIVATE);
        String nameSharedProfile = shReadProfile.getString("name", "");
        int ageSharedProfile = shReadProfile.getInt("age", 0);

        profilView.setText("Ihre Daten:");
        if (ageSharedProfile == 0){
        nameView.setText("Name: ");
        ageView.setText("Alter: " );
        }else {
            nameView.setText("Name:  " + nameSharedProfile);
            ageView.setText("Alter:  " + ageSharedProfile);
        }



    }

    public void deletePreferences(){
        SharedPreferences prefUser = getActivity().getSharedPreferences("UserDataSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser = prefUser.edit();
        editorUser.remove("name").apply();
        editorUser.remove("age").apply();

        SharedPreferences prefData = getContext().getSharedPreferences("DataSaveCalender",Context.MODE_PRIVATE);
        SharedPreferences.Editor editorData = prefData.edit();
        editorData.clear().apply();

    }

}