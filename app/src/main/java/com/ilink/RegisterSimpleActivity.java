package com.ilink;

import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSimpleActivity extends AppCompatActivity implements View.OnClickListener {

    // LISTE PAYS
    private String e_pays = "Gabon";
    private Spinner ListPays = null;
    private String[] paysItem;

    // LISTE RESEAU
    private String e_reseau;
    private Spinner ListReseau = null;
    private String[] reseauItem;

    private static final String REGISTER_URL = "http://ilink-app.com/app/";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_COUNTRY = "country_code";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_MEMBER_CODE = "member_code";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_VALIDATE = "validate";
    public static final String KEY_TAG = "tag";


    private EditText firstname;
    private EditText lastname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    private EditText editTextPhone;

    private Button buttonRegister;
    private LocationManager locationManager;
    private String latitude = "0";
    private String longitude = "0";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static String phone ;
    private static String mdp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_simple);
        // get Extras
        Intent in = getIntent();
        // Extras a recevoir
        phone = in.getStringExtra("phone");
        mdp = in.getStringExtra("password");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setText(mdp);
        editTextPasswordRepeat = (EditText) findViewById(R.id.editTextPasswordRepeat);
        editTextPasswordRepeat.setText(mdp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPhone.setText(phone);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);



        buttonRegister.setOnClickListener(this);

        // Pays

        ListPays = (Spinner) findViewById(R.id.CountryCode);
        List<String> listePays = new ArrayList<String>();
        paysItem = getResources().getStringArray(R.array.country_code);
        final int paysLength = paysItem.length;

        for (int i = 0 ; i < paysLength ; i++) {
            listePays.add(paysItem[i]);

        }
        ArrayAdapter<String> paysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listePays);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        paysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ListPays.setAdapter(paysAdapter);
        ListPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                e_pays = parent.getItemAtPosition(position).toString();
                if(e_pays.equals("Burkina-Faso")){
                    reseauItem = getResources().getStringArray(R.array.network_burkina);

                } else if (e_pays.equals("Cameroun")) {
                    reseauItem = getResources().getStringArray(R.array.network_cameroun);

                } else if (e_pays.equals("France")) {
                    reseauItem = getResources().getStringArray(R.array.network_france);

                } else if (e_pays.equals("Gabon")){
                    reseauItem = getResources().getStringArray(R.array.network_gabon);
                }
                List<String> listReseau = new ArrayList<String>();
                final int reseauLength = reseauItem.length;

                for (int i = 0; i < reseauLength; i++) {
                    listReseau.add(reseauItem[i]);

                }
                ArrayAdapter<String> reseauAdapter = new ArrayAdapter<String>(RegisterSimpleActivity.this, android.R.layout.simple_spinner_item, listReseau);
                //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
                reseauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ListReseau.setAdapter(reseauAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                e_pays = paysItem[0].toString();
            }

        });

        // Reseau


        ListReseau = (Spinner) findViewById(R.id.Network);
        List<String> listReseau = new ArrayList<String>();
        reseauItem = getResources().getStringArray(R.array.network_gabon);
        final int reseauLength = reseauItem.length;

        for (int i = 0; i < reseauLength; i++) {
            listReseau.add(reseauItem[i]);

        }
        ArrayAdapter<String> reseauAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listReseau);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        reseauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ListReseau.setAdapter(reseauAdapter);
        ListReseau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                e_reseau = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                e_reseau = reseauItem[0].toString();

            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerUser();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();


    }






    private void registerUser() {
        final String prenom = firstname.getText().toString().trim();
        final String nom = lastname.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        final String phone = editTextPhone.getText().toString().trim();
        final String member_code = "0";

        // Test values
        if ((!lastname.getText().toString().equals("")) && (!editTextPassword.getText().toString().equals("")) && (!firstname.getText().toString().equals("")) && (!editTextPhone.getText().toString().equals("")) && (!editTextEmail.getText().toString().equals(""))) {
            if (editTextPhone.getText().toString().length() > 4) {


                if (editTextPassword.getText().toString().length() == editTextPasswordRepeat.getText().toString().length()) {

                    if (latitude != null && longitude != null) {


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                                            Toast.makeText(RegisterSimpleActivity.this, "Enregistrement reussi! Recuperez le code de validation qui vous a ete envoye, ensuite, reconnectez-vous avec le numero de telephone et le mot de passe specifie lors de votre enregistrement.", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(RegisterSimpleActivity.this, LoginActivity.class);
                                            startActivity(i);

                                            finish();
                                        }else{
                                            //If the server response is not success
                                            //Displaying an error message on toast
                                            Toast.makeText(RegisterSimpleActivity.this, "Probleme lors de l'enregistrement", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(RegisterSimpleActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put(KEY_FIRSTNAME, prenom);
                                params.put(KEY_LASTNAME, nom);
                                params.put(KEY_PASSWORD, password);
                                params.put(KEY_EMAIL, email);
                                params.put(KEY_PHONE, phone);
                                params.put(KEY_NETWORK, e_reseau);
                                params.put(KEY_MEMBER_CODE, member_code);
                                params.put(KEY_LATITUDE, latitude);
                                params.put(KEY_LONGITUDE, longitude);
                                params.put(KEY_COUNTRY, e_pays);
                                params.put(KEY_CATEGORY, "utilisateur");
                                params.put(KEY_VALIDATE, "non");
                                params.put(KEY_TAG, "register");
                                return params;
                            }


                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(stringRequest);


                    } else {
                        Toast.makeText(RegisterSimpleActivity.this, "Pas encore localise!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Les mots de passe entres ne correspondent pas. Essayez de nouveau!", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Le pseudonyme doit être au minimum de 5 caractères", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Un ou plusieurs champs sont vides", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ilink/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ilink/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
