package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class UpdateRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/kanvamart/update.php";
    private Map<String,String> params;

    public UpdateRequest(String phone, String address,String blood,String username, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("phone",phone);
        params.put("address",address);
        params.put("blood",blood);
        params.put("username",username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
