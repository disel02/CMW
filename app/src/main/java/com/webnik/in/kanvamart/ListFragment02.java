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

public class ListFragment02 extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    DbHelper02 dbHelper;
    Fragment fragment;
    FloatingActionButton fab;
    ListView lsttask;
    List<subjects> subjectsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list02, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();

        fab = view.findViewById(R.id.fab);
        dbHelper = new DbHelper02(getActivity());
        lsttask = (ListView) view.findViewById(R.id.lsttask);

        loadTaskList();

        lsttask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lvtask = ((TextView) view.findViewById(R.id.tvtitle)).getText().toString();
                String lvdate = ((TextView) view.findViewById(R.id.tvdate)).getText().toString();
                String lvtime = ((TextView) view.findViewById(R.id.tvtime)).getText().toString();

                editor.putBoolean("textclick", true);
                editor.apply();

                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new AddFragment();
                Bundle args = new Bundle();
                args.putString("taskkey", lvtask);
                args.putString("datekey", lvdate);
                args.putString("timekey", lvtime);
                fragment.setArguments(args);
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("textclick", false);
                editor.apply();
                final FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                fragment = new AddFragment02();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

    }

    private void loadTaskList() {
        subjectsList=dbHelper.getTodos();
        Context context = getActivity();
        if (subjectsList != null) {
            ListAdapterClass4 adapter = new ListAdapterClass4(subjectsList, context);
            lsttask.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
    }
}

