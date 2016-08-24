package com.ilink;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnHome1;
    private Button btnHome2;
    private Button btnHome3;
    private Button btnHome4;
    private Button btnHome5;
    private Button btnHome6;
    private boolean registered;

    /* renamed from: com.ilink.MainActivity.1 */
    class C15411 implements OnClickListener {
        C15411() {
        }

        public void onClick(View view) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    /* renamed from: com.ilink.MainActivity.2 */
    class C15422 implements OnClickListener {
        C15422() {
        }

        public void onClick(View view) {
            MainActivity.this.registered = MainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).getBoolean(Config.REGISTERED_SHARED_PREF, false);
            if (MainActivity.this.registered) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginGeolocatedActivity.class));
                return;
            }
            MainActivity.this.geolocated();
        }
    }

    /* renamed from: com.ilink.MainActivity.3 */
    class C15433 implements OnClickListener {
        C15433() {
        }

        public void onClick(View view) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, HelpActivity.class));
        }
    }

    /* renamed from: com.ilink.MainActivity.4 */
    class C15444 implements OnClickListener {
        C15444() {
        }

        public void onClick(View view) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, HelpActivity.class));
        }
    }

    /* renamed from: com.ilink.MainActivity.5 */
    class C15455 implements DialogInterface.OnClickListener {
        C15455() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginGeolocatedActivity.class));
        }
    }

    /* renamed from: com.ilink.MainActivity.6 */
    class C15466 implements DialogInterface.OnClickListener {
        C15466() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }
    }

    public MainActivity() {
        this.registered = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.fragment_accueil);
        getSupportActionBar().hide();
        this.btnHome1 = (Button) findViewById(C1558R.id.btnHome1);
        this.btnHome2 = (Button) findViewById(C1558R.id.btnHome2);
        this.btnHome3 = (Button) findViewById(C1558R.id.btnHome3);
        this.btnHome6 = (Button) findViewById(C1558R.id.btnHome6);
        this.btnHome1.setOnClickListener(new C15411());
        this.btnHome2.setOnClickListener(new C15422());
        this.btnHome3.setOnClickListener(new C15433());
        this.btnHome6.setOnClickListener(new C15444());
    }

    private void geolocated() {
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Que souhaitez vous faire?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Se Connecter", new C15455());
        alertDialogBuilder.setNegativeButton((CharSequence) "S'enregistrer", new C15466());
        alertDialogBuilder.create().show();
    }
}
