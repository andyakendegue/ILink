package com.ilink;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting.TravelMode;
import com.directions.route.BuildConfig;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wearable.MessageApi;
import com.ilink.Util.Operations;
import com.ilink.app.AppController;
import com.ilink.model.myMarker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener, OnMarkerClickListener, LocationListener, RoutingListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static int ADVICE_TIME_OUT = 0;
    private static final int[] COLORS;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TAG = "tag";
    private static final String LOG_TAG = "MyActivity";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    private long FASTEST_INTERVAL;
    private Spinner ListDistance;
    private Spinner ListMontant;
    private String TAG;
    private long UPDATE_INTERVAL;
    private boolean checked;
    private int connexionCompte;
    private Criteria crit;
    private Location currentLoc;
    private View customSearch;
    private TextView displayDistance;
    private TextView displayDuration;
    private String[] distanceItem;
    private String e_distance;
    private String e_montant;
    protected LatLng end;
    private FrameLayout frameLayout;
    private ImageButton imageBtnRoute;
    private LayoutInflater inflater;
    Double latitude;
    private LocationManager locationManager;
    private boolean loggedIn;
    Double longitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private HashMap<Marker, myMarker> mMarkersHashMap;
    private ArrayList<myMarker> mMyMarkersArray;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private RelativeLayout mapLayout;
    private Marker marker;
    private String[] montantItem;
    private boolean notShowing;
    private MaterialDialog pDialog;
    private ArrayList<Polyline> polylines;
    private LinearLayout relativeLayout;
    private Button sendRoute;
    private SharedPreferences sharedPreferences;
    protected LatLng start;
    private int typeReseau;

    /* renamed from: com.ilink.MapsActivity.17 */
    class AnonymousClass17 extends StringRequest {
        final /* synthetic */ Double val$latit;
        final /* synthetic */ Double val$longit;
        final /* synthetic */ String val$phoneT;

        AnonymousClass17(int x0, String x1, Listener x2, ErrorListener x3, String str, Double d, Double d2) {
            this.val$phoneT = str;
            this.val$latit = d;
            this.val$longit = d2;
            super(x0, x1, x2, x3);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
            params.put(MapsActivity.KEY_PHONE, this.val$phoneT);
            params.put(MapsActivity.KEY_LATITUDE, String.valueOf(this.val$latit));
            params.put(MapsActivity.KEY_LONGITUDE, String.valueOf(this.val$longit));
            params.put(MapsActivity.KEY_TAG, "updateLocation");
            return params;
        }
    }

    /* renamed from: com.ilink.MapsActivity.1 */
    class C15471 implements OnClickListener {
        C15471() {
        }

        public void onClick(View view) {
            MapsActivity.this.logout();
        }
    }

    /* renamed from: com.ilink.MapsActivity.2 */
    class C15482 implements OnClickListener {
        C15482() {
        }

        public void onClick(View v) {
            MapsActivity.this.inflater = (LayoutInflater) MapsActivity.this.getSystemService("layout_inflater");
            MapsActivity.this.customSearch = MapsActivity.this.inflater.inflate(C1558R.layout.dialog_customlayout, null, false);
            MapsActivity.this.ListDistance = (Spinner) MapsActivity.this.customSearch.findViewById(C1558R.id.spinDistance);
            List<String> listeDistance = new ArrayList();
            MapsActivity.this.distanceItem = MapsActivity.this.getResources().getStringArray(C1558R.array.distance_minimale);
            for (Object add : MapsActivity.this.distanceItem) {
                listeDistance.add(add);
            }
            ArrayAdapter<String> distanceAdapter = new ArrayAdapter(MapsActivity.this, 17367048, listeDistance);
            distanceAdapter.setDropDownViewResource(17367049);
            MapsActivity.this.ListDistance.setAdapter(distanceAdapter);
            MapsActivity.this.ListDistance.setOnItemSelectedListener(new 1(this));
            MapsActivity.this.ListMontant = (Spinner) MapsActivity.this.customSearch.findViewById(C1558R.id.spinMontant);
            List<String> listeMontant = new ArrayList();
            MapsActivity.this.montantItem = MapsActivity.this.getResources().getStringArray(C1558R.array.montant_transaction);
            for (Object add2 : MapsActivity.this.montantItem) {
                listeMontant.add(add2);
            }
            ArrayAdapter<String> montantAdapter = new ArrayAdapter(MapsActivity.this, 17367048, listeMontant);
            montantAdapter.setDropDownViewResource(17367049);
            MapsActivity.this.ListMontant.setAdapter(montantAdapter);
            MapsActivity.this.ListMontant.setOnItemSelectedListener(new 2(this));
            new Builder(MapsActivity.this).title((CharSequence) "Affinez votre recherche").customView(MapsActivity.this.customSearch, true).positiveText((CharSequence) "Rechercher").onPositive(new 4(this)).negativeText((CharSequence) "Annuler").onNegative(new 3(this)).show();
        }
    }

    /* renamed from: com.ilink.MapsActivity.3 */
    class C15493 implements OnCheckedChangeListener {
        C15493() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                MapsActivity.this.frameLayout.setVisibility(4);
                MapsActivity.this.typeReseau = 2;
                MapsActivity.this.map.clear();
                new CheckAllLocations(null).execute(new String[0]);
                MapsActivity.this.map.setOnMapLoadedCallback(new 2(this));
                return;
            }
            MapsActivity.this.typeReseau = 1;
            MapsActivity.this.map.clear();
            MapsActivity.this.map.setOnMapLoadedCallback(new 1(this));
            new CheckLocations(null).execute(new String[0]);
        }
    }

    /* renamed from: com.ilink.MapsActivity.4 */
    class C15504 implements OnClickListener {
        C15504() {
        }

        public void onClick(View view) {
            MapsActivity.this.sendRequest();
        }
    }

    /* renamed from: com.ilink.MapsActivity.5 */
    class C15515 implements OnClickListener {
        C15515() {
        }

        public void onClick(View view) {
            MapsActivity.this.sendRequest();
        }
    }

    /* renamed from: com.ilink.MapsActivity.6 */
    class C15526 implements Runnable {
        C15526() {
        }

        public void run() {
            MapsActivity.this.advice();
        }
    }

    /* renamed from: com.ilink.MapsActivity.7 */
    class C15537 implements Listener<JSONArray> {
        C15537() {
        }

        public void onResponse(JSONArray response) {
            Log.d(MapsActivity.this.TAG, response.toString());
            MapsActivity.this.hidePDialog();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    String network = MapsActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(RegisterSimpleActivity.KEY_NETWORK, "Pas disponible");
                    if (obj.getString(MapsActivity.KEY_LATITUDE) != null && obj.getString(RegisterSimpleActivity.KEY_NETWORK).equals(network) && obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equals("geolocated") && obj.getString("active").equals("oui")) {
                        MapsActivity.this.map.addMarker(new MarkerOptions().title(obj.getString(RegisterSimpleActivity.KEY_LASTNAME)).position(new LatLng(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)), Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)))).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.marker_member)));
                    }
                } catch (JSONException e) {
                    Toast.makeText(MapsActivity.this, "Impossible de generer les marqueurs", 1).show();
                }
            }
        }
    }

    /* renamed from: com.ilink.MapsActivity.8 */
    class C15548 implements ErrorListener {
        C15548() {
        }

        public void onErrorResponse(VolleyError error) {
            VolleyLog.m15d(MapsActivity.this.TAG, "Error: " + error.getMessage());
            Toast.makeText(MapsActivity.this, "Connexion au serveur impossible", 1).show();
            MapsActivity.this.hidePDialog();
        }
    }

    /* renamed from: com.ilink.MapsActivity.9 */
    class C15559 implements Listener<JSONArray> {
        C15559() {
        }

        public void onResponse(JSONArray response) {
            Log.d(MapsActivity.this.TAG, response.toString());
            MapsActivity.this.hidePDialog();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    String network = MapsActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(RegisterSimpleActivity.KEY_NETWORK, "Pas disponible");
                    if (obj.getString(MapsActivity.KEY_LATITUDE) != null && obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equals("geolocated") && obj.getString("active").equals("oui")) {
                        MapsActivity.this.map.addMarker(new MarkerOptions().title(obj.getString(RegisterSimpleActivity.KEY_LASTNAME)).position(new LatLng(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)), Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)))).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.marker_member)));
                    }
                } catch (JSONException e) {
                    Toast.makeText(MapsActivity.this, "Impossible de generer les marqueurs", 1).show();
                }
            }
        }
    }

    private class CheckAllLocations extends AsyncTask<String, String, Boolean> {
        private CheckAllLocations() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.pDialog = new Builder(MapsActivity.this).title((CharSequence) "Attendez svp!").content((CharSequence) "V\u00e9rification de la connexion r\u00e9seau").progress(true, 0).cancelable(false).show();
        }

        protected Boolean doInBackground(String... args) {
            NetworkInfo netInfo = ((ConnectivityManager) MapsActivity.this.getBaseContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                if (Operations.isOnline(MapsActivity.this)) {
                    return Boolean.valueOf(true);
                }
                Toast.makeText(MapsActivity.this, "Pas de connexion internet", 0).show();
            }
            return Boolean.valueOf(false);
        }

        protected void onPostExecute(Boolean th) {
            if (th.booleanValue()) {
                MapsActivity.this.hidePDialog();
                MapsActivity.this.allLocations();
                return;
            }
            MapsActivity.this.hidePDialog();
            Toast.makeText(MapsActivity.this.getBaseContext().getApplicationContext(), "Erreur lors de la connexion au r\u00e9seau", 1).show();
        }
    }

    private class CheckLocations extends AsyncTask<String, String, Boolean> {
        private CheckLocations() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.pDialog = new Builder(MapsActivity.this).title((CharSequence) "Attendez svp!").content((CharSequence) "V\u00e9rification de la connexion r\u00e9seau").progress(true, 0).cancelable(false).show();
        }

        protected Boolean doInBackground(String... args) {
            NetworkInfo netInfo = ((ConnectivityManager) MapsActivity.this.getBaseContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                if (Operations.isOnline(MapsActivity.this)) {
                    return Boolean.valueOf(true);
                }
                Toast.makeText(MapsActivity.this, "Pas de connexion internet", 0).show();
            }
            return Boolean.valueOf(false);
        }

        protected void onPostExecute(Boolean th) {
            if (th.booleanValue()) {
                MapsActivity.this.hidePDialog();
                MapsActivity.this.locations();
                return;
            }
            MapsActivity.this.hidePDialog();
            Toast.makeText(MapsActivity.this.getBaseContext().getApplicationContext(), "Erreur lors de la connexion au r\u00e9seau", 1).show();
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            this.mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            this.mDialog = dialog;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return this.mDialog;
        }
    }

    private class SearchAllLocations extends AsyncTask<String, String, Boolean> {
        private SearchAllLocations() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MapsActivity.this.pDialog = new Builder(MapsActivity.this).title((CharSequence) "Attendez svp!").content((CharSequence) "V\u00e9rification de la connexion r\u00e9seau").progress(true, 0).cancelable(false).show();
        }

        protected Boolean doInBackground(String... args) {
            NetworkInfo netInfo = ((ConnectivityManager) MapsActivity.this.getBaseContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                if (Operations.isOnline(MapsActivity.this)) {
                    return Boolean.valueOf(true);
                }
                Toast.makeText(MapsActivity.this, "Pas de connexion internet", 0).show();
            }
            return Boolean.valueOf(false);
        }

        protected void onPostExecute(Boolean th) {
            if (th.booleanValue()) {
                MapsActivity.this.hidePDialog();
                MapsActivity.this.searchallLocations(MapsActivity.this.e_distance, MapsActivity.this.e_montant);
                return;
            }
            MapsActivity.this.hidePDialog();
            Toast.makeText(MapsActivity.this.getBaseContext().getApplicationContext(), "Erreur lors de la connexion au r\u00e9seau", 1).show();
        }
    }

    public MapsActivity() {
        this.TAG = KEY_TAG;
        this.notShowing = false;
        this.loggedIn = false;
        this.typeReseau = 1;
        this.crit = new Criteria();
        this.UPDATE_INTERVAL = 60000;
        this.FASTEST_INTERVAL = 5000;
        this.mMyMarkersArray = new ArrayList();
    }

    static {
        ADVICE_TIME_OUT = GamesStatusCodes.STATUS_ACHIEVEMENT_UNLOCK_FAILURE;
        COLORS = new int[]{C1558R.color.primary_dark, C1558R.color.primary, C1558R.color.primary_light, C1558R.color.accent, C1558R.color.primary_dark_material_light};
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1558R.layout.activity_maps);
        CheckEnableGPS();
        this.latitude = Double.valueOf(0.0d);
        this.longitude = Double.valueOf(0.0d);
        this.displayDistance = (TextView) findViewById(C1558R.id.textDistance);
        this.displayDuration = (TextView) findViewById(C1558R.id.textDuration);
        this.frameLayout = (FrameLayout) findViewById(C1558R.id.frameMaps);
        this.frameLayout.setVisibility(4);
        this.relativeLayout = (LinearLayout) findViewById(C1558R.id.tgLayout);
        this.sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        this.connexionCompte = 0;
        FloatingActionButton fab = (FloatingActionButton) findViewById(C1558R.id.fabMap);
        fab.setOnClickListener(new C15471());
        fab.setVisibility(4);
        ((FloatingActionButton) findViewById(C1558R.id.fabSearch)).setOnClickListener(new C15482());
        this.mapLayout = (RelativeLayout) findViewById(C1558R.id.maplayout);
        this.polylines = new ArrayList();
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(C1558R.id.map);
        if (this.mapFragment != null) {
            this.mapFragment.getMapAsync(this);
        }
        new CheckLocations().execute(new String[0]);
        this.locationManager = (LocationManager) getSystemService("location");
        this.crit.setAccuracy(1);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.currentLoc = this.locationManager.getLastKnownLocation(this.locationManager.getBestProvider(this.crit, true));
            ((ToggleButton) findViewById(C1558R.id.tgBtnSimple)).setOnCheckedChangeListener(new C15493());
            this.sendRoute = (Button) findViewById(C1558R.id.sendRoute);
            this.sendRoute.setOnClickListener(new C15504());
            this.sendRoute.setVisibility(4);
            this.imageBtnRoute = (ImageButton) findViewById(C1558R.id.imageBtnRoute);
            this.imageBtnRoute.setOnClickListener(new C15515());
            this.imageBtnRoute.setVisibility(4);
            this.loggedIn = getSharedPreferences(Config.SHARED_PREF_NAME, 0).getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
            if (this.loggedIn) {
                fab.setVisibility(0);
            }
            new Handler().postDelayed(new C15526(), (long) ADVICE_TIME_OUT);
        }
    }

    public void onBackPressed() {
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onLocationChanged(Location currentloc) {
        currentloc.setAccuracy(TextTrackStyle.DEFAULT_FONT_SCALE);
        StringBuilder msg = new StringBuilder("latitude : ");
        msg.append(currentloc.getLatitude());
        msg.append("; longitude : ");
        msg.append(currentloc.getLongitude());
        if (this.marker != null) {
            this.marker.remove();
        }
        LatLng latLng = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
        this.latitude = Double.valueOf(currentloc.getLatitude());
        this.longitude = Double.valueOf(currentloc.getLongitude());
        this.start = latLng;
        String phone = getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_PHONE, "Not Available");
        this.marker = this.map.addMarker(new MarkerOptions().title("Vous \u00eates ici!").position(latLng).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.main_marker)));
        if (this.connexionCompte == 0) {
            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            this.connexionCompte = 1;
        }
        this.marker.showInfoWindow();
    }

    private void locations() {
        this.pDialog = new Builder(this).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new C15537(), new C15548());
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void allLocations() {
        this.pDialog = new Builder(this).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new C15559(), new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.m15d(MapsActivity.this.TAG, "Error: " + error.getMessage());
                Toast.makeText(MapsActivity.this, "Connexion au serveur impossible", 1).show();
                MapsActivity.this.hidePDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void searchallLocations(String distance, String montant) {
        this.pDialog = new Builder(this).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement des marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d(MapsActivity.this.TAG, response.toString());
                MapsActivity.this.hidePDialog();
                float distance_minimale = 0.0f;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        LatLng latLng;
                        Marker custom;
                        Location starting;
                        Location arrival;
                        float localDistance;
                        float dista;
                        String dist;
                        JSONObject obj = response.getJSONObject(i);
                        MapsActivity mapsActivity = MapsActivity.this;
                        String network = r0.getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(RegisterSimpleActivity.KEY_NETWORK, "Pas disponible");
                        if (MapsActivity.this.typeReseau == 1) {
                            if (obj.getString(MapsActivity.KEY_LATITUDE) != null) {
                                if (obj.getString(RegisterSimpleActivity.KEY_NETWORK).equals(network)) {
                                    if (obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equals("geolocated")) {
                                        if (obj.getString("active").equals("oui")) {
                                            latLng = new LatLng(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)), Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)));
                                            custom = MapsActivity.this.map.addMarker(new MarkerOptions().title(obj.getString(RegisterSimpleActivity.KEY_LASTNAME)).position(latLng).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.marker_member)));
                                            starting = new Location("Depart");
                                            starting.setLatitude(MapsActivity.this.start.latitude);
                                            starting.setLongitude(MapsActivity.this.start.longitude);
                                            arrival = new Location("Arrivee");
                                            arrival.setLatitude(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)));
                                            arrival.setLongitude(Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)));
                                            localDistance = MapsActivity.this.getlocalDistance(starting, arrival);
                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("500m")) {
                                                distance_minimale = 500.0f;
                                            } else {
                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("1km")) {
                                                    distance_minimale = 1000.0f;
                                                } else {
                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("1,5km")) {
                                                        distance_minimale = 1500.0f;
                                                    } else {
                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("2km")) {
                                                            distance_minimale = 2000.0f;
                                                        } else {
                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("2,5km")) {
                                                                distance_minimale = 2500.0f;
                                                            } else {
                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("3km")) {
                                                                    distance_minimale = 3000.0f;
                                                                } else {
                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("4km")) {
                                                                        distance_minimale = 4000.0f;
                                                                    } else {
                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("5km")) {
                                                                            distance_minimale = 5000.0f;
                                                                        } else {
                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("6km")) {
                                                                                distance_minimale = 6000.0f;
                                                                            } else {
                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("7km")) {
                                                                                    distance_minimale = 7000.0f;
                                                                                } else {
                                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("8km")) {
                                                                                        distance_minimale = 8000.0f;
                                                                                    } else {
                                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("9km")) {
                                                                                            distance_minimale = 9000.0f;
                                                                                        } else {
                                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("10km")) {
                                                                                                distance_minimale = 10000.0f;
                                                                                            } else {
                                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("20km")) {
                                                                                                    distance_minimale = 20000.0f;
                                                                                                } else {
                                                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("30km")) {
                                                                                                        distance_minimale = 30000.0f;
                                                                                                    } else {
                                                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("50km")) {
                                                                                                            distance_minimale = 50000.0f;
                                                                                                        } else {
                                                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("100km")) {
                                                                                                                distance_minimale = 100000.0f;
                                                                                                            } else {
                                                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("200km")) {
                                                                                                                    distance_minimale = 200000.0f;
                                                                                                                } else {
                                                                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("Tous les points")) {
                                                                                                                        distance_minimale = 2.0E9f;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (localDistance >= distance_minimale) {
                                                custom.setVisible(false);
                                            }
                                            dista = (float) (((double) Math.round(((double) localDistance) * 100.0d)) / 100.0d);
                                            dist = dista + " M";
                                            if (localDistance > 1000.0f) {
                                                dista = (float) (((double) Math.round(((double) (localDistance / 1000.0f)) * 100.0d)) / 100.0d);
                                                dista + " KM";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (MapsActivity.this.typeReseau == 2) {
                            if (obj.getString(MapsActivity.KEY_LATITUDE) != null) {
                                if (obj.getString(RegisterSimpleActivity.KEY_CATEGORY).equals("geolocated")) {
                                    if (obj.getString("active").equals("oui")) {
                                        latLng = new LatLng(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)), Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)));
                                        custom = MapsActivity.this.map.addMarker(new MarkerOptions().title(obj.getString(RegisterSimpleActivity.KEY_LASTNAME)).position(latLng).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.marker_member)));
                                        starting = new Location("Depart");
                                        starting.setLatitude(MapsActivity.this.start.latitude);
                                        starting.setLongitude(MapsActivity.this.start.longitude);
                                        arrival = new Location("Arrivee");
                                        arrival.setLatitude(Double.parseDouble(obj.getString(MapsActivity.KEY_LATITUDE)));
                                        arrival.setLongitude(Double.parseDouble(obj.getString(MapsActivity.KEY_LONGITUDE)));
                                        localDistance = MapsActivity.this.getlocalDistance(starting, arrival);
                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("500m")) {
                                            distance_minimale = 500.0f;
                                        } else {
                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("1km")) {
                                                distance_minimale = 1000.0f;
                                            } else {
                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("1,5km")) {
                                                    distance_minimale = 1500.0f;
                                                } else {
                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("2km")) {
                                                        distance_minimale = 2000.0f;
                                                    } else {
                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("2,5km")) {
                                                            distance_minimale = 2500.0f;
                                                        } else {
                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("3km")) {
                                                                distance_minimale = 3000.0f;
                                                            } else {
                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("4km")) {
                                                                    distance_minimale = 4000.0f;
                                                                } else {
                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("5km")) {
                                                                        distance_minimale = 5000.0f;
                                                                    } else {
                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("6km")) {
                                                                            distance_minimale = 6000.0f;
                                                                        } else {
                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("7km")) {
                                                                                distance_minimale = 7000.0f;
                                                                            } else {
                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("8km")) {
                                                                                    distance_minimale = 8000.0f;
                                                                                } else {
                                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("9km")) {
                                                                                        distance_minimale = 9000.0f;
                                                                                    } else {
                                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("10km")) {
                                                                                            distance_minimale = 10000.0f;
                                                                                        } else {
                                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("20km")) {
                                                                                                distance_minimale = 20000.0f;
                                                                                            } else {
                                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("30km")) {
                                                                                                    distance_minimale = 30000.0f;
                                                                                                } else {
                                                                                                    if (MapsActivity.this.e_distance.equalsIgnoreCase("50km")) {
                                                                                                        distance_minimale = 50000.0f;
                                                                                                    } else {
                                                                                                        if (MapsActivity.this.e_distance.equalsIgnoreCase("100km")) {
                                                                                                            distance_minimale = 100000.0f;
                                                                                                        } else {
                                                                                                            if (MapsActivity.this.e_distance.equalsIgnoreCase("200km")) {
                                                                                                                distance_minimale = 200000.0f;
                                                                                                            } else {
                                                                                                                if (MapsActivity.this.e_distance.equalsIgnoreCase("Tous les points")) {
                                                                                                                    distance_minimale = 2.0E9f;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (localDistance >= distance_minimale) {
                                            custom.setVisible(false);
                                        }
                                        dista = (float) (((double) Math.round(((double) localDistance) * 100.0d)) / 100.0d);
                                        dist = dista + " M";
                                        if (localDistance > 1000.0f) {
                                            dista = (float) (((double) Math.round(((double) (localDistance / 1000.0f)) * 100.0d)) / 100.0d);
                                            dista + " KM";
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MapsActivity.this, "Impossible de generer les marqueurs", 1).show();
                    }
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.m15d(MapsActivity.this.TAG, "Error: " + error.getMessage());
                Toast.makeText(MapsActivity.this, "Connexion au serveur impossible", 1).show();
                MapsActivity.this.hidePDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void hidePDialog() {
        if (this.pDialog != null) {
            this.pDialog.dismiss();
            this.pDialog = null;
        }
    }

    public void sendRequest() {
        if (Operations.isOnline(this)) {
            route();
        } else {
            Toast.makeText(this, "Pas de connexion internet", 0).show();
        }
    }

    public void route() {
        if (this.start == null || this.end == null) {
            Toast.makeText(this, "Votre position et votre destination sont inconnues.", 0).show();
            return;
        }
        this.pDialog = new Builder(this).title((CharSequence) "Attendez svp!").content((CharSequence) "Nous cherchons votre chemin...").progress(true, 0).cancelable(false).show();
        new Routing.Builder().travelMode(TravelMode.DRIVING).withListener(this).alternativeRoutes(false).waypoints(this.start, this.end).build().execute(new Void[0]);
        Location arrival = new Location("Destination");
        arrival.setLatitude(this.end.latitude);
        arrival.setLongitude(this.end.longitude);
        Location starting = new Location("Depart");
        starting.setLatitude(this.start.latitude);
        starting.setLongitude(this.start.longitude);
        toastDistance(starting, arrival);
        timeToDirection(getDirectionsUrl(this.start, this.end));
    }

    public void onRoutingFailure(RouteException e) {
        hidePDialog();
        if (e != null) {
            Toast.makeText(this, "Erreur", 1).show();
        } else {
            Toast.makeText(this, "Une erreur est survenue, Essayez encore", 0).show();
        }
    }

    public void onRoutingStart() {
    }

    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        hidePDialog();
        CameraUpdate center = CameraUpdateFactory.newLatLng(this.start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16.0f);
        this.map.moveCamera(center);
        if (this.polylines.size() > 0) {
            Iterator it = this.polylines.iterator();
            while (it.hasNext()) {
                ((Polyline) it.next()).remove();
            }
        }
        this.polylines = new ArrayList();
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[1]));
        polyOptions.width(11.0f);
        polyOptions.addAll(((Route) route.get(0)).getPoints());
        this.polylines.add(this.map.addPolyline(polyOptions));
        MarkerOptions options = new MarkerOptions();
        options.position(this.start);
        options.icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.start_blue));
        this.map.addMarker(options);
        options = new MarkerOptions();
        options.position(this.end);
        options.icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.end_green));
        this.map.addMarker(options);
    }

    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "La recherche du chemin a \u00e9t\u00e9 annul\u00e9e.");
    }

    public boolean onMarkerClick(Marker marker) {
        Log.i("MapsActivity", "onMarkerClick");
        String title = marker.getTitle();
        if (title.equals("Vous \u00eates ici!") || title.equals("Gabon")) {
            this.sendRoute.setVisibility(4);
            this.imageBtnRoute.setVisibility(4);
        } else {
            this.end = marker.getPosition();
            this.sendRoute.setVisibility(0);
            this.imageBtnRoute.setVisibility(0);
        }
        return false;
    }

    private void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Etes vous sur de vouloir vous deconnecter?");
        alertDialogBuilder.setPositiveButton((CharSequence) "Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Editor editor = MapsActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, 0).edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                editor.putString(MapsActivity.KEY_EMAIL, BuildConfig.VERSION_NAME);
                editor.putBoolean(Config.CHECK_BOX_ADVICE_PREF, false);
                editor.commit();
                MapsActivity.this.startActivity(new Intent(MapsActivity.this, MainActivity.class));
                MapsActivity.this.finish();
            }
        });
        alertDialogBuilder.setNegativeButton((CharSequence) "Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialogBuilder.create().show();
    }

    private void toastDistance(Location starting, Location arrival) {
        this.frameLayout.setVisibility(0);
        if (starting != null) {
            float distance = starting.distanceTo(arrival);
            String dist = ((float) (((double) Math.round(((double) distance) * 100.0d)) / 100.0d)) + " M";
            if (distance > 1000.0f) {
                dist = ((float) (((double) Math.round(((double) (distance / 1000.0f)) * 100.0d)) / 100.0d)) + " KM";
            }
            this.displayDistance.setText("Le point est situ\u00e9 \u00e0 : " + dist);
            return;
        }
        Toast.makeText(this, "Nous ne vous trouvons pas. Annulation...", 1).show();
    }

    private float getlocalDistance(Location start, Location arrive) {
        this.frameLayout.setVisibility(4);
        if (start != null) {
            return start.distanceTo(arrive);
        }
        Toast.makeText(this, "Nous ne vous trouvons pas. Annulation...", 1).show();
        return 0.0f;
    }

    private String getDistance(Location starting, Location arrival) {
        this.frameLayout.setVisibility(0);
        if (starting != null) {
            String dist;
            float distance = starting.distanceTo(arrival);
            if (distance > 1000.0f) {
                dist = ((float) (((double) Math.round(((double) (distance / 1000.0f)) * 100.0d)) / 100.0d)) + " KM";
            } else {
                dist = ((float) (((double) Math.round(((double) distance) * 100.0d)) / 100.0d)) + " M";
            }
            this.displayDistance.setText("Le point est situ\u00e9 \u00e0 : " + dist);
            return dist;
        }
        Toast.makeText(this, "Nous ne vous trouvons pas. Annulation...", 1).show();
        return null;
    }

    private void updateLocation(Double latit, Double longit, String phoneT) {
        String phone = getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_PHONE, "Not Available");
        Volley.newRequestQueue(this).add(new AnonymousClass17(1, REGISTER_URL, new Listener<String>() {
            public void onResponse(String response) {
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Connexion au serveur impossible", 1).show();
            }
        }, phoneT, latit, longit));
    }

    public void onMapClick(LatLng latLng) {
        this.sendRoute.setVisibility(4);
        this.imageBtnRoute.setVisibility(4);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        loadMap(this.map);
        double code = Math.random();
        this.map.setOnMarkerClickListener(this);
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.map.setOnMapClickListener(this);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.map.setMyLocationEnabled(true);
            this.map.getUiSettings().setMyLocationButtonEnabled(true);
            this.map.getMyLocation();
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        this.map = googleMap;
        if (this.map == null) {
            Toast.makeText(this, "Error - Map was null!!", 0).show();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.map.setMyLocationEnabled(true);
            this.mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void connectClient() {
        if (isGooglePlayServicesAvailable() && this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }

    protected void onStart() {
        super.onStart();
        connectClient();
    }

    protected void onStop() {
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST /*9000*/:
                switch (resultCode) {
                    case MessageApi.UNKNOWN_REQUEST_ID /*-1*/:
                        this.mGoogleApiClient.connect();
                    default:
                }
            default:
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == 0) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        }
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
        return false;
    }

    public void onConnected(Bundle dataBundle) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return;
        }
        if (LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient) != null) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Nous ne pouvons vous localiser, activez le GPS!", 0).show();
        }
    }

    protected void startLocationUpdates() {
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        this.mLocationRequest.setInterval(this.UPDATE_INTERVAL);
        this.mLocationRequest.setFastestInterval(this.FASTEST_INTERVAL);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
        }
    }

    public void onConnectionSuspended(int i) {
        if (i == 1) {
            Toast.makeText(this, "Disconnected. Please re-connect.", 0).show();
        } else if (i == 2) {
            Toast.makeText(this, "Network lost. Please re-connect.", 0).show();
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                return;
            } catch (SendIntentException e) {
                e.printStackTrace();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Sorry. Location services not available to you", 1).show();
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        return "https://maps.googleapis.com/maps/api/directions/" + "json" + "?" + (str_origin + "&" + ("destination=" + dest.latitude + "," + dest.longitude) + "&" + "sensor=false");
    }

    private void timeToDirection(String url) {
        JsonObjectRequest movieReq = new JsonObjectRequest(0, url, null, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.d(MapsActivity.this.TAG, response.toString());
                MapsActivity.this.hidePDialog();
                for (int i = 0; i < 1; i++) {
                    try {
                        JSONArray jRoutes = response.getJSONArray("routes");
                        for (int j = 0; j < 1; j++) {
                            JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                            for (int k = 0; k < 1; k++) {
                                MapsActivity.this.displayDuration.setText("Vous y serez en " + ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text"));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MapsActivity.this, "Impossible de generer les marqueurs", 1).show();
                    }
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.m15d(MapsActivity.this.TAG, "Error: " + error.getMessage());
                Toast.makeText(MapsActivity.this, "Connexion au serveur impossible" + error.toString(), 1).show();
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void advice() {
        CharSequence[] values = new String[]{"Ne plus afficher ce message?"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(getLayoutInflater().inflate(C1558R.layout.dialog_advice, null)).setMultiChoiceItems(values, null, new OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                MapsActivity.this.checked = isChecked;
            }
        }).setPositiveButton((CharSequence) "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (MapsActivity.this.checked) {
                    Editor editor = MapsActivity.this.sharedPreferences.edit();
                    editor.putBoolean(Config.CHECK_BOX_ADVICE_PREF, true);
                    editor.commit();
                }
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        this.notShowing = this.sharedPreferences.getBoolean(Config.CHECK_BOX_ADVICE_PREF, false);
        if (!this.notShowing) {
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void CheckEnableGPS() {
        LocationManager lm = (LocationManager) getSystemService("location");
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled("gps");
        } catch (Exception e) {
        }
        try {
            network_enabled = lm.isProviderEnabled(RegisterSimpleActivity.KEY_NETWORK);
        } catch (Exception e2) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage((CharSequence) "Vos options de localisation semblent ne pas \u00eatre activ\u00e9e. Le GPS et la localisation par le r\u00e9seau (Wifi ou r\u00e9seau mobile) doivent \u00eatre tous les deux activ\u00e9s. Souhaitez vous le faire et profiter pleinement des fonctions de Nkala?");
            dialog.setPositiveButton((CharSequence) "Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    MapsActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }
            });
            dialog.setNegativeButton((CharSequence) "Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }
}
