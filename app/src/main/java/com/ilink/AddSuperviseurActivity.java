package com.ilink;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSuperviseurActivity extends AppCompatActivity implements OnClickListener, LocationListener {
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_MEMBER_CODE = "member";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    public static final String KEY_VALIDATE = "validate";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    private Spinner ListPays;
    private Spinner ListReseau;
    private Button buttonRegister;
    private String e_pays;
    private String e_reseau;
    private EditText editTextEmail;
    private EditText editTextMember;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    private EditText editTextPhone;
    private EditText firstname;
    private EditText lastname;
    private String latitude;
    private LocationManager locationManager;
    private String longitude;
    private String[] paysItem;
    private String[] reseauItem;

    /* renamed from: com.ilink.AddSuperviseurActivity.1 */
    class C15101 implements OnItemSelectedListener {
        C15101() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AddSuperviseurActivity.this.e_pays = parent.getItemAtPosition(position).toString();
            if (AddSuperviseurActivity.this.e_pays.equals("Burkina-Faso")) {
                AddSuperviseurActivity.this.reseauItem = AddSuperviseurActivity.this.getResources().getStringArray(C1558R.array.network_burkina);
            } else if (AddSuperviseurActivity.this.e_pays.equals("Cameroun")) {
                AddSuperviseurActivity.this.reseauItem = AddSuperviseurActivity.this.getResources().getStringArray(C1558R.array.network_cameroun);
            } else if (AddSuperviseurActivity.this.e_pays.equals("France")) {
                AddSuperviseurActivity.this.reseauItem = AddSuperviseurActivity.this.getResources().getStringArray(C1558R.array.network_france);
            } else if (AddSuperviseurActivity.this.e_pays.equals("Gabon")) {
                AddSuperviseurActivity.this.reseauItem = AddSuperviseurActivity.this.getResources().getStringArray(C1558R.array.network_gabon);
            }
            List<String> listReseau = new ArrayList();
            for (Object add : AddSuperviseurActivity.this.reseauItem) {
                listReseau.add(add);
            }
            ArrayAdapter<String> reseauAdapter = new ArrayAdapter(AddSuperviseurActivity.this, 17367048, listReseau);
            reseauAdapter.setDropDownViewResource(17367049);
            AddSuperviseurActivity.this.ListReseau.setAdapter(reseauAdapter);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            AddSuperviseurActivity.this.e_pays = AddSuperviseurActivity.this.paysItem[0].toString();
        }
    }

    /* renamed from: com.ilink.AddSuperviseurActivity.2 */
    class C15112 implements OnItemSelectedListener {
        C15112() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            AddSuperviseurActivity.this.e_reseau = parent.getItemAtPosition(position).toString();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            AddSuperviseurActivity.this.e_reseau = AddSuperviseurActivity.this.reseauItem[0].toString();
        }
    }

    /* renamed from: com.ilink.AddSuperviseurActivity.3 */
    class C15123 implements Listener<String> {
        C15123() {
        }

        public void onResponse(String response) {
            Toast.makeText(AddSuperviseurActivity.this, "Enregistrement reussi!", 1).show();
            AddSuperviseurActivity.this.startActivity(new Intent(AddSuperviseurActivity.this, LoginActivity.class));
            AddSuperviseurActivity.this.finish();
        }
    }

    /* renamed from: com.ilink.AddSuperviseurActivity.4 */
    class C15134 implements ErrorListener {
        C15134() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(AddSuperviseurActivity.this, "Impossible de se connecter au serveur", 1).show();
        }
    }

    /* renamed from: com.ilink.AddSuperviseurActivity.5 */
    class C15145 extends StringRequest {
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$member_code;
        final /* synthetic */ String val$nom;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phone;
        final /* synthetic */ String val$prenom;

        C15145(int x0, String x1, Listener x2, ErrorListener x3, String str, String str2, String str3, String str4, String str5, String str6) {
            this.val$prenom = str;
            this.val$nom = str2;
            this.val$password = str3;
            this.val$email = str4;
            this.val$phone = str5;
            this.val$member_code = str6;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put(AddSuperviseurActivity.KEY_FIRSTNAME, this.val$prenom);
            params.put(AddSuperviseurActivity.KEY_LASTNAME, this.val$nom);
            params.put(AddSuperviseurActivity.KEY_PASSWORD, this.val$password);
            params.put(AddSuperviseurActivity.KEY_EMAIL, this.val$email);
            params.put(AddSuperviseurActivity.KEY_PHONE, this.val$phone);
            params.put(AddSuperviseurActivity.KEY_NETWORK, AddSuperviseurActivity.this.e_reseau);
            params.put(AddSuperviseurActivity.KEY_MEMBER_CODE, this.val$member_code);
            params.put(AddSuperviseurActivity.KEY_LATITUDE, AddSuperviseurActivity.this.latitude);
            params.put(AddSuperviseurActivity.KEY_LONGITUDE, AddSuperviseurActivity.this.longitude);
            params.put(AddSuperviseurActivity.KEY_COUNTRY, AddSuperviseurActivity.this.e_pays);
            params.put(AddSuperviseurActivity.KEY_CATEGORY, "superviseur");
            params.put(AddSuperviseurActivity.KEY_VALIDATE, "non");
            params.put(AddSuperviseurActivity.KEY_TAG, "register");
            return params;
        }
    }

    public AddSuperviseurActivity() {
        this.e_pays = "Gabon";
        this.ListPays = null;
        this.ListReseau = null;
        this.latitude = "0";
        this.longitude = "0";
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1558R.layout.activity_register_superviseur);
        getSupportActionBar().hide();
        this.firstname = (EditText) findViewById(C1558R.id.firstname);
        this.lastname = (EditText) findViewById(C1558R.id.lastname);
        this.editTextPassword = (EditText) findViewById(C1558R.id.editTextPassword);
        this.editTextPasswordRepeat = (EditText) findViewById(C1558R.id.editTextPasswordRepeat);
        this.editTextEmail = (EditText) findViewById(C1558R.id.editTextEmail);
        this.editTextPhone = (EditText) findViewById(C1558R.id.editTextPhone);
        this.editTextMember = (EditText) findViewById(C1558R.id.editTextMemberCode);
        this.buttonRegister = (Button) findViewById(C1558R.id.buttonRegister);
        this.buttonRegister.setOnClickListener(this);
        this.ListPays = (Spinner) findViewById(C1558R.id.CountryCode);
        List<String> listePays = new ArrayList();
        this.paysItem = getResources().getStringArray(C1558R.array.country_code);
        for (Object add : this.paysItem) {
            listePays.add(add);
        }
        ArrayAdapter<String> paysAdapter = new ArrayAdapter(this, 17367048, listePays);
        paysAdapter.setDropDownViewResource(17367049);
        this.ListPays.setAdapter(paysAdapter);
        this.ListPays.setOnItemSelectedListener(new C15101());
        this.ListReseau = (Spinner) findViewById(C1558R.id.Network);
        List<String> listReseau = new ArrayList();
        this.reseauItem = getResources().getStringArray(C1558R.array.network_gabon);
        for (Object add2 : this.reseauItem) {
            listReseau.add(add2);
        }
        ArrayAdapter<String> reseauAdapter = new ArrayAdapter(this, 17367048, listReseau);
        reseauAdapter.setDropDownViewResource(17367049);
        this.ListReseau.setAdapter(reseauAdapter);
        this.ListReseau.setOnItemSelectedListener(new C15112());
    }

    public void onClick(View v) {
        if (v == this.buttonRegister) {
            registerUser();
        }
    }

    public void onResume() {
        super.onResume();
        this.locationManager = (LocationManager) getSystemService("location");
        if (this.locationManager.isProviderEnabled("gps")) {
            abonnementGPS();
        }
    }

    public void onPause() {
        super.onPause();
        desabonnementGPS();
    }

    public void abonnementGPS() {
        this.locationManager.requestLocationUpdates("gps", 5000, 10.0f, this);
    }

    public void desabonnementGPS() {
        this.locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location) {
        String email = getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_EMAIL, "Not Available");
        StringBuilder msg = new StringBuilder("latitude : ");
        msg.append(location.getLatitude());
        msg.append("; longitude : ");
        msg.append(location.getLongitude());
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
    }

    public void onProviderDisabled(String provider) {
        if ("gps".equals(provider)) {
            desabonnementGPS();
        }
    }

    public void onProviderEnabled(String provider) {
        if ("gps".equals(provider)) {
            abonnementGPS();
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void registerUser() {
        String prenom = this.firstname.getText().toString().trim();
        String nom = this.lastname.getText().toString().trim();
        String password = this.editTextPassword.getText().toString().trim();
        String email = this.editTextEmail.getText().toString().trim();
        String phone = this.editTextPhone.getText().toString().trim();
        String member_code = this.editTextMember.getText().toString().trim();
        if (this.lastname.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextPassword.getText().toString().equals(BuildConfig.VERSION_NAME) || this.firstname.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextPhone.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextEmail.getText().toString().equals(BuildConfig.VERSION_NAME) || this.editTextMember.getText().toString().equals(BuildConfig.VERSION_NAME)) {
            Toast.makeText(getApplicationContext(), "Un ou plusieurs champs sont vides", 0).show();
        } else if (this.editTextPhone.getText().toString().length() <= 4) {
            Toast.makeText(getApplicationContext(), "Le pseudonyme doit \u00eatre au minimum de 5 caract\u00e8res", 0).show();
        } else if (this.editTextPassword.getText().toString().length() != this.editTextPasswordRepeat.getText().toString().length()) {
            Toast.makeText(getApplicationContext(), "Les mots de passe entres ne correspondent pas. Essayez de nouveau!", 0).show();
        } else if (this.latitude == null || this.longitude == null) {
            Toast.makeText(this, "Pas encore localise!", 1).show();
        } else {
            Volley.newRequestQueue(this).add(new C15145(1, REGISTER_URL, new C15123(), new C15134(), prenom, nom, password, email, phone, member_code));
        }
    }
}
