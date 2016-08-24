package com.ilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.ilink.helper.PrefManager;
import java.util.HashMap;

public class MainSmsActivity extends AppCompatActivity {
    private TextView email;
    private TextView mobile;
    private TextView name;
    private PrefManager pref;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_main);
        this.toolbar = (Toolbar) findViewById(C1558R.id.toolbar);
        this.name = (TextView) findViewById(C1558R.id.name);
        this.email = (TextView) findViewById(C1558R.id.email);
        this.mobile = (TextView) findViewById(C1558R.id.mobile);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.pref = new PrefManager(getApplicationContext());
        if (!this.pref.isLoggedIn()) {
            logout();
        }
        HashMap<String, String> profile = this.pref.getUserDetails();
        this.name.setText("Name: " + ((String) profile.get("name")));
        this.email.setText("Email: " + ((String) profile.get(TamponGeolocatedActivity.KEY_EMAIL)));
        this.mobile.setText("Mobile: " + ((String) profile.get("mobile")));
    }

    private void logout() {
        this.pref.clearSession();
        Intent intent = new Intent(this, SmsActivity.class);
        intent.setFlags(335544320);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C1558R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != C1558R.id.action_logout) {
            return super.onOptionsItemSelected(item);
        }
        logout();
        return true;
    }
}
