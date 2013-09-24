package org.misty.rc.VolleySample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.misty.rc.VolleySample.models.Tag;
import org.misty.rc.VolleySample.models.User;


public class VolleySampleActivity extends Activity {

    private SharedPreferences preferences;
    private FragmentManager fragmentManager;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        fragmentManager = getFragmentManager();
        setContentView(R.layout.main);

        drawerLayout = (DrawerLayout)findViewById(R.id.main_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerList = (ListView)findViewById(R.id.left_drawer_list);

        GsonRequest request = GsonRequest.GET(
                "https://qiita.com/api/v1/users/misty_rc",
                User.class,
                userListener,
                errorListener);

        request.setTag(this);
        VolleyHolder.getRequestQueue(this).add(request);
    }

    private void initTagList(ListView listView) {
        GsonRequest request = GsonRequest.GET(
                "",
                Tag.class,
                tagListener,
                errorListener);

        VolleyHolder.getRequestQueue(this).add(request);
    }

    private Response.Listener<Tag> tagListener = new Response.Listener<Tag>() {
        @Override
        public void onResponse(Tag tag) {

        }
    };

    private Response.Listener<User> userListener = new Response.Listener<User>() {
        @Override
        public void onResponse(User user) {
            Gson gson = new GsonBuilder().create();
            String val = gson.toJson(user, user.getClass());

            Bundle args = new Bundle();
            args.putString("debug", val);

            DebugFragment fragment = new DebugFragment();
            fragment.setArguments(args);

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    public static class DebugFragment extends Fragment {
        public DebugFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.debug_fragment, container, false);

            Bundle args = getArguments();
            String val = args.getString("debug");

            ((TextView)root.findViewById(R.id.debug_textview)).setText(val);

            return root;
        }
    }
}
