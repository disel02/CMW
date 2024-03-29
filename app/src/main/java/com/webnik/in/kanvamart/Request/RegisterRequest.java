package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RegisterRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://disel.site/kanvamart/register.php";
    private Map<String,String> params;

    public RegisterRequest(String username, String password, String master, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params.put("master",master);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
