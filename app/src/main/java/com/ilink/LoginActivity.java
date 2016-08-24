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

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private AppCompatButton btnForgotPassword;
    private AppCompatButton buttonLogin;
    private AppCompatButton buttonRegister;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private TextView linkSignup;
    private boolean loggedIn;

    /* renamed from: com.ilink.LoginActivity.1 */
    class C15311 implements OnClickListener {
        C15311() {
        }

        public void onClick(View view) {
            String phone = LoginActivity.this.editTextPhone.getText().toString().trim();
            String password = LoginActivity.this.editTextPassword.getText().toString().trim();
            Intent intent = new Intent(LoginActivity.this, RegisterSimpleActivity.class);
            intent.putExtra(TamponGeolocatedActivity.KEY_PHONE, phone);
            intent.putExtra(RegisterSimpleActivity.KEY_PASSWORD, password);
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.LoginActivity.2 */
    class C15322 implements OnClickListener {
        C15322() {
        }

        public void onClick(View v) {
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, ForgottenPasswordSimpleActivity.class));
        }
    }

    /* renamed from: com.ilink.LoginActivity.3 */
    class C15333 implements Listener<String> {
        final /* synthetic */ String val$phone;

        C15333(String str) {
            this.val$phone = str;
        }

        public void onResponse(String response) {
            if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                Editor editor = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                editor.putString(TamponGeolocatedActivity.KEY_PHONE, this.val$phone);
                editor.commit();
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, TamponActivity.class));
                LoginActivity.this.finish();
                return;
            }
            Toast.makeText(LoginActivity.this, "Telephone ou mot de passe invalide " + response, 1).show();
        }
    }

    /* renamed from: com.ilink.LoginActivity.4 */
    class C15344 implements ErrorListener {
        C15344() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(LoginActivity.this, "Probleme de connexion au serveur", 1).show();
        }
    }

    /* renamed from: com.ilink.LoginActivity.5 */
    class C15355 extends StringRequest {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phone;

        C15355(int x0, String x1, Listener x2, ErrorListener x3, String str, String str2) {
            this.val$phone = str;
            this.val$password = str2;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap();
            params.put(TamponGeolocatedActivity.KEY_PHONE, this.val$phone);
            params.put(RegisterSimpleActivity.KEY_PASSWORD, this.val$password);
            params.put(TamponGeolocatedActivity.KEY_TAG, "login");
            return params;
        }
    }

    public LoginActivity() {
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
        this.buttonRegister.setOnClickListener(new C15311());
        this.btnForgotPassword.setOnClickListener(new C15322());
        this.linkSignup.setVisibility(0);
        this.buttonRegister.setVisibility(0);
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
        String tag = "login";
        Volley.newRequestQueue(this).add(new C15355(1, Config.LOGIN_URL, new C15333(phone), new C15344(), phone, this.editTextPassword.getText().toString().trim()));
    }

    public void onClick(View v) {
        login();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
