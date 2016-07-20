package com.ilink;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ModifyProfileActivity extends AppCompatActivity {

    public TextView nomLabel;
    public TextView prenomLabel;
    public TextView phoneLabel;
    public TextView emailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        nomLabel = (TextView) findViewById(R.id.lastnameModifyAccountView);
        prenomLabel = (TextView) findViewById(R.id.firstnameModifyAccountView);
        phoneLabel = (TextView) findViewById(R.id.phoneModifyAccountView);
        emailLabel = (TextView) findViewById(R.id.emailModifyAccountView);


        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String lastname = sharedPreferences.getString(Config.LASTNAME_SHARED_PREF, "Not Available");
        final String firstname = sharedPreferences.getString(Config.FIRSTNAME_SHARED_PREF, "Not Available");
        final String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF, "Not Available");


        nomLabel.setText(lastname);
        prenomLabel.setText(firstname);
        phoneLabel.setText(phone);
        emailLabel.setText(email);
    }
}
