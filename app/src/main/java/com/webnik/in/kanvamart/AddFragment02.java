package com.webnik.in.kanvamart;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFragment02 extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment;
    DbHelper02 dbHelper;
    EditText ettitle,etnote;
    Button btndate,btntime,btnsubmit,btndelete,btnedit;
    String title,note,date,time,title2,date2,time2,dbTime,dbDate,dbTime2;
    Boolean textclick;
    LinearLayout llhide;
    Calendar calendar,calendar2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add02,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
        textclick = prefs.getBoolean("textclick", false);

        dbHelper=new DbHelper02(getActivity());
        ettitle=(EditText)view.findViewById(R.id.ettitle);
        etnote=(EditText)view.findViewById(R.id.etnote);
        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        llhide=(LinearLayout)view.findViewById(R.id.llhide);
        btndelete=(Button)view.findViewById(R.id.btndelete);
        btnedit=(Button)view.findViewById(R.id.btnedit);

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
/*
        if (textclick) {
            llhide.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            task2= getArguments().getString("taskkey");
            date2 = getArguments().getString("datekey");
            time2 = getArguments().getString("timekey");
         //   ettask.setText(task2);
         //   etdate.setText(date2);
         //   ettime.setText(time2);
        }
*/
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deletetask(title);
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
                title=ettitle.getText().toString();
                note=etnote.getText().toString();
                try {
                    dbHelper.updatetask(title, dbDate, dbTime, title, date2, time2,note);
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
                fragment = new ListFragment02();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=ettitle.getText().toString();
                note=etnote.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dbDate=dateFormat.format(new Date());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
              //  dbTime2=timeFormat.format(calendar.getTime());
              //  dbTime=dbTime2.substring(0,4);
                dbTime="21:20";
                Toast.makeText(getActivity(),title+" "+note+" "+dbDate+" "+dbTime, Toast.LENGTH_SHORT).show();
                try {
                    dbHelper.insertNewTask(title,dbDate,dbTime,note);
                    Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Tflag", true);
                    editor.apply();
                    final FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction t = fragmentManager.beginTransaction();
                    fragment = new ListFragment02();
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
