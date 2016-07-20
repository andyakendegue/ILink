package com.ilink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ilink.lib.DatabaseHandler;

public class ContenuTestActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenu_test);

        textView1.findViewById(R.id.textView10);
        textView2.findViewById(R.id.textView11);
    }
}
