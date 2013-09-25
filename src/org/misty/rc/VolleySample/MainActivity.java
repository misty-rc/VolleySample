package org.misty.rc.VolleySample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.misty.rc.VolleySample.models.Auth;
import org.misty.rc.VolleySample.models.Tag;
import org.misty.rc.VolleySample.models.User;


public class MainActivity extends Activity {
    private String _url_name;
    private String _token;

    private SharedPreferences preferences;
    private FragmentManager fragmentManager;
    private GestureDetector gestureDetector;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private LinearLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //init application
        initialize();

        //test get userinfo
        getUser();
    }

    private void initialize() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _url_name = preferences.getString(Auth.URL_NAME, null);
        _token = preferences.getString(Auth.TOKEN, null);

        gestureDetector = new GestureDetector(this, new FlingHandler());

        //imageLoader for NetworkImageView
        imageLoader = new ImageLoader(VolleyHolder.getRequestQueue(this), new QiitaImageCache());

        //FragmentManager
        fragmentManager = getFragmentManager();

        //main layout with NavigationDrawer
        drawerLayout = (DrawerLayout)findViewById(R.id.main_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //navigation drawer
        drawer = (LinearLayout)findViewById(R.id.left_drawer_layout);

        //list in navigation drawer
        drawerList = (ListView)findViewById(R.id.left_drawer_list);
        drawerList.setOnItemClickListener(new TagClickListener());
        initTagList();

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setProfileIcon(String url) {
        NetworkImageView view = (NetworkImageView)findViewById(R.id.profile_icon);
        view.setImageUrl(url, imageLoader);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else
            switch (item.getItemId()) {
                case R.id.action_preference:
                    //TODO: open preference
                    Intent intent = new Intent(this, QiitaPreferenceActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    /*
    * request queue
    *
    * */

    private void initTagList() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getFollowingTags(_url_name, _token),
                Tag[].class,
                tagListener,
                errorListener);
        request.setTag(this);
        VolleyHolder.getRequestQueue(this).add(request);
    }

    private void getUser() {
        GsonRequest request = GsonRequest.GET(
                QiitaAPI.getUser(_token),
                User.class,
                userListener,
                errorListener);
        request.setTag(this);
        VolleyHolder.getRequestQueue(this).add(request);
    }


    /*
    * event listener
    *
    * */

    private class TagClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(drawer);
        }
    }


    /*
    * volley response listener
    *
    * */

    private Response.Listener<Tag[]> tagListener = new Response.Listener<Tag[]>() {
        @Override
        public void onResponse(Tag[] tags) {
            drawerList.setAdapter(new TagAdapter(getApplication(), R.layout.tag_item, tags));
        }
    };

    private Response.Listener<User> userListener = new Response.Listener<User>() {
        @Override
        public void onResponse(User user) {
            Gson gson = new GsonBuilder().create();
            setProfileIcon(user.profile_image_url);

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
            //TODO: error handling logic
        }
    };


    /*
    * inner fragment
    *
    * */

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


    private class FlingHandler implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {}

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dx = Math.abs(velocityX);
            float dy = Math.abs(velocityY);
            if(dx > dy && dx > 200) {
                if(e1.getX() - e2.getX() < 150) {
                    drawerLayout.openDrawer(drawer);
                    return true;
                }
            }
            return false;
        }
    }
}
