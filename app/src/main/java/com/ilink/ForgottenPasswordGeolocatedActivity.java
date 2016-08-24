package com.ilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.BuildConfig;
import java.util.HashMap;
import java.util.Map;

public class ForgottenPasswordGeolocatedActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    private Button btnModifyPassword;
    private EditText editTextPassword1;
    private EditText editTextPassword2;
    private EditText editTextPhone;

    /* renamed from: com.ilink.ForgottenPasswordGeolocatedActivity.1 */
    class C15151 implements OnClickListener {
        C15151() {
        }

        public void onClick(View v) {
            ForgottenPasswordGeolocatedActivity.this.modifyPassword();
        }
    }

    /* renamed from: com.ilink.ForgottenPasswordGeolocatedActivity.2 */
    class C15162 implements Listener<String> {
        C15162() {
        }

        public void onResponse(String response) {
            if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                Toast.makeText(ForgottenPasswordGeolocatedActivity.this, "Changement de mot de passe reussi! Reconnectez-vous avec le numero de telephone et le nouveau mot de passe.", 1).show();
                ForgottenPasswordGeolocatedActivity.this.startActivity(new Intent(ForgottenPasswordGeolocatedActivity.this, LoginActivity.class));
                ForgottenPasswordGeolocatedActivity.this.finish();
                return;
            }
            Toast.makeText(ForgottenPasswordGeolocatedActivity.this, response, 1).show();
        }
    }

    /* renamed from: com.ilink.ForgottenPasswordGeolocatedActivity.3 */
    class C15173 implements ErrorListener {
        C15173() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ForgottenPasswordGeolocatedActivity.this, "Connexion au serveur impossible", 1).show();
        }
    }

    /* renamed from: com.ilink.ForgottenPasswordGeolocatedActivity.4 */
    class C15184 extends StringRequest {
        final /* synthetic */ String val$password1;
        final /* synthetic */ String val$phone;

        C15184(int x0, String x1, Listener x2, ErrorListener x3, String str, String str2) {
            this.val$phone = str;
            this.val$password1 = str2;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put(ForgottenPasswordGeolocatedActivity.KEY_PHONE, this.val$phone);
            params.put(ForgottenPasswordGeolocatedActivity.KEY_PASSWORD, this.val$password1);
            params.put(ForgottenPasswordGeolocatedActivity.KEY_CATEGORY, "geolocated");
            params.put(ForgottenPasswordGeolocatedActivity.KEY_TAG, "chgpass");
            return params;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_forgotten_password_geolocated);
        this.btnModifyPassword = (Button) findViewById(C1558R.id.btnModifyPassword);
        this.editTextPassword2 = (EditText) findViewById(C1558R.id.editTextPassword2);
        this.editTextPassword1 = (EditText) findViewById(C1558R.id.editTextPassword1);
        this.editTextPhone = (EditText) findViewById(C1558R.id.editTextPhone);
        this.btnModifyPassword.setOnClickListener(new C15151());
    }

    private void modifyPassword() {
        String password1 = this.editTextPassword1.getText().toString().trim();
        String password2 = this.editTextPassword2.getText().toString().trim();
        String phone = this.editTextPhone.getText().toString().trim();
        if (this.editTextPassword1.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextPassword2.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextPhone.getText().toString().equals(BuildConfig.VERSION_NAME)) {
            Toast.makeText(getApplicationContext(), "Un ou plusieurs champs sont vides", 0).show();
        } else if (this.editTextPassword1.getText().toString().length() == this.editTextPassword2.getText().toString().length()) {
            Volley.newRequestQueue(this).add(new C15184(1, REGISTER_URL, new C15162(), new C15173(), phone, password1));
        } else {
            Toast.makeText(getApplicationContext(), "Les mots de passe entres ne correspondent pas. Essayez de nouveau!", 0).show();
        }
    }
}
