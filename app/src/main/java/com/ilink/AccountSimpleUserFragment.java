package com.ilink;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class AccountSimpleUserFragment extends Fragment implements LocationListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TAG = "tag";
    private static final String REGISTER_URL = "http://ilink-app.com/app/";
    private String TAG;
    public TextView abonnementLabel;
    public TextView emailLabel;
    private LocationManager locationManager;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    public TextView nomLabel;
    private MaterialDialog pDialog;
    public TextView phoneLabel;
    public TextView prenomLabel;
    public TextView soldeLabel;

    public interface OnFragmentInteractionListener {
        void onAccountSimpleUserFragmentInteraction(Uri uri);
    }

    public static AccountSimpleUserFragment newInstance(String param1, String param2) {
        AccountSimpleUserFragment fragment = new AccountSimpleUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountSimpleUserFragment() {
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
        View rootView = inflater.inflate(C1558R.layout.fragment_account_simple_user, container, false);
        this.nomLabel = (TextView) rootView.findViewById(C1558R.id.lastnameAccountView);
        this.prenomLabel = (TextView) rootView.findViewById(C1558R.id.firstnameAccountView);
        this.phoneLabel = (TextView) rootView.findViewById(C1558R.id.phoneAccountView);
        this.emailLabel = (TextView) rootView.findViewById(C1558R.id.emailAccountView);
        this.abonnementLabel = (TextView) rootView.findViewById(C1558R.id.balanceAccountView);
        Button reSubscribe = (Button) rootView.findViewById(C1558R.id.button_re_subscribe);
        ((Button) rootView.findViewById(C1558R.id.button_modify_profile)).setOnClickListener(new 1(this));
        reSubscribe.setOnClickListener(new 2(this));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        String email = sharedPreferences.getString(KEY_EMAIL, "Not Available");
        String lastname = sharedPreferences.getString(RegisterSimpleActivity.KEY_LASTNAME, "Not Available");
        String firstname = sharedPreferences.getString(RegisterSimpleActivity.KEY_FIRSTNAME, "Not Available");
        String phone = sharedPreferences.getString(TamponGeolocatedActivity.KEY_PHONE, "Not Available");
        this.nomLabel.setText(lastname);
        this.prenomLabel.setText(firstname);
        this.phoneLabel.setText(phone);
        this.emailLabel.setText(email);
        this.abonnementLabel.setText("En cours");
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onAccountSimpleUserFragmentInteraction(uri);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onResume() {
        super.onResume();
        this.locationManager = (LocationManager) getActivity().getSystemService("location");
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
        StringBuilder msg = new StringBuilder("lat : ");
        msg.append(location.getLatitude());
        msg.append("; lng : ");
        msg.append(location.getLongitude());
        String email = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_EMAIL, "Not Available");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Volley.newRequestQueue(getActivity()).add(new 5(this, 1, REGISTER_URL, new 3(this), new 4(this), email, location));
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

    private void user() throws JSONException {
        String tag_json_arry = "json_array_req";
        this.pDialog = new Builder(getActivity()).title((CharSequence) "Attendez svp!").content((CharSequence) "Chargement des informations...").progress(true, 0).cancelable(false).show();
        String email = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0).getString(KEY_EMAIL, "Not Available");
        Map<String, String> params = new HashMap();
        params.put(KEY_EMAIL, email);
        params.put(KEY_TAG, "getuser");
        Volley.newRequestQueue(getActivity()).add(new CustomRequest(1, "http://ilink-app.com/app/select/users.php", params, new 6(this), new 7(this)));
    }

    private void hidePDialog() {
        if (this.pDialog != null) {
            this.pDialog.dismiss();
            this.pDialog = null;
        }
    }
}
