package lyon.hanks.read.txt.volley.java.VolleyTool;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/*
 * https://bng86.gitbooks.io/android-third-party-/content/volley.html
 */
public abstract class VolleyTool {

    String TAG = VolleyTool.class.getSimpleName();
    private RequestQueue mQueue;


    public VolleyTool(Context context,String url){

        Log.d(TAG,"url:"+ url);
        mQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
//                Log.d(TAG,"Response:"+ response);
                Next();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d(TAG,"Error.Response:"+ error.toString());
                Next();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "mohammad_username");
                params.put("password", "mohammad_password");

                return params;
            }

        };

        mQueue.add(mStringRequest);
    }

    public abstract void Next();
}
