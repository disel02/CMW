package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ListRequest3 extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://disel.site/kanvamart/listview3.php";
    private Map<String,String> params;

    public ListRequest3(String date, String username, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("date",date);
        params.put("username",username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
