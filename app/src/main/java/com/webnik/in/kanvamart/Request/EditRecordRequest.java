package com.webnik.in.kanvamart.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class EditRecordRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL="http://theextrastep.in/kanvamart/try_edit.php";
    private Map<String,String> params;

    public EditRecordRequest(String date, String username, String reason,int money,String method, int money2, String reason2, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL ,listener, null);
        params=new HashMap<>();
        params.put("date",date);
        params.put("username",username);
        params.put("reason",reason);
        params.put("money", String.valueOf(money));
        params.put("method",method);
        params.put("money2", String.valueOf(money2));
        params.put("reason2",reason2);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
