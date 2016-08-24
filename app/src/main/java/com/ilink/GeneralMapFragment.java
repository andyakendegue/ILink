package com.ilink;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.directions.route.AbstractRouting.TravelMode;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.GamesStatusCodes;
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
import com.ilink.Util.Operations;
import com.ilink.app.AppController;
import com.ilink.lib.DatabaseHandler;
import com.ilink.proximity.ProximityAlertService;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GeneralMapFragment extends Fragment implements OnMapReadyCallback, OnMapClickListener, OnMarkerClickListener, RoutingListener, OnConnectionFailedListener, ConnectionCallbacks {
    private static int ADVICE_TIME_OUT = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int[] COLORS;
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_MEMBER_CODE = "member_code";
    private static final String KEY_NETWORK = "network";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO = "photo";
    public static final String KEY_TAG = "tag";
    private static final String KEY_VALIDATE = "active";
    private static final String KEY_VALIDATION_CODE = "validation_code";
    private static final String LOG_TAG = "MyActivity";
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
    private static final long POINT_RADIUS = 1000;
    private static final long PROX_ALERT_EXPIRATION = -1;
    private static final String PROX_ALERT_INTENT = "com.nkala.ProximityAlert";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    static String laTitude;
    static String lonGitude;
    private static final NumberFormat nf;
    private String TAG;
    private boolean checked;
    private LatLng currentLoc;
    DatabaseHandler db;
    protected LatLng end;
    private GoogleMap gMap;
    private ImageButton imageBtnRoute2;
    private Intent intent;
    Double latitude;
    private LocationManager locationManager;
    Double longitude;
    protected GoogleApiClient mGoogleApiClient;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private RelativeLayout mapLayout;
    private Marker marker;
    String nameSnippet;
    private boolean notShowing;
    private MaterialDialog pDialog;
    private ArrayList<Polyline> polylines;
    private String provider;
    private PendingIntent proximityIntent;
    private Button sendRoute2;
    private SharedPreferences sharedPreferences;
    protected LatLng start;

    public interface OnFragmentInteractionListener {
        void onGeneralMapFragmentInteraction(Uri uri);
    }

    static {
        nf = new DecimalFormat("##.########");
        ADVICE_TIME_OUT = GamesStatusCodes.STATUS_ACHIEVEMENT_UNLOCK_FAILURE;
        COLORS = new int[]{C1558R.color.primary_dark, C1558R.color.primary, C1558R.color.primary_light, C1558R.color.accent, C1558R.color.primary_dark_material_light};
    }

    public static GeneralMapFragment newInstance(String param1, String param2) {
        GeneralMapFragment fragment = new GeneralMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GeneralMapFragment() {
        this.notShowing = false;
        this.TAG = KEY_TAG;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C1558R.layout.fragment_general_map, container, false);
        this.sendRoute2 = (Button) rootView.findViewById(C1558R.id.sendRoute2);
        this.imageBtnRoute2 = (ImageButton) rootView.findViewById(C1558R.id.imageBtnRoute2);
        this.db = new DatabaseHandler(getActivity());
        new CheckPharmacie(this, null).execute(new String[0]);
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
        this.sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        this.nameSnippet = this.sharedPreferences.getString(KEY_LASTNAME, "Not Available");
        laTitude = this.sharedPreferences.getString(KEY_LATITUDE, "Pas disponible");
        lonGitude = this.sharedPreferences.getString(KEY_LONGITUDE, "Pas disponible");
        this.polylines = new ArrayList();
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.gMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(C1558R.id.genralMap)).getMap();
            this.gMap.setOnMarkerClickListener(new 1(this));
            onMapReady(this.gMap);
            this.gMap.setOnMapClickListener(this);
            this.gMap.setOnMarkerClickListener(this);
            this.gMap.getUiSettings().setCompassEnabled(true);
            this.gMap.getUiSettings().setZoomControlsEnabled(true);
            this.gMap.getUiSettings().setCompassEnabled(true);
            this.gMap.getUiSettings().setMapToolbarEnabled(false);
            this.sendRoute2.setOnClickListener(new 2(this));
            this.sendRoute2.setVisibility(4);
            this.imageBtnRoute2.setOnClickListener(new 3(this));
            this.imageBtnRoute2.setVisibility(4);
            ((ToggleButton) rootView.findViewById(C1558R.id.sendReseau2)).setOnCheckedChangeListener(new 4(this));
            new Handler().postDelayed(new 5(this), (long) ADVICE_TIME_OUT);
        } else {
            this.gMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(C1558R.id.genralMap)).getMap();
            this.gMap.setOnMarkerClickListener(new 1(this));
            onMapReady(this.gMap);
            this.gMap.setOnMapClickListener(this);
            this.gMap.setOnMarkerClickListener(this);
            this.gMap.getUiSettings().setCompassEnabled(true);
            this.gMap.getUiSettings().setZoomControlsEnabled(true);
            this.gMap.getUiSettings().setCompassEnabled(true);
            this.gMap.getUiSettings().setMapToolbarEnabled(false);
            this.sendRoute2.setOnClickListener(new 2(this));
            this.sendRoute2.setVisibility(4);
            this.imageBtnRoute2.setOnClickListener(new 3(this));
            this.imageBtnRoute2.setVisibility(4);
            ((ToggleButton) rootView.findViewById(C1558R.id.sendReseau2)).setOnCheckedChangeListener(new 4(this));
            new Handler().postDelayed(new 5(this), (long) ADVICE_TIME_OUT);
        }
        return rootView;
    }

    protected synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == 0) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "This device is not supported.", 1).show();
            getActivity().finish();
        }
        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onGeneralMapFragmentInteraction(uri);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onMapClick(LatLng latLng) {
        this.sendRoute2.setVisibility(4);
        this.imageBtnRoute2.setVisibility(4);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(laTitude), Double.parseDouble(lonGitude));
        this.marker = this.gMap.addMarker(new MarkerOptions().title("Vous localis\u00e9 ici!").snippet(this.nameSnippet).position(latLng).icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.main_marker)));
        this.gMap.setOnMarkerClickListener(new 6(this));
        this.gMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.gMap.getUiSettings().setCompassEnabled(true);
        this.gMap.getUiSettings().setZoomControlsEnabled(true);
        this.gMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.gMap.getUiSettings().setCompassEnabled(true);
        this.gMap.getUiSettings().setMapToolbarEnabled(false);
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        this.marker.showInfoWindow();
        this.start = latLng;
    }

    public void onResume() {
        super.onResume();
    }

    private void pharmacies() {
        this.pDialog = new MaterialDialog.Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new 7(this), new 8(this));
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void hidePDialog() {
        if (this.pDialog != null) {
            this.pDialog.dismiss();
            this.pDialog = null;
        }
    }

    public void sendRequest() {
        if (Operations.isOnline(getActivity())) {
            route();
        } else {
            Toast.makeText(getActivity(), "Aucune Connexion Internet", 0).show();
        }
    }

    public void route() {
        if (this.start == null || this.end == null) {
            Toast.makeText(getActivity(), "Votre Position et votre destination semblent \u00eatre inconnus.", 0).show();
            return;
        }
        this.pDialog = new MaterialDialog.Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "R\u00e9cup\u00e9ration des informations sur le chemin...").progress(true, 0).cancelable(false).show();
        new Routing.Builder().travelMode(TravelMode.DRIVING).withListener(this).alternativeRoutes(true).waypoints(this.start, this.end).build().execute(new Void[0]);
    }

    public void onRoutingFailure(RouteException e) {
        this.pDialog.dismiss();
        if (e != null) {
            Toast.makeText(getActivity(), "Erreur: " + e.getMessage(), 1).show();
        } else {
            Toast.makeText(getActivity(), "Une erreur s'est produite, Essayez encore", 0).show();
        }
    }

    public void onRoutingStart() {
    }

    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        this.pDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(this.start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16.0f);
        this.gMap.moveCamera(center);
        if (this.polylines.size() > 0) {
            Iterator it = this.polylines.iterator();
            while (it.hasNext()) {
                ((Polyline) it.next()).remove();
            }
        }
        this.polylines = new ArrayList();
        for (int i = 0; i < route.size(); i++) {
            int colorIndex = i % COLORS.length;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width((float) ((i * 1) + 10));
            polyOptions.addAll(((Route) route.get(i)).getPoints());
            this.polylines.add(this.gMap.addPolyline(polyOptions));
        }
        MarkerOptions options = new MarkerOptions();
        options.position(this.start);
        options.icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.start_blue));
        this.gMap.addMarker(options);
        options = new MarkerOptions();
        options.position(this.end);
        options.icon(BitmapDescriptorFactory.fromResource(C1558R.drawable.end_green));
        this.gMap.addMarker(options);
    }

    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "La recherche du chemin a \u00e9t\u00e9 annul\u00e9e.");
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(LOG_TAG, connectionResult.toString());
    }

    public void onConnected(Bundle bundle) {
    }

    public void onConnectionSuspended(int i) {
    }

    public boolean onMarkerClick(Marker marker) {
        Log.i("GeneralMapFragment", "onMarkerClick");
        return false;
    }

    private void advice() {
        CharSequence[] values = new String[]{"Ne plus afficher ce message?"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(getActivity().getLayoutInflater().inflate(C1558R.layout.dialog_advice, null)).setMultiChoiceItems(values, null, new 10(this)).setPositiveButton((CharSequence) "Ok", new 9(this));
        AlertDialog alertDialog = alertDialogBuilder.create();
        this.notShowing = this.sharedPreferences.getBoolean(Config.CHECK_BOX_ADVICE_PREF, false);
        if (!this.notShowing) {
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void locations() {
        this.pDialog = new MaterialDialog.Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement marqueurs...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new 11(this), new 12(this));
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    private void allLocations() {
        this.pDialog = new MaterialDialog.Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "Nous cherchons votre chemin...").progress(true, 0).cancelable(false).show();
        JsonArrayRequest movieReq = new JsonArrayRequest("http://ilink-app.com/app/select/locations.php", new 13(this), new 14(this));
        AppController.getInstance().addToRequestQueue(movieReq, "json_array_req");
    }

    public void onClearProximityAlertClick() {
        if (this.currentLoc == null) {
            Intent intent = new Intent(getActivity(), ProximityAlertService.class);
        } else if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.removeProximityAlert(this.proximityIntent);
        }
    }

    private void saveProximityAlertPointManual() {
        double latitude = this.end.latitude;
        double longitude = this.end.longitude;
        if (Double.compare(latitude, Double.NaN) == 0 && Double.compare(longitude, Double.NaN) == 0) {
            Toast.makeText(getActivity(), "No currentloc enter. Aborting...", 1).show();
        } else {
            saveCoordinatesInPreferences((float) latitude, (float) longitude);
        }
    }

    private void saveProximityAlertPoint() {
        if (this.currentLoc == null) {
            Toast.makeText(getActivity(), "No last known currentloc. Aborting...", 1).show();
        } else {
            saveCoordinatesInPreferences((float) Double.parseDouble(laTitude), (float) Double.parseDouble(lonGitude));
        }
    }

    private void addProximityAlert(double latitude, double longitude) {
        if (100 == null) {
            new Criteria().setAccuracy(2);
            Intent intent = new Intent(getActivity(), ProximityAlertService.class);
            intent.putExtra(ProximityAlertService.LATITUDE_INTENT_KEY, latitude);
            intent.putExtra(ProximityAlertService.LONGITUDE_INTENT_KEY, longitude);
            intent.putExtra(ProximityAlertService.RADIUS_INTENT_KEY, (float) 100);
            Toast.makeText(getActivity().getApplicationContext(), "custom Alert Added", 0).show();
        } else if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.addProximityAlert(latitude, longitude, (float) 100, PROX_ALERT_EXPIRATION, this.proximityIntent);
        }
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
    }

    private void populateCoordinatesFromLastKnownLocation() {
        if ((ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") != 0 && ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") != 0) || this.locationManager.getLastKnownLocation(this.provider) == null) {
        }
    }

    private void saveCoordinatesInPreferences(float latitude, float longitude) {
        Editor prefsEditor = this.sharedPreferences.edit();
        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        prefsEditor.commit();
    }

    private Location retrievelocationFromPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences(getClass().getSimpleName(), 0);
        Location currentloc = new Location("POINT_LOCATION");
        currentloc.setLatitude((double) prefs.getFloat(POINT_LATITUDE_KEY, 0.0f));
        currentloc.setLongitude((double) prefs.getFloat(POINT_LONGITUDE_KEY, 0.0f));
        return currentloc;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(Cast.MAX_NAMESPACE_LENGTH);
        for (int i = 0; i < randomLength; i++) {
            randomStringBuilder.append((char) (generator.nextInt(96) + 32));
        }
        return randomStringBuilder.toString();
    }
}
