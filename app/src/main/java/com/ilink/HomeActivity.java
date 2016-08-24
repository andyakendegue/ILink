package com.ilink;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.directions.route.BuildConfig;
import com.ilink.GeneralMapFragment.OnFragmentInteractionListener;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnFragmentInteractionListener, AccountSimpleUserFragment.OnFragmentInteractionListener {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TAG = "tag";

    /* renamed from: com.ilink.HomeActivity.1 */
    class C15231 implements OnClickListener {
        C15231() {
        }

        public void onClick(View view) {
            HomeActivity.this.logout();
        }
    }

    /* renamed from: com.ilink.HomeActivity.2 */
    class C15242 implements DialogInterface.OnClickListener {
        C15242() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            Editor editor = HomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            editor.putString(HomeActivity.KEY_EMAIL, BuildConfig.VERSION_NAME);
            editor.commit();
            HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
            HomeActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.HomeActivity.3 */
    class C15253 implements DialogInterface.OnClickListener {
        C15253() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
        }
    }

    /* renamed from: com.ilink.HomeActivity.4 */
    class C15264 implements Listener<JSONArray> {
        final /* synthetic */ SharedPreferences val$sharedPreferences;

        C15264(SharedPreferences sharedPreferences) {
            this.val$sharedPreferences = sharedPreferences;
        }

        public void onResponse(JSONArray response) {
            Toast.makeText(HomeActivity.this, "Nice!", 1).show();
            try {
                JSONObject obj = response.getJSONObject(0);
                Editor editor = this.val$sharedPreferences.edit();
                editor.putString(RegisterSimpleActivity.KEY_LASTNAME, obj.getString(RegisterSimpleActivity.KEY_LASTNAME));
                editor.putString(RegisterSimpleActivity.KEY_FIRSTNAME, obj.getString(RegisterSimpleActivity.KEY_FIRSTNAME));
                editor.putString(TamponGeolocatedActivity.KEY_PHONE, obj.getString(TamponGeolocatedActivity.KEY_PHONE));
                editor.commit();
            } catch (JSONException e) {
                Toast.makeText(HomeActivity.this, "Impossible de recuperer vos donnees : " + e.toString(), 1).show();
            }
        }
    }

    /* renamed from: com.ilink.HomeActivity.5 */
    class C15275 implements ErrorListener {
        C15275() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(HomeActivity.this, "Impossible de se connecter au serveur :" + error.toString(), 1).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_home);
        String email = getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_EMAIL, "Not Available");
        View header = LayoutInflater.from(this).inflate(C1558R.layout.nav_header_home, null);
        Toolbar toolbar = (Toolbar) findViewById(C1558R.id.toolbar);
        setSupportActionBar(toolbar);
        ((FloatingActionButton) findViewById(C1558R.id.fab)).setOnClickListener(new C15231());
        DrawerLayout drawer = (DrawerLayout) findViewById(C1558R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, C1558R.string.navigation_drawer_open, C1558R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(C1558R.id.container, new GeneralMapFragment());
        ft.commit();
        ((NavigationView) findViewById(C1558R.id.nav_view)).setNavigationItemSelectedListener(this);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(C1558R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) MediaRouterJellybean.ALL_ROUTE_TYPES)) {
            drawer.closeDrawer((int) MediaRouterJellybean.ALL_ROUTE_TYPES);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C1558R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        CharSequence title = getString(C1558R.string.app_name);
        if (id == C1558R.id.general_map) {
            fragment = new GeneralMapFragment();
        } else if (id == C1558R.id.my_account_simple) {
            fragment = new AccountSimpleUserFragment();
        } else if (id == C1558R.id.ask_credit) {
            fragment = new AskCreditFragment();
        } else if (id == C1558R.id.ask_supervisor) {
            fragment = new AskSupervisorFragment();
        } else if (id == C1558R.id.exit) {
            logout();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(C1558R.id.container, fragment);
            ft.commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        ((DrawerLayout) findViewById(C1558R.id.drawer_layout)).closeDrawer((int) MediaRouterJellybean.ALL_ROUTE_TYPES);
        return true;
    }

    private void logout() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Etes vous sur de vouloir vous deconnecter?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Oui", new C15242());
        alertDialogBuilder.setNegativeButton((CharSequence) "Non", new C15253());
        alertDialogBuilder.create().show();
    }

    private void user() throws JSONException {
        String tag_json_arry = "json_array_req";
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        String email = sharedPreferences.getString(KEY_EMAIL, "Not Available");
        Map<String, String> params = new HashMap();
        params.put(KEY_EMAIL, email);
        params.put(KEY_TAG, "getuser");
        Volley.newRequestQueue(this).add(new CustomRequest(1, "http://ilink-app.com/app/select/users.php", params, new C15264(sharedPreferences), new C15275()));
    }

    public void onGeneralMapFragmentInteraction(Uri uri) {
    }

    public void onAccountSimpleUserFragmentInteraction(Uri uri) {
    }
}
