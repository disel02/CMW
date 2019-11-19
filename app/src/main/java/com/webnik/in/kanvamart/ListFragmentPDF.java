package com.webnik.in.kanvamart;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.webnik.in.kanvamart.ListAdapter.ListAdapterClass4;
import com.webnik.in.kanvamart.ListAdapter.subjects;

import java.util.List;

public class ListFragmentPDF extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment;
    FloatingActionButton fab;
    ListView lsttask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_pdf, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        fab = view.findViewById(R.id.fab);
        lsttask = (ListView) view.findViewById(R.id.lsttask);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new AddFragmentPDF();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

    }


}

