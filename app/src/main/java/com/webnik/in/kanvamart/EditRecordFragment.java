package com.webnik.in.kanvamart;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.Request.DeleteRecordRequest;
import com.webnik.in.kanvamart.Request.EditRecordRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class EditRecordFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    String method=null,reason,reason2,date2,date3,username,d_date,m_date,y_date;
    int money,money2;
    Fragment fragment=null;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    Button btnadd,btnchange,btndelete;
    EditText etmoney2,etreason2;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView tvdate;
    int year;
    int month;
    int dayOfMonth;
    RadioButton rbdebit,rbcredit;
    Calendar calendar;
    String dd,mm;
    DatePickerDialog datePickerDialog;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_record,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        SharedPreferences prefs =getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = prefs.edit();
        username = prefs.getString("userkey", "0");
        date2 = prefs.getString("date", "0");
        money= getArguments().getInt("price");
        reason=getArguments().getString("reason");
        method=getArguments().getString("status");

        editor.putBoolean("exit", false);
        editor.commit();

        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);
        etreason2 = (EditText)view.findViewById(R.id.etreason2);
        etmoney2 = (EditText)view.findViewById(R.id.etmoney2);
        btnadd=(Button)view.findViewById(R.id.btnadd);
        btndelete=(Button)view.findViewById(R.id.btndelete);
        btnchange=(Button)view.findViewById(R.id.btnchange);
        tvdate=(TextView)view.findViewById(R.id.tvdate);

        try {
            String setmoney= String.valueOf(money);
            etmoney2.setText(setmoney);
            etreason2.setText(reason);
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you want to delete ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBarSubject.setVisibility(View.VISIBLE);
                                    deleteRecord(money, reason, method);
                                }})
                            .create()
                            .show();
                }
                else
                    Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
            }
        });

        btnchange.setVisibility(View.GONE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date3=dateFormat.format(new Date());
        if (date2.equals(date3))
            tvdate.setText("Today");
        else
        {
            d_date=date2.substring(8,10);
            m_date=date2.substring(5,7);
            y_date=date2.substring(0,4);
            tvdate.setText(d_date+" "+MONTHS[Integer.parseInt(m_date)-1]+" "+y_date);
        }


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
                                tvdate.setText(day + " " + MONTHS[month] + " " + year);
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
                                date2=dd + "/" + mm + "/" + year;
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etreason2.getWindowToken(), 0);

                reason2 = etreason2.getText().toString();
                money2 = Integer.parseInt(etmoney2.getText().toString());

            //    Toast.makeText(getActivity(), date2+" "+username+" "+money+" "+reason+" "+money2+" "+reason2+" "+method, Toast.LENGTH_SHORT).show();

                if (reason2.equals("") || method.equals("") || money==0)
                {
                    Toast.makeText(getActivity(), "fill all fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (money>999999)
                    {
                        Toast.makeText(getActivity(), "Sorry! Amount Exceed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (isNetworkAvailable()) {
                            progressBarSubject.setVisibility(View.VISIBLE);

                            final Response.Listener<String> responseListner = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        Boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            progressBarSubject.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Edit successfully", Toast.LENGTH_SHORT).show();
                                            fragment = new ShowRecordFragment();
                                            ft.replace(R.id.screen_area, fragment, "Fragment One");
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        } else {
                                            progressBarSubject.setVisibility(View.GONE);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        progressBarSubject.setVisibility(View.GONE);
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            EditRecordRequest editRecordRequest = new EditRecordRequest(date2, username, reason, money, method, money2, reason2, responseListner);
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            queue.add(editRecordRequest);
                        } else {
                            Snackbar.make(view, "you are offline", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void deleteRecord(int money,String reason,String method)
    {
        final Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        progressBarSubject.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                        fragment= new ShowRecordFragment();
                        ft.replace(R.id.screen_area,fragment,"Fragment One");
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {
                        progressBarSubject.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBarSubject.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        };
        DeleteRecordRequest deleteRecordRequest = new DeleteRecordRequest(date2,username,reason,money,method, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(deleteRecordRequest);
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