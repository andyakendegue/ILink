package com.ilink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class ActivateActivity extends AppCompatActivity {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    public static final String KEY_VALIDATE = "validate";
    Button btnValidate;
    EditText editTextValidation;
    String validation;

    /* renamed from: com.ilink.ActivateActivity.1 */
    class C15021 implements OnClickListener {
        C15021() {
        }

        public void onClick(View v) {
            SharedPreferences sharedPreferences = ActivateActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0);
            String validation_code = sharedPreferences.getString(Config.VALIDATION_CODE_SHARED_PREF, "Not Available");
            ActivateActivity.this.validation = ActivateActivity.this.editTextValidation.getText().toString();
            if (validation_code.equalsIgnoreCase(ActivateActivity.this.validation)) {
                Editor editor = sharedPreferences.edit();
                editor.putString(ActivateActivity.KEY_VALIDATE, "oui");
                editor.commit();
                try {
                    ActivateActivity.this.validateUser();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(ActivateActivity.this, "Code de Validation incorrect", 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateActivity.2 */
    class C15032 implements Listener<String> {
        C15032() {
        }

        public void onResponse(String response) {
            if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                String category = ActivateActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
                if (category.equalsIgnoreCase("utilisateur")) {
                    ActivateActivity.this.startActivity(new Intent(ActivateActivity.this, MapsActivity.class));
                    ActivateActivity.this.finish();
                    return;
                } else if (category.equalsIgnoreCase("super")) {
                    ActivateActivity.this.startActivity(new Intent(ActivateActivity.this, SuperviseurHomeActivity.class));
                    ActivateActivity.this.finish();
                    return;
                } else if (category.equalsIgnoreCase("hyper")) {
                    ActivateActivity.this.startActivity(new Intent(ActivateActivity.this, HyperviseurHomeActivity.class));
                    ActivateActivity.this.finish();
                    return;
                } else if (category.equalsIgnoreCase("geolocated")) {
                    ActivateActivity.this.startActivity(new Intent(ActivateActivity.this, HomeActivity.class));
                    ActivateActivity.this.finish();
                    return;
                } else {
                    Toast.makeText(ActivateActivity.this, "Impossible de vous connecter. Veuillez redemarrer l'application", 1).show();
                    return;
                }
            }
            Toast.makeText(ActivateActivity.this, "Impossible de mettre a jour votre compte", 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateActivity.3 */
    class C15043 implements ErrorListener {
        C15043() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ActivateActivity.this, "Impossible de se connecter au serveur :" + error.toString(), 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateActivity.4 */
    class C15054 extends StringRequest {
        final /* synthetic */ String val$phone;

        C15054(int x0, String x1, Listener x2, ErrorListener x3, String str) {
            this.val$phone = str;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put(ActivateActivity.KEY_PHONE, this.val$phone);
            params.put(ActivateActivity.KEY_VALIDATE, "oui");
            return params;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView((int) C1558R.layout.activity_activate);
        this.editTextValidation = (EditText) findViewById(C1558R.id.editTextValidation);
        this.btnValidate = (Button) findViewById(C1558R.id.btnValidate);
        this.btnValidate.setOnClickListener(new C15021());
    }

    public void validateUser() throws JSONException {
        String tag_json_arry = "json_array_req";
        Volley.newRequestQueue(this).add(new C15054(1, "http://ilink-app.com/app/select/validation_simple.php", new C15032(), new C15043(), getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_PHONE, "Not Available")));
    }
}
