package com.webnik.in.kanvamart;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.webnik.in.kanvamart.Request.AddPDFRequest;
import com.webnik.in.kanvamart.Request.DeletePDFRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddFragmentPDF extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Fragment fragment;
    ProgressBar progressBarSubject;
    EditText ettask;
    Button btnsubmit,btndelete,btnedit;
    String task,task2,username,filenamenew,filename="",filename2,file_path,content_type;
    Boolean textclick;
    LinearLayout llhide;
    int RESULT_OK=0;
    File f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_pdf,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
        textclick = prefs.getBoolean("textclick", false);
        username = prefs.getString("userkey", "name");
        progressBarSubject = (ProgressBar)view.findViewById(R.id.progressBar);

        ettask=(EditText)view.findViewById(R.id.ettask);

        btnsubmit=(Button)view.findViewById(R.id.btnsubmit);
        llhide=(LinearLayout)view.findViewById(R.id.llhide);
        btndelete=(Button)view.findViewById(R.id.btndelete);
        btnedit=(Button)view.findViewById(R.id.btnedit);

        if (textclick) {
            llhide.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            task2= getArguments().getString("taskkey");
            ettask.setText(task2);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure want to delete ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressBarSubject.setVisibility(View.VISIBLE);

                                final Response.Listener<String> responseListner = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            Boolean success = jsonResponse.getBoolean("success");
                                            if(success){
                                                progressBarSubject.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(), "Delete successfully", Toast.LENGTH_SHORT).show();
                                            }else{
                                                progressBarSubject.setVisibility(View.GONE);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage("Failed")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            progressBarSubject.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "pdf unavailable", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                DeletePDFRequest deletePDFRequest= new DeletePDFRequest(username,task2,responseListner);
                                RequestQueue queue= Volley.newRequestQueue(getActivity());
                                queue.add(deletePDFRequest);
                            }})
                        .create()
                        .show();





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
                task=ettask.getText().toString();
                try {
                    // later

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
                fragment = new ListFragment();
                t.replace(R.id.screen_area, fragment, "fragment three");
                t.addToBackStack(null);
                t.commit();
            }
        });

        enable_button();

    }

    private void enable_button(){
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task=ettask.getText().toString();
                try {
                    //
                    if (task.equals("")) {
                        Toast.makeText(getActivity(), "fill fields !", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        filenamenew=username+":"+task;
                        if (isNetworkAvailable()) {
                            new MaterialFilePicker()
                                    .withActivity(getActivity())
                                    .withRequestCode(10)
                                    .withFilter(Pattern.compile(".*\\.pdf$"))
                                    .start();
                        } else
                            Toast.makeText(getActivity(), "you are offline !", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), " "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addtodatabase(){

        progressBarSubject.setVisibility(View.VISIBLE);
        final com.android.volley.Response.Listener<String> responseListner = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        progressBarSubject.setVisibility(View.GONE);
                        ettask.setText("");
                        //    Toast.makeText(Add_topic.this, "uploaded", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "something wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        AddPDFRequest addPDFRequest = new AddPDFRequest(username,filenamenew, responseListner);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(addPDFRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100&&(grantResults[0]==PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    ProgressDialog progressDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("uploading");
            progressDialog.show();

            f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            //    content_type=getMimeType(f.getPath());

            content_type=".pdf";

            // Http
            file_path=f.getAbsolutePath();
            //    filename=file_path.substring(file_path.lastIndexOf("/")+1); //file name
            filename2=username+"@"+task;

            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {

                    OkHttpClient client=new OkHttpClient();
                    RequestBody file_body=RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody requestBody=new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",filename2,file_body)
                            .build();

                    Request request=new Request.Builder()
                            .url("http://disel.site/kanvamart/upload.php")
                            .post(requestBody)
                            .build();

                    try {

                        okhttp3.Response response=client.newCall(request).execute();
                        if (!response.isSuccessful()){
                            progressDialog.dismiss();
                            throw new IOException("Error: "+response);
                        }
                        else {
                            progressDialog.dismiss();
                            //      Toast.makeText(Add_topic.this, "uploaded", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }
            });
            t.start();
            addtodatabase();
            //    Toast.makeText(this, "file uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
