package org.misty.rc.VolleySample;

import com.android.volley.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/17
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class GsonRequest<T> extends Request<T> {

    private final Response.Listener<T> _listener;
    private final Class<T> _clazz;

    public GsonRequest(
            int method,
            String url,
            Class<T> clazz,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {

        super(method, url, errorListener);

        _clazz = clazz;
        _listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        return null;
    }

    @Override
    protected void deliverResponse(T t) {
        _listener.onResponse(t);
    }
}
