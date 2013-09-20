package org.misty.rc.VolleySample;

import android.app.Activity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;


public class VolleySampleActivity extends Activity {

    RequestQueue _queue;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GsonRequest<User> testRequest = new GsonRequest<User>(
                Request.Method.GET,
                "",
                User.class,
                responseListener,
                errorListener
        );

        _queue = Volley.newRequestQueue(this);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                "",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });

        _queue.add(request);

    }

    private Response.Listener responseListener = new Response.Listener() {
        @Override
        public void onResponse(Object o) {

        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

}
