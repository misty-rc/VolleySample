package org.misty.rc.VolleySample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.misty.rc.VolleySample.models.Auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/24
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //debug
        preferences.edit().remove(Auth.TOKEN).commit();

        if(TextUtils.isEmpty(preferences.getString(Auth.TOKEN, null))) {
            setContentView(R.layout.login);

            Button button = (Button)findViewById(R.id.login_button);
            button.setOnClickListener(loginClick);

        } else {
            //still login
            changeActivity();
        }
    }

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            String url_name = ((EditText)findViewById(R.id.url_name)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put(Auth.URL_NAME, url_name);
            params.put(Auth.PASSWORD, password);

            GsonRequest request = GsonRequest.POST(
                    "https://qiita.com/api/v1/auth",
                    Auth.class,
                    params,
                    authListener,
                    errorListener
            );

            VolleyHolder.getRequestQueue(getApplication()).add(request);
        }
    };

    private Response.Listener<Auth> authListener = new Response.Listener<Auth>() {
        @Override
        public void onResponse(Auth auth) {
            String token = auth.token;

            preferences.edit().putString(Auth.TOKEN, token).commit();
            changeActivity();
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getApplication(), "認証失敗", Toast.LENGTH_LONG).show();
        }
    };

    private void changeActivity() {
        Intent i = new Intent(getApplication(), VolleySampleActivity.class);
        startActivity(i);

        LoginActivity.this.finish();
    }

}
