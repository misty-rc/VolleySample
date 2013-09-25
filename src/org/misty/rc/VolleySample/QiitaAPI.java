package org.misty.rc.VolleySample;

import android.text.TextUtils;
import android.util.Log;
import org.misty.rc.VolleySample.models.Auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/25
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class QiitaAPI {
    private static final String BASE_URL = "https://qiita.com/api/v1";
    private static final String QUERY_STRING = "?";
    private static final String SEPARATOR = "/";
    private static final String SUFFIX_TOKEN = "token=";
    private static final String SUFFIX_PER_PAGE = "per_page=";
    private static final String SUFFIX_PAGE = "page=";

    private static final String SUFFIX_AUTH = "/auth";
    private static final String SUFFIX_USER = "/user";
    private static final String SUFFIX_USERS = "/users";
    private static final String SUFFIX_FOLLOWING_TAGS = "/following_tags";

    public static String getToken() {
        return BASE_URL + SUFFIX_AUTH;
    }

    public static Map<String, String> getTokenParams(String url_name, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Auth.URL_NAME, url_name);
        map.put(Auth.PASSWORD, password);

        return map;
    }

    public static String getUser(String token) {
        return BASE_URL + SUFFIX_USER + QUERY_STRING + SUFFIX_TOKEN + token;
    }

    public static String getFollowingTags(String url_name) {
        return getFollowingTags(url_name, null);
    }

    public static String getFollowingTags(String url_name, String token) {

        String val = BASE_URL + SUFFIX_USERS + SEPARATOR + url_name + SUFFIX_FOLLOWING_TAGS;
        if(!TextUtils.isEmpty(token)) {
            val = val + QUERY_STRING + token;
        }

        Log.d("qiita", val);
        return val;
    }

}
