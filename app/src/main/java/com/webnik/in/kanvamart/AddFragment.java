package com.webnik.in.kanvamart;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddFragment extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment;
    DbHelper dbHelper;
    EditText ettask,etdate,ettime;
    Button btndate,btntime,btnsubmit,btndelete,btnedit;
    String task,date,time,task2,date2,time2,dbTime,dbDate;
    Boolean textclick;
    LinearLayout llhide;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar,calendar2;
    DatePickerDialog datePickerDialog;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
        textclick = prefs.getBoolean("textclick", false);

        dbHelper=new DbHelper(getActivity());
        ettask=(EditText)view.findViewById(R.id.ettask);
        etdate=(EditText)view.findViewById(R.id.etdate);
        ettime=(EditText)view.findViewById(R.id.ettime);
        btndate=(Button)view.findViewById(R.id.btndate);
        btntime=(Button)view.findViewById(R.id.btntime);
        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        llhide=(LinearLayout)view.findViewById(R.id.llhide);
        btndelete=(Button)view.findViewById(R.id.btndelete);
        btnedit=(Button)view.findViewById(R.id.btnedit);

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        if (textclick) {
            llhide.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            task2= getArguments().getString("taskkey");
            date2 = getArguments().getString("datekey");
            time2 = getArguments().getString("timekey");
            ettask.setText(task2);
            etdate.setText(date2);
            ettime.setText(time2);
        }

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dbDate=year+"-"+month+"-"+day;
                                date=day + " " + MONTHS[month] + " " + year;
                                etdate.setText(date);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dbTime=selectedHour+":"+selectedMinute+":"+"00";
                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >=13 && selectedHour < 24) {
                                selectedHour -= 12;
                            }
                            else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        if (selectedMinute < 10) {
                            mm_precede = "0";
                        }
                        time=selectedHour + ":" +mm_precede + selectedMinute+" "+AM_PM;
                        ettime.setText(time);
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deletetask(task2);
                Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                editor.putBoolean("Tflag", true);
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new ListFragment();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task=ettask.getText().toString();
                date=etdate.getText().toString();
                time=ettime.getText().toString();
                try {
                    dbHelper.updatetask(task, dbDate, dbTime, task2, date2, time2);
                    Toast.makeText(getActivity(), "update Successfully", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Tflag", true);
                    editor.apply();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), " "+e, Toast.LENGTH_SHORT).show();
                }
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new ListFragment();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task=ettask.getText().toString();
                date=etdate.getText().toString();
                time=ettime.getText().toString();
                try {
                    dbHelper.insertNewTask(task,dbDate,dbTime);
                    Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Tflag", true);
                    editor.apply();
                    final FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction t = fragmentManager.beginTransaction();
                    fragment = new ListFragment();
                    t.replace(R.id.screen_area, fragment, "fragment three");
                    t.addToBackStack(null);
                    t.commit();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), " "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
