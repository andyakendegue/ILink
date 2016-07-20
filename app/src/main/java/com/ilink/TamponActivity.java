package com.ilink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class TamponActivity extends AppCompatActivity {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TAG = "tag";
    public Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_tampon);

        btnRetry = (Button) findViewById(R.id.button_retry);
        btnRetry.setVisibility(View.INVISIBLE);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    user();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(TamponActivity.this, "impossible de recuperer vos informations", Toast.LENGTH_LONG).show();

                }
            }

        });
        try {
            user();
        } catch (JSONException e) {
            //e.printStackTrace();

            Toast.makeText(TamponActivity.this, "impossible de recuperer vos informations", Toast.LENGTH_LONG).show();

        }
    }


    private void user() throws JSONException {
        String tag_json_arry = "json_array_req";



        String url = "http://ilink-app.com/app/select/users_simple.php";

        final SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF, "Not Available");



        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_PHONE, phone);
        params.put(KEY_TAG, "getuser");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Toast.makeText(TamponActivity.this, "Nice!", Toast.LENGTH_LONG).show();
                        // Toast.makeText(getActivity(), "Localisation a jour!", Toast.LENGTH_LONG).show();

                        try {
                            JSONObject obj = response.getJSONObject(0);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor

                            editor.putString(Config.EMAIL_SHARED_PREF, obj.getString("email"));
                            editor.putString(Config.LASTNAME_SHARED_PREF, obj.getString("lastname"));
                            editor.putString(Config.FIRSTNAME_SHARED_PREF, obj.getString("firstname"));
                            editor.putString(Config.PHONE_SHARED_PREF, obj.getString("phone"));
                            editor.putString(Config.CATEGORY_SHARED_PREF, obj.getString("category"));
                            editor.putString(Config.COUNTRY_CODE_SHARED_PREF, obj.getString("country_code"));
                            editor.putString(Config.NETWORK_SHARED_PREF, obj.getString("network"));
                            editor.putString(Config.MEMBER_CODE_SHARED_PREF, obj.getString("member_code"));
                            editor.putString(Config.VALIDATION_SHARED_PREF, obj.getString("active"));
                            editor.putString(Config.VALIDATION_CODE_SHARED_PREF, obj.getString("validation_code"));
                            editor.putString(Config.LATITUDE_SHARED_PREF, obj.getString("latitude"));
                            editor.putString(Config.LONGITUDE_SHARED_PREF, obj.getString("longitude"));


                            //Saving values to editor
                            editor.commit();



                            /*if (obj.getString("category").equalsIgnoreCase("utilisateur")) {

                                Intent i = new Intent(TamponActivity.this, MapsActivity.class);
                                //Intent i = new Intent(TamponActivity.this, HyperviseurHomeActivity.class);

                                startActivity(i);
                                finish();

                            } else if (obj.getString("category").equalsIgnoreCase("super")) {
                                Intent i = new Intent(TamponActivity.this, SuperviseurHomeActivity.class);
                                startActivity(i);
                                finish();

                            } else if (obj.getString("category").equalsIgnoreCase("hyper")) {
                                Intent i = new Intent(TamponActivity.this, HyperviseurHomeActivity.class);
                                startActivity(i);
                                finish();

                            } else if (obj.getString("category").equalsIgnoreCase("geolocated")) {
                                Intent i = new Intent(TamponActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(TamponActivity.this, "Impossible de vous connecter. Veuillez redemarrer l'application", Toast.LENGTH_LONG).show();
                            }
                            */

                            if(obj.getString("active").equalsIgnoreCase("non") &&obj.getString("category").equalsIgnoreCase("utilisateur")) {
                                Intent i = new Intent(TamponActivity.this, ActivateActivity.class);
                                startActivity(i);
                                finish();

                            } else if (obj.getString("active").equalsIgnoreCase("oui") && obj.getString("category").equalsIgnoreCase("utilisateur")){



                                    Intent i = new Intent(TamponActivity.this, MapsActivity.class);
                                    startActivity(i);
                                    finish();


                            }else {
                                Toast.makeText(TamponActivity.this, "Impossible de vous connecter. Veuillez redemarrer l'application", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Toast.makeText(TamponActivity.this, "Impossible de recuperer vos donnees", Toast.LENGTH_LONG).show();

                            btnRetry.setVisibility(View.VISIBLE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(TamponActivity.this,"Impossible de se connecter au serveur", Toast.LENGTH_LONG).show();
                        btnRetry.setVisibility(View.VISIBLE);

                        //nomLabel.setText(error.toString());
                    }
                }




        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(userRequest);

        requestQueue.add(jsObjRequest);

    }
}
