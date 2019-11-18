package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class DeleteRecordRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/kanvamart/delete.php";
    private Map<String,String> params;

    public DeleteRecordRequest(String date, String username, String reason, int money, String method, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("date",date);
        params.put("username",username);
        params.put("reason",reason);
        params.put("money", String.valueOf(money));
        params.put("method",method);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
