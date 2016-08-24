package com.ilink;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.directions.route.BuildConfig;
import com.ilink.lib.DatabaseHandler;

public class HyperviseurHomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    FloatingActionButton fab;

    /* renamed from: com.ilink.HyperviseurHomeActivity.1 */
    class C15281 implements OnClickListener {
        C15281() {
        }

        public void onClick(View view) {
            HyperviseurHomeActivity.this.startActivity(new Intent(HyperviseurHomeActivity.this.getApplicationContext(), AddSuperviseurActivity.class));
        }
    }

    /* renamed from: com.ilink.HyperviseurHomeActivity.2 */
    class C15292 implements DialogInterface.OnClickListener {
        C15292() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            Editor editor = HyperviseurHomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            editor.putString(TamponGeolocatedActivity.KEY_EMAIL, BuildConfig.VERSION_NAME);
            editor.putBoolean(Config.CHECK_BOX_ADVICE_PREF, false);
            editor.commit();
            HyperviseurHomeActivity.this.startActivity(new Intent(HyperviseurHomeActivity.this, MainActivity.class));
            new DatabaseHandler(HyperviseurHomeActivity.this).resetTables();
            HyperviseurHomeActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.HyperviseurHomeActivity.3 */
    class C15303 implements DialogInterface.OnClickListener {
        C15303() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_hyperviseur_home);
        Toolbar toolbar = (Toolbar) findViewById(C1558R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((CharSequence) "Ilink");
        this.fab = (FloatingActionButton) findViewById(C1558R.id.fab);
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
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        CharSequence title = getString(C1558R.string.app_name);
        switch (id) {
            case C1558R.id.group_map /*2131689848*/:
                fragment = new GeneralMapFragment();
                this.fab.setVisibility(4);
                break;
            case C1558R.id.group_members /*2131689849*/:
                fragment = new MemberGroupFragment();
                this.fab.setVisibility(0);
                this.fab.setOnClickListener(new C15281());
                break;
            case C1558R.id.group_ask_superviseur /*2131689850*/:
                fragment = new MemberAskGroupFragment();
                this.fab.setVisibility(4);
                break;
            case C1558R.id.my_account_hyper /*2131689851*/:
                fragment = new AccountSimpleUserFragment();
                this.fab.setVisibility(4);
                break;
            case C1558R.id.exit /*2131689852*/:
                logout();
                break;
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
        alertDialogBuilder.setPositiveButton((CharSequence) "Oui", new C15292());
        alertDialogBuilder.setNegativeButton((CharSequence) "Non", new C15303());
        alertDialogBuilder.create().show();
    }
}
