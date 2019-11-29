package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ListRequest5 extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://disel.site/kanvamart/userlist.php";
    private Map<String,String> params;

    public ListRequest5( Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
