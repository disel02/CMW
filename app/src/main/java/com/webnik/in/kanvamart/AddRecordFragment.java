package com.webnik.in.kanvamart;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.Request.AddRecordRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AddRecordFragment extends Fragment implements View.OnClickListener{

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    String method="",reason="",date2,date3,d_date,m_date,y_date,username;
    int money=0;
    RadioGroup rgtype;
    Fragment fragment=null;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    Button btnadd,btnchange;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView tvdate;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String dd,mm;
    DatePickerDialog datePickerDialog;
    String type="0";
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_record,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs =getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = prefs.edit();
        username = prefs.getString("userkey", "0");
        date2 = prefs.getString("date", "0");

        type= getArguments().getString("type");

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        editor.putBoolean("exit", false);
        editor.commit();

        rgtype=(RadioGroup)view.findViewById(R.id.rgtype);
        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);
        final EditText etreason = (EditText)view.findViewById(R.id.etreason);
        final EditText etmoney = (EditText)view.findViewById(R.id.etmoney);
        btnadd=(Button)view.findViewById(R.id.btnadd);
        btnchange=(Button)view.findViewById(R.id.btnchange);
        tvdate=(TextView)view.findViewById(R.id.tvdate);
        view.findViewById(R.id.rbdebit).setOnClickListener(this);
        view.findViewById(R.id.rbcredit).setOnClickListener(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date3=dateFormat.format(new Date());
        if (date2.equals(date3))
            tvdate.setText("Today");
        else {
            d_date=date2.substring(8,10);
            m_date=date2.substring(5,7);
            y_date=date2.substring(0,4);
            tvdate.setText(d_date+" "+MONTHS[Integer.parseInt(m_date)-1]+" "+y_date);
        }

        if (!type.equals("0")) {
            rgtype.setVisibility(View.GONE);
            if (type.equals("1")) {
                method = "credit";
                btnadd.setText("Add  credit");
            } else
            {
                method="debit";
                btnadd.setText("Add  debit");
            }

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
                            //    tvdate.setText(day + " " + MONTHS[month] + " " + year);
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
                imm.hideSoftInputFromWindow(etreason.getWindowToken(), 0);

                reason = etreason.getText().toString();
                money = Integer.parseInt(etmoney.getText().toString());

                if (reason.equals("") || method.equals("") || money==0 )
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
                                            Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_SHORT).show();
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
                                    }
                                }
                            };
                            AddRecordRequest addRecordRequest = new AddRecordRequest(date2, username, reason, money, method, responseListner);
                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            queue.add(addRecordRequest);
                        } else {
                            Snackbar.make(view, "you are offline", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view)  {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbdebit:
                if (checked)
                    method="debit";
                break;
            case R.id.rbcredit:
                if (checked)
                    method="credit";
                break;
        }
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