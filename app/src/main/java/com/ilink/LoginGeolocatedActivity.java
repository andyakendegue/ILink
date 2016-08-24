package com.ilink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class LoginGeolocatedActivity extends AppCompatActivity implements OnClickListener {
    private AppCompatButton btnForgotPassword;
    private AppCompatButton buttonLogin;
    private AppCompatButton buttonRegister;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private TextView linkSignup;
    private boolean loggedIn;

    /* renamed from: com.ilink.LoginGeolocatedActivity.1 */
    class C15361 implements OnClickListener {
        C15361() {
        }

        public void onClick(View view) {
            String email = LoginGeolocatedActivity.this.editTextPhone.getText().toString().trim();
            String password = LoginGeolocatedActivity.this.editTextPassword.getText().toString().trim();
            Intent intent = new Intent(LoginGeolocatedActivity.this, RegisterSimpleActivity.class);
            intent.putExtra(TamponGeolocatedActivity.KEY_EMAIL, email);
            intent.putExtra(RegisterSimpleActivity.KEY_PASSWORD, password);
            LoginGeolocatedActivity.this.startActivity(intent);
            LoginGeolocatedActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.LoginGeolocatedActivity.2 */
    class C15372 implements OnClickListener {
        C15372() {
        }

        public void onClick(View v) {
            LoginGeolocatedActivity.this.startActivity(new Intent(LoginGeolocatedActivity.this, ForgottenPasswordGeolocatedActivity.class));
        }
    }

    /* renamed from: com.ilink.LoginGeolocatedActivity.3 */
    class C15383 implements Listener<String> {
        final /* synthetic */ String val$phone;

        C15383(String str) {
            this.val$phone = str;
        }

        public void onResponse(String response) {
            if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                Editor editor = LoginGeolocatedActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                editor.putString(TamponGeolocatedActivity.KEY_PHONE, this.val$phone);
                editor.commit();
                LoginGeolocatedActivity.this.startActivity(new Intent(LoginGeolocatedActivity.this, TamponGeolocatedActivity.class));
                LoginGeolocatedActivity.this.finish();
                return;
            }
            Toast.makeText(LoginGeolocatedActivity.this, "Email ou mot de passe invalide" + response, 1).show();
        }
    }

    /* renamed from: com.ilink.LoginGeolocatedActivity.4 */
    class C15394 implements ErrorListener {
        C15394() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(LoginGeolocatedActivity.this, "Probleme de connexion au serveur", 1).show();
        }
    }

    /* renamed from: com.ilink.LoginGeolocatedActivity.5 */
    class C15405 extends StringRequest {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phone;

        C15405(int x0, String x1, Listener x2, ErrorListener x3, String str, String str2) {
            this.val$phone = str;
            this.val$password = str2;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap();
            params.put(TamponGeolocatedActivity.KEY_PHONE, this.val$phone);
            params.put(RegisterSimpleActivity.KEY_PASSWORD, this.val$password);
            params.put(TamponGeolocatedActivity.KEY_TAG, "login_geolocated");
            return params;
        }
    }

    public LoginGeolocatedActivity() {
        this.loggedIn = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView((int) C1558R.layout.activity_login);
        this.editTextPhone = (EditText) findViewById(C1558R.id.editTextPhone);
        this.editTextPassword = (EditText) findViewById(C1558R.id.editTextPassword);
        this.linkSignup = (TextView) findViewById(C1558R.id.linkSignup);
        this.buttonLogin = (AppCompatButton) findViewById(C1558R.id.buttonLogin);
        this.btnForgotPassword = (AppCompatButton) findViewById(C1558R.id.btnForgotPassword);
        this.buttonLogin.setOnClickListener(this);
        this.buttonRegister = (AppCompatButton) findViewById(C1558R.id.buttonRegister);
        this.buttonRegister.setOnClickListener(new C15361());
        this.btnForgotPassword.setOnClickListener(new C15372());
        this.linkSignup.setVisibility(4);
        this.buttonRegister.setVisibility(4);
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        this.loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        String category = sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Aucun resultat");
        if (!this.loggedIn) {
            return;
        }
        if (category.equalsIgnoreCase("utilisateur")) {
            startActivity(new Intent(this, TamponActivity.class));
            finish();
        } else if (category.equalsIgnoreCase("super")) {
            startActivity(new Intent(this, TamponGeolocatedActivity.class));
            finish();
        } else if (category.equalsIgnoreCase("hyper")) {
            startActivity(new Intent(this, TamponGeolocatedActivity.class));
            finish();
        } else if (category.equalsIgnoreCase("geolocated")) {
            startActivity(new Intent(this, TamponGeolocatedActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Impossible de vous connecter. Veuillez redemarrer l'application", 1).show();
        }
    }

    private void login() {
        String phone = this.editTextPhone.getText().toString().trim();
        String tag = "login_geolocated";
        Volley.newRequestQueue(this).add(new C15405(1, Config.LOGIN_URL, new C15383(phone), new C15394(), phone, this.editTextPassword.getText().toString().trim()));
    }

    public void onClick(View v) {
        login();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
