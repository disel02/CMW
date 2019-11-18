package com.webnik.in.kanvamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ProgressBar progressBarSubject;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String username,password,password2,phone,address,blood;
    EditText etusername,etpassword,etpassword2,etphone,etaddress;
    Button btnchoose,btnconfirm;
    ImageView ivbackpress,ivdone;
    TextView tvnote;
    private final int IMG_REQUEST=1;
    private Bitmap bitmap;
    private String UploadUrl="http://theextrastep.in/kanvamart/uploadimage.php";
    File f;
    Boolean flag=false;
    private Spinner spinner;
    private static final String[]paths = {"A +ve", "A -ve", "B +ve","B -ve","AB +ve","AB -ve", "O +ve", "O -ve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBarSubject = (ProgressBar) findViewById(R.id.progressBar);

        etusername = (EditText) findViewById(R.id.etusername);
         etpassword = (EditText) findViewById(R.id.etpassword);
         etpassword2 = (EditText) findViewById(R.id.etpassword2);
        etphone = (EditText) findViewById(R.id.etphone);
        etaddress = (EditText) findViewById(R.id.etaddress);
        btnconfirm= (Button)findViewById(R.id.btnconfirm);
        btnchoose=(Button)findViewById(R.id.btnchoose);
        tvnote=(TextView)findViewById(R.id.tvnote);

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        ivbackpress = (ImageView)findViewById(R.id.ivbackpress);
        ivdone = (ImageView)findViewById(R.id.ivdone);

        ivbackpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(RegisterActivity.this);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = etusername.getText().toString();
                password = etpassword.getText().toString();
                password2 = etpassword2.getText().toString();
                phone = etphone.getText().toString();
                address = etaddress.getText().toString();

                if (username.equals("") || password.equals("") || password2.equals("")|| phone.equals("")|| address.equals("")|| blood.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "fill all fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (password.equals(password2))
                    {
                            if (isNetworkAvailable()) {
                                progressBarSubject.setVisibility(View.VISIBLE);
                                uploadImage();
                            } else {
                                Snackbar.make(view, "you are offline", Snackbar.LENGTH_LONG).show();
                            }
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "Password not same !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                blood="A +ve";
                break;
            case 1:
                blood="A -ve";
                break;
            case 2:
                blood="B +ve";
                break;
            case 3:
                blood="B -ve";
                break;
            case 4:
                blood="AB +ve";
                break;
            case 5:
                blood="AB -ve";
                break;
            case 6:
                blood="O +ve";
                break;
            case 7:
                blood="O -ve";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void selectImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri path=data.getData();
            try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    flag=true;
                    ivdone.setVisibility(View.VISIBLE);
                    btnchoose.setVisibility(View.GONE);
                    tvnote.setVisibility(View.GONE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            Boolean Success=jsonObject.getBoolean("response");
                            progressBarSubject.setVisibility(View.GONE);
                           if (Success)
                           {
                               SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                               SharedPreferences.Editor editor = prefs.edit();
                               editor.putString("loginkey", "1");
                               editor.putString("userkey", username);
                               editor.putString("passkey", password);
                               editor.commit();
                               Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                               startActivity(intent);
                           }
                           else
                           {
                               Toast.makeText(RegisterActivity.this, "failed", Toast.LENGTH_SHORT).show();
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("phone",phone);
                params.put("address",address);
                params.put("blood",blood);
                params.put("image",imageToString(bitmap));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
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
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
