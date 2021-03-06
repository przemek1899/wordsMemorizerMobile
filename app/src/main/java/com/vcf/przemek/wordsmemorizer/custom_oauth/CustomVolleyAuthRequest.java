package com.vcf.przemek.wordsmemorizer.custom_oauth;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Przemek on 2017-07-01.
 */

public class CustomVolleyAuthRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private String credentials;

//    public CustomVolleyAuthRequest(String url, Map<String, String> params,
//                                   Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
//        super(Method.GET, url, errorListener);
//        this.listener = reponseListener;
//        this.params = params;
//    }

    public CustomVolleyAuthRequest(int method, String url, Map<String, String> params, String credentials,
                                   Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.credentials = credentials;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String auth = "Basic "
                + Base64.encodeToString(this.credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", auth);
        return headers;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}
