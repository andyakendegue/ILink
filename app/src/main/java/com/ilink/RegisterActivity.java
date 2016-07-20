package com.ilink;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.location.LocationListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private EditText editTextMember;
    private TextView locatedText;
    private TextView latitudeText;
    private TextView longitudeText;
    private ImageView locatedImage;

    private Button buttonRegister;
    private LocationManager locationManager;
    private String latitude = "0";
    private String longitude = "0";

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        CheckEnableGPS();


        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordRepeat = (EditText) findViewById(R.id.editTextPasswordRepeat);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextMember = (EditText) findViewById(R.id.editTextMemberCode);
        locatedText = (TextView) findViewById(R.id.textLocated);
        latitudeText = (TextView) findViewById(R.id.textLatitude);
        longitudeText = (TextView) findViewById(R.id.textLongitude);
        locatedImage = (ImageView) findViewById(R.id.imageLocated);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        buttonRegister.setOnClickListener(this);


        // Map Location

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRegister));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }


        // End Map Location


        // Pays

        ListPays = (Spinner) findViewById(R.id.CountryCode);
        List<String> listePays = new ArrayList<String>();
        paysItem = getResources().getStringArray(R.array.country_code);
        final int paysLength = paysItem.length;

        for (int i = 0; i < paysLength; i++) {
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
                if (e_pays.equals("Burkina-Faso")) {
                    reseauItem = getResources().getStringArray(R.array.network_burkina);

                } else if (e_pays.equals("Cameroun")) {
                    reseauItem = getResources().getStringArray(R.array.network_cameroun);

                } else if (e_pays.equals("France")) {
                    reseauItem = getResources().getStringArray(R.array.network_france);

                } else if (e_pays.equals("Gabon")) {
                    reseauItem = getResources().getStringArray(R.array.network_gabon);
                }
                List<String> listReseau = new ArrayList<String>();
                final int reseauLength = reseauItem.length;

                for (int i = 0; i < reseauLength; i++) {
                    listReseau.add(reseauItem[i]);

                }
                ArrayAdapter<String> reseauAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, listReseau);
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

    }


    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerUser();
        }
    }

    // Locations Method
    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                map.setMyLocationEnabled(true);
            }


            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            connectClient();
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }

    }



    // End Location Method

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

//Obtention de la référence du service
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            abonnementGPS();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
//On appelle la méthode pour se désabonner
        desabonnementGPS();

    }


    /**
     * Méthode permettant de s'abonner à la localisation par GPS.
     */
    public void abonnementGPS() {
        //On s'abonne
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {

            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, (android.location.LocationListener) this);

        }

    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            //locationManager.removeUpdates((android.location.LocationListener) this);

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(final Location location) {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        //On affiche dans un Toat la nouvelle Localisation
        final StringBuilder msg = new StringBuilder("latitude : ");
        msg.append(location.getLatitude());
        msg.append("; longitude : ");
        msg.append(location.getLongitude());

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        //Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();

        if (latitude != "0" && longitude != "0") {
            locatedText.setText("Vous avez été localisé");
            locatedImage.setImageResource(R.drawable.gps_located);
            latitudeText.setText(latitude);
            longitudeText.setText(longitude);
        }


    }

    /**
     * {@inheritDoc}
     */

    /*
    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if ("gps".equals(provider)) {
            desabonnementGPS();
        }
    }


    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if ("gps".equals(provider)) {
            abonnementGPS();
        }
    }


    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
    }
    */

    // Other Location Method

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    // End other Location Method

    private void registerUser() {
        final String prenom = firstname.getText().toString().trim();
        final String nom = lastname.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        final String phone = editTextPhone.getText().toString().trim();
        final String member_code = editTextMember.getText().toString().trim();

        // Test values
        if ((!lastname.getText().toString().equals("")) && (!editTextPassword.getText().toString().equals("")) && (!firstname.getText().toString().equals("")) && (!editTextPhone.getText().toString().equals("")) && (!editTextEmail.getText().toString().equals("")) && (!editTextMember.getText().toString().equals(""))) {
            if (editTextPhone.getText().toString().length() > 4) {


                if (editTextPassword.getText().toString().length() == editTextPasswordRepeat.getText().toString().length()) {

                    if (latitude != null && longitude != null) {


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                                            Toast.makeText(RegisterActivity.this, "Enregistrement reussi! Recuperez le code de validation qui vous a ete envoye, ensuite, reconnectez-vous avec le numero de telephone et le mot de passe specifie lors de votre enregistrement.", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(RegisterActivity.this, LoginGeolocatedActivity.class);
                                            startActivity(i);

                                            finish();
                                        }else {
                                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(RegisterActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_LONG).show();
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
                                params.put(KEY_CATEGORY, "geolocated");
                                params.put(KEY_VALIDATE, "non");
                                params.put(KEY_TAG, "register_geolocated");
                                return params;
                            }


                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                        requestQueue.add(stringRequest);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Pas encore localise!", Toast.LENGTH_LONG).show();
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


    private void CheckEnableGPS(){


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Vos options de localisation semblent ne pas être activée. Le GPS et la localisation par le réseau (Wifi ou réseau mobile) doivent être tous les deux activés. Souhaitez vous le faire et profiter pleinement des fonctions de Nkala?");
            dialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }

    }



}
