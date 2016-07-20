package com.ilink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountSimpleUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountSimpleUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSimpleUserFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String REGISTER_URL = "http://ilink-app.com/app/";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TAG = "tag";

    public TextView nomLabel;
    public TextView prenomLabel;
    public TextView phoneLabel;
    public TextView emailLabel;
    public TextView abonnementLabel;
    public TextView soldeLabel;


    private LocationManager locationManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MaterialDialog pDialog;
    private String TAG = "tag";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSimpleUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSimpleUserFragment newInstance(String param1, String param2) {
        AccountSimpleUserFragment fragment = new AccountSimpleUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountSimpleUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account_simple_user, container, false);
        //return inflater.inflate(R.layout.fragment_account_simple_user, container, false);



        nomLabel = (TextView) rootView.findViewById(R.id.lastnameAccountView);
        prenomLabel = (TextView) rootView.findViewById(R.id.firstnameAccountView);
        phoneLabel = (TextView) rootView.findViewById(R.id.phoneAccountView);
        emailLabel = (TextView) rootView.findViewById(R.id.emailAccountView);
        abonnementLabel = (TextView) rootView.findViewById(R.id.balanceAccountView);
        Button modifyProfile = (Button) rootView.findViewById(R.id.button_modify_profile);
        Button reSubscribe = (Button) rootView.findViewById(R.id.button_re_subscribe);

        modifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ModifyProfileActivity.class);
                startActivity(i);
                getActivity().finish();

            }

        });

        reSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RenewSubscriptionActivity.class);
                startActivity(i);
                getActivity().finish();

            }

        });

        //new CheckUsers().execute();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String lastname = sharedPreferences.getString(Config.LASTNAME_SHARED_PREF, "Not Available");
        final String firstname = sharedPreferences.getString(Config.FIRSTNAME_SHARED_PREF, "Not Available");
        final String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF, "Not Available");


        nomLabel.setText(lastname);
        prenomLabel.setText(firstname);
        phoneLabel.setText(phone);
        emailLabel.setText(email);
        abonnementLabel.setText("En cours");

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAccountSimpleUserFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAccountSimpleUserFragmentInteraction(Uri uri);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        //Obtention de la référence du service
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        locationManager.removeUpdates(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(final Location location) {
        //Fetching email from shared preferences

        //On affiche dans un Toat la nouvelle Localisation
        final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(location.getLatitude());
        msg.append("; lng : ");
        msg.append(location.getLongitude());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        //Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT).show();

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        marker.setPosition(latLng);
        */



        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        // Toast.makeText(getActivity(), "Localisation a jour!", Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Impossible de mettre a jour votre position : "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(KEY_EMAIL, email);
                params.put(KEY_LATITUDE, String.valueOf(location.getLatitude()));
                params.put(KEY_LONGITUDE, String.valueOf(location.getLongitude()));
                params.put(KEY_TAG, "updateLocation");
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if ("gps".equals(provider)) {
            desabonnementGPS();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if ("gps".equals(provider)) {
            abonnementGPS();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
    }


    private class CheckUsers extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new MaterialDialog.Builder(getActivity())
                    .title("Attendez svp!")
                    .content("Vérification de la connexion réseau")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();



            nomLabel.setText(" ");
            prenomLabel.setText(" ");
            phoneLabel.setText(" ");
            emailLabel.setText(" ");
            abonnementLabel.setText(" ");
            soldeLabel.setText(" ");
        }

        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args) {


            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    //e1.printStackTrace();
                    Toast.makeText(getActivity(), "Url ivalide : "+ e1.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();

                    Toast.makeText(getActivity(), "Connexion au serveur impossible : "+e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean th) {

            if (th == true) {
                pDialog.dismiss();
                try {
                    user();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getActivity(), "Impossible de recuperer vos informations : "+e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                pDialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Erreur lors de la connexion au réseau", Toast.LENGTH_LONG).show();

            }
        }
    }


    private void user() throws JSONException {
        String tag_json_arry = "json_array_req";

        pDialog = new MaterialDialog.Builder(getActivity())
                .title("Attendez svp!")
                .content("Chargement des informations...")
                .progress(true, 0)
                .cancelable(false)
                .show();


        String url = "http://ilink-app.com/app/select/users.php";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");



        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_EMAIL, email);
        params.put(KEY_TAG, "getuser");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hidePDialog();
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        // Toast.makeText(getActivity(), "Localisation a jour!", Toast.LENGTH_LONG).show();

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            nomLabel.setText(obj.getString("lastname"));
                            prenomLabel.setText(obj.getString("firstname"));
                            phoneLabel.setText(obj.getString("phone"));
                            emailLabel.setText(obj.getString("email"));
                            abonnementLabel.setText("En cours");

                            if(obj.getString("balance").isEmpty() || obj.getString("balance").equalsIgnoreCase("0")){

                                soldeLabel.setText("0 CFA");

                            } else {
                                soldeLabel.setText(obj.getString("balance")+" CFA");
                            }

                        } catch (JSONException e) {
                            //e.printStackTrace();

                            Toast.makeText(getActivity(), "Impossible de recuperer vos informations : "+e.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getActivity(),"Connexion au serveur impossible :" +error.toString(), Toast.LENGTH_LONG).show();

                        nomLabel.setText(error.toString());
                    }
                }




        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //requestQueue.add(userRequest);

        requestQueue.add(jsObjRequest);

    }



    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
