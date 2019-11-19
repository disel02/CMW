package com.webnik.in.kanvamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.webnik.in.kanvamart.Request.ChpassRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChpasswordActivity extends AppCompatActivity {

    ProgressBar progressBarSubject;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chpassword);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        final TextInputLayout oldpassWrapper = (TextInputLayout) findViewById(R.id.oldpassWrapper);
        final TextInputLayout new1Wrapper = (TextInputLayout) findViewById(R.id.new1passWrapper);
        final TextInputLayout new2Wrapper = (TextInputLayout) findViewById(R.id.new2passWrapper);

        final EditText etoldpass = (EditText) findViewById(R.id.etoldpass);
        final EditText etnew1pass = (EditText) findViewById(R.id.etnew1pass);
        final EditText etnew2pass = (EditText) findViewById(R.id.etnew2pass);
        final Button btnconfirm= (Button)findViewById(R.id.btnconfirm);

        oldpassWrapper.setHint("old password");
        new1Wrapper.setHint("new password");
        new2Wrapper.setHint("Retry-new password");

        final ImageView ivbackpress = (ImageView)findViewById(R.id.ivbackpress);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChpasswordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String oldpass = etoldpass.getText().toString();
                final String new1pass = etnew1pass.getText().toString();
                final String new2pass = etnew2pass.getText().toString();

                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String log = prefs.getString("passkey", "0");
                String username = prefs.getString("userkey", "name");

                if (oldpass.equals("") || new1pass.equals("") || new2pass.equals(""))
                {
                    Toast.makeText(ChpasswordActivity.this, "fill all fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (new1pass.equals(new2pass)) {

                        if (oldpass.equals(log)) {
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
                                                Toast.makeText(ChpasswordActivity.this, "changed successfully", Toast.LENGTH_SHORT).show();

                                            } else {
                                                progressBarSubject.setVisibility(View.GONE);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ChpasswordActivity.this);
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
                                ChpassRequest chpassRequest = new ChpassRequest(username ,oldpass, new1pass, responseListner);
                                RequestQueue queue = Volley.newRequestQueue(ChpasswordActivity.this);
                                queue.add(chpassRequest);
                            }
                            else
                            {
                                Snackbar.make(view, "you are offline", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                            Toast.makeText(ChpasswordActivity.this, "old password are wrong", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ChpasswordActivity.this, "new pass 1 & new pass2 not same !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChpasswordActivity.this,MainActivity.class);
        ChpasswordActivity.this.startActivity(intent);
        ChpasswordActivity.this.finish();
    }
}
