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
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class ActivateGeolocatedActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY = "categorie";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MEMBER_CODE = "code_membre";
    public static final String KEY_NOMBRE_CODES = "nbre_code";
    public static final String KEY_NOMBRE_CODES_SUPERVISEUR = "nbre_code_superviseur";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    public static final String KEY_VALIDATE = "validate";
    Button btnValidate;
    public String category;
    String code_membre;
    EditText editTextNombreGeo;
    EditText editTextNombreMembres;
    EditText editTextValidation;
    String nbre_codes;
    String nbre_codes_geo;
    TextView textNombreGeo;
    TextView textNombreMembres;
    String validation;

    /* renamed from: com.ilink.ActivateGeolocatedActivity.1 */
    class C15061 implements OnClickListener {
        final /* synthetic */ SharedPreferences val$sharedPreferences;
        final /* synthetic */ String val$validation_code;

        C15061(String str, SharedPreferences sharedPreferences) {
            this.val$validation_code = str;
            this.val$sharedPreferences = sharedPreferences;
        }

        public void onClick(View v) {
            ActivateGeolocatedActivity.this.validation = ActivateGeolocatedActivity.this.editTextValidation.getText().toString();
            if (ActivateGeolocatedActivity.this.category.equalsIgnoreCase("geolocated")) {
                ActivateGeolocatedActivity.this.nbre_codes = "0";
                ActivateGeolocatedActivity.this.nbre_codes_geo = "0";
            } else {
                ActivateGeolocatedActivity.this.nbre_codes = ActivateGeolocatedActivity.this.editTextNombreMembres.getText().toString();
                ActivateGeolocatedActivity.this.nbre_codes_geo = ActivateGeolocatedActivity.this.editTextNombreGeo.getText().toString();
            }
            if (this.val$validation_code.equalsIgnoreCase(ActivateGeolocatedActivity.this.validation)) {
                Editor editor = this.val$sharedPreferences.edit();
                editor.putString(ActivateGeolocatedActivity.KEY_VALIDATE, "oui");
                editor.commit();
                try {
                    ActivateGeolocatedActivity.this.validateUser();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(ActivateGeolocatedActivity.this, "Code de Validation incorrect", 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateGeolocatedActivity.2 */
    class C15072 implements Listener<String> {
        C15072() {
        }

        public void onResponse(String response) {
            if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                SharedPreferences sharedPreferences = ActivateGeolocatedActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0);
                ActivateGeolocatedActivity.this.category = sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
                if (ActivateGeolocatedActivity.this.category.equalsIgnoreCase("utilisateur")) {
                    ActivateGeolocatedActivity.this.startActivity(new Intent(ActivateGeolocatedActivity.this, MapsActivity.class));
                    ActivateGeolocatedActivity.this.finish();
                    return;
                } else if (ActivateGeolocatedActivity.this.category.equalsIgnoreCase("super")) {
                    ActivateGeolocatedActivity.this.startActivity(new Intent(ActivateGeolocatedActivity.this, SuperviseurHomeActivity.class));
                    ActivateGeolocatedActivity.this.finish();
                    return;
                } else if (ActivateGeolocatedActivity.this.category.equalsIgnoreCase("hyper")) {
                    ActivateGeolocatedActivity.this.startActivity(new Intent(ActivateGeolocatedActivity.this, HyperviseurHomeActivity.class));
                    ActivateGeolocatedActivity.this.finish();
                    return;
                } else if (ActivateGeolocatedActivity.this.category.equalsIgnoreCase("geolocated")) {
                    ActivateGeolocatedActivity.this.startActivity(new Intent(ActivateGeolocatedActivity.this, HomeActivity.class));
                    ActivateGeolocatedActivity.this.finish();
                    return;
                } else {
                    Toast.makeText(ActivateGeolocatedActivity.this, "Impossible de vous connecter. Veuillez redemarrer l'application", 1).show();
                    return;
                }
            }
            Toast.makeText(ActivateGeolocatedActivity.this, "Impossible de mettre a jour votre compte " + response, 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateGeolocatedActivity.3 */
    class C15083 implements ErrorListener {
        C15083() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ActivateGeolocatedActivity.this, "Impossible de se connecter au serveur :" + error.toString(), 1).show();
        }
    }

    /* renamed from: com.ilink.ActivateGeolocatedActivity.4 */
    class C15094 extends StringRequest {
        final /* synthetic */ String val$phone;

        C15094(int x0, String x1, Listener x2, ErrorListener x3, String str) {
            this.val$phone = str;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put(ActivateGeolocatedActivity.KEY_PHONE, this.val$phone);
            params.put(ActivateGeolocatedActivity.KEY_NOMBRE_CODES, ActivateGeolocatedActivity.this.nbre_codes);
            params.put(ActivateGeolocatedActivity.KEY_NOMBRE_CODES_SUPERVISEUR, ActivateGeolocatedActivity.this.nbre_codes_geo);
            params.put(ActivateGeolocatedActivity.KEY_CATEGORY, ActivateGeolocatedActivity.this.category);
            params.put(ActivateGeolocatedActivity.KEY_MEMBER_CODE, ActivateGeolocatedActivity.this.code_membre);
            params.put(ActivateGeolocatedActivity.KEY_VALIDATE, "oui");
            return params;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView((int) C1558R.layout.activity_activate);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        String validation_code = sharedPreferences.getString(Config.VALIDATION_CODE_SHARED_PREF, "Not Available");
        this.category = sharedPreferences.getString(RegisterSimpleActivity.KEY_CATEGORY, "Not Available");
        this.code_membre = sharedPreferences.getString(RegisterSimpleActivity.KEY_MEMBER_CODE, "Not Available");
        this.editTextValidation = (EditText) findViewById(C1558R.id.editTextValidation);
        this.editTextNombreMembres = (EditText) findViewById(C1558R.id.editTextNombreMembres);
        this.editTextNombreGeo = (EditText) findViewById(C1558R.id.editTextNombreGeo);
        this.textNombreGeo = (TextView) findViewById(C1558R.id.textNombreGeo);
        this.textNombreMembres = (TextView) findViewById(C1558R.id.textNombreMembres);
        if (this.category.equalsIgnoreCase("geolocated") || this.category.equalsIgnoreCase("super")) {
            this.textNombreMembres.setVisibility(4);
            this.textNombreMembres.setHeight(0);
            this.editTextNombreMembres.setVisibility(4);
            this.editTextNombreMembres.setHeight(0);
            this.textNombreGeo.setVisibility(4);
            this.textNombreGeo.setHeight(0);
            this.editTextNombreGeo.setVisibility(4);
            this.editTextNombreGeo.setHeight(0);
        } else {
            this.textNombreMembres.setVisibility(0);
            this.editTextNombreMembres.setVisibility(0);
            this.textNombreGeo.setVisibility(0);
            this.editTextNombreGeo.setVisibility(0);
        }
        this.btnValidate = (Button) findViewById(C1558R.id.btnValidate);
        this.btnValidate.setOnClickListener(new C15061(validation_code, sharedPreferences));
    }

    public void validateUser() throws JSONException {
        String tag_json_arry = "json_array_req";
        Volley.newRequestQueue(this).add(new C15094(1, "http://ilink-app.com/app/select/validation.php", new C15072(), new C15083(), getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_PHONE, "Not Available")));
    }
}
