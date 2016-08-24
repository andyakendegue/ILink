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

public class SuperviseurHomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    FloatingActionButton fab;

    /* renamed from: com.ilink.SuperviseurHomeActivity.1 */
    class C15771 implements OnClickListener {
        C15771() {
        }

        public void onClick(View view) {
            SuperviseurHomeActivity.this.startActivity(new Intent(SuperviseurHomeActivity.this.getApplicationContext(), AddSuperviseurActivity.class));
        }
    }

    /* renamed from: com.ilink.SuperviseurHomeActivity.2 */
    class C15782 implements DialogInterface.OnClickListener {
        C15782() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            Editor editor = SuperviseurHomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            editor.putString(TamponGeolocatedActivity.KEY_EMAIL, BuildConfig.VERSION_NAME);
            editor.putBoolean(Config.CHECK_BOX_ADVICE_PREF, false);
            editor.commit();
            SuperviseurHomeActivity.this.startActivity(new Intent(SuperviseurHomeActivity.this, MainActivity.class));
            new DatabaseHandler(SuperviseurHomeActivity.this).resetTables();
            SuperviseurHomeActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.SuperviseurHomeActivity.3 */
    class C15793 implements DialogInterface.OnClickListener {
        C15793() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_superviseur_home);
        Toolbar toolbar = (Toolbar) findViewById(C1558R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle((CharSequence) "Ilink");
        }
        this.fab = (FloatingActionButton) findViewById(C1558R.id.fab);
        this.fab.setVisibility(4);
        DrawerLayout drawer = (DrawerLayout) findViewById(C1558R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, C1558R.string.navigation_drawer_open, C1558R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(C1558R.id.container, new GeneralMapFragment());
        ft.commit();
        NavigationView navigationView = (NavigationView) findViewById(C1558R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(C1558R.id.drawer_layout);
        if (drawer == null) {
            return;
        }
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
                this.fab.setOnClickListener(new C15771());
                break;
            case C1558R.id.my_account_hyper /*2131689851*/:
                fragment = new AccountSimpleUserFragment();
                this.fab.setVisibility(4);
                break;
            case C1558R.id.exit /*2131689852*/:
                logout();
                break;
            case C1558R.id.group_ask_geolocated /*2131689853*/:
                fragment = new MemberAskGroupFragment();
                this.fab.setVisibility(4);
                break;
            case C1558R.id.group_ask_credit /*2131689854*/:
                fragment = new DemandesCreditFragment();
                this.fab.setVisibility(4);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(C1558R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer((int) MediaRouterJellybean.ALL_ROUTE_TYPES);
        }
        return true;
    }

    private void logout() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Etes vous sur de vouloir vous deconnecter?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Oui", new C15782());
        alertDialogBuilder.setNegativeButton((CharSequence) "Non", new C15793());
        alertDialogBuilder.create().show();
    }
}
