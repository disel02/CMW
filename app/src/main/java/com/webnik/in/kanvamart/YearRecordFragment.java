package com.webnik.in.kanvamart;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass3;
import com.webnik.in.kanvamart.ListAdapter.subjects;
import com.webnik.in.kanvamart.Request.ListRequest4;
import com.webnik.in.kanvamart.Request.TotalRequest2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class YearRecordFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    DatePickerDialog datePickerDialog;
    String date2,username,fdate="";
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView tvdate,tvdebit,tvcredit,tvavailable;
    Button btnchange;
    ListView lvmonthlist;
    String dd,mm;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year_record,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = prefs.edit();
        username = prefs.getString("userkey", "name");

        editor.putBoolean("exit", false);
        editor.commit();

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);

        tvdate=(TextView)view.findViewById(R.id.tvdate);
        tvdebit=(TextView)view.findViewById(R.id.tvdebit);
        tvcredit=(TextView)view.findViewById(R.id.tvcredit);
        tvavailable=(TextView)view.findViewById(R.id.tvavailable);
        btnchange=(Button)view.findViewById(R.id.btnchange);
        lvmonthlist=(ListView)view.findViewById(R.id.lvmonthlist);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date2=dateFormat.format(new Date());
        fdate=date2.substring(0,4)+"%";
        tvdate.setText(date2.substring(0,4));

        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String yr= String.valueOf(year);
                                tvdate.setText(yr);
                                if (day<10)
                                {
                                    dd="0"+day;
                                }
                                else
                                {
                                    dd=""+day;
                                }
                                if (month<9)
                                {
                                    mm="0"+(month+1);
                                }
                                else
                                {
                                    mm=""+(month+1);
                                }
                                fdate=year+"%";
                                progressBarSubject.setVisibility(View.VISIBLE);
                                showrecord();
                                showRecordList();
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //----------------------------------------------------

        if (isNetworkAvailable()) {
            showrecord();
            showRecordList();
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
        }

    }

    public void showrecord()
    {
            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            String debit = jsonResponse.getString("debit");
                            String credit = jsonResponse.getString("credit");
                            String available=jsonResponse.getString("total");
                            tvdebit.setText(debit);
                            tvcredit.setText(credit);
                            tvavailable.setText(available);
                        } else {
                            progressBarSubject.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        progressBarSubject.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            };
            TotalRequest2 totalRequest2 = new TotalRequest2(fdate,username, responseListner);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(totalRequest2);
    }

    public void showRecordList()
    {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                List<subjects> subjectsList;
                Context context = getActivity();
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject;

                    subjects subjects;

                    subjectsList = new ArrayList<subjects>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        subjects = new subjects();

                            jsonObject = jsonArray.getJSONObject(i);
                            String day=jsonObject.getString("date");
                                subjects.day = MONTHS[Integer.parseInt(day.substring(5,7))-1];
                                subjects.debit = jsonObject.getString("debit");
                                subjects.credit = jsonObject.getString("credit");
                                subjectsList.add(subjects);
                    }

                    progressBarSubject.setVisibility(View.GONE);
                    lvmonthlist.setVisibility(View.VISIBLE);

                    if(subjectsList != null)
                    {
                        ListAdapterClass3 adapter = new ListAdapterClass3(subjectsList, context);

                        lvmonthlist.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    progressBarSubject.setVisibility(View.GONE);
                    e1.printStackTrace();
                    Toast.makeText(context, ""+e1, Toast.LENGTH_SHORT).show();
                    lvmonthlist.setVisibility(View.GONE);
                }
            }
        };
        ListRequest4 listRequest4= new ListRequest4(fdate,username,responseListner);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(listRequest4);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


}