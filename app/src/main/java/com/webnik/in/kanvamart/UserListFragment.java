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
import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass3;
import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass5;
import com.webnik.in.kanvamart.ListAdapter.subjects;
import com.webnik.in.kanvamart.Request.ListRequest3;
import com.webnik.in.kanvamart.Request.ListRequest5;
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

public class UserListFragment extends Fragment {

    ListView lvusers;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    ProgressBar progressBarSubject;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager=getActivity().getSupportFragmentManager();
        ft=fragmentManager.beginTransaction();

        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);

        lvusers=(ListView)view.findViewById(R.id.lvusers);

        if (isNetworkAvailable()) {
            showRecordList();
        }
        else
        {
            progressBarSubject.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
        }

        lvusers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((TextView) view.findViewById(R.id.tvusername)).getText().toString();
                Bundle bundle=new Bundle();
                bundle.putString("username", username);
                fragment= new UserViewFragment();
                fragment.setArguments(bundle);
                ft.replace(R.id.screen_area,fragment,"Fragment One");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

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

                        subjects.sUsername=jsonObject.getString("username");

                        subjectsList.add(subjects);
                    }

                    progressBarSubject.setVisibility(View.GONE);

                    if(subjectsList != null)
                    {
                        ListAdapterClass5 adapter = new ListAdapterClass5(subjectsList, context);

                        lvusers.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    progressBarSubject.setVisibility(View.GONE);
                    e1.printStackTrace();
                    lvusers.setVisibility(View.GONE);
                }
            }
        };
        ListRequest5 listRequest5= new ListRequest5(responseListner);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(listRequest5);
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