package com.ilink;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.directions.route.BuildConfig;

public class ProfileActivity extends AppCompatActivity {
    private TextView textView;

    /* renamed from: com.ilink.ProfileActivity.1 */
    class C15561 implements OnClickListener {
        C15561() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            Editor editor = ProfileActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            editor.putString(TamponGeolocatedActivity.KEY_EMAIL, BuildConfig.VERSION_NAME);
            editor.commit();
            ProfileActivity.this.startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            ProfileActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.ProfileActivity.2 */
    class C15572 implements OnClickListener {
        C15572() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_profile);
        this.textView = (TextView) findViewById(C1558R.id.textView);
        this.textView.setText("Current User: " + getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(TamponGeolocatedActivity.KEY_EMAIL, "Not Available"));
    }

    private void logout() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Yes", new C15561());
        alertDialogBuilder.setNegativeButton((CharSequence) "No", new C15572());
        alertDialogBuilder.create().show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C1558R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C1558R.id.menuLogout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
}
