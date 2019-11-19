package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class DeletePDFRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/kanvamart/deletePDF.php";
    private Map<String,String> params;

    public DeletePDFRequest(String username, String task, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("username",username);
        params.put("task",task);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
