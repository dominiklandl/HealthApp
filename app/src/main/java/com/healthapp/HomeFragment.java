package com.healthapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


    private CalendarView mCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date =  year + "/" + (month+1) + "/" + dayOfMonth;
                Log.wtf("wtf","onSelectedDayChange" + date);
                Intent intent = new Intent(getActivity(), DateClickedActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        TextView textView = view.findViewById(R.id.greetingMessage);
        TextView profilAnlegen = view.findViewById(R.id.profilAnlegenTV);
        ImageView pfeilIV = view.findViewById(R.id.downwardArrowIV);


        SharedPreferences shRead = getActivity().getSharedPreferences("UserDataSharedPref", Context.MODE_PRIVATE);
        String nameShared = shRead.getString("name", "");


        if (shRead.contains("name")){
            String text1 = "Hallo "+ nameShared +"!";
            Spannable spannable = new SpannableString(text1);
            spannable.setSpan(new ForegroundColorSpan(Color.rgb(163, 27, 27)), 6, (6+nameShared.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable, TextView.BufferType.SPANNABLE);
            profilAnlegen.setVisibility(View.INVISIBLE);
            pfeilIV.setVisibility(View.INVISIBLE);

        }else {
            textView.setText("Herzlich Wilkommen!");
            profilAnlegen.setVisibility(View.VISIBLE);
            pfeilIV.setVisibility(View.VISIBLE);
        }


        return view;
    }
}