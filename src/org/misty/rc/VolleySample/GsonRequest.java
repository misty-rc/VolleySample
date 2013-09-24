package org.misty.rc.VolleySample;

import com.android.volley.*;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

    private final Gson gson = new Gson();
    private final Class<T> _clazz;
    private final Map<String, String> _headers;
    private final Map<String, String> _params;
    private final Response.Listener<T> _listener;

    public static <T> GsonRequest GET(
            String url,
            Class<T> clazz,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {

        return new GsonRequest<T>(
                Method.GET,
                url,
                clazz,
                null,
                null,
                listener,
                errorListener
        );
    }

    public static <T> GsonRequest POST(
            String url,
            Class<T> clazz,
            Map<String, String> params,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {

        return new GsonRequest<T>(
                Method.POST,
                url,
                clazz,
                null,
                params,
                listener,
                errorListener
        );
    }


    public GsonRequest(
            int method,
            String url,
            Class<T> clazz,
            Map<String, String> headers,
            Map<String, String> params,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener
    ) {
        super(method, url, errorListener);
        this._clazz = clazz;
        this._headers = headers;
        this._params = params;
        this._listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return _headers != null ? _headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return _params != null ? _params : super.getParams();
    }

    @Override
    protected void deliverResponse(T t) {
        _listener.onResponse(t);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            return Response.success(gson.fromJson(json, _clazz), HttpHeaderParser.parseCacheHeaders(networkResponse));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
