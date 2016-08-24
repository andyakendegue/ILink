package com.ilink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ilink.app.AppController;
import com.ilink.lib.DatabaseHandler;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TamponGeolocatedActivity extends AppCompatActivity {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    private String TAG;
    public Button btnRetry;

    /* renamed from: com.ilink.TamponGeolocatedActivity.1 */
    class C15831 implements OnClickListener {
        C15831() {
        }

        public void onClick(View view) {
            try {
                TamponGeolocatedActivity.this.user();
                TamponGeolocatedActivity.this.localusers();
            } catch (JSONException e) {
                Toast.makeText(TamponGeolocatedActivity.this, "impossible de recuperer vos informations", 1).show();
            }
        }
    }

    /* renamed from: com.ilink.TamponGeolocatedActivity.2 */
    class C15842 implements Listener<JSONArray> {
        final /* synthetic */ SharedPreferences val$sharedPreferences;

        C15842(SharedPreferences sharedPreferences) {
            this.val$sharedPreferences = sharedPreferences;
        }

        public void onResponse(JSONArray response) {
            try {
                JSONObject obj = response.getJSONObject(0);
                Editor editor = this.val$sharedPreferences.edit();
                editor.putString(TamponGeolocatedActivity.KEY_EMAIL, obj.getString(TamponGeolocatedActivity.KEY_EMAIL));
                editor.putString(RegisterSimpleActivity.KEY_LASTNAME, obj.getString(RegisterSimpleActivity.KEY_LASTNAME));
                editor.putString(RegisterSimpleActivity.KEY_FIRSTNAME, obj.getString(RegisterSimpleActivity.KEY_FIRSTNAME));
                editor.putString(TamponGeolocatedActivity.KEY_PHONE, obj.getString(TamponGeolocatedActivity.KEY_PHONE));
                editor.putString(RegisterSimpleActivity.KEY_CATEGORY, obj.getString(RegisterSimpleActivity.KEY_CATEGORY));
                editor.putString(RegisterSimpleActivity.KEY_COUNTRY, obj.getString(RegisterSimpleActivity.KEY_COUNTRY));
                editor.putString(RegisterSimpleActivity.KEY_NETWORK, obj.getString(RegisterSimpleActivity.KEY_NETWORK));
                editor.putString(RegisterSimpleActivity.KEY_MEMBER_CODE, obj.getString(RegisterSimpleActivity.KEY_MEMBER_CODE));
                editor.putString(RegisterSimpleActivity.KEY_VALIDATE, obj.getString("active"));
                editor.putString(Config.VALIDATION_CODE_SHARED_PREF, obj.getString(Config.VALIDATION_CODE_SHARED_PREF));
                editor.putString(TamponGeolocatedActivity.KEY_LATITUDE, obj.getString(TamponGeolocatedActivity.KEY_LATITUDE));
                editor.putString(TamponGeolocatedActivity.KEY_LONGITUDE, obj.getString(TamponGeolocatedActivity.KEY_LONGITUDE));
                editor.putString(Config.BALANCE_SHARED_PREF, obj.getString(Config.BALANCE_SHARED_PREF));
                editor.commit();
                if (obj.getString("active").equalsIgnoreCase("non")) {
                    TamponGeolocatedActivity.this.startActivity(new Intent(TamponGeolocatedActivity.this, ActivateGeolocatedActivity.class));
                    TamponGeolocatedActivity.this.finish();
                } else if (obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equalsIgnoreCase("super")) {
                    TamponGeolocatedActivity.this.startActivity(new Intent(TamponGeolocatedActivity.this, SuperviseurHomeActivity.class));
                    TamponGeolocatedActivity.this.finish();
                } else if (obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equalsIgnoreCase("hyper")) {
                    TamponGeolocatedActivity.this.startActivity(new Intent(TamponGeolocatedActivity.this, HyperviseurHomeActivity.class));
                    TamponGeolocatedActivity.this.finish();
                } else if (obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equalsIgnoreCase("geolocated")) {
                    TamponGeolocatedActivity.this.startActivity(new Intent(TamponGeolocatedActivity.this, HomeActivity.class));
                    TamponGeolocatedActivity.this.finish();
                } else {
                    Toast.makeText(TamponGeolocatedActivity.this, "Impossible de vous connecter. Veuillez redemarrer l'application", 1).show();
                }
            } catch (JSONException e) {
                Toast.makeText(TamponGeolocatedActivity.this, "Impossible de recuperer vos donnees", 1).show();
                TamponGeolocatedActivity.this.btnRetry.setVisibility(0);
            }
        }
    }

    /* renamed from: com.ilink.TamponGeolocatedActivity.3 */
    class C15853 implements ErrorListener {
        C15853() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(TamponGeolocatedActivity.this, "Impossible de se connecter au serveur", 1).show();
            TamponGeolocatedActivity.this.btnRetry.setVisibility(0);
        }
    }

    /* renamed from: com.ilink.TamponGeolocatedActivity.4 */
    class C15864 implements Listener<JSONArray> {
        C15864() {
        }

        public void onResponse(JSONArray response) {
            Log.d(TamponGeolocatedActivity.this.TAG, response.toString());
            DatabaseHandler db = new DatabaseHandler(TamponGeolocatedActivity.this);
            db.resetTables();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    db.addUsers(obj.getString(RegisterSimpleActivity.KEY_FIRSTNAME), obj.getString(RegisterSimpleActivity.KEY_LASTNAME), obj.getString(TamponGeolocatedActivity.KEY_EMAIL), obj.getString(TamponGeolocatedActivity.KEY_PHONE), obj.getString(RegisterSimpleActivity.KEY_COUNTRY), obj.getString(RegisterSimpleActivity.KEY_NETWORK), obj.getString(RegisterSimpleActivity.KEY_MEMBER_CODE), obj.getString("code_parrain"), obj.getString(RegisterSimpleActivity.KEY_CATEGORY), obj.getString(Config.BALANCE_SHARED_PREF), obj.getString(TamponGeolocatedActivity.KEY_LATITUDE), obj.getString(TamponGeolocatedActivity.KEY_LONGITUDE), obj.getString("mbre_reseau"), obj.getString("mbre_ss_reseau"), obj.getString(Config.VALIDATION_CODE_SHARED_PREF), obj.getString("active"));
                } catch (JSONException e) {
                    Toast.makeText(TamponGeolocatedActivity.this, "Impossible de recuperer les marqueurs : " + e.toString(), 1).show();
                }
            }
        }
    }

    /* renamed from: com.ilink.TamponGeolocatedActivity.5 */
    class C15875 implements ErrorListener {
        C15875() {
        }

        public void onErrorResponse(VolleyError error) {
            VolleyLog.m15d(TamponGeolocatedActivity.this.TAG, "Erreur : " + error.getMessage());
        }
    }

    public TamponGeolocatedActivity() {
        this.TAG = KEY_TAG;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView((int) C1558R.layout.activity_tampon);
        this.btnRetry = (Button) findViewById(C1558R.id.button_retry);
        this.btnRetry.setVisibility(4);
        this.btnRetry.setOnClickListener(new C15831());
        try {
            user();
            localusers();
        } catch (JSONException e) {
            Toast.makeText(this, "impossible de recuperer vos informations", 1).show();
        }
    }

    private void user() throws JSONException {
        String tag_json_arry = "json_array_req";
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        String phone = sharedPreferences.getString(KEY_PHONE, "Not Available");
        Map<String, String> params = new HashMap();
        params.put(KEY_PHONE, phone);
        params.put(KEY_TAG, "getuser");
        Volley.newRequestQueue(this).add(new CustomRequest(1, "http://ilink-app.com/app/select/users.php", params, new C15842(sharedPreferences), new C15853()));
    }

    private void localusers() {
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new C15864(), new C15875());
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }
}
