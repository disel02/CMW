package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ChpassRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/kanvamart/chpass.php";
    private Map<String,String> params;

    public ChpassRequest(String username, String oldpass, String new1pass, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("username",username);
        params.put("oldpass",oldpass);
        params.put("new1pass",new1pass);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
