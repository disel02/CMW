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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.webnik.in.kanvamart.Request.ListRequest3;
import com.webnik.in.kanvamart.Request.TotalRequest2;
import com.webnik.in.kanvamart.Request.UserDeleteRequest;
import com.webnik.in.kanvamart.Request.UserViewRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserViewFragment extends Fragment {

    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    TextView tvusername,tvpassword,tvaddress,tvblood,tvphone;
    Button btndelete;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_user,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);

        tvusername=(TextView)view.findViewById(R.id.tvusername);
        tvpassword=(TextView)view.findViewById(R.id.tvpassword);
        tvphone=(TextView)view.findViewById(R.id.tvphone);
        tvaddress=(TextView)view.findViewById(R.id.tvaddress);
        tvblood=(TextView)view.findViewById(R.id.tvblood);
        btndelete=(Button)view.findViewById(R.id.btndelete);

        username= getArguments().getString("username");

        if (isNetworkAvailable()) {
            showrecord();
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
        }

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
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
                            String username2 = jsonResponse.getString("username");
                            String password = jsonResponse.getString("password");
                            String phone=jsonResponse.getString("phone");
                            String address=jsonResponse.getString("address");
                            String blood=jsonResponse.getString("blood");
                            tvusername.setText(username2);
                            tvpassword.setText(password);
                            tvphone.setText(phone);
                            tvaddress.setText(address);
                            tvblood.setText(blood);
                        } else {
                            progressBarSubject.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        progressBarSubject.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            };
            UserViewRequest userViewRequest = new UserViewRequest(username, responseListner);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(userViewRequest);
    }

    public void deleteUser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you want to delete ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Response.Listener<String> responseListner = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        progressBarSubject.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                                        fragment= new UserListFragment();
                                        ft.replace(R.id.screen_area,fragment,"Fragment One");
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    } else {
                                        progressBarSubject.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    progressBarSubject.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        };
                        UserDeleteRequest userDeleteRequest = new UserDeleteRequest(username, responseListner);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(userDeleteRequest);
                    }})
                .create()
                .show();
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