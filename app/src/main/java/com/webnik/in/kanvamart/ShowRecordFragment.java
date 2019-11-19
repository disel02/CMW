package com.webnik.in.kanvamart;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass;
import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass2;
import com.webnik.in.kanvamart.ListAdapter.subjects;
import com.webnik.in.kanvamart.Request.DeleteRecordRequest;
import com.webnik.in.kanvamart.Request.ListRequest;
import com.webnik.in.kanvamart.Request.ListRequest2;
import com.webnik.in.kanvamart.Request.TotalRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ShowRecordFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    DatePickerDialog datePickerDialog;
    String date2,date3,username,d_date,m_date,y_date;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView tvdate,tvdebit,tvcredit,tvavailable,btncredit,btndebit;
    Button btnchange;
    ListView lvdebit,lvcredit;
    String dd,mm,date_mm;
    Boolean monthlist=false;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_record,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = prefs.edit();
        username = prefs.getString("userkey", "name");
        date2 = prefs.getString("date", "0");

        monthlist= prefs.getBoolean("monthlist", false);

        editor.putBoolean("exit", true);
        editor.commit();

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);

        tvdate=(TextView)view.findViewById(R.id.tvdate);
        btncredit=(TextView)view.findViewById(R.id.btncredit);
        btndebit=(TextView)view.findViewById(R.id.btndebit);
        tvdebit=(TextView)view.findViewById(R.id.tvdebit);
        tvcredit=(TextView)view.findViewById(R.id.tvcredit);
        tvavailable=(TextView)view.findViewById(R.id.tvavailable);
        btnchange=(Button)view.findViewById(R.id.btnchange);
        lvdebit=(ListView)view.findViewById(R.id.lvdebit);
        lvcredit=(ListView)view.findViewById(R.id.lvcredit);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date3=dateFormat.format(new Date());

        if (monthlist)
        {

            date_mm= getArguments().getString("date");
            date2=date_mm;
            editor.putBoolean("monthlist", false);
            editor.commit();

            if (date2.equals(date3))
                tvdate.setText("Today");
            else {
                d_date = date2.substring(8, 10);
                m_date = date2.substring(5, 7);
                y_date = date2.substring(0, 4);
                tvdate.setText(d_date + " " + MONTHS[Integer.parseInt(m_date)-1] + " " + y_date);
            }
        }else {
            if (date2.equals(date3))
                tvdate.setText("Today");
            else {
                d_date = date3.substring(8, 10);
                m_date = date3.substring(5, 7);
                y_date = date3.substring(0, 4);
                tvdate.setText(d_date + " " + MONTHS[Integer.parseInt(m_date) - 1] + " " + y_date);
            }
        }

        btncredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("type", "1");
                fragment= new AddRecordFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btndebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("type", "2");
                fragment= new AddRecordFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

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
                                date2=year + "-" + mm + "-" + dd;
                                if (date2.equals(date3))
                                    tvdate.setText("Today");
                                else
                                    tvdate.setText(day + " " + MONTHS[month] + " " + year);
                                editor.putString("date", date2);
                                editor.commit();
                                progressBarSubject.setVisibility(View.VISIBLE);
                                showrecord();
                                showRecordList();
                                showRecordList2();
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
            showRecordList2();
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
        }

        lvdebit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int price = Integer.parseInt(((TextView) view.findViewById(R.id.tvvalue)).getText().toString());
                String reason = ((TextView) view.findViewById(R.id.tvtext)).getText().toString();
                Bundle bundle=new Bundle();
                bundle.putInt("price", price);
                bundle.putString("reason", reason);
                bundle.putString("status", "debit");
                fragment= new EditRecordFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        lvcredit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int price = Integer.parseInt(((TextView) view.findViewById(R.id.tvvalue)).getText().toString());
                String reason = ((TextView) view.findViewById(R.id.tvtext)).getText().toString();
                Bundle bundle=new Bundle();
                bundle.putInt("price", price);
                bundle.putString("reason", reason);
                bundle.putString("status", "credit");
                fragment= new EditRecordFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("type", "0");
                fragment= new AddRecordFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
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
                            progressBarSubject.setVisibility(View.GONE);
                            String debit = jsonResponse.getString("debit");
                            String credit = jsonResponse.getString("credit");
                            String available=jsonResponse.getString("available");
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
            TotalRequest totalRequest = new TotalRequest(date2,username, responseListner);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(totalRequest);
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

                        subjects.debit_value = jsonObject.getString("debit");
                        subjects.debit_res = jsonObject.getString("debit_res");

                        subjectsList.add(subjects);
                    }

                 //   progressBarSubject.setVisibility(View.GONE);
                    lvdebit.setVisibility(View.VISIBLE);

                    if(subjectsList != null)
                    {
                        ListAdapterClass adapter = new ListAdapterClass(subjectsList, context);

                        lvdebit.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                 //   progressBarSubject.setVisibility(View.GONE);
                    e1.printStackTrace();
                    lvdebit.setVisibility(View.GONE);
                }
            }
        };
        ListRequest listRequest= new ListRequest(date2,username,responseListner);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(listRequest);
    }

    public void showRecordList2()
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

                        subjects.credit_value = jsonObject.getString("credit");
                        subjects.credit_res = jsonObject.getString("credit_res");

                        subjectsList.add(subjects);
                    }

                  //  progressBarSubject.setVisibility(View.GONE);
                    lvcredit.setVisibility(View.VISIBLE);

                    if(subjectsList != null)
                    {
                        ListAdapterClass2 adapter = new ListAdapterClass2(subjectsList, context);

                        lvcredit.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                 //   progressBarSubject.setVisibility(View.GONE);
                    e1.printStackTrace();
                    lvcredit.setVisibility(View.GONE);
                }
            }
        };
        ListRequest2 listRequest2= new ListRequest2(date2,username,responseListner);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(listRequest2);
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