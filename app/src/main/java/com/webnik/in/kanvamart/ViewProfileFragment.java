package com.webnik.in.kanvamart;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.Request.ProfileRequest;
import com.webnik.in.kanvamart.Request.UpdateRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProfileFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;
    EditText etphone,etaddress,etblood;
    Button btnconfirm;
    String phone,address,blood,username;
    ProgressBar progressBarSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_profile,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
        username = prefs.getString("userkey", "name");

        etphone = (EditText)view.findViewById(R.id.etphone);
        etaddress = (EditText) view.findViewById(R.id.etaddress);
        etblood = (EditText) view.findViewById(R.id.etblood);
        btnconfirm= (Button)view.findViewById(R.id.btnconfirm);
        progressBarSubject = (ProgressBar) view.findViewById(R.id.progressBar);

        ShowDetails();

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etphone.getText().toString();
                address = etaddress.getText().toString();
                blood = etblood.getText().toString();

                if ( phone.equals("")|| address.equals("")|| blood.equals(""))
                {
                    Toast.makeText(getActivity(), "fill all fields !", Toast.LENGTH_SHORT).show();
                }else {
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
                                                Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressBarSubject.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            progressBarSubject.setVisibility(View.GONE);
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), " "+e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                UpdateRequest updateRequest = new UpdateRequest(phone, address,blood,username, responseListner);
                                RequestQueue queue = Volley.newRequestQueue(getActivity());
                                queue.add(updateRequest);
                            } else {
                                Toast.makeText(getActivity(), "you are offline", Toast.LENGTH_SHORT).show();
                            }
                }
            }
        });

    }

    public void ShowDetails()
    {
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
                            etphone.setText(jsonResponse.getString("phone"));
                            etaddress.setText(jsonResponse.getString("address"));
                            etblood.setText(jsonResponse.getString("blood"));
                        } else {
                            progressBarSubject.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressBarSubject.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(getActivity(), " "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            };
            ProfileRequest profileRequest = new ProfileRequest(username, responseListner);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(profileRequest);
        } else {
            Toast.makeText(getActivity(), "you are offline", Toast.LENGTH_SHORT).show();
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